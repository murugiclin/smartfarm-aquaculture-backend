package com.smartfarm.aquaculture.dto;

import com.smartfarm.aquaculture.domain.model.PondStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PondDto {
    private Long id;
    private String name;
    private String code;
    private Long farmId;
    private BigDecimal size;
    private BigDecimal depth;
    private Integer capacity;
    private BigDecimal currentStock;
    private String primarySpecies;
    private String waterSource;
    private String pondType;
    private String aerationType;
    private PondStatus status;
    
    // Water quality parameters
    private BigDecimal oxygenLevel;
    private BigDecimal phLevel;
    private BigDecimal temperature;
    private BigDecimal ammonia;
    private BigDecimal nitrite;
    private BigDecimal nitrate;
    
    // Performance metrics
    private BigDecimal feedConversionRatio;
    private BigDecimal mortalityRate;
    private BigDecimal growthRate;
    private BigDecimal estimatedYield;
    private BigDecimal averageWeight;
    
    // Dates
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime stockDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expectedHarvestDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastFeedingTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastWaterTest;
    
    // Additional info
    private String notes;
    private String location;
    private String createdBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
    
    // Computed fields for frontend
    private BigDecimal stockingDensity;
    private Boolean needsFeeding;
    private Boolean needsWaterTest;
    private Boolean hasPoorWaterQuality;
    private Boolean isOverstocked;
    private String waterQualityStatus;
}
