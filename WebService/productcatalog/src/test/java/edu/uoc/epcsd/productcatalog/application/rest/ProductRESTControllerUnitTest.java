package edu.uoc.epcsd.productcatalog.application.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import edu.uoc.epcsd.productcatalog.application.rest.request.FindProductsCriteria;

import edu.uoc.epcsd.productcatalog.application.rest.response.GetProductsAndAvailabilityResponse;
import edu.uoc.epcsd.productcatalog.domain.Item;
import edu.uoc.epcsd.productcatalog.domain.ItemStatus;
import edu.uoc.epcsd.productcatalog.domain.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import edu.uoc.epcsd.productcatalog.application.rest.request.CreateProductRequest;
import edu.uoc.epcsd.productcatalog.application.rest.response.GetProductResponse;
import edu.uoc.epcsd.productcatalog.domain.Product;
import edu.uoc.epcsd.productcatalog.domain.service.ProductService;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ProductRESTControllerUnitTest {

    @Mock
    private ProductService productService;
    @Mock
    private ItemService itemService;

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ProductRESTController productRestController;

    private Product mockProduct;
    private List<Product> mockProducts;
    @BeforeEach
    void setUp(){
        mockProduct = new Product(150.5,"Brand", "Model",1L);
        mockProducts = Arrays.asList(mockProduct,new Product(150.5,"Brand", "Model",1L) );
    }
    @Test
    void whenGetAllProductsShouldReturnListOfProducts() {


        when(productService.findAllProducts()).thenReturn(mockProducts);
        // Act
        List<Product> result = productRestController.getAllProducts();
        // Assert
        assertEquals(mockProducts, result);
    }

    @Test
    void whenGetProductByIdShouldReturnProductResponseWhenProductExists() {
        // Arrange


        when(productService.findProductById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));

        // Act
        ResponseEntity<GetProductResponse> responseEntity = productRestController.getProductById(mockProduct.getId());

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(GetProductResponse.fromDomain(mockProduct), responseEntity.getBody());
    }
    @Test
    void whenGetProductByIdShouldReturnNotFoundWhenProductDoesNotExist() {
        // Arrange
        Long productId = 1L;
        when(productService.findProductById(productId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<GetProductResponse> responseEntity = productRestController.getProductById(productId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void whenDeleteProductWithValidId() {
        // Arrange

        doNothing().when(productService).deleteProduct(mockProduct.getId());

        // Act
        ResponseEntity<Boolean> responseEntity = productRestController.deleteProduct(mockProduct.getId());

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody());
        verify(productService, times(1)).deleteProduct(mockProduct.getId());
    }

    @Test
    void deleteProductWithInvalidId(){
        Long productId = 1L; // Assume an invalid product ID for the test

        doThrow(new IllegalArgumentException("Invalid product ID")).when(productService).deleteProduct(productId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                productRestController.deleteProduct(productId));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The specified product id 1 does not exist.", exception.getReason());
    }

    @Test
    void whenCreateProductthenGetProductId() {
        CreateProductRequest createProductRequest = new CreateProductRequest("Name","Description",26.0,"Brand","Model",1L);
        Long productId = 2L;
        MockMvcBuilders.standaloneSetup(productRestController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));


        when(productService.createProduct(any())).thenReturn(productId);

        // Act
        ResponseEntity<Long> responseEntity = productRestController.createProduct(createProductRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(productId, responseEntity.getBody());


    }

    @Test
    void testCreateProductWithInvalidCategory(){
        CreateProductRequest createProductRequest = new CreateProductRequest("Name","Description",26.0,"Brand","Model",1L);
        MockMvcBuilders.standaloneSetup(productRestController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(productService.createProduct(any(Product.class))).thenThrow(new IllegalArgumentException("Invalid category"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                productRestController.createProduct(createProductRequest));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The specified product category 1 does not exist.", exception.getReason());
    }

    @Test
    void findProductsByCriteria(){
        FindProductsCriteria criteria = new FindProductsCriteria("Product",1L);
        mockProduct.setId(1L);
        List<Product> list = Arrays.asList(mockProduct);

        MockMvcBuilders.standaloneSetup(productRestController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));


        when(productService.findProductsByExample(any(Product.class))).thenReturn(list);

        ResponseEntity<List<Product>> response = productRestController.findProductsByCriteria(criteria);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }
    @Test
    void findProductsByCriteriaWithIllegalArgumentException() {
        // Arrange
        FindProductsCriteria criteria = new FindProductsCriteria("InvalidProduct", 1L);

        when(productService.findProductsByExample(any(Product.class)))
                .thenThrow(new IllegalArgumentException("Category not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> productRestController.findProductsByCriteria(criteria));
    }



    @Test
    void getCatalogTest() throws NoSuchFieldException, IllegalAccessException {
        // Setup
        Pageable pageable = Pageable.unpaged();
        Page<Product> mockedProductPage = new PageImpl<>(Arrays.asList(mockProduct));
        List<Item> itemList = Arrays.asList(new Item("111",ItemStatus.OPERATIONAL,1L));

        MockMvcBuilders.standaloneSetup(productRestController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String getReservedQty = "http://example.com/products/{productId}";// replace with your actual URL
        Field productCatalogUrlField = ProductRESTController.class.getDeclaredField("getReservedQty");
        productCatalogUrlField.setAccessible(true);
        productCatalogUrlField.set(productRestController, getReservedQty);

        when(restTemplate.getForObject(getReservedQty, Integer.class, mockProduct.getId())).thenReturn(1);
        when(productService.findCatalog(pageable)).thenReturn(mockedProductPage);

        when(itemService.findByProductId(any())).thenReturn(itemList);

        // Execute
        PageImpl result = (PageImpl) productRestController.getCatalog(pageable);

        // Verify
        assertNotNull(result);

    }

    @Test
    void getCatalogTest_Exception() throws NoSuchFieldException, IllegalAccessException {
        // Setup
        Pageable pageable = Pageable.unpaged();
        Page<Product> mockedProductPage = new PageImpl<>(Arrays.asList(mockProduct));
        List<Item> itemList = Arrays.asList(new Item("111",ItemStatus.OPERATIONAL,1L));

        MockMvcBuilders.standaloneSetup(productRestController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String getReservedQty = "http://example.com/products/{productId}";// replace with your actual URL
        Field productCatalogUrlField = ProductRESTController.class.getDeclaredField("getReservedQty");
        productCatalogUrlField.setAccessible(true);
        productCatalogUrlField.set(productRestController, getReservedQty);
        when(productService.findCatalog(pageable)).thenReturn(mockedProductPage);
        when(restTemplate.getForObject(getReservedQty, Integer.class, mockProduct.getId()))
                .thenThrow(new RestClientException("Simulating RestClientException"));

        // Execute and verify the exception
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class,
                () -> productRestController.getCatalog(pageable));

        assertEquals("Could not found the item: null", thrownException.getMessage());

        // Verify


    }


}