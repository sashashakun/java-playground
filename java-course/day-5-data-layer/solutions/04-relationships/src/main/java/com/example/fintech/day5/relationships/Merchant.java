package com.example.fintech.day5.relationships;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "merchants")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    private String country;

    @OneToMany(
        mappedBy = "merchant",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<Payment> payments = new ArrayList<>();

    protected Merchant() {}

    public Merchant(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setMerchant(this);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setMerchant(null);
    }

    public String getId()              { return id; }
    public String getName()            { return name; }
    public String getCountry()         { return country; }
    public List<Payment> getPayments() { return payments; }
}
