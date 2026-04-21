package com.quickapp.customer.config;

import com.quickapp.customer.model.Customer;
import com.quickapp.customer.model.Gender;
import com.quickapp.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    private final CustomerRepository customerRepository;

    public DataSeeder(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        if (customerRepository.count() == 0) {
            logger.info("Seeding customer data");

            Customer c1 = new Customer();
            c1.setName("Ebenezer Monney");
            c1.setEmail("contact@ebenmonney.com");
            c1.setGender(Gender.Male);
            customerRepository.save(c1);

            Customer c2 = new Customer();
            c2.setName("Itachi Uchiha");
            c2.setEmail("uchiha@narutoverse.com");
            c2.setPhoneNumber("+81123456789");
            c2.setAddress("Some fictional Address, Street 123, Konoha");
            c2.setCity("Konoha");
            c2.setGender(Gender.Male);
            customerRepository.save(c2);

            Customer c3 = new Customer();
            c3.setName("John Doe");
            c3.setEmail("johndoe@anonymous.com");
            c3.setPhoneNumber("+18585858");
            c3.setAddress("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio.");
            c3.setCity("Lorem Ipsum");
            c3.setGender(Gender.Male);
            customerRepository.save(c3);

            Customer c4 = new Customer();
            c4.setName("Jane Doe");
            c4.setEmail("Janedoe@anonymous.com");
            c4.setPhoneNumber("+18585858");
            c4.setAddress("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio.");
            c4.setCity("Lorem Ipsum");
            c4.setGender(Gender.Male);
            customerRepository.save(c4);

            // Additional seed data (Task 8)
            seedAdditionalCustomers();

            logger.info("Customer data seeded successfully");
        }
    }

    private void seedAdditionalCustomers() {
        String[][] extras = {
            {"Alice Johnson", "alice.johnson@example.com", "+14155551234", "123 Oak Street", "San Francisco", "Female"},
            {"Bob Williams", "bob.williams@example.com", "+12125559876", "456 Elm Avenue", "New York", "Male"},
            {"Carol Martinez", "carol.martinez@example.com", "+13105554567", "789 Pine Road", "Los Angeles", "Female"},
            {"David Brown", "david.brown@example.com", "+17735558901", "321 Maple Drive", "Chicago", "Male"},
            {"Eva Garcia", "eva.garcia@example.com", "+12815553456", "654 Cedar Lane", "Houston", "Female"},
            {"Frank Wilson", "frank.wilson@example.com", "+16025557890", "987 Birch Court", "Phoenix", "Male"},
            {"Grace Lee", "grace.lee@example.com", "+12155552345", "147 Willow Way", "Philadelphia", "Female"},
            {"Henry Taylor", "henry.taylor@example.com", "+12105556789", "258 Spruce Blvd", "San Antonio", "Male"},
            {"Iris Anderson", "iris.anderson@example.com", "+12145551230", "369 Poplar Place", "Dallas", "Female"},
            {"Jack Thomas", "jack.thomas@example.com", "+14085554560", "480 Ash Street", "San Jose", "Male"}
        };

        for (String[] data : extras) {
            Customer c = new Customer();
            c.setName(data[0]);
            c.setEmail(data[1]);
            c.setPhoneNumber(data[2]);
            c.setAddress(data[3]);
            c.setCity(data[4]);
            c.setGender(Gender.valueOf(data[5]));
            customerRepository.save(c);
        }
    }
}
