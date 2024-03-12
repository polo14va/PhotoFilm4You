package edu.uoc.epcsd.productcatalog.application.rest;



import edu.uoc.epcsd.productcatalog.application.rest.request.CreateProductRequest;
import edu.uoc.epcsd.productcatalog.application.rest.request.FindProductsCriteria;
import edu.uoc.epcsd.productcatalog.application.rest.response.GetProductResponse;
import edu.uoc.epcsd.productcatalog.application.rest.response.GetProductsAndAvailabilityResponse;
import edu.uoc.epcsd.productcatalog.domain.ItemStatus;
import edu.uoc.epcsd.productcatalog.domain.Product;

import edu.uoc.epcsd.productcatalog.domain.service.ItemService;
import edu.uoc.epcsd.productcatalog.domain.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@Log4j2
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost")
public class ProductRESTController  implements WebMvcConfigurer {

    private final ProductService productService;

    private final ItemService itemService;

    private final RestTemplate restTemplate;
    private static final String NOT_FOUND = " does not exist.";
    @Value("${user.orders.getReservedQty.url}")
    private  String getReservedQty;

    @Autowired
    public ProductRESTController(ProductService productService, ItemService itemService, RestTemplate restTemplate) {

        this.productService = productService;
        this.itemService = itemService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        log.trace("getAllProducts");

        return productService.findAllProducts();
    }

    @GetMapping("/catalog")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetProductsAndAvailabilityResponse> getCatalog(Pageable pageable) {

        Page<Product> products = productService.findCatalog(pageable);
        List<GetProductsAndAvailabilityResponse> response = new ArrayList<>();
        Integer reservedProducts;
        int itemsQty;

        for (Product product : products.getContent()) {
            try {
                reservedProducts = restTemplate.getForObject(getReservedQty, Integer.class, product.getId());
            } catch (RestClientException e) {
                throw new IllegalArgumentException("Could not found the item: " + product.getId(), e);
            }
            itemsQty = (int) itemService.findByProductId(product.getId()).stream().
                    filter(s -> s.getStatus() == ItemStatus.OPERATIONAL).count();
            itemsQty = itemsQty - reservedProducts;

            response.add(GetProductsAndAvailabilityResponse.fromDomain(product, itemsQty));
        }

        return new PageImpl<>(response, pageable, products.getTotalElements());
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetProductResponse> getProductById(@PathVariable(name = "productId") @NotNull Long productId) {
        log.trace("getProductById");

        return productService.findProductById(productId).map(product -> ResponseEntity.ok().body(GetProductResponse.fromDomain(product)))
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Product>> findProductsByCriteria(FindProductsCriteria findProductsCriteria) {
        log.trace("findProductsByCriteria");

        try {
            return ResponseEntity.ok(productService.findProductsByExample(Product.builder()
                    .name(findProductsCriteria.getName())
                    .categoryId(findProductsCriteria.getCategoryId())
                    .build()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified product category " + findProductsCriteria.getCategoryId() + NOT_FOUND, e);
        }
    }

    @PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody @NotNull @Valid CreateProductRequest createProductRequest) {
        log.trace("createProduct");

        log.trace("Creating product " + createProductRequest);

        try {
            Long productId = productService.createProduct(Product.builder()
                    .name(createProductRequest.getName())
                    .description(createProductRequest.getDescription())
                    .dailyPrice(createProductRequest.getDailyPrice())
                    .model(createProductRequest.getModel())
                    .brand(createProductRequest.getBrand())
                    .categoryId(createProductRequest.getCategoryId())
                    .build());

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(productId)
                    .toUri();

            return ResponseEntity.created(uri).body(productId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified product category " + createProductRequest.getCategoryId() + NOT_FOUND, e);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") @NotNull Long productId) {
        log.trace("deleteProduct");

        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified product id " + productId + NOT_FOUND, e);
        }
    }
}
