# Vehicle Rental Management System (VRMS)
### Built for Software Engineering Laboratory &bull; IEEE 830 SRS Specification
**Prepared By:** AKSHAYA.R &bull; Department of B.Tech Information Technology &bull; Date: 01.07.2026

---

## 🌟 Executive Overview
This project is an enterprise-grade, end-to-end **Vehicle Rental Management System** built with **Java Spring Boot 3**, **Spring Data JPA**, **H2 In-Memory Database** (with ready **MySQL** DDL schema), and a **Modern Glassmorphic Web UI**.

It strictly implements all functional requirements (FR1 - FR13), data entities, constraints, and acceptance criteria specified in the SRS.

---

## 🔐 Role-Based Access Control Architecture
Per user requirements, the system strictly separates authentication, navigation, and functional capabilities across distinct portals and dashboards:

| Role | Dedicated Login | Dedicated Dashboard | Key Information & Permitted Operations |
| :--- | :--- | :--- | :--- |
| **Customer** | [`customer-login.html`](file:///C:/Users/Akshaya/.gemini/antigravity-ide/scratch/vehicle-rental-system/src/main/resources/static/customer-login.html) | [`customer-dashboard.html`](file:///C:/Users/Akshaya/.gemini/antigravity-ide/scratch/vehicle-rental-system/src/main/resources/static/customer-dashboard.html) | Search catalog with live dynamic pricing calculator, submit booking requests, review trip status, pay online with instant tax invoice receipt, extend active rental dates, submit 5-star ratings & reviews, view profile. |
| **Rental Manager** | [`manager-login.html`](file:///C:/Users/Akshaya/.gemini/antigravity-ide/scratch/vehicle-rental-system/src/main/resources/static/manager-login.html) | [`manager-dashboard.html`](file:///C:/Users/Akshaya/.gemini/antigravity-ide/scratch/vehicle-rental-system/src/main/resources/static/manager-dashboard.html) | Operational queue of pending booking approvals, driving licence verification, vehicle assignment, active return processing with odometer reading & fuel level check, damage inspection & repair cost billing, maintenance scheduling. |
| **Administrator** | [`admin-login.html`](file:///C:/Users/Akshaya/.gemini/antigravity-ide/scratch/vehicle-rental-system/src/main/resources/static/admin-login.html) | [`admin-dashboard.html`](file:///C:/Users/Akshaya/.gemini/antigravity-ide/scratch/vehicle-rental-system/src/main/resources/static/admin-dashboard.html) | Executive KPIs (fleet utilization, monthly revenue, registered users), complete fleet CRUD (enforcing unique registration numbers), customer licence auditing, insurance & pollution certificate compliance tracking, and 9 SRS reports. |

---

## 🔑 Demo Credentials (Ready-to-Use)
All users are pre-seeded in the database on application startup:

- **Customer:** `john_doe` / `user123` (Driving Licence: `DL-US-2023-9981`)
- **Customer 2:** `emily_r` / `user123` (Driving Licence: `DL-NY-2024-4412`)
- **Rental Manager:** `manager_sarah` / `mgr123`
- **System Administrator:** `admin` / `admin123`

---

## 📋 SRS Requirements Implementation Matrix

| Requirement | Implementation Details |
| :--- | :--- |
| **FR1 User Login** | Role-authenticated endpoints (`/api/auth/login`) with role guard redirecting unauthorized attempts. |
| **FR2 Vehicle Management** | Full CRUD for vehicles (`/api/vehicles`). Checks for duplicate registration numbers per Constraint 8. |
| **FR3 Customer Registration** | Self-service customer signup (`/api/auth/register-customer`) validating driving licence format and uniqueness. |
| **FR4 Vehicle Booking & Approval** | Customer submits booking; system checks for unavailable vehicles, maintenance status, and overlapping date conflicts. Rental Manager verifies DL and approves/rejects. |
| **FR5 Reservation Management** | Extension feature (`/api/bookings/{id}/extend`) calculating new duration, checking conflicts, and updating invoice. Safe cancellation with refund processing. |
| **FR6 Dynamic Rental Charge Calculation** | Dynamic pricing algorithm: base daily rate * duration + 15% weekend surcharge + optional extras ($15/day insurance waiver, $5/day GPS). |
| **FR7 Payment Processing** | Online payment simulator (Credit Card, Debit Card, UPI, Net Banking) updating status to PAID and activating vehicle pickup. Generates printable official tax invoice. |
| **FR8 Vehicle Return** | Manager records return date, ending odometer, fuel level (full/75%/50%/25%/low), calculates late fee if overdue ($30/day penalty + daily rate), updates availability. |
| **FR9 Damage Inspection** | Formal damage report generation with repair cost estimation. Automatically adds repair fee to customer's final billing invoice. |
| **FR10 Vehicle Maintenance** | Maintenance scheduling sets vehicle to `UNDER_MAINTENANCE` (blocking bookings per Constraint 8). Tracks insurance and pollution certificate expiry alerts. |
| **FR11 Vehicle Search** | Filter by vehicle type (Sedan, SUV, Bike, Luxury, Car), brand, model, daily rate, and availability. |
| **FR12 Customer Feedback** | Post-trip 1 to 5 star rating and written reviews (`/api/reviews`). |
| **FR13 Analytics & Reports** | 9 interactive audit reports (Available, Booked, Active, Returned, Revenue, Maintenance, Damage, Most Popular Vehicles, Top Customers) with CSV export and print. |

---

## 🚀 How to Run the Application

```bash
# 1. Open the project folder:
cd C:\Users\Akshaya\.gemini\antigravity-ide\scratch\vehicle-rental-system

# 2. Start the Spring Boot application using the included Maven Wrapper:
.\mvnw.cmd spring-boot:run
```

Once started:
- Open your browser to: **`http://localhost:8080/`**
- H2 Console (Database inspection): **`http://localhost:8080/h2-console`** (JDBC URL: `jdbc:h2:mem:vehiclerentaldb`, User: `sa`, Password: empty)
