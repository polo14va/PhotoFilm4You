package edu.uoc.epcsd.productcatalog.domain.repository;

import edu.uoc.epcsd.productcatalog.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAllProducts();

    Page<Product> findCatalog(Pageable pageable);

    Optional<Product> findProductById(Long id);

    List<Product> findProductsByExample(Product product);

    Long createProduct(Product product);

    void deleteProduct(Product product);
}
