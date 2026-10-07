package com.rental.service;

import com.rental.entity.*;
import com.rental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DataInitializerService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private DamageReportRepository damageReportRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Data already exists
        }

        // 1. Seed Users (Admin, Manager, Customers)
        User admin = userRepository.save(new User("admin", "admin123", "ADMIN", "Alex Vance (Admin)", "admin@vehiclerental.com"));
        User manager = userRepository.save(new User("manager_sarah", "mgr123", "RENTAL_MANAGER", "Sarah Jenkins (Manager)", "sarah.mgr@vehiclerental.com"));
        
        User cust1User = userRepository.save(new User("john_doe", "user123", "CUSTOMER", "John Doe", "john.doe@example.com"));
        User cust2User = userRepository.save(new User("emily_r", "user123", "CUSTOMER", "Emily Rose", "emily.rose@example.com"));
        User cust3User = userRepository.save(new User("david_k", "user123", "CUSTOMER", "David Kumar", "david.kumar@example.com"));

        // 2. Seed Customers
        Customer c1 = customerRepository.save(new Customer(cust1User.getUserId(), "John Doe", "742 Evergreen Terrace, Springfield", "+1 555-0144", "DL-US-2023-9981"));
        Customer c2 = customerRepository.save(new Customer(cust2User.getUserId(), "Emily Rose", "100 Broadway Ave, New York", "+1 555-0182", "DL-NY-2024-4412"));
        Customer c3 = customerRepository.save(new Customer(cust3User.getUserId(), "David Kumar", "456 Tech Park Way, Austin", "+1 555-0199", "DL-TX-2025-1033"));

        // 3. Seed Fleet of Vehicles (Cars, SUVs, Sedans, Bikes, Electric)
        Vehicle v1 = vehicleRepository.save(new Vehicle(
                "Sedan", "Tesla", "Model 3 Long Range", "EV-TES-101", 85.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(8),
                5, "Electric", "Automatic",
                "https://images.unsplash.com/photo-1536700503339-1e4b06520771?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v2 = vehicleRepository.save(new Vehicle(
                "Luxury", "BMW", "330i M-Sport", "LUX-BMW-330", 115.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(9), LocalDate.now().plusMonths(7),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v3 = vehicleRepository.save(new Vehicle(
                "SUV", "Toyota", "Fortuner 4x4", "SUV-TOY-404", 95.0, "RENTED",
                LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(6), LocalDate.now().plusMonths(5),
                7, "Diesel", "Automatic",
                "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v4 = vehicleRepository.save(new Vehicle(
                "SUV", "Jeep", "Wrangler Rubicon", "SUV-JEP-909", 110.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(9),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1506015391300-4802dc74de2e?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v5 = vehicleRepository.save(new Vehicle(
                "Sedan", "Toyota", "Camry Hybrid", "SED-TOY-202", 65.0, "AVAILABLE",
                LocalDate.now().minusMonths(4), LocalDate.now().plusMonths(8), LocalDate.now().plusMonths(4),
                5, "Hybrid", "Automatic",
                "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v6 = vehicleRepository.save(new Vehicle(
                "Bike", "Royal Enfield", "Classic 350 Stealth", "BK-RE-350", 40.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(6),
                2, "Petrol", "Manual",
                "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v7 = vehicleRepository.save(new Vehicle(
                "Bike", "Yamaha", "YZF-R15 V4 Racing", "BK-YAM-015", 45.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(8),
                2, "Petrol", "Manual",
                "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v8 = vehicleRepository.save(new Vehicle(
                "Bike", "Harley Davidson", "Iron 883 Special", "BK-HD-883", 75.0, "UNDER_MAINTENANCE",
                LocalDate.now().minusMonths(6), LocalDate.now().plusMonths(3), LocalDate.now().plusDays(10), // Expiry warning
                2, "Petrol", "Manual",
                "https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v9 = vehicleRepository.save(new Vehicle(
                "Luxury", "Mercedes-Benz", "E-Class AMG Line", "LUX-MB-505", 130.0, "BOOKED",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(8), LocalDate.now().plusMonths(7),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?auto=format&fit=crop&w=800&q=80"
        ));

        // Additional Vehicles (Cars, Sedans, SUVs, Luxury, Electric, Bikes)
        Vehicle v10 = vehicleRepository.save(new Vehicle(
                "Car", "Honda", "Civic Sport Touring", "CAR-HON-101", 55.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(9),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1590362891991-f776e747a588?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v11 = vehicleRepository.save(new Vehicle(
                "Car", "Hyundai", "Elantra N-Line", "CAR-HYU-202", 58.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(7),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1619682817481-e994891cd1f5?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v12 = vehicleRepository.save(new Vehicle(
                "Car", "Volkswagen", "Golf GTI Turbo", "CAR-VW-303", 62.0, "AVAILABLE",
                LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(9), LocalDate.now().plusMonths(6),
                5, "Petrol", "Manual",
                "https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v13 = vehicleRepository.save(new Vehicle(
                "Car", "Toyota", "Corolla Cross Hybrid", "CAR-TOY-404", 52.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(10),
                5, "Hybrid", "Automatic",
                "https://images.unsplash.com/photo-1593460354583-4224ab273fe7?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v14 = vehicleRepository.save(new Vehicle(
                "Car", "Ford", "Mustang GT Fastback", "CAR-FRD-505", 105.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(8),
                4, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1584345604476-8ec5e12e42dd?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v15 = vehicleRepository.save(new Vehicle(
                "Sedan", "Audi", "A4 45 TFSI Quattro", "SED-AUD-505", 88.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(7),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v16 = vehicleRepository.save(new Vehicle(
                "Sedan", "Mercedes-Benz", "C-Class C300", "SED-MB-300", 98.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(8),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v17 = vehicleRepository.save(new Vehicle(
                "SUV", "Range Rover", "Velar R-Dynamic", "SUV-RR-606", 145.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(8), LocalDate.now().plusMonths(6),
                5, "Diesel", "Automatic",
                "https://images.unsplash.com/photo-1606016159991-dfe4f2746ad5?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v18 = vehicleRepository.save(new Vehicle(
                "SUV", "Mahindra", "Thar 4x4 Hardtop", "SUV-MTH-707", 68.0, "AVAILABLE",
                LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(9), LocalDate.now().plusMonths(5),
                4, "Diesel", "Manual",
                "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v19 = vehicleRepository.save(new Vehicle(
                "SUV", "Hyundai", "Creta SX Executive", "SUV-HYU-808", 62.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(9),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v20 = vehicleRepository.save(new Vehicle(
                "Electric", "Tesla", "Model Y Long Range", "EV-TES-909", 92.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(10),
                5, "Electric", "Automatic",
                "https://images.unsplash.com/photo-1560958089-b8a1929cea89?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v21 = vehicleRepository.save(new Vehicle(
                "Electric", "Hyundai", "Ioniq 5 AWD", "EV-ION-110", 85.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(8),
                5, "Electric", "Automatic",
                "https://images.unsplash.com/photo-1617788138017-80ad40651399?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v22 = vehicleRepository.save(new Vehicle(
                "Luxury", "Porsche", "911 Carrera S", "LUX-POR-911", 210.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(9),
                2, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v23 = vehicleRepository.save(new Vehicle(
                "Bike", "Kawasaki", "Ninja 400 ABS", "BK-KAW-400", 50.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(8),
                2, "Petrol", "Manual",
                "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v24 = vehicleRepository.save(new Vehicle(
                "Bike", "KTM", "Duke 390 Adventure", "BK-KTM-390", 42.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(7),
                2, "Petrol", "Manual",
                "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=800&q=80"
        ));

        // === Additional Vehicles - Expanded Fleet ===

        // Hatchback vehicles
        Vehicle v25 = vehicleRepository.save(new Vehicle(
                "Hatchback", "Volkswagen", "Polo GTI", "HB-VW-GTI", 48.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(9),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v26 = vehicleRepository.save(new Vehicle(
                "Hatchback", "Honda", "Jazz RS Sport", "HB-HON-JAZZ", 42.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(8),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1590362891991-f776e747a588?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v27 = vehicleRepository.save(new Vehicle(
                "Hatchback", "Suzuki", "Swift Sport Turbo", "HB-SUZ-SWIFT", 38.0, "AVAILABLE",
                LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(9), LocalDate.now().plusMonths(7),
                5, "Petrol", "Manual",
                "https://images.unsplash.com/photo-1619682817481-e994891cd1f5?auto=format&fit=crop&w=800&q=80"
        ));

        // Coupe vehicles
        Vehicle v28 = vehicleRepository.save(new Vehicle(
                "Coupe", "Ford", "Mustang Shelby GT500", "COU-FRD-GT500", 150.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(9),
                4, "Petrol", "Manual",
                "https://images.unsplash.com/photo-1584345604476-8ec5e12e42dd?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v29 = vehicleRepository.save(new Vehicle(
                "Coupe", "BMW", "M4 Competition", "COU-BMW-M4", 175.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(8),
                4, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&w=800&q=80"
        ));

        // Convertible
        Vehicle v30 = vehicleRepository.save(new Vehicle(
                "Convertible", "Audi", "TT RS Roadster", "CONV-AUD-TTRS", 160.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(9),
                2, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v31 = vehicleRepository.save(new Vehicle(
                "Convertible", "Mercedes-Benz", "SLC 300 AMG", "CONV-MB-SLC", 180.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(9), LocalDate.now().plusMonths(7),
                2, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?auto=format&fit=crop&w=800&q=80"
        ));

        // Van / Minivan
        Vehicle v32 = vehicleRepository.save(new Vehicle(
                "Van", "Toyota", "HiAce Commuter GL", "VAN-TOY-HIACE", 90.0, "AVAILABLE",
                LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(9), LocalDate.now().plusMonths(6),
                12, "Diesel", "Automatic",
                "https://images.unsplash.com/photo-1600861194802-a2b11076bc51?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v33 = vehicleRepository.save(new Vehicle(
                "Van", "Ford", "Transit Custom", "VAN-FRD-TRANSIT", 80.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(8),
                9, "Diesel", "Manual",
                "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80"
        ));

        // Pickup Trucks
        Vehicle v34 = vehicleRepository.save(new Vehicle(
                "Pickup", "Ford", "F-150 Raptor 4WD", "PKP-FRD-F150", 120.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(8),
                5, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v35 = vehicleRepository.save(new Vehicle(
                "Pickup", "Toyota", "Hilux Revo 4x4", "PKP-TOY-HILUX", 105.0, "AVAILABLE",
                LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(9), LocalDate.now().plusMonths(7),
                5, "Diesel", "Manual",
                "https://images.unsplash.com/photo-1506015391300-4802dc74de2e?auto=format&fit=crop&w=800&q=80"
        ));

        // More Electric vehicles
        Vehicle v36 = vehicleRepository.save(new Vehicle(
                "Electric", "Tesla", "Model S Plaid", "EV-TES-PLAID", 145.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(10),
                5, "Electric", "Automatic",
                "https://images.unsplash.com/photo-1560958089-b8a1929cea89?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v37 = vehicleRepository.save(new Vehicle(
                "Electric", "BYD", "Atto 3 AWD", "EV-BYD-ATTO3", 72.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(8),
                5, "Electric", "Automatic",
                "https://images.unsplash.com/photo-1617788138017-80ad40651399?auto=format&fit=crop&w=800&q=80"
        ));

        // More premium bikes
        Vehicle v38 = vehicleRepository.save(new Vehicle(
                "Bike", "Ducati", "Panigale V4 S", "BK-DUC-V4S", 95.0, "AVAILABLE",
                LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11), LocalDate.now().plusMonths(9),
                2, "Petrol", "Manual",
                "https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=800&q=80"
        ));

        Vehicle v39 = vehicleRepository.save(new Vehicle(
                "Bike", "BMW", "R 1250 GS Adventure", "BK-BMW-R1250", 85.0, "AVAILABLE",
                LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(8),
                2, "Petrol", "Automatic",
                "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=800&q=80"
        ));

        // 4. Seed Bookings
        // Booking 1: Pending Approval for Manager action
        Booking b1 = new Booking(
                c1.getCustomerId(), v1.getVehicleId(),
                LocalDate.now().plusDays(2), LocalDate.now().plusDays(5),
                255.0, 85.0, 3, 1.0, 0.0
        );
        b1.setVehicle(v1);
        b1.setCustomer(c1);
        b1.setBookingStatus("PENDING_APPROVAL");
        b1.setPaymentStatus("PENDING");
        b1.setManagerNotes("Customer requested GPS unit included.");
        bookingRepository.save(b1);

        // Booking 2: Approved, awaiting payment from Emily
        Booking b2 = new Booking(
                c2.getCustomerId(), v9.getVehicleId(),
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4),
                390.0, 130.0, 3, 1.0, 0.0
        );
        b2.setVehicle(v9);
        b2.setCustomer(c2);
        b2.setBookingStatus("APPROVED");
        b2.setPaymentStatus("PENDING");
        b2.setManagerNotes("Verified Driving Licence DL-NY-2024-4412. Approved.");
        bookingRepository.save(b2);

        // Booking 3: Active on Road (Fortuner with David)
        Booking b3 = new Booking(
                c3.getCustomerId(), v3.getVehicleId(),
                LocalDate.now().minusDays(2), LocalDate.now().plusDays(3),
                546.25, 95.0, 5, 1.15, 0.0 // Weekend rate
        );
        b3.setVehicle(v3);
        b3.setCustomer(c3);
        b3.setBookingStatus("ACTIVE");
        b3.setPaymentStatus("PAID");
        b3.setManagerNotes("Handover completed with full tank fuel.");
        b3 = bookingRepository.save(b3);

        Payment p3 = new Payment(b3.getBookingId(), "Credit Card", 546.25, "PAID", "TXN-882190A");
        paymentRepository.save(p3);

        // Booking 4: Completed Booking (John rented BMW last week)
        Booking b4 = new Booking(
                c1.getCustomerId(), v2.getVehicleId(),
                LocalDate.now().minusDays(8), LocalDate.now().minusDays(4),
                460.0, 115.0, 4, 1.0, 0.0
        );
        b4.setVehicle(v2);
        b4.setCustomer(c1);
        b4.setBookingStatus("COMPLETED");
        b4.setPaymentStatus("PAID");
        b4.setActualReturnDate(LocalDate.now().minusDays(4));
        b4.setReturnOdometer(14520);
        b4.setReturnFuelLevel("Full");
        b4.setManagerNotes("Vehicle returned on time in spotless condition.");
        b4 = bookingRepository.save(b4);

        Payment p4 = new Payment(b4.getBookingId(), "UPI", 460.0, "PAID", "TXN-119488B");
        paymentRepository.save(p4);

        // Review for BMW from John
        reviewRepository.save(new Review(c1.getCustomerId(), v2.getVehicleId(), b4.getBookingId(), 5, "Phenomenal driving dynamics and silky smooth engine! Flawless pickup and return experience."));

        // 5. Seed Maintenance
        Maintenance m1 = new Maintenance(
                v8.getVehicleId(),
                LocalDate.now().minusMonths(6),
                LocalDate.now().plusDays(3),
                "IN_PROGRESS",
                "Brake pad replacement and comprehensive electronic diagnosis.",
                180.0
        );
        maintenanceRepository.save(m1);

        // 6. Seed a sample Damage Report
        DamageReport dr = new DamageReport(
                b4.getBookingId(),
                v2.getVehicleId(),
                "Minor cosmetic rim scuff on rear-right alloy wheel during parallel parking.",
                65.0,
                "Sarah Jenkins (Manager)"
        );
        damageReportRepository.save(dr);
    }
}
