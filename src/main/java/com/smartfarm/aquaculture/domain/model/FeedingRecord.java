package com.smartfarm.aquaculture.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "feeding_records", indexes = {
    @Index(name = "idx_feeding_pond_id", columnList = "pondId"),
    @Index(name = "idx_feeding_timestamp", columnList = "timestamp")
})
@EntityListeners(AuditingEntityListener.class)
public class FeedingRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Pond ID is required")
    @Column(nullable = false)
    private Long pondId;
    
    @NotNull(message = "Feeding time is required")
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @NotNull(message = "Feed amount is required")
    @DecimalMin(value = "0.1", message = "Feed amount must be at least 0.1 kg")
    @Digits(integer = 6, fraction = 2, message = "Feed amount must have valid format")
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal feedAmount; // in kg
    
    @Column(length = 100)
    private String feedType;
    
    @Column(length = 50)
    private String feedBrand;
    
    @Column(length = 20)
    private String feedingMethod; // manual, automatic, broadcast
    
    @Column(precision = 4, scale = 2)
    private BigDecimal feedConversion; // feed to growth ratio
    
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
    public FeedingRecord() {}
    
    public FeedingRecord(Long pondId, BigDecimal feedAmount, String feedType) {
        this.pondId = pondId;
        this.timestamp = LocalDateTime.now();
        this.feedAmount = feedAmount;
        this.feedType = feedType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPondId() { return pondId; }
    public void setPondId(Long pondId) { this.pondId = pondId; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public BigDecimal getFeedAmount() { return feedAmount; }
    public void setFeedAmount(BigDecimal feedAmount) { this.feedAmount = feedAmount; }
    
    public String getFeedType() { return feedType; }
    public void setFeedType(String feedType) { this.feedType = feedType; }
    
    public String getFeedBrand() { return feedBrand; }
    public void setFeedBrand(String feedBrand) { this.feedBrand = feedBrand; }
    
    public String getFeedingMethod() { return feedingMethod; }
    public void setFeedingMethod(String feedingMethod) { this.feedingMethod = feedingMethod; }
    
    public BigDecimal getFeedConversion() { return feedConversion; }
    public void setFeedConversion(BigDecimal feedConversion) { this.feedConversion = feedConversion; }
    
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
