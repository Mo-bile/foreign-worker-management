package com.hr.fwc.domain.worker;

import java.util.Objects;

public class PersonalInfo {

    private String name;

    private String passportNumber;

    private String contactPhone;

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
