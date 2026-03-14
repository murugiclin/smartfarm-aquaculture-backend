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
@Table(name = "fish_species", indexes = {
    @Index(name = "idx_species_common_name", columnList = "commonName"),
    @Index(name = "idx_species_scientific_name", columnList = "scientificName"),
    @Index(name = "idx_species_category", columnList = "category")
})
@EntityListeners(AuditingEntityListener.class)
public class FishSpecies {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Common name is required")
    @Size(max = 100, message = "Common name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String commonName;
    
    @NotBlank(message = "Scientific name is required")
    @Size(max = 150, message = "Scientific name must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String scientificName;
    
    @NotBlank(message = "Species code is required")
    @Size(max = 20, message = "Species code must not exceed 20 characters")
    @Column(nullable = false, unique = true, length = 20)
    private String code;
    
    @Column(length = 50)
    private String localName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FishCategory category;
    
    @NotNull(message = "Optimal temperature range is required")
    @Column(nullable = false)
    private String optimalTemperature; // e.g., "25-30°C"
    
    @NotNull(message = "Optimal pH range is required")
    @Column(nullable = false)
    private String optimalPh; // e.g., "7.0-8.0"
    
    @NotNull(message = "Optimal oxygen level is required")
    @DecimalMin(value = "0.0", message = "Oxygen level cannot be negative")
    @Digits(integer = 3, fraction = 2, message = "Oxygen level must have valid format")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal optimalOxygenLevel; // mg/L
    
    @DecimalMin(value = "0.0", message = "Growth rate cannot be negative")
    @Digits(integer = 3, fraction = 2, message = "Growth rate must have valid format")
    @Column(precision = 5, scale = 2)
    private BigDecimal growthRate; // percentage per week
    
    @DecimalMin(value = "0.0", message = "Feed conversion ratio cannot be negative")
    @Digits(integer = 3, fraction = 2, message = "Feed conversion ratio must have valid format")
    @Column(precision = 5, scale = 2)
    private BigDecimal feedConversionRatio;
    
    @DecimalMin(value = "0.0", message = "Survival rate cannot be negative")
    @Digits(integer = 3, fraction = 2, message = "Survival rate must have valid format")
    @Column(precision = 5, scale = 2)
    private BigDecimal survivalRate; // percentage
    
    @DecimalMin(value = "0.0", message = "Market size cannot be negative")
    @Digits(integer = 4, fraction = 2, message = "Market size must have valid format")
    @Column(precision = 6, scale = 2)
    private BigDecimal marketSize; // grams
    
    @DecimalMin(value = "0.0", message = "Market price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Market price must have valid format")
    @Column(precision = 10, scale = 2)
    private BigDecimal marketPrice; // per kg
    
    @Min(value = 0, message = "Growth period cannot be negative")
    @Column
    private Integer growthPeriodDays; // days to reach market size
    
    @DecimalMin(value = "0.0", message = "Stocking density cannot be negative")
    @Digits(integer = 5, fraction = 2, message = "Stocking density must have valid format")
    @Column(precision = 7, scale = 2)
    private BigDecimal recommendedStockingDensity; // fish per square meter
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 1000)
    private String characteristics;
    
    @Column(length = 1000)
    private String feedingRequirements;
    
    @Column(length = 500)
    private String diseaseSusceptibility;
    
    @Column(length = 500)
    private String waterQualityRequirements;
    
    @Column(length = 50)
    private String nativeRegion;
    
    @Column(length = 50)
    private String habitatType;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TemperaturePreference temperaturePreference;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private WaterType waterType;
    
    @Column(length = 20)
    private String breedingType; // e.g., "mouthbrooder", "egg-scatterer"
    
    @Column(length = 20)
    private String feedingType; // e.g., "omnivore", "herbivore", "carnivore"
    
    @Column
    private Boolean isActive = true;
    
    @Column(length = 100)
    private String createdBy;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;
    
    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FishStock> fishStocks = new HashSet<>();
    
    // Constructors
    public FishSpecies() {}
    
    public FishSpecies(String commonName, String scientificName, String code, FishCategory category) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.code = code;
        this.category = category;
    }
    
    // Business logic methods
    public boolean isSuitableForTemperature(BigDecimal temperature) {
        if (optimalTemperature == null || temperature == null) return false;
        
        String[] range = optimalTemperature.replace("°C", "").split("-");
        if (range.length != 2) return false;
        
        try {
            BigDecimal min = new BigDecimal(range[0].trim());
            BigDecimal max = new BigDecimal(range[1].trim());
            return temperature.compareTo(min) >= 0 && temperature.compareTo(max) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public boolean isSuitableForPh(BigDecimal ph) {
        if (optimalPh == null || ph == null) return false;
        
        String[] range = optimalPh.split("-");
        if (range.length != 2) return false;
        
        try {
            BigDecimal min = new BigDecimal(range[0].trim());
            BigDecimal max = new BigDecimal(range[1].trim());
            return ph.compareTo(min) >= 0 && ph.compareTo(max) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public boolean isSuitableForOxygen(BigDecimal oxygen) {
        return optimalOxygenLevel != null && oxygen != null &&
               oxygen.compareTo(optimalOxygenLevel) >= 0;
    }
    
    public BigDecimal calculateEstimatedProduction(BigDecimal pondSize, BigDecimal stockingDensity) {
        if (pondSize == null || stockingDensity == null || marketSize == null || survivalRate == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal numberOfFish = pondSize.multiply(stockingDensity);
        BigDecimal survivingFish = numberOfFish.multiply(survivalRate.divide(BigDecimal.valueOf(100)));
        BigDecimal totalWeight = survivingFish.multiply(marketSize).divide(BigDecimal.valueOf(1000)); // Convert to kg
        
        return totalWeight;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }
    
    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getLocalName() { return localName; }
    public void setLocalName(String localName) { this.localName = localName; }
    
    public FishCategory getCategory() { return category; }
    public void setCategory(FishCategory category) { this.category = category; }
    
    public String getOptimalTemperature() { return optimalTemperature; }
    public void setOptimalTemperature(String optimalTemperature) { this.optimalTemperature = optimalTemperature; }
    
    public String getOptimalPh() { return optimalPh; }
    public void setOptimalPh(String optimalPh) { this.optimalPh = optimalPh; }
    
    public BigDecimal getOptimalOxygenLevel() { return optimalOxygenLevel; }
    public void setOptimalOxygenLevel(BigDecimal optimalOxygenLevel) { this.optimalOxygenLevel = optimalOxygenLevel; }
    
    public BigDecimal getGrowthRate() { return growthRate; }
    public void setGrowthRate(BigDecimal growthRate) { this.growthRate = growthRate; }
    
    public BigDecimal getFeedConversionRatio() { return feedConversionRatio; }
    public void setFeedConversionRatio(BigDecimal feedConversionRatio) { this.feedConversionRatio = feedConversionRatio; }
    
    public BigDecimal getSurvivalRate() { return survivalRate; }
    public void setSurvivalRate(BigDecimal survivalRate) { this.survivalRate = survivalRate; }
    
    public BigDecimal getMarketSize() { return marketSize; }
    public void setMarketSize(BigDecimal marketSize) { this.marketSize = marketSize; }
    
    public BigDecimal getMarketPrice() { return marketPrice; }
    public void setMarketPrice(BigDecimal marketPrice) { this.marketPrice = marketPrice; }
    
    public Integer getGrowthPeriodDays() { return growthPeriodDays; }
    public void setGrowthPeriodDays(Integer growthPeriodDays) { this.growthPeriodDays = growthPeriodDays; }
    
    public BigDecimal getRecommendedStockingDensity() { return recommendedStockingDensity; }
    public void setRecommendedStockingDensity(BigDecimal recommendedStockingDensity) { this.recommendedStockingDensity = recommendedStockingDensity; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCharacteristics() { return characteristics; }
    public void setCharacteristics(String characteristics) { this.characteristics = characteristics; }
    
    public String getFeedingRequirements() { return feedingRequirements; }
    public void setFeedingRequirements(String feedingRequirements) { this.feedingRequirements = feedingRequirements; }
    
    public String getDiseaseSusceptibility() { return diseaseSusceptibility; }
    public void setDiseaseSusceptibility(String diseaseSusceptibility) { this.diseaseSusceptibility = diseaseSusceptibility; }
    
    public String getWaterQualityRequirements() { return waterQualityRequirements; }
    public void setWaterQualityRequirements(String waterQualityRequirements) { this.waterQualityRequirements = waterQualityRequirements; }
    
    public String getNativeRegion() { return nativeRegion; }
    public void setNativeRegion(String nativeRegion) { this.nativeRegion = nativeRegion; }
    
    public String getHabitatType() { return habitatType; }
    public void setHabitatType(String habitatType) { this.habitatType = habitatType; }
    
    public TemperaturePreference getTemperaturePreference() { return temperaturePreference; }
    public void setTemperaturePreference(TemperaturePreference temperaturePreference) { this.temperaturePreference = temperaturePreference; }
    
    public WaterType getWaterType() { return waterType; }
    public void setWaterType(WaterType waterType) { this.waterType = waterType; }
    
    public String getBreedingType() { return breedingType; }
    public void setBreedingType(String breedingType) { this.breedingType = breedingType; }
    
    public String getFeedingType() { return feedingType; }
    public void setFeedingType(String feedingType) { this.feedingType = feedingType; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public Set<FishStock> getFishStocks() { return fishStocks; }
    public void setFishStocks(Set<FishStock> fishStocks) { this.fishStocks = fishStocks; }
}
