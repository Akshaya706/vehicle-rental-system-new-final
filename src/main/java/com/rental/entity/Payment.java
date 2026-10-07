package com.rental.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @Column(name = "booking_id", nullable = false)
    private Integer bookingId;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod; // Credit Card, Debit Card, UPI, Net Banking

    @Column(nullable = false)
    private Double amount;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "PAID"; // PAID, PENDING, REFUNDED

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate = LocalDate.now();

    @Column(name = "transaction_ref")
    private String transactionRef;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", insertable = false, updatable = false)
    private Booking booking;

    public Payment() {}

    public Payment(Integer bookingId, String paymentMethod, Double amount, String paymentStatus, String transactionRef) {
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = LocalDate.now();
        this.transactionRef = transactionRef;
    }

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}
