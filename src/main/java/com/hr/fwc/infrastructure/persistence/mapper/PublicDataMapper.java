package com.hr.fwc.infrastructure.persistence.mapper;

import com.hr.fwc.domain.publicdata.*;
import com.hr.fwc.infrastructure.persistence.entity.*;

public final class PublicDataMapper {
    private PublicDataMapper() {}

    public static RegionalIndustryEntity toEntity(RegionalIndustry d) {
        RegionalIndustryEntity e = new RegionalIndustryEntity();
        e.setId(d.id()); e.setSnapshotId(d.snapshotId()); e.setRegion(d.region());
        e.setIndustry(d.industry()); e.setQuarter(d.quarter());
        e.setWorkerCount(d.workerCount()); e.setReferenceDate(d.referenceDate());
        return e;
    }
    public static RegionalIndustry toDomain(RegionalIndustryEntity e) {
        return RegionalIndustry.reconstitute(e.getId(), e.getSnapshotId(), e.getRegion(),
            e.getIndustry(), e.getQuarter(), e.getWorkerCount(), e.getReferenceDate());
    }

    public static ManufacturingEntity toEntity(Manufacturing d) {
        ManufacturingEntity e = new ManufacturingEntity();
        e.setId(d.id()); e.setSnapshotId(d.snapshotId()); e.setSubIndustry(d.subIndustry());
        e.setWorkerCount(d.workerCount()); e.setYear(d.year()); e.setReferenceDate(d.referenceDate());
        return e;
    }
    public static Manufacturing toDomain(ManufacturingEntity e) {
        return Manufacturing.reconstitute(e.getId(), e.getSnapshotId(), e.getSubIndustry(),
            e.getWorkerCount(), e.getYear(), e.getReferenceDate());
    }

    public static VietnamE9Entity toEntity(VietnamE9 d) {
        VietnamE9Entity e = new VietnamE9Entity();
        e.setId(d.id()); e.setSnapshotId(d.snapshotId()); e.setIndustry(d.industry());
        e.setTotalCount(d.totalCount()); e.setMaleCount(d.maleCount());
        e.setFemaleCount(d.femaleCount()); e.setReferenceDate(d.referenceDate());
        return e;
    }
    public static VietnamE9 toDomain(VietnamE9Entity e) {
        return VietnamE9.reconstitute(e.getId(), e.getSnapshotId(), e.getIndustry(),
            e.getTotalCount(), e.getMaleCount(), e.getFemaleCount(), e.getReferenceDate());
    }

    public static QuotaEntity toEntity(Quota d) {
        QuotaEntity e = new QuotaEntity();
        e.setId(d.id()); e.setYear(d.year()); e.setIndustry(d.industry());
        e.setQuotaCount(d.quotaCount()); e.setSource(d.source());
        return e;
    }
    public static Quota toDomain(QuotaEntity e) {
        return Quota.reconstitute(e.getId(), e.getYear(), e.getIndustry(),
            e.getQuotaCount(), e.getSource());
    }
}
