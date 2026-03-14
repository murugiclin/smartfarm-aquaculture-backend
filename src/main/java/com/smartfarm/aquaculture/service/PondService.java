package com.smartfarm.aquaculture.service;

import com.smartfarm.aquaculture.domain.model.Pond;
import com.smartfarm.aquaculture.domain.model.PondStatus;
import com.smartfarm.aquaculture.dto.PondDto;
import com.smartfarm.aquaculture.dto.PondCreateRequest;
import com.smartfarm.aquaculture.dto.PondUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PondService {
    
    // Basic CRUD operations
    Page<PondDto> getPonds(Long farmId, PondStatus status, String search, Pageable pageable);
    PondDto getPondById(Long id);
    PondDto getPondByCode(String code);
    PondDto createPond(PondCreateRequest request);
    PondDto updatePond(Long id, PondUpdateRequest request);
    PondDto updatePondStatus(Long id, PondStatus status);
    PondDto updatePondStock(Long id, BigDecimal currentStock);
    void deletePond(Long id);
    
    // Water quality management
    List<PondDto> getPondsWithWaterQualityAlerts(Long farmId);
    PondDto updateWaterQuality(Long id, BigDecimal oxygenLevel, BigDecimal phLevel, 
                              BigDecimal temperature, BigDecimal ammonia, 
                              BigDecimal nitrite, BigDecimal nitrate);
    
    // Feeding management
    List<PondDto> getPondsNeedingFeeding(Long farmId);
    PondDto recordFeeding(Long id, BigDecimal feedAmount, String feedType);
    
    // Harvest management
    List<PondDto> getPondsScheduledForHarvest(Long farmId, LocalDateTime startDate, LocalDateTime endDate);
    List<PondDto> getPondsReadyForHarvest(Long farmId);
    
    // Analytics and reporting
    Map<String, Object> getPondAnalytics(Long farmId);
    Map<String, Object> getPerformanceMetrics(Long farmId);
    List<Map<String, Object>> getSpeciesBreakdown(Long farmId);
    
    // Maintenance and operations
    List<PondDto> getPondsNeedingMaintenance(Long farmId);
    List<PondDto> getOverstockedPonds(Long farmId, BigDecimal threshold);
    
    // Bulk operations
    List<PondDto> bulkUpdateStatus(List<Long> pondIds, PondStatus status);
    Map<String, Object> bulkWaterQualityCheck(List<Long> pondIds);
    
    // Real-time monitoring
    Map<String, Object> getRealTimeStatus(Long farmId);
    List<Map<String, Object>> getRecentActivity(Long farmId, int limit);
    
    // Validation and business logic
    boolean validatePondCapacity(Long pondId, BigDecimal proposedStock);
    boolean isPondSuitableForSpecies(Long pondId, String speciesCode);
    BigDecimal calculateStockingDensity(Long pondId);
    BigDecimal calculateEstimatedYield(Long pondId);
    
    // Notifications and alerts
    void checkAndTriggerAlerts(Long pondId);
    List<Map<String, Object>> getActiveAlerts(Long farmId);
    void acknowledgeAlert(Long alertId, String acknowledgedBy);
}
