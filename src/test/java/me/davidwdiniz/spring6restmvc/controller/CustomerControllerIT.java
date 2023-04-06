package me.davidwdiniz.spring6restmvc.controller;

import me.davidwdiniz.spring6restmvc.entities.Beer;
import me.davidwdiniz.spring6restmvc.entities.Customer;
import me.davidwdiniz.spring6restmvc.mappers.CustomerMapper;
import me.davidwdiniz.spring6restmvc.model.BeerDTO;
import me.davidwdiniz.spring6restmvc.model.CustomerDTO;
import me.davidwdiniz.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testListCustomers() {
        List<CustomerDTO> customerDTOS = customerController.listCustomers();
        assertThat(customerDTOS.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyListCustomers() {
        customerRepository.deleteAll();
        List<CustomerDTO> customerDTOS = customerController.listCustomers();
        assertThat(customerDTOS.size()).isEqualTo(0);
    }

    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());
        assertThat(customerDTO).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder().name("New Customer").build();

        ResponseEntity responseEntity = customerController.handleCustomer(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Customer customer = customerRepository.findById(savedUUID).orElse(null);
        assertThat(customer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "UPDATED";
        customerDTO.setName(customerName);

        ResponseEntity responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElse(null);
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);
    }

    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testPatchCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "UPDATED";
        customerDTO.setName(customerName);

        ResponseEntity responseEntity = customerController.patchCustomerById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElse(null);
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);
    }

    @Test
    void testPatchCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.patchCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteById() {
        Customer customer = customerRepository.findAll().get(0);
        ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustomerById(UUID.randomUUID());
        });
    }
}