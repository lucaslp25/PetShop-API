package com.lucasdev.petshop_api.configuration;

import com.lucasdev.petshop_api.model.entities.Customer;
import com.lucasdev.petshop_api.model.entities.Employee;
import com.lucasdev.petshop_api.model.entities.Pet;
import com.lucasdev.petshop_api.model.entities.Product;
import com.lucasdev.petshop_api.model.enums.PetType;
import com.lucasdev.petshop_api.repositories.CustomerRepository;
import com.lucasdev.petshop_api.repositories.EmployeeRepository;
import com.lucasdev.petshop_api.repositories.PetRepository;
import com.lucasdev.petshop_api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataBasePopulation implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public void run(String... args) throws Exception {

        customerRepository.deleteAll();
        petRepository.deleteAll();
        productRepository.deleteAll();
        employeeRepository.deleteAll();

        Customer customer1 = new Customer(null, "Lucas", "lucas@gmail.com", "99999999999", "12345678910");

        Customer customer2 = new Customer(null, "Fulano", "fulano@gmail.com", "51999999999", "12345678911");

        customerRepository.saveAll(List.of(customer1, customer2));

        Pet pet1 = new Pet(null, "Mel", PetType.DOG, customer1);
        Pet pet2 = new Pet(null, "Rex", PetType.DOG, customer1);
        Pet pet3 = new Pet(null, "Ruby", PetType.DOG, customer2);

        petRepository.saveAll(List.of(pet1, pet2, pet3));

        Product p1 = new Product(null, "Golden Dog Food", "Golden brand dog food for adult dogs, chicken and rice flavor, 15kg bag.", new BigDecimal("150.00"), 50);
        Product p2 = new Product(null, "Durable Chew Toy", "Durable chew toy for medium-sized dogs, helps with dental hygiene.", new BigDecimal("35.50"), 100);
        Product p3 = new Product(null, "Flea & Tick Collar", "Collar for dogs and cats, provides up to 8 months of protection against fleas and ticks.", new BigDecimal("89.90"), 30);

        productRepository.saveAll(List.of(p1, p2, p3) );

        Employee employee1 = new Employee(null, "Alice", "alice@email.com", "98765432100", "11122233344");
        Employee employee2 = new Employee(null, "Bob", "bob@email.com", "99887766555", "55566677788");

        employeeRepository.saveAll(List.of(employee1, employee2));
    }
}
