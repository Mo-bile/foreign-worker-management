package com.hr.fwc.domain.company;

import java.time.LocalDateTime;
import java.util.Objects;

public class Company {

    private Long id;
    private String name;
    private String businessNumber;
    private Region region;
    private String subRegion;
    private IndustryCategory industryCategory;
    private String industrySubCategory;
    private int employeeCount;
    private int foreignWorkerCount;
    private String address;
    private String contactPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Company() {
    }

    private Company(String name, String businessNumber,
                    Region region, String subRegion,
                    IndustryCategory industryCategory, String industrySubCategory,
                    int employeeCount, int foreignWorkerCount,
                    String address, String contactPhone) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.businessNumber = Objects.requireNonNull(businessNumber, "Business number cannot be null");
        this.region = Objects.requireNonNull(region, "Region cannot be null");
        this.industryCategory = Objects.requireNonNull(industryCategory, "Industry category cannot be null");
        validateEmployeeCounts(employeeCount, foreignWorkerCount);
        this.subRegion = subRegion;
        this.industrySubCategory = industrySubCategory;
        this.employeeCount = employeeCount;
        this.foreignWorkerCount = foreignWorkerCount;
        this.address = address;
        this.contactPhone = contactPhone;
        this.createdAt = LocalDateTime.now();
    }

    public static Company create(String name, String businessNumber,
                                  Region region, String subRegion,
                                  IndustryCategory industryCategory, String industrySubCategory,
                                  int employeeCount, int foreignWorkerCount,
                                  String address, String contactPhone) {
        return new Company(name, businessNumber, region, subRegion,
            industryCategory, industrySubCategory,
            employeeCount, foreignWorkerCount, address, contactPhone);
    }

    public static Company reconstitute(Long id, String name, String businessNumber,
                                        Region region, String subRegion,
                                        IndustryCategory industryCategory, String industrySubCategory,
                                        int employeeCount, int foreignWorkerCount,
                                        String address, String contactPhone,
                                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        Company c = new Company(name, businessNumber, region, subRegion,
            industryCategory, industrySubCategory,
            employeeCount, foreignWorkerCount, address, contactPhone);
        c.id = id;
        c.createdAt = createdAt;
        c.updatedAt = updatedAt;
        return c;
    }

    public Company updateInfo(String name, Region region, String subRegion,
                               IndustryCategory industryCategory, String industrySubCategory,
                               int employeeCount, int foreignWorkerCount,
                               String address, String contactPhone) {
        Company updated = new Company(name, this.businessNumber, region, subRegion,
            industryCategory, industrySubCategory,
            employeeCount, foreignWorkerCount, address, contactPhone);
        updated.id = this.id;
        updated.createdAt = this.createdAt;
        updated.updatedAt = LocalDateTime.now();
        return updated;
    }

    private static void validateEmployeeCounts(int employeeCount, int foreignWorkerCount) {
        if (employeeCount < 1) {
            throw new IllegalArgumentException("Employee count must be at least 1");
        }
        if (foreignWorkerCount < 0) {
            throw new IllegalArgumentException("Foreign worker count cannot be negative");
        }
        if (foreignWorkerCount > employeeCount) {
            throw new IllegalArgumentException("Foreign worker count cannot exceed employee count");
        }
    }

    public Long id() { return id; }
    public String name() { return name; }
    public String businessNumber() { return businessNumber; }
    public Region region() { return region; }
    public String subRegion() { return subRegion; }
    public IndustryCategory industryCategory() { return industryCategory; }
    public String industrySubCategory() { return industrySubCategory; }
    public int employeeCount() { return employeeCount; }
    public int foreignWorkerCount() { return foreignWorkerCount; }
    public String address() { return address; }
    public String contactPhone() { return contactPhone; }
    public LocalDateTime createdAt() { return createdAt; }
    public LocalDateTime updatedAt() { return updatedAt; }
}
