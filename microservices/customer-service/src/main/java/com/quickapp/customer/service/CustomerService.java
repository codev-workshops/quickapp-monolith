package com.quickapp.customer.service;

import com.quickapp.customer.dto.CustomerDTO;
import com.quickapp.customer.model.Customer;
import com.quickapp.customer.model.Gender;
import com.quickapp.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return toDTO(customer);
    }

    public CustomerDTO createCustomer(CustomerDTO dto, String userId) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        if (dto.getGender() != null) {
            customer.setGender(Gender.valueOf(dto.getGender()));
        }
        customer.setCreatedBy(userId);
        customer.setUpdatedBy(userId);
        customer = customerRepository.save(customer);
        return toDTO(customer);
    }

    public CustomerDTO updateCustomer(int id, CustomerDTO dto, String userId) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        if (dto.getName() != null) customer.setName(dto.getName());
        if (dto.getEmail() != null) customer.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) customer.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getAddress() != null) customer.setAddress(dto.getAddress());
        if (dto.getCity() != null) customer.setCity(dto.getCity());
        if (dto.getGender() != null) customer.setGender(Gender.valueOf(dto.getGender()));
        customer.setUpdatedBy(userId);
        customer = customerRepository.save(customer);
        return toDTO(customer);
    }

    public void deleteCustomer(int id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found");
        }
        customerRepository.deleteById(id);
    }

    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setAddress(customer.getAddress());
        dto.setCity(customer.getCity());
        dto.setGender(customer.getGender().name());
        return dto;
    }
}
