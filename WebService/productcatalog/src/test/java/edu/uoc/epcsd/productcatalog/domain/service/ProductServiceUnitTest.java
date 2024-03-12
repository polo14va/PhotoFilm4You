package edu.uoc.epcsd.productcatalog.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.uoc.epcsd.productcatalog.domain.Item;
import edu.uoc.epcsd.productcatalog.domain.Product;
import edu.uoc.epcsd.productcatalog.domain.repository.ProductRepository;
@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {
    @Mock
    private ItemService itemService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void finAllProductsTest() {

        // Arrange
        when(productRepository.findAllProducts()).thenReturn(Arrays.asList(new Product(), new Product()));

        // Act
        List<Product> products = productService.findAllProducts();

        // Assert
        assert !products.isEmpty();
        verify(productRepository, times(1)).findAllProducts();
    }

    @Test
    void whenDeleteProductthenShouldDeleteProductAndAssociatedItems() {
        // Arrange

        Double dailyPrice = 150.5;

        String brand = "Brand";

        String model = "Model";

        Long categoryId = 1L;

        Product product = new Product(dailyPrice,brand, model,categoryId);


        Item item1 = Item.builder().serialNumber("001").productId(product.getId()).build();
        Item item2 = Item.builder().serialNumber("002").productId(product.getId()).build();

        // When
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.of(product));
        when(itemService.findByProductId(product.getId())).thenReturn(Arrays.asList(item1, item2));

        // Act
        productService.deleteProduct(product.getId());
       List<Item> items =  itemService.findByProductId(product.getId());
        // Assert
        for(Item item: items){
            verify(itemService, times(1)).deleteItem(item.getSerialNumber());
        }

        verify(productRepository, times(1)).deleteProduct(product);
    }

    @Test
    void whenFindByIdthenGetProduct(){
        Double dailyPrice = 150.5;

        String brand = "Brand";

        String model = "Model";

        Long categoryId = 1L;

        Product product = new Product(dailyPrice,brand, model,categoryId);
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.of(product));

        productService.findProductById(product.getId());

        verify(productRepository, times(1)).findProductById(product.getId());

    }


    @Test
    void whenFindProductsByExample_thenGetCategories(){

        Product product1 = new Product(150.5,"Brand", "Model",1L);
        Product product2 = new Product(150.5,"Brand", "Model",1L);
        Product product3 = new Product(150.5,"Brand", "Model",1L);

        List<Product> mockProducts = Arrays.asList(
                product2,
                product3

        );

        when(productRepository.findProductsByExample((product1))).thenReturn(mockProducts);

        List<Product> result = productService.findProductsByExample(product1);

        assertEquals(mockProducts.size(), result.size());
        assertEquals(product1.getId(), result.get(0).getId());
        assertEquals(product1.getName(), result.get(0).getName());
        assertEquals(product1.getDescription(), result.get(0).getDescription());

        assertEquals(product2.getId(), result.get(1).getId());
        assertEquals(product2.getName(), result.get(1).getName());
        assertEquals(product2.getDescription(), result.get(1).getDescription());
    }

    @Test
    void whenCreateProduct_thenGetId(){
       
        Product mockProduct = new Product();
        mockProduct.setId(1L);

        when(productRepository.createProduct(any())).thenReturn(mockProduct.getId());

        // Act
        Long result = productService.createProduct(mockProduct);

        // Assert
        assertEquals(mockProduct.getId(), result);

        // Verify that createProduct method of productRepository is called once with the expected argument
        verify(productRepository, times(1)).createProduct(any());

    }


}