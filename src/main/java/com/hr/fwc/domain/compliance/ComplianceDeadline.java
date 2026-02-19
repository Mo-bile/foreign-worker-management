package com.hr.fwc.domain.compliance;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "compliance_deadlines")
public class ComplianceDeadline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "worker_id", nullable = false)
    private Long workerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "deadline_type", nullable = false, length = 30)
    private DeadlineType deadlineType;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DeadlineStatus status;

    @Column(name = "description", length = 500)
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

        if (daysUntil < 0) {
            return DeadlineStatus.OVERDUE;
        } else if (daysUntil <= 7) {
            return DeadlineStatus.URGENT;
        } else if (daysUntil <= 30) {
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
