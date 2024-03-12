package edu.uoc.epcsd.user.services;




import edu.uoc.epcsd.user.controllers.dtos.GetProductResponse;
import edu.uoc.epcsd.user.entities.Rent;
import edu.uoc.epcsd.user.repositories.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RentService {

    public static final String ITEM_NOT_FOUND = "Could not found the item: ";

    private final RentRepository rentRepository;

    private final RestTemplate restTemplate;


    @Value("${productCatalog.getProductDetails.url}")
    private String productCatalogUrl;
    @Value("${productCatalog.getAvailableProductQuantity.url}")
    private String productItemAvailableQty;
    @Autowired
    public RentService(RentRepository rentRepository, RestTemplate restTemplate) {
        this.rentRepository = rentRepository;
        this.restTemplate = restTemplate;
    }

    public boolean deleteOrderLine(Long orderId) {
        Optional<Rent> order = rentRepository.findCarritoById(orderId);

        if (!order.isPresent()) {
            return false;
        }
        rentRepository.deleteLineaDeCarritoById(orderId);
        return true;
    }

    public List<Rent> getCartByUser(Long userId) {
        return rentRepository.findCarritoByUser(userId);
    }


    public Long addRentLine(Long productId, Long userId, int quantity) {
        try {
            restTemplate.getForEntity(productCatalogUrl, GetProductResponse.class, productId);
        } catch (RestClientException e) {
            throw new IllegalArgumentException("Could not found the productId: " + productId, e);
        }

        Integer availableItems;
        try {
            availableItems = restTemplate.getForObject(productItemAvailableQty, Integer.class, productId);
        } catch (RestClientException e) {
            throw new IllegalArgumentException(ITEM_NOT_FOUND + productId, e);
        }


        Optional<Integer> response = rentRepository.getReservedQty(productId, -1); //If it's a new line we don't need to take out the current line
        int reservedQty = 0;
        if(response.isPresent()) {
            reservedQty = response.get();
        }

        int isProductAvailable = availableItems - reservedQty - quantity;
        if(isProductAvailable < 0) {
            return (long) isProductAvailable; //We return a negative number
        }

        Rent rent = Rent.builder().productId(productId)
                .userId(userId)
                .quantity(quantity)
                .estado('E')
                .build();

        List<Rent> currentRent = rentRepository.findCarritoByUser(userId); //Serch for and open cart for this user

        //If there's an open cart the new line will go in there
        if(!currentRent.isEmpty()) {
            rent.setOrderId(currentRent.get(0).getOrderId());
        }

        Long id = rentRepository.save(rent).getId(); //Insert new line

        //If there's no open cart the orderId will be this line id
        if (currentRent.isEmpty()) {
            rentRepository.updateOrderId(id);
        }
        return rent.getUserId();
    }

    public boolean payCart (long userId) {
        return rentRepository.payCart(userId) > 0;
    }

    public int updateCartQty(long rentId, int quantity) {
        long productId = rentRepository.getProductByRentId(rentId);
        Integer availableItems;
        try {
            availableItems = restTemplate.getForObject(productItemAvailableQty, Integer.class, productId);
        } catch (RestClientException e) {
            throw new IllegalArgumentException(ITEM_NOT_FOUND + productId, e);
        }


        Optional<Integer> response = rentRepository.getReservedQty(productId, rentId);
        int reservedQty = 0;
        if(response.isPresent()) {
            reservedQty = response.get();
        }

        int isProductAvailable = availableItems - reservedQty - quantity;
        if(isProductAvailable < 0) {
            return  isProductAvailable; //We return a negative number
        }
        return rentRepository.updateCartQty(rentId, quantity);
    }

    public List<Rent> getReservationsById(Long reservationId) {
        return rentRepository.getReservationsById(reservationId);
    }

    public List<Rent> getReservationsByUser(long userId) {
        return rentRepository.getReservationsByUser(userId);
    }

    public Long updateReservation(Long id, Long productId, Long userId, int quantity) {

        int n = rentRepository.updateReservation(id, productId, userId, quantity);

        return (long) n;
    }

    public List<Rent> getReservationsByOrderId(long orderId) {
        return rentRepository.getReservationsByOrderId(orderId);
    }

    public int getStockReservedByProduct(long productId) {
        Optional<Integer> response = rentRepository.getReservedQty(productId, -1);
        int reservedQty = 0;
        if(response.isPresent()) {
            reservedQty = response.get();
        }

        return reservedQty;
    }

    public int getAvailableQty(Long productId) {
        Integer availableItems;
        try {
            availableItems = restTemplate.getForObject(productItemAvailableQty, Integer.class, productId);
        } catch (RestClientException e) {
            throw new IllegalArgumentException(ITEM_NOT_FOUND + productId, e);
        }

        Optional<Integer> response = rentRepository.getReservedQty(productId, -1);
        int reservedQty = 0;
        if(response.isPresent()) {
            reservedQty = response.get();
        }

        return availableItems - reservedQty;
    }
}
