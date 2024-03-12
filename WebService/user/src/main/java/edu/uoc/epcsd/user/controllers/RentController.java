package edu.uoc.epcsd.user.controllers;


import edu.uoc.epcsd.user.controllers.dtos.*;
import edu.uoc.epcsd.user.entities.Rent;
import edu.uoc.epcsd.user.services.RentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class RentController {

    @Autowired
    private final RentService orderservice;
    private static final  String CONST_MESSAGE =  "message";
    @Autowired
    public RentController(RentService orderservice) {
        this.orderservice = orderservice;
    }

    @Value("${productCatalog.getProductDetails.url}")
    private String productCatalogUrl;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GetOrdersResponse> getCartByUser(@PathVariable Long userId) {

        return buildFrontendCartShopResponse(orderservice.getCartByUser(userId));
    }

    @PostMapping
    public List<GetOrdersResponse> createCart(@RequestBody CreateRentRequest createRentRequest) {
        Long userId = orderservice.addRentLine(createRentRequest.getProductId(), createRentRequest.getUserId(),
                createRentRequest.getQuantity());
        if (userId < 0) {
            return new ArrayList<>();
        }
        List<Rent> rents = orderservice.getCartByUser(userId);

        return buildFrontendCartShopResponse(rents);
    }

    @GetMapping("/isavailable/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String,String>> isProductAvailable(@PathVariable Long productId) {
        int qtyAvailable = orderservice.getAvailableQty(productId);

        if (qtyAvailable <= 0) {
            Map<String, String> response = new HashMap<>();
            response.put(CONST_MESSAGE, "Error: Not enough stock");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put(CONST_MESSAGE, "Quantity available");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/cart/updateqty")
    public ResponseEntity<Map<String, String>>  updateCartQty(@RequestBody UpdateCartQtyRequest updateCartQty) {
        int cartUpdated = orderservice.updateCartQty(updateCartQty.getCartId(), updateCartQty.getQuantity());

        if(cartUpdated == 1) {
            Map<String, String> response = new HashMap<>();
            response.put(CONST_MESSAGE, "Quantity updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (cartUpdated < 0) {
            Map<String, String> response = new HashMap<>();
            response.put(CONST_MESSAGE, "Error: Not enough stock");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("cartId") int cartId) {

        boolean deleted = orderservice.deleteOrderLine((long)cartId);
        if (deleted) {
            Map<String, String> response = new HashMap<>();
            response.put(CONST_MESSAGE, "Product deleted successfully");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cart/pay/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> payCart(@PathVariable int userId) {
        if (orderservice.payCart(userId)) {
            Map<String, String> response = new HashMap<>();
            response.put(CONST_MESSAGE, "Product deleted successfully");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/reservations/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Rent> getReservation(@PathVariable Long reservationId) {
        return orderservice.getReservationsById(reservationId);
    }

    @GetMapping("/reservations/byuserid/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GetOrdersResponse> getReservation(@PathVariable int userId) {
        List<Rent> rents = orderservice.getReservationsByUser(userId);

        return buildFrontendCartShopResponse(rents);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Long> updateReservation(@RequestBody UpdateReservationRequest updateReservationRequest) {
        Long orderId = orderservice.updateReservation(updateReservationRequest.getId(),
                updateReservationRequest.getProductId(), updateReservationRequest.getUserId(),
                updateReservationRequest.getQuantity());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderId)
                .toUri();

        return ResponseEntity.created(uri).body(orderId);
    }

    @GetMapping("/reservations/getByOrderId/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GetOrdersResponse> getReservationByOrderId(@PathVariable int userId) {
        List<Rent> rents = orderservice.getReservationsByOrderId(userId);

        return buildFrontendCartShopResponse(rents);
    }

    @GetMapping("/stockReservedByProduct/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> stockReservedByProduct(@PathVariable int productId) {
        int reservedQty = orderservice.getStockReservedByProduct(productId);

        ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservedQty)
                .toUri();

        return ResponseEntity.ok().body(reservedQty);
    }

    private List<GetOrdersResponse> buildFrontendCartShopResponse(List<Rent> rents) {

        ArrayList<GetOrdersResponse> rentsRequests = new ArrayList<>();

        for (Rent rent : rents) {
            try {
                ResponseEntity<GetProductResponse> getProductResponseEntity = new RestTemplate().getForEntity(productCatalogUrl, GetProductResponse.class, rent.getProductId());

                if (getProductResponseEntity.getBody().getDailyPrice() != null) {
                    GetOrdersResponse orderResponse = GetOrdersResponse.fromDomain(rent.getUserId(), getProductResponseEntity.getBody(),rent.getQuantity(),getProductResponseEntity.getBody().getDailyPrice() * rent.getQuantity(),getProductResponseEntity.getBody().getDailyPrice()  * 0.21,rent.getId(),rent.getOrderId()
                    );

                    rentsRequests.add(orderResponse);
                }

            } catch (Exception e) {
                throw new IllegalArgumentException("Could not found the productId: " + rent.getProductId(), e);
            }
        }

        return rentsRequests;
    }
}
