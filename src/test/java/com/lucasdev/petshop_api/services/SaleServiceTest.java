package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.exceptions.PetShopSaleException;
import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.CreateOrderItemDTO;
import com.lucasdev.petshop_api.model.DTO.CreatePetShopServiceDTO;
import com.lucasdev.petshop_api.model.DTO.SaleCreateDTO;
import com.lucasdev.petshop_api.model.DTO.SaleResponseDTO;
import com.lucasdev.petshop_api.model.entities.*;
import com.lucasdev.petshop_api.model.enums.PaymentMode;
import com.lucasdev.petshop_api.model.enums.PaymentStatus;
import com.lucasdev.petshop_api.model.enums.PetType;
import com.lucasdev.petshop_api.model.enums.ServiceType;
import com.lucasdev.petshop_api.repositories.SaleRepository;
import com.lucasdev.petshop_api.security.model.User;
import com.lucasdev.petshop_api.security.model.UserRole;
import com.lucasdev.petshop_api.security.services.UserService;
import com.lucasdev.petshop_api.services.payment.PaymentResultDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    SaleRepository repository;

    @Mock
    UserService userService;

    @Mock
    ProductService productService;

    @Mock
    PetShopServiceService petShopServiceService;

    @Mock
    PetService petService;

    @Mock
    PaymentService paymentService;

    @InjectMocks
    SaleService saleService;

    long id = 1L;
    long fakeId = 253L;

    User customer, employee;
    Product product;
    PetShopService petShopService;
    Pet pet1;

    @BeforeEach
    void setUp() {

        employee = new User(1L, "bob.employee", "password", UserRole.EMPLOYEE, null, null);
        Employee employeeEntity = new Employee(null, "Bob", "bob@email.com", "99887766555", "55566677788");
        employee.setEmployeeProfile(employeeEntity);

        customer = new User(2L, "lucas.customer", "password", UserRole.CUSTOMER, null, null);
        Customer customerEntity = new Customer(null, "Lucas", "lucas@gmail.com", "99999999999", "12345678910");
        customer.setCustomerProfile(customerEntity);

        product = new Product(id, "Golden Dog Food", "Golden brand dog food for adult dogs, chicken and rice flavor, 15kg bag.", new BigDecimal("150.00"), 50);

        petShopService = new PetShopService(id, "Fast Shower", ServiceType.BATH_AND_GROOMING, BigDecimal.valueOf(158.80));

        pet1 = new Pet(id, "Mel", PetType.DOG, customer.getCustomerProfile());
    }

    //happy ways

    @Test
    @DisplayName("Should make successfully the sale, when the parameters are corrects")
    void insertSale_shouldSuccessfullyTheSale_whenTheParametersAreCorrects() {

        int quantityToBuy = 5;
        int initialStock = product.getStock();

        SaleCreateDTO createDTO = new SaleCreateDTO(
                customer.getLogin(),
                employee.getLogin(),
                pet1.getId(),
                List.of(new CreateOrderItemDTO(product.getId(), quantityToBuy)),
                List.of(new CreatePetShopServiceDTO(petShopService.getId())),
                PaymentMode.DEBIT_CARD
        );

        //see if the entities exists by login
        when(userService.findUserByLogin("lucas.customer")).thenReturn(customer);
        when(userService.findUserByLogin("bob.employee")).thenReturn(employee);

        when(productService.findEntityById(product.getId())).thenReturn(product);
        when(petService.findEntityById(pet1.getId())).thenReturn(pet1);

        when(petShopServiceService.findEntityById(petShopService.getId())).thenReturn(petShopService);
        when(paymentService.executePaymentForOrder(any(Sale.class))).thenReturn(new PaymentResultDTO(true, "GTHY-LORG-9487", "Payment successfully!"));

        when(repository.save(any(Sale.class))).thenAnswer(lp -> lp.getArgument(0));

        SaleResponseDTO resultDTO = saleService.insertSale(createDTO);

        int finalstock = initialStock - quantityToBuy;

        Assertions.assertNotNull(resultDTO);
        Assertions.assertEquals(0, resultDTO.total_price().compareTo(product.getPrice().multiply(new BigDecimal(quantityToBuy)).add(petShopService.getPrice())), "Should be the correct price in sale(resultDTO)");
        Assertions.assertEquals(resultDTO.customer().name(), "Lucas");
        Assertions.assertEquals(resultDTO.employee().name(), "Bob");
        Assertions.assertEquals(resultDTO.products().size(), 1);
        Assertions.assertEquals(resultDTO.petShop_services().size(), 1);
        Assertions.assertEquals(finalstock, product.getStock(), "Should has been updated the stock");
        Assertions.assertEquals(resultDTO.payment().status(), PaymentStatus.APPROVED);

        verify(userService, times(1)).findUserByLogin("lucas.customer");
        verify(userService, times(1)).findUserByLogin("bob.employee");
        verify(repository, times(1)).save(any(Sale.class));
    }

    @Test
    @DisplayName("Should make successfully the sale, even if has only products")
    void insertSale_shouldSuccessfullyTheSale_whenHasOnlyProducts() {

        int quantityToBuy = 5;
        int initialStock = product.getStock();

        SaleCreateDTO createDTO = new SaleCreateDTO(
                customer.getLogin(),
                employee.getLogin(),
                null,
                List.of(new CreateOrderItemDTO(product.getId(), quantityToBuy)),
                null, //create without the services
                PaymentMode.DEBIT_CARD
        );


        when(userService.findUserByLogin("lucas.customer")).thenReturn(customer);
        when(userService.findUserByLogin("bob.employee")).thenReturn(employee);

        when(productService.findEntityById(product.getId())).thenReturn(product);

        when(paymentService.executePaymentForOrder(any(Sale.class))).thenReturn(new PaymentResultDTO(true, "GTHY-LORG-9487", "Payment successfully!"));

        when(repository.save(any(Sale.class))).thenAnswer(lp -> lp.getArgument(0));

        SaleResponseDTO resultDTO = saleService.insertSale(createDTO);

        int finalstock = initialStock - quantityToBuy;

        Assertions.assertNotNull(resultDTO);
        Assertions.assertEquals(0, resultDTO.total_price().compareTo(product.getPrice().multiply(new BigDecimal(quantityToBuy))), "Should be the correct price in sale(resultDTO)");
        Assertions.assertEquals(resultDTO.customer().name(), "Lucas");
        Assertions.assertEquals(resultDTO.employee().name(), "Bob");
        Assertions.assertEquals(resultDTO.products().size(), 1);
        Assertions.assertEquals(finalstock, product.getStock(), "Should has been updated the stock");
        Assertions.assertEquals(resultDTO.payment().status(), PaymentStatus.APPROVED);

        verify(userService, times(1)).findUserByLogin("lucas.customer");
        verify(userService, times(1)).findUserByLogin("bob.employee");
        verify(petService, never()).findEntityById(any());

        verify(petShopServiceService, never()).findEntityById(any());
        verify(repository, times(1)).save(any(Sale.class));
    }

    @Test
    @DisplayName("Should make successfully the sale, even if has only services")
    void insertSale_shouldSuccessfullyTheSale_whenHasOnlyServices() {

        SaleCreateDTO createDTO = new SaleCreateDTO(
                customer.getLogin(),
                employee.getLogin(),
                pet1.getId(),
                null, //create without the product
                List.of(new CreatePetShopServiceDTO(petShopService.getId())),
                PaymentMode.DEBIT_CARD
        );


        when(userService.findUserByLogin("lucas.customer")).thenReturn(customer);
        when(userService.findUserByLogin("bob.employee")).thenReturn(employee);

        when(petService.findEntityById(pet1.getId())).thenReturn(pet1);
        when(petShopServiceService.findEntityById(petShopService.getId())).thenReturn(petShopService);

        when(paymentService.executePaymentForOrder(any(Sale.class))).thenReturn(new PaymentResultDTO(true, "GTHY-LORG-9487", "Payment successfully!"));

        when(repository.save(any(Sale.class))).thenAnswer(lp -> lp.getArgument(0));

        SaleResponseDTO resultDTO = saleService.insertSale(createDTO);

        Assertions.assertNotNull(resultDTO);
        Assertions.assertEquals(0, resultDTO.total_price().compareTo(petShopService.getPrice()), "Should be the correct price in sale(resultDTO)");
        Assertions.assertEquals(resultDTO.customer().name(), "Lucas");
        Assertions.assertEquals(resultDTO.employee().name(), "Bob");
        Assertions.assertEquals(resultDTO.petShop_services().size(), 1);
        Assertions.assertEquals(resultDTO.products().size(), 0);
        Assertions.assertEquals(resultDTO.payment().status(), PaymentStatus.APPROVED);


        verify(userService, times(1)).findUserByLogin("lucas.customer");
        verify(userService, times(1)).findUserByLogin("bob.employee");
        verify(petService, times(1)).findEntityById(pet1.getId());
        verify(productService, never()).findEntityById(any());
        verify(petShopServiceService, times(1)).findEntityById(petShopService.getId());
        verify(repository, times(1)).save(any(Sale.class));
    }

    //sad ways

    @Test
    @DisplayName("Should create successfully the sale, even if payments denied, and the stock cannot be decreased")
    void insertSale_shouldSuccessfullyTheSale_whenPaymentsDeniedAndStockCannotBeDecreased() {

        int quantityToBuy = 5;
        int initialStock = product.getStock();

        SaleCreateDTO createDTO = new SaleCreateDTO(
                customer.getLogin(),
                employee.getLogin(),
                null,
                List.of(new CreateOrderItemDTO(product.getId(), quantityToBuy)),
                null, //create without the services
                PaymentMode.DEBIT_CARD
        );


        when(userService.findUserByLogin("lucas.customer")).thenReturn(customer);
        when(userService.findUserByLogin("bob.employee")).thenReturn(employee);

        when(productService.findEntityById(product.getId())).thenReturn(product);

        //here whe teaches to fail the payment
        when(paymentService.executePaymentForOrder(any(Sale.class))).thenReturn(new PaymentResultDTO(false, "GTHY-LORG-9487", "Payment Denied!"));

        when(repository.save(any(Sale.class))).thenAnswer(lp -> lp.getArgument(0));

        SaleResponseDTO resultDTO = saleService.insertSale(createDTO);

        Assertions.assertNotNull(resultDTO);
        Assertions.assertEquals(0, resultDTO.total_price().compareTo(product.getPrice().multiply(new BigDecimal(quantityToBuy))), "Should be the correct price in sale(resultDTO)");
        Assertions.assertEquals(resultDTO.customer().name(), "Lucas");
        Assertions.assertEquals(resultDTO.employee().name(), "Bob");
        Assertions.assertEquals(resultDTO.products().size(), 1);
        Assertions.assertEquals(initialStock, product.getStock(), "Should not be updated the stock");
        Assertions.assertEquals(resultDTO.payment().status(), PaymentStatus.REJECTED); //have to stay rejected the payment status

        verify(userService, times(1)).findUserByLogin("lucas.customer");
        verify(userService, times(1)).findUserByLogin("bob.employee");
        verify(petService, never()).findEntityById(any());

        verify(petShopServiceService, never()).findEntityById(any());
        verify(repository, times(1)).save(any(Sale.class));
    }

    @Test
    @DisplayName("Should throws my personalized exception whne the customer id is incorrect")
    void insertSale_shouldFailAndthrowsMyException_whenCustomerDontExists() {

        String fakeCustomer = "wrongCustomer";

        SaleCreateDTO createDTO = new SaleCreateDTO(
                fakeCustomer,
                employee.getLogin(),
                null,
                List.of(new CreateOrderItemDTO(product.getId(), 1)),
                null, //create without the services
                PaymentMode.DEBIT_CARD
        );

        when(userService.findUserByLogin(fakeCustomer)).thenThrow(new ResourceNotFoundException("User cannot be find because the " + fakeCustomer + " don´t exists"));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> saleService.insertSale(createDTO));

        verify(userService, times(1)).findUserByLogin(nullable(String.class));
        verify(petService, never()).findEntityById(any());

        verify(petShopServiceService, never()).findEntityById(any());
        verify(repository, never()).save(any(Sale.class));
    }

    @Test
    @DisplayName("Should throws my personalized exception when the employee id is incorrect")
    void insertSale_shouldFailAndthrowsMyException_whenEmployeeDontExists() {

        String wrongEmployee = "wrongEmployee";

        SaleCreateDTO createDTO = new SaleCreateDTO(
                customer.getLogin(),
                wrongEmployee,
                null,
                List.of(new CreateOrderItemDTO(product.getId(), 1)),
                null, //create without the services
                PaymentMode.DEBIT_CARD
        );

        when(userService.findUserByLogin(customer.getLogin())).thenReturn(customer);

        when(userService.findUserByLogin(wrongEmployee)).thenThrow(new ResourceNotFoundException("User cannot be find because the " + wrongEmployee + " don´t exists"));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> saleService.insertSale(createDTO));

        verify(userService, times(2)).findUserByLogin(nullable(String.class));
        verify(petService, never()).findEntityById(any());

        verify(petShopServiceService, never()).findEntityById(any());
        verify(repository, never()).save(any(Sale.class));
    }

    @Test
    @DisplayName("Should throws my personalized exception when don´t exists the Products and Services in list")
    void insertSale_shouldFailAndthrowsMyException_whenDontExistAnyItemsOnOrders() {

        SaleCreateDTO createDTO = new SaleCreateDTO(
                customer.getLogin(),
                employee.getLogin(),
                null,
                null,
                null, //create without the services
                PaymentMode.DEBIT_CARD
        );

        PetShopSaleException ex = Assertions.assertThrows(PetShopSaleException.class, () -> saleService.insertSale(createDTO));

        Assertions.assertNotNull(ex);

        verify(userService, never()).findUserByLogin(nullable(String.class));
        verify(petService, never()).findEntityById(any());

        verify(petShopServiceService, never()).findEntityById(any());
        verify(repository, never()).save(any(Sale.class));
    }

    @Test
    @DisplayName("This method will return the correct Sale if id is Correct")
    void findById_whenIdExists_shouldReturnSale() {

        Payment pay = new Payment();

        Sale sale = new Sale(id, Instant.now(), BigDecimal.ZERO, customer, employee,pay , new ArrayList<>(), new ArrayList<>());

        when(repository.findById(id)).thenReturn(Optional.of(sale));

        SaleResponseDTO result = saleService.findById(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.id());
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("This method will return my exception if id is incorrect")
    void findById_shouldreturnMyPersonalizedException_whneIdInconrrect() {

        when(repository.findById(fakeId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, ()-> saleService.findById(fakeId));

        Assertions.assertNotNull(ex);
        Assertions.assertEquals(ex.getMessage(), "Not found sale with id: " + fakeId);
        verify(repository, times(1)).findById(fakeId);
    }


    @Test
    @DisplayName("This method will return a list of sales")
    void findAll_shouldReturnAListOfSales() {

        Payment pay = new Payment();

        Sale sale = new Sale(id, Instant.now(), BigDecimal.ZERO, customer, employee,pay , new ArrayList<>(), new ArrayList<>());

        when(repository.findAll()).thenReturn(List.of(sale));

        List<SaleResponseDTO> result = saleService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.get(0).id());
        Assertions.assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }
}
