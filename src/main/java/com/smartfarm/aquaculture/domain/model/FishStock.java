package com.smartfarm.aquaculture.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fish_stocks", indexes = {
    @Index(name = "idx_stock_pond_id", columnList = "pondId"),
    @Index(name = "idx_stock_species_id", columnList = "speciesId"),
    @Index(name = "idx_stock_date", columnList = "stockDate")
})
@EntityListeners(AuditingEntityListener.class)
public class FishStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Pond ID is required")
    @Column(nullable = false)
    private Long pondId;
    
    @NotNull(message = "Species ID is required")
    @Column(nullable = false)
    private Long speciesId;
    
    @NotNull(message = "Stock date is required")
    @Column(nullable = false)
    private LocalDateTime stockDate;
    
    @NotNull(message = "Initial quantity is required")
    @Min(value = 1, message = "Initial quantity must be at least 1")
    @Column(nullable = false)
    private Integer initialQuantity;
    
    @Min(value = 0, message = "Current quantity cannot be negative")
    @Column(nullable = false)
    private Integer currentQuantity;
    
    @Min(value = 0, message = "Mortality cannot be negative")
    @Column(nullable = false)
    private Integer mortality;
    
    @DecimalMin(value = "0.0", message = "Average weight cannot be negative")
    @Digits(integer = 6, fraction = 2, message = "Average weight must have valid format")
    @Column(precision = 8, scale = 2)
    private BigDecimal averageWeight; // grams
    
    @DecimalMin(value = "0.0", message = "Target weight cannot be negative")
    @Digits(integer = 6, fraction = 2, message = "Target weight must have valid format")
    @Column(precision = 8, scale = 2)
    private BigDecimal targetWeight; // grams
    
    @Column(length = 50)
    private String source; // hatchery, wild caught, purchased
    
    @Column(length = 100)
    private String supplier;
    
    @Column(precision = 8, scale = 2)
    private BigDecimal purchasePrice; // per fish
    
    @Column(precision = 8, scale = 2)
    private BigDecimal totalCost;
    
    @Column(length = 20)
    private String stockType; // fingerlings, juveniles, adults
    
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speciesId", insertable = false, updatable = false)
    private FishSpecies species;
    
    // Constructors
    public FishStock() {}
    
    public FishStock(Long pondId, Long speciesId, Integer initialQuantity, String stockType) {
        this.pondId = pondId;
        this.speciesId = speciesId;
        this.stockDate = LocalDateTime.now();
        this.initialQuantity = initialQuantity;
        this.currentQuantity = initialQuantity;
        this.mortality = 0;
        this.stockType = stockType;
    }
    
    // Business logic methods
    public BigDecimal getSurvivalRate() {
        if (initialQuantity == null || initialQuantity == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal surviving = BigDecimal.valueOf(currentQuantity == null ? 0 : currentQuantity);
        BigDecimal initial = BigDecimal.valueOf(initialQuantity);
        
        return surviving.divide(initial, 4, BigDecimal.ROUND_HALF_UP)
                     .multiply(BigDecimal.valueOf(100));
    }
    
    public BigDecimal getMortalityRate() {
        if (initialQuantity == null || initialQuantity == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal mortalityRate = BigDecimal.valueOf(mortality == null ? 0 : mortality);
        BigDecimal initial = BigDecimal.valueOf(initialQuantity);
        
        return mortalityRate.divide(initial, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
    }
    
    public BigDecimal getGrowthProgress() {
        if (targetWeight == null || targetWeight.compareTo(BigDecimal.ZERO) == 0 ||
            averageWeight == null || averageWeight.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        return averageWeight.divide(targetWeight, 4, BigDecimal.ROUND_HALF_UP)
                       .multiply(BigDecimal.valueOf(100));
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPondId() { return pondId; }
    public void setPondId(Long pondId) { this.pondId = pondId; }
    
    public Long getSpeciesId() { return speciesId; }
    public void setSpeciesId(Long speciesId) { this.speciesId = speciesId; }
    
    public LocalDateTime getStockDate() { return stockDate; }
    public void setStockDate(LocalDateTime stockDate) { this.stockDate = stockDate; }
    
    public Integer getInitialQuantity() { return initialQuantity; }
    public void setInitialQuantity(Integer initialQuantity) { this.initialQuantity = initialQuantity; }
    
    public Integer getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(Integer currentQuantity) { this.currentQuantity = currentQuantity; }
    
    public Integer getMortality() { return mortality; }
    public void setMortality(Integer mortality) { this.mortality = mortality; }
    
    public BigDecimal getAverageWeight() { return averageWeight; }
    public void setAverageWeight(BigDecimal averageWeight) { this.averageWeight = averageWeight; }
    
    public BigDecimal getTargetWeight() { return targetWeight; }
    public void setTargetWeight(BigDecimal targetWeight) { this.targetWeight = targetWeight; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    
    public String getStockType() { return stockType; }
    public void setStockType(String stockType) { this.stockType = stockType; }
    
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
    
    public FishSpecies getSpecies() { return species; }
    public void setSpecies(FishSpecies species) { this.species = species; }
}
