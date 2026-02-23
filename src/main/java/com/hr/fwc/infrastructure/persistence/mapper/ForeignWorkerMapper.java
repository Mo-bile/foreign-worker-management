package com.hr.fwc.infrastructure.persistence.mapper;

import com.hr.fwc.domain.worker.EmploymentInfo;
import com.hr.fwc.domain.worker.ForeignWorker;
import com.hr.fwc.domain.worker.PersonalInfo;
import com.hr.fwc.domain.worker.VisaInfo;
import com.hr.fwc.infrastructure.persistence.entity.EmploymentInfoEmbeddable;
import com.hr.fwc.infrastructure.persistence.entity.ForeignWorkerEntity;
import com.hr.fwc.infrastructure.persistence.entity.PersonalInfoEmbeddable;
import com.hr.fwc.infrastructure.persistence.entity.VisaInfoEmbeddable;

public final class ForeignWorkerMapper {

    private ForeignWorkerMapper() {
    }

    public static ForeignWorkerEntity toEntity(ForeignWorker domain) {
        ForeignWorkerEntity entity = new ForeignWorkerEntity();
        entity.setId(domain.id());
        entity.setPersonalInfo(toPersonalInfoEmbeddable(domain.personalInfo()));
        entity.setVisaInfo(toVisaInfoEmbeddable(domain.visaInfo()));
        entity.setEmploymentInfo(toEmploymentInfoEmbeddable(domain.employmentInfo()));
        entity.setNationality(domain.nationality());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }

    public static ForeignWorker toDomain(ForeignWorkerEntity entity) {
        return ForeignWorker.reconstitute(
            entity.getId(),
            toPersonalInfo(entity.getPersonalInfo()),
            toVisaInfo(entity.getVisaInfo()),
            toEmploymentInfo(entity.getEmploymentInfo()),
            entity.getNationality(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private static PersonalInfoEmbeddable toPersonalInfoEmbeddable(PersonalInfo personalInfo) {
        PersonalInfoEmbeddable embeddable = new PersonalInfoEmbeddable();
        embeddable.setName(personalInfo.name());
        embeddable.setPassportNumber(personalInfo.passportNumber());
        embeddable.setContactPhone(personalInfo.contactPhone());
        embeddable.setContactEmail(personalInfo.contactEmail());
        return embeddable;
    }

    private static VisaInfoEmbeddable toVisaInfoEmbeddable(VisaInfo visaInfo) {
        VisaInfoEmbeddable embeddable = new VisaInfoEmbeddable();
        embeddable.setVisaType(visaInfo.visaType());
        embeddable.setVisaExpiryDate(visaInfo.visaExpiryDate());
        embeddable.setEntryDate(visaInfo.entryDate());
        embeddable.setRegistrationNumber(visaInfo.registrationNumber());
        return embeddable;
    }

    private static EmploymentInfoEmbeddable toEmploymentInfoEmbeddable(EmploymentInfo employmentInfo) {
        EmploymentInfoEmbeddable embeddable = new EmploymentInfoEmbeddable();
        embeddable.setContractStartDate(employmentInfo.contractStartDate());
        embeddable.setContractEndDate(employmentInfo.contractEndDate());
        embeddable.setWorkplaceId(employmentInfo.workplaceId());
        return embeddable;
    }

    private static PersonalInfo toPersonalInfo(PersonalInfoEmbeddable embeddable) {
        return PersonalInfo.of(
            embeddable.getName(),
            embeddable.getPassportNumber(),
            embeddable.getContactPhone(),
            embeddable.getContactEmail()
        );
    }

    private static VisaInfo toVisaInfo(VisaInfoEmbeddable embeddable) {
        return VisaInfo.of(
            embeddable.getVisaType(),
            embeddable.getVisaExpiryDate(),
            embeddable.getEntryDate(),
            embeddable.getRegistrationNumber()
        );
    }

    private static EmploymentInfo toEmploymentInfo(EmploymentInfoEmbeddable embeddable) {
        return EmploymentInfo.of(
            embeddable.getContractStartDate(),
            embeddable.getContractEndDate(),
            embeddable.getWorkplaceId()
        );
    }
}
