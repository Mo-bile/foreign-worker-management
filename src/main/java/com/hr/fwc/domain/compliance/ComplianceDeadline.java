package com.hr.fwc.domain.compliance;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ComplianceDeadline {

    private Long id;

    private Long workerId;

    private DeadlineType deadlineType;

    private LocalDate dueDate;

    private DeadlineStatus status;

    private String description;

    protected ComplianceDeadline() {
    }

    private ComplianceDeadline(Long workerId, DeadlineType deadlineType, LocalDate dueDate, String description) {
        this.workerId = Objects.requireNonNull(workerId, "Worker ID cannot be null");
        this.deadlineType = Objects.requireNonNull(deadlineType, "Deadline type cannot be null");
        this.dueDate = Objects.requireNonNull(dueDate, "Due date cannot be null");
        this.description = description;
        this.status = calculateStatus(dueDate);
    }

    public static ComplianceDeadline create(Long workerId, DeadlineType deadlineType, LocalDate dueDate, String description) {
        return new ComplianceDeadline(workerId, deadlineType, dueDate, description);
    }

    public static ComplianceDeadline reconstitute(Long id, Long workerId, DeadlineType deadlineType,
                                                  LocalDate dueDate, DeadlineStatus status, String description) {
        ComplianceDeadline d = new ComplianceDeadline();
        d.id = id;
        d.workerId = workerId;
        d.deadlineType = deadlineType;
        d.dueDate = dueDate;
        d.status = status;
        d.description = description;
        return d;
    }

    public void updateDueDate(LocalDate newDueDate) {
        this.dueDate = Objects.requireNonNull(newDueDate, "Due date cannot be null");
        this.status = calculateStatus(newDueDate);
    }

    public void markAsCompleted() {
        this.status = DeadlineStatus.COMPLETED;
    }

    public void refreshStatus() {
        this.status = calculateStatus(this.dueDate);
    }

    private DeadlineStatus calculateStatus(LocalDate dueDate) {
        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
        int[] thresholds = deadlineType.alertThresholds();

        if (daysUntil < 0) {
            return DeadlineStatus.OVERDUE;
        } else if (daysUntil <= thresholds[2]) {
            return DeadlineStatus.URGENT;
        } else if (daysUntil <= thresholds[1]) {
            return DeadlineStatus.APPROACHING;
        } else {
            return DeadlineStatus.PENDING;
        }
    }

    public boolean requiresAlert() {
        return status == DeadlineStatus.APPROACHING 
            || status == DeadlineStatus.URGENT 
            || status == DeadlineStatus.OVERDUE;
    }

    public Long id() {
        return id;
    }

    public Long workerId() {
        return workerId;
    }

    public DeadlineType deadlineType() {
        return deadlineType;
    }

    public LocalDate dueDate() {
        return dueDate;
    }

    public DeadlineStatus status() {
        return status;
    }

    public String description() {
        return description;
    }

}
