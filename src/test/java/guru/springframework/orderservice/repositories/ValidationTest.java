package guru.springframework.orderservice.repositories;

import guru.springframework.orderservice.domain.Address;
import guru.springframework.orderservice.domain.Customer;
import guru.springframework.orderservice.services.ProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ValidationTest {

    @Autowired
    CustomerRepository customerRepository;


    @Test
    void testCustomerValidation() {

        Customer invalidCustomer = new Customer();
        invalidCustomer.setEmail("invalid@@@email.com");
        Address address = new Address();
        address.setAddress("too Long address 012345678901234567890123456789");
        address.setCity("too Long city 012345678901234567890123456789");
        address.setState("too Long state 012345678901234567890123456789");
        address.setZipCode("too Long zipCode 012345678901234567890123456789");
        invalidCustomer.setAddress(address);
        invalidCustomer.setPhone("too Long phone 012345678901234567890123456789");

        try {
            customerRepository.save(invalidCustomer);
            fail(" Should Throw Exception");
        }catch (Exception e){
            assertThat(ConstraintViolationException.class).isEqualTo(e.getClass());
            var cve = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();
            assertThat(6).isEqualTo(constraintViolations.size());
            var messages = constraintViolations.stream()
                    .map(ConstraintViolation::getMessageTemplate)
                    .collect(Collectors.toSet());
            System.out.println(" TESTLOG :" + messages);
            assertThat(messages.contains("{jakarta.validation.constraints.Email.message}")).isEqualTo(true);
        }

    }
}
