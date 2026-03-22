package com.hr.fwc.infrastructure.persistence.entity;

import com.hr.fwc.domain.company.IndustryCategory;
import com.hr.fwc.domain.company.Region;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "business_number", nullable = false, unique = true, length = 20)
    private String businessNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false, length = 30)
    private Region region;

    @Column(name = "sub_region", length = 50)
    private String subRegion;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry_category", nullable = false, length = 30)
    private IndustryCategory industryCategory;

    @Column(name = "industry_sub_category", length = 50)
    private String industrySubCategory;

    @Column(name = "employee_count", nullable = false)
    private int employeeCount;

    @Column(name = "foreign_worker_count", nullable = false)
    private int foreignWorkerCount;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBusinessNumber() { return businessNumber; }
    public void setBusinessNumber(String businessNumber) { this.businessNumber = businessNumber; }
    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }
    public String getSubRegion() { return subRegion; }
    public void setSubRegion(String subRegion) { this.subRegion = subRegion; }
    public IndustryCategory getIndustryCategory() { return industryCategory; }
    public void setIndustryCategory(IndustryCategory industryCategory) { this.industryCategory = industryCategory; }
    public String getIndustrySubCategory() { return industrySubCategory; }
    public void setIndustrySubCategory(String industrySubCategory) { this.industrySubCategory = industrySubCategory; }
    public int getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(int employeeCount) { this.employeeCount = employeeCount; }
    public int getForeignWorkerCount() { return foreignWorkerCount; }
    public void setForeignWorkerCount(int foreignWorkerCount) { this.foreignWorkerCount = foreignWorkerCount; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
