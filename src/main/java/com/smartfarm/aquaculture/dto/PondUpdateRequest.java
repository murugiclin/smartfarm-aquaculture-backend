package com.smartfarm.aquaculture.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PondUpdateRequest {
    
    @Size(max = 100, message = "Pond name must not exceed 100 characters")
    private String name;
    
    @Size(max = 20, message = "Pond code must not exceed 20 characters")
    private String code;
    
    @DecimalMin(value = "0.1", message = "Size must be at least 0.1")
    private BigDecimal size;
    
    @DecimalMin(value = "0.5", message = "Depth must be at least 0.5 meters")
    private BigDecimal depth;
    
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;
    
    private String primarySpecies;
    private String waterSource;
    private String pondType;
    private String aerationType;
    private String notes;
    private String location;
    
    // Water quality parameters
    @DecimalMin(value = "0.0", message = "Oxygen level cannot be negative")
    private BigDecimal oxygenLevel;
    
    @DecimalMin(value = "0.0", message = "pH level cannot be negative")
    private BigDecimal phLevel;
    
    @DecimalMin(value = "0.0", message = "Temperature cannot be negative")
    private BigDecimal temperature;
    
    @DecimalMin(value = "0.0", message = "Ammonia cannot be negative")
    private BigDecimal ammonia;
    
    @DecimalMin(value = "0.0", message = "Nitrite cannot be negative")
    private BigDecimal nitrite;
    
    @DecimalMin(value = "0.0", message = "Nitrate cannot be negative")
    private BigDecimal nitrate;
    
    // Performance metrics
    @DecimalMin(value = "0.0", message = "Feed conversion ratio cannot be negative")
    private BigDecimal feedConversionRatio;
    
    @DecimalMin(value = "0.0", message = "Mortality rate cannot be negative")
    private BigDecimal mortalityRate;
    
    @DecimalMin(value = "0.0", message = "Growth rate cannot be negative")
    private BigDecimal growthRate;
    
    @DecimalMin(value = "0.0", message = "Estimated yield cannot be negative")
    private BigDecimal estimatedYield;
    
    @DecimalMin(value = "0.0", message = "Average weight cannot be negative")
    private BigDecimal averageWeight;
}
