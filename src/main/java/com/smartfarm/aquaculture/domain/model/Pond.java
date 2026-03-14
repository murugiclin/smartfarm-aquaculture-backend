package com.smartfarm.aquaculture.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ponds", indexes = {
    @Index(name = "idx_pond_farm_id", columnList = "farmId"),
    @Index(name = "idx_pond_status", columnList = "status"),
    @Index(name = "idx_pond_species", columnList = "primarySpecies")
})
@EntityListeners(AuditingEntityListener.class)
public class Pond {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Pond name is required")
    @Size(max = 100, message = "Pond name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "Pond code is required")
    @Size(max = 20, message = "Pond code must not exceed 20 characters")
    @Column(nullable = false, unique = true, length = 20)
    private String code;
    
    @NotNull(message = "Farm ID is required")
    @Column(nullable = false)
    private Long farmId;
    
    @NotNull(message = "Size is required")
    @DecimalMin(value = "0.1", message = "Size must be at least 0.1")
    @Digits(integer = 8, fraction = 2, message = "Size must have valid format")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal size; // in square meters
    
    @NotNull(message = "Depth is required")
    @DecimalMin(value = "0.5", message = "Depth must be at least 0.5 meters")
    @Digits(integer = 3, fraction = 2, message = "Depth must have valid format")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal depth; // in meters
    
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private Integer capacity; // maximum number of fish
    
    @Column(precision = 5, scale = 2)
    private BigDecimal currentStock; // current number of fish
    
    @Column(length = 50)
    private String primarySpecies;
    
    @Column(length = 100)
    private String waterSource;
    
    @Column(length = 20)
    private String pondType; // e.g., "earthen", "concrete", "lined"
    
    @Column(length = 20)
    private String aerationType; // e.g., "paddlewheel", "diffuser", "cascade"
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PondStatus status = PondStatus.ACTIVE;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal oxygenLevel; // mg/L
    
    @Column(precision = 4, scale = 2)
    private BigDecimal phLevel;
    
    @Column(precision = 4, scale = 2)
    private BigDecimal temperature; // Celsius
    
    @Column(precision = 5, scale = 2)
    private BigDecimal ammonia; // mg/L
    @Column(precision = 5, scale = 2)
    private BigDecimal nitrite; // mg/L
    @Column(precision = 5, scale = 2)
    private BigDecimal nitrate; // mg/L
    
    @Column(precision = 5, scale = 4)
    private BigDecimal feedConversionRatio;
    
    @Column(precision = 4, scale = 2)
    private BigDecimal mortalityRate; // percentage
    
    @Column(precision = 5, scale = 2)
    private BigDecimal growthRate; // percentage per week
    
    @Column(precision = 8, scale = 2)
    private BigDecimal estimatedYield; // kg
    
    @Column(precision = 4, scale = 2)
    private BigDecimal averageWeight; // grams
    
    @Column
    private LocalDateTime stockDate;
    
    @Column
    private LocalDateTime expectedHarvestDate;
    
    @Column
    private LocalDateTime lastFeedingTime;
    
    @Column
    private LocalDateTime lastWaterTest;
    
    @Column(length = 500)
    private String notes;
    
    @Column(length = 100)
    private String location;
    
    @Column(length = 50)
    private String createdBy;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;
    
    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<WaterQualityReading> waterQualityReadings = new HashSet<>();
    
    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FeedingRecord> feedingRecords = new HashSet<>();
    
    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<HealthRecord> healthRecords = new HashSet<>();
    
    // Constructors
    public Pond() {}
    
    public Pond(String name, String code, Long farmId, BigDecimal size, BigDecimal depth, Integer capacity) {
        this.name = name;
        this.code = code;
        this.farmId = farmId;
        this.size = size;
        this.depth = depth;
        this.capacity = capacity;
    }
    
    // Business logic methods
    public boolean isOverstocked() {
        return currentStock != null && currentStock.compareTo(BigDecimal.valueOf(capacity)) > 0;
    }
    
    public boolean needsWaterQualityCheck() {
        return lastWaterTest == null || 
               lastWaterTest.isBefore(LocalDateTime.now().minusHours(6));
    }
    
    public boolean needsFeeding() {
        return lastFeedingTime == null || 
               lastFeedingTime.isBefore(LocalDateTime.now().minusHours(4));
    }
    
    public boolean hasPoorWaterQuality() {
        return (oxygenLevel != null && oxygenLevel.compareTo(BigDecimal.valueOf(5.0)) < 0) ||
               (phLevel != null && (phLevel.compareTo(BigDecimal.valueOf(6.5)) < 0 || 
                                  phLevel.compareTo(BigDecimal.valueOf(8.5)) > 0)) ||
               (temperature != null && (temperature.compareTo(BigDecimal.valueOf(20.0)) < 0 || 
                                       temperature.compareTo(BigDecimal.valueOf(30.0)) > 0));
    }
    
    public BigDecimal getStockingDensity() {
        if (currentStock == null || size.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentStock.divide(size, 2, BigDecimal.ROUND_HALF_UP);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public Long getFarmId() { return farmId; }
    public void setFarmId(Long farmId) { this.farmId = farmId; }
    
    public BigDecimal getSize() { return size; }
    public void setSize(BigDecimal size) { this.size = size; }
    
    public BigDecimal getDepth() { return depth; }
    public void setDepth(BigDecimal depth) { this.depth = depth; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public BigDecimal getCurrentStock() { return currentStock; }
    public void setCurrentStock(BigDecimal currentStock) { this.currentStock = currentStock; }
    
    public String getPrimarySpecies() { return primarySpecies; }
    public void setPrimarySpecies(String primarySpecies) { this.primarySpecies = primarySpecies; }
    
    public String getWaterSource() { return waterSource; }
    public void setWaterSource(String waterSource) { this.waterSource = waterSource; }
    
    public String getPondType() { return pondType; }
    public void setPondType(String pondType) { this.pondType = pondType; }
    
    public String getAerationType() { return aerationType; }
    public void setAerationType(String aerationType) { this.aerationType = aerationType; }
    
    public PondStatus getStatus() { return status; }
    public void setStatus(PondStatus status) { this.status = status; }
    
    public BigDecimal getOxygenLevel() { return oxygenLevel; }
    public void setOxygenLevel(BigDecimal oxygenLevel) { this.oxygenLevel = oxygenLevel; }
    
    public BigDecimal getPhLevel() { return phLevel; }
    public void setPhLevel(BigDecimal phLevel) { this.phLevel = phLevel; }
    
    public BigDecimal getTemperature() { return temperature; }
    public void setTemperature(BigDecimal temperature) { this.temperature = temperature; }
    
    public BigDecimal getAmmonia() { return ammonia; }
    public void setAmmonia(BigDecimal ammonia) { this.ammonia = ammonia; }
    
    public BigDecimal getNitrite() { return nitrite; }
    public void setNitrite(BigDecimal nitrite) { this.nitrite = nitrite; }
    
    public BigDecimal getNitrate() { return nitrate; }
    public void setNitrate(BigDecimal nitrate) { this.nitrate = nitrate; }
    
    public BigDecimal getFeedConversionRatio() { return feedConversionRatio; }
    public void setFeedConversionRatio(BigDecimal feedConversionRatio) { this.feedConversionRatio = feedConversionRatio; }
    
    public BigDecimal getMortalityRate() { return mortalityRate; }
    public void setMortalityRate(BigDecimal mortalityRate) { this.mortalityRate = mortalityRate; }
    
    public BigDecimal getGrowthRate() { return growthRate; }
    public void setGrowthRate(BigDecimal growthRate) { this.growthRate = growthRate; }
    
    public BigDecimal getEstimatedYield() { return estimatedYield; }
    public void setEstimatedYield(BigDecimal estimatedYield) { this.estimatedYield = estimatedYield; }
    
    public BigDecimal getAverageWeight() { return averageWeight; }
    public void setAverageWeight(BigDecimal averageWeight) { this.averageWeight = averageWeight; }
    
    public LocalDateTime getStockDate() { return stockDate; }
    public void setStockDate(LocalDateTime stockDate) { this.stockDate = stockDate; }
    
    public LocalDateTime getExpectedHarvestDate() { return expectedHarvestDate; }
    public void setExpectedHarvestDate(LocalDateTime expectedHarvestDate) { this.expectedHarvestDate = expectedHarvestDate; }
    
    public LocalDateTime getLastFeedingTime() { return lastFeedingTime; }
    public void setLastFeedingTime(LocalDateTime lastFeedingTime) { this.lastFeedingTime = lastFeedingTime; }
    
    public LocalDateTime getLastWaterTest() { return lastWaterTest; }
    public void setLastWaterTest(LocalDateTime lastWaterTest) { this.lastWaterTest = lastWaterTest; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public Set<WaterQualityReading> getWaterQualityReadings() { return waterQualityReadings; }
    public void setWaterQualityReadings(Set<WaterQualityReading> waterQualityReadings) { this.waterQualityReadings = waterQualityReadings; }
    
    public Set<FeedingRecord> getFeedingRecords() { return feedingRecords; }
    public void setFeedingRecords(Set<FeedingRecord> feedingRecords) { this.feedingRecords = feedingRecords; }
    
    public Set<HealthRecord> getHealthRecords() { return healthRecords; }
    public void setHealthRecords(Set<HealthRecord> healthRecords) { this.healthRecords = healthRecords; }
}
