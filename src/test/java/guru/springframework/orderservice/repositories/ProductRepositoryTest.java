package guru.springframework.orderservice.repositories;

import guru.springframework.orderservice.domain.Product;
import guru.springframework.orderservice.domain.ProductStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setDescription("Samsung X2300");
        product.setProductStatus(ProductStatus.IN_STOCK);

        Product savedProduct = productRepository.save(product);

        Product fetchedProduct = productRepository.findById(savedProduct.getId()).get();
        assertNotNull(fetchedProduct.getId());
        assertNotNull(fetchedProduct.getCreatedDate());
        assertNotNull(fetchedProduct.getLastModifiedDate());

    }
}