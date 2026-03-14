package com.smartfarm.aquaculture.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_records", indexes = {
    @Index(name = "idx_health_pond_id", columnList = "pondId"),
    @Index(name = "idx_health_timestamp", columnList = "timestamp"),
    @Index(name = "idx_health_type", columnList = "healthIssueType")
})
@EntityListeners(AuditingEntityListener.class)
public class HealthRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Pond ID is required")
    @Column(nullable = false)
    private Long pondId;
    
    @NotNull(message = "Health check time is required")
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(length = 100)
    private String disease;
    
    @Column(length = 50)
    private String healthIssueType; // disease, parasite, injury, stress
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SeverityLevel severity;
    
    @Column(length = 1000)
    private String symptoms;
    
    @Column(length = 1000)
    private String treatment;
    
    @Column(length = 100)
    private String medication;
    
    @Column(length = 50)
    private String dosage;
    
    @Column(length = 20)
    private String treatmentDuration;
    
    @Column(length = 500)
    private String preventiveMeasures;
    
    @DecimalMin(value = "0.0", message = "Mortality count cannot be negative")
    @Digits(integer = 6, fraction = 0, message = "Mortality count must have valid format")
    @Column(precision = 7)
    private Integer mortalityCount;
    
    @DecimalMin(value = "0.0", message = "Affected fish cannot be negative")
    @Digits(integer = 6, fraction = 0, message = "Affected fish must have valid format")
    @Column(precision = 7)
    private Integer affectedFish;
    
    @Column(length = 100)
    private String veterinarian;
    
    @Column(length = 50)
    private String treatmentStatus; // ongoing, completed, monitoring
    
    @Column(precision = 8, scale = 2)
    private BigDecimal treatmentCost;
    
    @Column(length = 500)
    private String notes;
    
    @Column(length = 50)
    private String recordedBy;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pondId", insertable = false, updatable = false)
    private Pond pond;
    
    // Constructors
    public HealthRecord() {}
    
    public HealthRecord(Long pondId, String healthIssueType, SeverityLevel severity) {
        this.pondId = pondId;
        this.timestamp = LocalDateTime.now();
        this.healthIssueType = healthIssueType;
        this.severity = severity;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPondId() { return pondId; }
    public void setPondId(Long pondId) { this.pondId = pondId; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }
    
    public String getHealthIssueType() { return healthIssueType; }
    public void setHealthIssueType(String healthIssueType) { this.healthIssueType = healthIssueType; }
    
    public SeverityLevel getSeverity() { return severity; }
    public void setSeverity(SeverityLevel severity) { this.severity = severity; }
    
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    
    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }
    
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    
    public String getTreatmentDuration() { return treatmentDuration; }
    public void setTreatmentDuration(String treatmentDuration) { this.treatmentDuration = treatmentDuration; }
    
    public String getPreventiveMeasures() { return preventiveMeasures; }
    public void setPreventiveMeasures(String preventiveMeasures) { this.preventiveMeasures = preventiveMeasures; }
    
    public Integer getMortalityCount() { return mortalityCount; }
    public void setMortalityCount(Integer mortalityCount) { this.mortalityCount = mortalityCount; }
    
    public Integer getAffectedFish() { return affectedFish; }
    public void setAffectedFish(Integer affectedFish) { this.affectedFish = affectedFish; }
    
    public String getVeterinarian() { return veterinarian; }
    public void setVeterinarian(String veterinarian) { this.veterinarian = veterinarian; }
    
    public String getTreatmentStatus() { return treatmentStatus; }
    public void setTreatmentStatus(String treatmentStatus) { this.treatmentStatus = treatmentStatus; }
    
    public BigDecimal getTreatmentCost() { return treatmentCost; }
    public void setTreatmentCost(BigDecimal treatmentCost) { this.treatmentCost = treatmentCost; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getRecordedBy() { return recordedBy; }
    public void setRecordedBy(String recordedBy) { this.recordedBy = recordedBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public Pond getPond() { return pond; }
    public void setPond(Pond pond) { this.pond = pond; }
}
