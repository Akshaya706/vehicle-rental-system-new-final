package com.rental.service;

import com.rental.entity.Customer;
import com.rental.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
    }

    public Customer getCustomerByUserId(Integer userId) {
        return customerRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Customer record not found for User ID: " + userId));
    }

    @Transactional
    public Customer updateCustomer(Integer id, Customer updated) {
        Customer existing = getCustomerById(id);
        
        if (!existing.getDrivingLicence().equalsIgnoreCase(updated.getDrivingLicence().trim())) {
            if (customerRepository.existsByDrivingLicence(updated.getDrivingLicence().trim())) {
                throw new IllegalArgumentException("Driving Licence '" + updated.getDrivingLicence() + "' is already registered to another customer.");
            }
            existing.setDrivingLicence(updated.getDrivingLicence().trim());
        }

        existing.setName(updated.getName().trim());
        existing.setAddress(updated.getAddress().trim());
        existing.setPhone(updated.getPhone().trim());

        return customerRepository.save(existing);
    }

    @Transactional
    public void deleteCustomer(Integer id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }
}
