package me.davidwdiniz.spring6restmvc.mappers;

import me.davidwdiniz.spring6restmvc.entities.Customer;
import me.davidwdiniz.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO dto);
    CustomerDTO customerToCustomerDto(Customer customer);
}
