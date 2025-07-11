package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.exceptions.PetShopIntegrityException;
import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.CustomerCreateDTO;
import com.lucasdev.petshop_api.model.DTO.CustomerResponseDTO;
import com.lucasdev.petshop_api.model.DTO.CustomerSummaryDTO;
import com.lucasdev.petshop_api.model.DTO.CustomerUpdateDTO;
import com.lucasdev.petshop_api.model.entities.Customer;
import com.lucasdev.petshop_api.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    // -- The dependencies and mocks --

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    // -- test preparation --

    long id = 123L;
    long fake_id = 985412654L;

    Customer c1, c2;
    CustomerCreateDTO customerCreateDTO;
    CustomerUpdateDTO customerUpdateDTO;
    CustomerSummaryDTO customerSummaryDTO;
    CustomerResponseDTO customerResponseDTO;

    @BeforeEach
    void setUp() {

        c1 = new Customer(id, "Lucas", "lucas@gmail.com", "5112345678", "12345678910");

        c2 = new Customer(id, "Fulano", "fulano@gmail.com", "51999999999", "12345678911");

        customerCreateDTO = new CustomerCreateDTO(c1);

        customerUpdateDTO = new CustomerUpdateDTO(c2);

        customerSummaryDTO = new CustomerSummaryDTO(c1);

        customerResponseDTO = new CustomerResponseDTO(c1);
    }


    // -- tests --

    @Test
    @DisplayName("This method should return the correct customer when id is valid")
    void findById_shouldReturnCustomer_whenIdIsValid() {

        when(customerRepository.findById(id)).thenReturn(Optional.of(c1));

        CustomerResponseDTO result = customerService.findById(id);

        assertNotNull(result);
        Assertions.assertEquals("Lucas", result.name());
        Assertions.assertEquals("12345678910", result.cpf());
        Assertions.assertEquals(id, result.id());

        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("This method should throw my exception when id invalid")
    void findById_shouldReturnException_whenIdIsInvalid() {

        when(customerRepository.findById(fake_id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()-> customerService.findById(fake_id));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Customer with id " + fake_id + " not found.", exception.getMessage());

        verify(customerRepository, times(1)).findById(fake_id);
    }

    @Test
    @DisplayName("This method should return the correct customer when cpf is valid")
    void findByCpf_shouldReturnCustomer_whenCpfIsValid() {

        when(customerRepository.findByCpf(c1.getCpf())).thenReturn(Optional.of(c1));

        CustomerResponseDTO result = customerService.findByCpf(c1.getCpf());

        assertNotNull(result);
        Assertions.assertEquals("Lucas", result.name());
        Assertions.assertEquals("12345678910", result.cpf());
        Assertions.assertEquals(id, result.id());

        verify(customerRepository, times(1)).findByCpf(c1.getCpf());
    }

    @Test
    @DisplayName("This method should throw my exception when cpf invalid")
    void findByCpf_shouldReturnException_whenCpfIsInvalid() {

        String fakeCpf = "12345698787";

        when(customerRepository.findByCpf(fakeCpf)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()-> customerService.findByCpf(fakeCpf));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Customer with CPF " + fakeCpf + " not found.", exception.getMessage());

        verify(customerRepository, times(1)).findByCpf(fakeCpf);
    }

    @Test
    @DisplayName("This method should return a list of all customers")
    void findAll_shouldReturnAllCustomers() {

        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CustomerSummaryDTO> result = customerService.findAll();

        assertNotNull(result);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Lucas", result.get(0).name());
        Assertions.assertEquals("12345678910", result.get(0).cpf());
        Assertions.assertEquals(id, result.get(0).id());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("This method should return the correct customer entity when id is valid")
    void findEntityById_shouldReturnCustomer_whenIdIsValid() {

        when(customerRepository.findById(id)).thenReturn(Optional.of(c1));

        Customer result = customerService.findEntityById(id);

        assertNotNull(result);
        Assertions.assertEquals("Lucas", result.getName());
        Assertions.assertEquals("12345678910", result.getCpf());
        Assertions.assertEquals(id, result.getId());

        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("This method should throw my exception when id invalid")
    void findEntityById_shouldReturnException_whenIdIsInvalid() {

        when(customerRepository.findById(fake_id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()-> customerService.findEntityById(fake_id));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Customer with id " + fake_id + " not found.", exception.getMessage());

        verify(customerRepository, times(1)).findById(fake_id);
    }

    @Test
    @DisplayName("This method should insert a customer and return a Response when args is correct.")
    void insert_shouldReturnCustomer_whenArgsIsCorrect() {

        when(customerRepository.save(any(Customer.class))).thenReturn(c1);

        CustomerResponseDTO result = customerService.insert(customerCreateDTO);

        assertNotNull(result);

        assertEquals("Lucas", result.name());
        assertEquals("12345678910", result.cpf());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("This method should return my exception if parameters are incorrect.")
    void insert_shouldReturnException_ifParametersAreIncorrect() {

        CustomerCreateDTO dtoFake = new CustomerCreateDTO("Novo Cliente",
                "novo@email.com",
                "51987654321",
                c1.getCpf()
        );

        when(customerRepository.existsByCpf(dtoFake.cpf())).thenReturn(true);

        Assertions.assertThrows(PetShopIntegrityException.class, () -> customerService.insert(dtoFake));

        verify(customerRepository, times(1)).existsByCpf(dtoFake.cpf());
        verify(customerRepository, never()).existsByEmail(dtoFake.email());

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("This method should update e return the customer")
    void update_shouldReturnCustomer_whenCustomerIsValid() {

        when(customerRepository.findById(id)).thenReturn(Optional.of(c1));

        CustomerResponseDTO result = customerService.update(id, customerUpdateDTO);

        assertNotNull(result);
    }

    @Test
    @DisplayName("This method should delete the customer if id valid.")
    void delete_shouldReturnCustomer_whenCustomerIsValid() {

        when(customerRepository.existsById(id)).thenReturn(true);

        doNothing().when(customerRepository).deleteById(id);
        Assertions.assertDoesNotThrow(() -> customerService.deleteById(id));

        verify(customerRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("This method should not delete the customer if id incorrect.")
    void delete_shouldReturnException_whenCustomerIsInvalid() {

        when(customerRepository.existsById(fake_id)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class , () -> customerService.deleteById(fake_id));

        verify(customerRepository, never()).deleteById(fake_id);
    }


}