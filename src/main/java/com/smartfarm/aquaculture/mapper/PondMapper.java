package com.smartfarm.aquaculture.mapper;

import com.smartfarm.aquaculture.domain.model.Pond;
import com.smartfarm.aquaculture.domain.model.PondStatus;
import com.smartfarm.aquaculture.dto.PondDto;
import com.smartfarm.aquaculture.dto.PondCreateRequest;
import com.smartfarm.aquaculture.dto.PondUpdateRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class PondMapper {
    
    public PondDto toDto(Pond pond) {
        if (pond == null) {
            return null;
        }
        
        PondDto dto = new PondDto();
        dto.setId(pond.getId());
        dto.setName(pond.getName());
        dto.setCode(pond.getCode());
        dto.setFarmId(pond.getFarmId());
        dto.setSize(pond.getSize());
        dto.setDepth(pond.getDepth());
        dto.setCapacity(pond.getCapacity());
        dto.setCurrentStock(pond.getCurrentStock());
        dto.setPrimarySpecies(pond.getPrimarySpecies());
        dto.setWaterSource(pond.getWaterSource());
        dto.setPondType(pond.getPondType());
        dto.setAerationType(pond.getAerationType());
        dto.setStatus(pond.getStatus());
        
        // Water quality parameters
        dto.setOxygenLevel(pond.getOxygenLevel());
        dto.setPhLevel(pond.getPhLevel());
        dto.setTemperature(pond.getTemperature());
        dto.setAmmonia(pond.getAmmonia());
        dto.setNitrite(pond.getNitrite());
        dto.setNitrate(pond.getNitrate());
        
        // Performance metrics
        dto.setFeedConversionRatio(pond.getFeedConversionRatio());
        dto.setMortalityRate(pond.getMortalityRate());
        dto.setGrowthRate(pond.getGrowthRate());
        dto.setEstimatedYield(pond.getEstimatedYield());
        dto.setAverageWeight(pond.getAverageWeight());
        
        // Dates
        dto.setStockDate(pond.getStockDate());
        dto.setExpectedHarvestDate(pond.getExpectedHarvestDate());
        dto.setLastFeedingTime(pond.getLastFeedingTime());
        dto.setLastWaterTest(pond.getLastWaterTest());
        
        // Additional info
        dto.setNotes(pond.getNotes());
        dto.setLocation(pond.getLocation());
        dto.setCreatedBy(pond.getCreatedBy());
        dto.setCreatedAt(pond.getCreatedAt());
        dto.setUpdatedAt(pond.getUpdatedAt());
        
        // Computed fields for frontend
        dto.setStockingDensity(pond.getStockingDensity());
        dto.setNeedsFeeding(pond.needsFeeding());
        dto.setNeedsWaterTest(pond.needsWaterQualityCheck());
        dto.setHasPoorWaterQuality(pond.hasPoorWaterQuality());
        dto.setIsOverstocked(pond.isOverstocked());
        dto.setWaterQualityStatus(determineWaterQualityStatus(pond));
        
        return dto;
    }
    
    public Pond toEntity(PondCreateRequest request) {
        if (request == null) {
            return null;
        }
        
        Pond pond = new Pond();
        pond.setName(request.getName());
        pond.setCode(request.getCode());
        pond.setFarmId(request.getFarmId());
        pond.setSize(request.getSize());
        pond.setDepth(request.getDepth());
        pond.setCapacity(request.getCapacity());
        pond.setPrimarySpecies(request.getPrimarySpecies());
        pond.setWaterSource(request.getWaterSource());
        pond.setPondType(request.getPondType());
        pond.setAerationType(request.getAerationType());
        pond.setNotes(request.getNotes());
        pond.setLocation(request.getLocation());
        
        return pond;
    }
    
    public void updateEntity(Pond pond, PondUpdateRequest request) {
        if (pond == null || request == null) {
            return;
        }
        
        if (request.getName() != null) {
            pond.setName(request.getName());
        }
        if (request.getCode() != null) {
            pond.setCode(request.getCode());
        }
        if (request.getSize() != null) {
            pond.setSize(request.getSize());
        }
        if (request.getDepth() != null) {
            pond.setDepth(request.getDepth());
        }
        if (request.getCapacity() != null) {
            pond.setCapacity(request.getCapacity());
        }
        if (request.getPrimarySpecies() != null) {
            pond.setPrimarySpecies(request.getPrimarySpecies());
        }
        if (request.getWaterSource() != null) {
            pond.setWaterSource(request.getWaterSource());
        }
        if (request.getPondType() != null) {
            pond.setPondType(request.getPondType());
        }
        if (request.getAerationType() != null) {
            pond.setAerationType(request.getAerationType());
        }
        if (request.getNotes() != null) {
            pond.setNotes(request.getNotes());
        }
        if (request.getLocation() != null) {
            pond.setLocation(request.getLocation());
        }
        
        // Water quality parameters
        if (request.getOxygenLevel() != null) {
            pond.setOxygenLevel(request.getOxygenLevel());
        }
        if (request.getPhLevel() != null) {
            pond.setPhLevel(request.getPhLevel());
        }
        if (request.getTemperature() != null) {
            pond.setTemperature(request.getTemperature());
        }
        if (request.getAmmonia() != null) {
            pond.setAmmonia(request.getAmmonia());
        }
        if (request.getNitrite() != null) {
            pond.setNitrite(request.getNitrite());
        }
        if (request.getNitrate() != null) {
            pond.setNitrate(request.getNitrate());
        }
        
        // Performance metrics
        if (request.getFeedConversionRatio() != null) {
            pond.setFeedConversionRatio(request.getFeedConversionRatio());
        }
        if (request.getMortalityRate() != null) {
            pond.setMortalityRate(request.getMortalityRate());
        }
        if (request.getGrowthRate() != null) {
            pond.setGrowthRate(request.getGrowthRate());
        }
        if (request.getEstimatedYield() != null) {
            pond.setEstimatedYield(request.getEstimatedYield());
        }
        if (request.getAverageWeight() != null) {
            pond.setAverageWeight(request.getAverageWeight());
        }
    }
    
    private String determineWaterQualityStatus(Pond pond) {
        if (pond.hasPoorWaterQuality()) {
            return "poor";
        }
        
        // Check if all parameters are optimal
        boolean optimalOxygen = pond.getOxygenLevel() != null && 
                               pond.getOxygenLevel().compareTo(BigDecimal.valueOf(6.0)) >= 0 &&
                               pond.getOxygenLevel().compareTo(BigDecimal.valueOf(8.0)) <= 0;
        
        boolean optimalPh = pond.getPhLevel() != null &&
                          pond.getPhLevel().compareTo(BigDecimal.valueOf(7.0)) >= 0 &&
                          pond.getPhLevel().compareTo(BigDecimal.valueOf(8.0)) <= 0;
        
        boolean optimalTemp = pond.getTemperature() != null &&
                             pond.getTemperature().compareTo(BigDecimal.valueOf(25.0)) >= 0 &&
                             pond.getTemperature().compareTo(BigDecimal.valueOf(28.0)) <= 0;
        
        if (optimalOxygen && optimalPh && optimalTemp) {
            return "excellent";
        } else if (pond.getOxygenLevel() != null && pond.getOxygenLevel().compareTo(BigDecimal.valueOf(5.0)) >= 0 &&
                   pond.getPhLevel() != null && pond.getPhLevel().compareTo(BigDecimal.valueOf(6.5)) >= 0 &&
                   pond.getPhLevel().compareTo(BigDecimal.valueOf(8.5)) <= 0 &&
                   pond.getTemperature() != null && pond.getTemperature().compareTo(BigDecimal.valueOf(20.0)) >= 0 &&
                   pond.getTemperature().compareTo(BigDecimal.valueOf(30.0)) <= 0) {
            return "good";
        } else {
            return "fair";
        }
    }
}
