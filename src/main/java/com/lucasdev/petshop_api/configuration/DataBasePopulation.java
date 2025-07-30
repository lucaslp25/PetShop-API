package com.lucasdev.petshop_api.configuration;

import com.lucasdev.petshop_api.model.entities.*;
import com.lucasdev.petshop_api.model.enums.PetType;
import com.lucasdev.petshop_api.model.enums.ServiceType;
import com.lucasdev.petshop_api.repositories.*;
import com.lucasdev.petshop_api.security.model.User;
import com.lucasdev.petshop_api.security.model.UserRole;
import com.lucasdev.petshop_api.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PetShopServiceRepository petShopServiceRepository;


    @Override
    public void run(String... args) throws Exception {

        petRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
        petShopServiceRepository.deleteAll();

        //new way than approached

        String password = "8118";
        String pass = passwordEncoder.encode(password);


        User userEmployee1 = new User(null, "alice.employee", pass, UserRole.EMPLOYEE, null, null);
        Employee employee1 = new Employee(null, "Alice", "alice@email.com", "98765432100", "11122233344");
        userEmployee1.setEmployeeProfile(employee1);

        User userEmployee2 = new User(null, "bob.employee", pass, UserRole.EMPLOYEE, null, null);
        Employee employee2 = new Employee(null, "Bob", "bob@email.com", "99887766555", "55566677788");
        userEmployee2.setEmployeeProfile(employee2);

        User userEmployee3 = new User(null, "lucas@gmail.com", pass, UserRole.EMPLOYEE, null, null);
        Employee employee3 = new Employee(null, "Lucas", "lucas@gmail.com", "98765432100", "11122233444");
        userEmployee3.setEmployeeProfile(employee3);


        User userCustomer1 = new User(null, "lucas.customer", pass, UserRole.CUSTOMER, null, null);
        Customer customer1 = new Customer(null, "Lucas", "lucas5@gmail.com", "99999999999", "12345678910");
        userCustomer1.setCustomerProfile(customer1);

        User userCustomer2 = new User(null, "fulano.customer", pass, UserRole.CUSTOMER, null, null);
        Customer customer2 = new Customer(null, "Fulano", "fulano@gmail.com", "51999999999", "12345678911");
        userCustomer2.setCustomerProfile(customer2);

        userRepository.saveAll(List.of(userEmployee1, userEmployee2, userCustomer1, userCustomer2, userEmployee3));


        //a little change here too

        Pet pet1 = new Pet(null, "Mel", PetType.DOG, userCustomer1.getCustomerProfile());
        Pet pet2 = new Pet(null, "Rex", PetType.DOG, userCustomer1.getCustomerProfile());
        Pet pet3 = new Pet(null, "Ruby", PetType.DOG, userCustomer2.getCustomerProfile());

        petRepository.saveAll(List.of(pet1, pet2, pet3));

        Product p1 = new Product(null, "Golden Dog Food", "Golden brand dog food for adult dogs, chicken and rice flavor, 15kg bag.", new BigDecimal("150.00"), "https://images.unsplash.com/photo-1647616350787-6428e907a7fa?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mzh8fHJhJUMzJUE3JUMzJUEzbyUyMGRlJTIwY2FjaG9ycm98ZW58MHx8MHx8fDA%3D", 50);
        Product p2 = new Product(null, "Durable Chew Toy", "Durable chew toy for medium-sized dogs, helps with dental hygiene.", new BigDecimal("35.50"), 100);
        Product p3 = new Product(null, "Flea & Tick Collar", "Collar for dogs and cats, provides up to 8 months of protection against fleas and ticks.", new BigDecimal("89.90"), 30);

        productRepository.saveAll(List.of(p1, p2, p3) );

        PetShopService pss1 = new PetShopService(null, "Fast Shower", ServiceType.BATH_AND_GROOMING, BigDecimal.valueOf(158.80));

        PetShopService pss2 = new PetShopService(null, "Morining training", ServiceType.TRAINING, BigDecimal.valueOf(71.84));

        petShopServiceRepository.saveAll(List.of(pss1, pss2));
    }

}
