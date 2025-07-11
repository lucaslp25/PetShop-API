package com.lucasdev.petshop_api.configuration;

import com.lucasdev.petshop_api.model.entities.Customer;
import com.lucasdev.petshop_api.model.entities.Pet;
import com.lucasdev.petshop_api.model.enums.PetType;
import com.lucasdev.petshop_api.repositories.CustomerRepository;
import com.lucasdev.petshop_api.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataBasePopulation implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;


    @Override
    public void run(String... args) throws Exception {

        customerRepository.deleteAll();
        petRepository.deleteAll();

        Customer customer1 = new Customer(null, "Lucas", "lucas@gmail.com", "99999999999", "12345678910");

        Customer customer2 = new Customer(null, "Fulano", "fulano@gmail.com", "51999999999", "12345678911");

        customerRepository.saveAll(List.of(customer1, customer2));

        Pet pet1 = new Pet(null, "Mel", PetType.DOG, customer1);
        Pet pet2 = new Pet(null, "Rex", PetType.DOG, customer1);
        Pet pet3 = new Pet(null, "Ruby", PetType.DOG, customer2);

        petRepository.saveAll(List.of(pet1, pet2, pet3));
    }
}
