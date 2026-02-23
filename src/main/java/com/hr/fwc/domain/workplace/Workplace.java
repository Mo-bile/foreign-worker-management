package com.hr.fwc.domain.workplace;

import java.time.LocalDateTime;
import java.util.Objects;

public class Workplace {

    private Long id;

    private String name;

    private String businessNumber;

    private String address;

    private String contactPhone;

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

    public static Workplace reconstitute(Long id, String name, String businessNumber, String address,
                                         String contactPhone, LocalDateTime createdAt) {
        Workplace w = new Workplace(name, businessNumber, address, contactPhone);
        w.id = id;
        w.createdAt = createdAt;
        return w;
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

    public LocalDateTime createdAt() {
        return createdAt;
    }

}
