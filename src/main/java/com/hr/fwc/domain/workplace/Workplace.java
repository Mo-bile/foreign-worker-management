package com.hr.fwc.domain.workplace;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "workplaces")
public class Workplace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "business_number", nullable = false, length = 20)
    private String businessNumber;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Workplace() {
    }

    private Workplace(String name, String businessNumber, String address, String contactPhone) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.businessNumber = Objects.requireNonNull(businessNumber, "Business number cannot be null");
        this.address = address;
        this.contactPhone = contactPhone;
        this.createdAt = LocalDateTime.now();
    }

    public static Workplace create(String name, String businessNumber, String address, String contactPhone) {
        return new Workplace(name, businessNumber, address, contactPhone);
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String businessNumber() {
        return businessNumber;
    }

    public String address() {
        return address;
    }

    public String contactPhone() {
        return contactPhone;
    }

}
