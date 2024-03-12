package edu.uoc.epcsd.productcatalog.domain.service;

import edu.uoc.epcsd.productcatalog.domain.Product;
import edu.uoc.epcsd.productcatalog.domain.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ProductService {

    private final ItemService itemService;

    private final ProductRepository productRepository;

    public ProductService(ItemService itemService, ProductRepository productRepository) {
        this.itemService = itemService;
        this.productRepository = productRepository;
    }

    public List<Product> findAllProducts() {
        return productRepository.findAllProducts();
    }

    public Page<Product> findCatalog(Pageable pageable) {
        return productRepository.findCatalog(pageable);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findProductById(id);
    }

    public List<Product> findProductsByExample(Product product) {
        return productRepository.findProductsByExample(product);
    }

    public Long createProduct(Product product) {
        return productRepository.createProduct(product);
    }

    public void deleteProduct(Long id) {

        Product product = productRepository.findProductById(id).orElseThrow(IllegalArgumentException::new);

        itemService.findByProductId(id).forEach(item -> itemService.deleteItem(item.getSerialNumber()));

        productRepository.deleteProduct(product);
    }
}
