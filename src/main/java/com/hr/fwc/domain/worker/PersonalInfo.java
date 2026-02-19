package com.hr.fwc.domain.worker;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PersonalInfo {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "passport_number", length = 50)
    private String passportNumber;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    protected PersonalInfo() {
    }

    private PersonalInfo(String name, String passportNumber, String contactPhone, String contactEmail) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.passportNumber = passportNumber;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }

    public static PersonalInfo of(String name, String passportNumber, String contactPhone, String contactEmail) {
        return new PersonalInfo(name, passportNumber, contactPhone, contactEmail);
    }

    public String name() {
        return name;
    }

    public String passportNumber() {
        return passportNumber;
    }

    public String contactPhone() {
        return contactPhone;
    }

    public String contactEmail() {
        return contactEmail;
    }

}
