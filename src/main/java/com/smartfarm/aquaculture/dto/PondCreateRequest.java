package com.smartfarm.aquaculture.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PondCreateRequest {
    
    @NotBlank(message = "Pond name is required")
    @Size(max = 100, message = "Pond name must not exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Pond code is required")
    @Size(max = 20, message = "Pond code must not exceed 20 characters")
    private String code;
    
    @NotNull(message = "Farm ID is required")
    private Long farmId;
    
    @NotNull(message = "Size is required")
    @DecimalMin(value = "0.1", message = "Size must be at least 0.1")
    private BigDecimal size;
    
    @NotNull(message = "Depth is required")
    @DecimalMin(value = "0.5", message = "Depth must be at least 0.5 meters")
    private BigDecimal depth;
    
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;
    
    private String primarySpecies;
    private String waterSource;
    private String pondType;
    private String aerationType;
    private String notes;
    private String location;
}
