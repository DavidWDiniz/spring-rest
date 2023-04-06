package me.davidwdiniz.spring6restmvc.services;

import me.davidwdiniz.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();

    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO saveNewCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer);

    Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer);

    Boolean deleteBeerById(UUID customerId);
}
