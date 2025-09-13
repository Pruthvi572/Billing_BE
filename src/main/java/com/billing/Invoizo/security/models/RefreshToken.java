package com.billing.Invoizo.security.models;

import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "refreshtoken")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

//    @OneToOne
//    @JoinColumn(name = "employeeid", referencedColumnName = "employeeid")
//    private EmployeeEntity employee;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false, name = "expirydate")
    private Instant expiryDate;

    public RefreshToken() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    public EmployeeEntity getEmployee() {
//        return employee;
//    }

//    public void setEmployee(EmployeeEntity employee) {
//        this.employee = employee;
//    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

}
