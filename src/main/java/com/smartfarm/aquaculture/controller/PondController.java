package com.smartfarm.aquaculture.controller;

import com.smartfarm.aquaculture.domain.model.Pond;
import com.smartfarm.aquaculture.domain.model.PondStatus;
import com.smartfarm.aquaculture.dto.PondDto;
import com.smartfarm.aquaculture.dto.PondCreateRequest;
import com.smartfarm.aquaculture.dto.PondUpdateRequest;
import com.smartfarm.aquaculture.dto.ApiResponse;
import com.smartfarm.aquaculture.service.PondService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ponds")
@Tag(name = "Pond Management", description = "APIs for managing aquaculture ponds")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PondController {

    private final PondService pondService;

    public PondController(PondService pondService) {
        this.pondService = pondService;
    }

    @GetMapping
    @Operation(summary = "Get all ponds", description = "Retrieve paginated list of ponds")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST', 'VIEWER')")
    public ResponseEntity<ApiResponse<Page<PondDto>>> getAllPonds(
            @Parameter(description = "Farm ID to filter ponds")
            @RequestParam(required = false) Long farmId,
            
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "desc") String sortDir,
            
            @Parameter(description = "Search term")
            @RequestParam(required = false) String search,
            
            @Parameter(description = "Filter by status")
            @RequestParam(required = false) PondStatus status) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<PondDto> ponds = pondService.getPonds(farmId, status, search, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(ponds));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pond by ID", description = "Retrieve detailed information about a specific pond")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST', 'VIEWER')")
    public ResponseEntity<ApiResponse<PondDto>> getPondById(
            @Parameter(description = "Pond ID")
            @PathVariable Long id) {
        
        PondDto pond = pondService.getPondById(id);
        return ResponseEntity.ok(ApiResponse.success(pond));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get pond by code", description = "Retrieve pond information using pond code")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST', 'VIEWER')")
    public ResponseEntity<ApiResponse<PondDto>> getPondByCode(
            @Parameter(description = "Pond code")
            @PathVariable String code) {
        
        PondDto pond = pondService.getPondByCode(code);
        return ResponseEntity.ok(ApiResponse.success(pond));
    }

    @PostMapping
    @Operation(summary = "Create new pond", description = "Add a new pond to the system")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST')")
    public ResponseEntity<ApiResponse<PondDto>> createPond(
            @Valid @RequestBody PondCreateRequest request) {
        
        PondDto createdPond = pondService.createPond(request);
        return ResponseEntity.ok(ApiResponse.success(createdPond, "Pond created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pond", description = "Update existing pond information")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST')")
    public ResponseEntity<ApiResponse<PondDto>> updatePond(
            @Parameter(description = "Pond ID")
            @PathVariable Long id,
            
            @Valid @RequestBody PondUpdateRequest request) {
        
        PondDto updatedPond = pondService.updatePond(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedPond, "Pond updated successfully"));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update pond status", description = "Change the status of a pond")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST')")
    public ResponseEntity<ApiResponse<PondDto>> updatePondStatus(
            @Parameter(description = "Pond ID")
            @PathVariable Long id,
            
            @Parameter(description = "New status")
            @RequestParam PondStatus status) {
        
        PondDto updatedPond = pondService.updatePondStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updatedPond, "Pond status updated successfully"));
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update pond stock", description = "Update the current stock count of a pond")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST')")
    public ResponseEntity<ApiResponse<PondDto>> updatePondStock(
            @Parameter(description = "Pond ID")
            @PathVariable Long id,
            
            @Parameter(description = "Current stock count")
            @RequestParam BigDecimal currentStock) {
        
        PondDto updatedPond = pondService.updatePondStock(id, currentStock);
        return ResponseEntity.ok(ApiResponse.success(updatedPond, "Pond stock updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pond", description = "Remove a pond from the system")
    @PreAuthorize("hasRole('FARM_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deletePond(
            @Parameter(description = "Pond ID")
            @PathVariable Long id) {
        
        pondService.deletePond(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Pond deleted successfully"));
    }

    // Water Quality Management Endpoints
    @GetMapping("/{id}/water-quality/alerts")
    @Operation(summary = "Get water quality alerts", description = "Get ponds with poor water quality")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST', 'VIEWER')")
    public ResponseEntity<ApiResponse<List<PondDto>>> getWaterQualityAlerts(
            @Parameter(description = "Farm ID")
            @RequestParam(required = false) Long farmId) {
        
        List<PondDto> ponds = pondService.getPondsWithWaterQualityAlerts(farmId);
        return ResponseEntity.ok(ApiResponse.success(ponds));
    }

    @PatchMapping("/{id}/water-quality")
    @Operation(summary = "Update water quality", description = "Update water quality parameters")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST')")
    public ResponseEntity<ApiResponse<PondDto>> updateWaterQuality(
            @Parameter(description = "Pond ID")
            @PathVariable Long id,
            
            @Parameter(description = "Oxygen level (mg/L)")
            @RequestParam(required = false) BigDecimal oxygenLevel,
            
            @Parameter(description = "pH level")
            @RequestParam(required = false) BigDecimal phLevel,
            
            @Parameter(description = "Temperature (°C)")
            @RequestParam(required = false) BigDecimal temperature,
            
            @Parameter(description = "Ammonia (mg/L)")
            @RequestParam(required = false) BigDecimal ammonia,
            
            @Parameter(description = "Nitrite (mg/L)")
            @RequestParam(required = false) BigDecimal nitrite,
            
            @Parameter(description = "Nitrate (mg/L)")
            @RequestParam(required = false) BigDecimal nitrate) {
        
        PondDto updatedPond = pondService.updateWaterQuality(id, oxygenLevel, phLevel, temperature, 
                                                            ammonia, nitrite, nitrate);
        return ResponseEntity.ok(ApiResponse.success(updatedPond, "Water quality updated successfully"));
    }

    // Feeding Management Endpoints
    @GetMapping("/{id}/feeding-schedule")
    @Operation(summary = "Get feeding schedule", description = "Get ponds that need feeding")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST', 'FEEDING_STAFF')")
    public ResponseEntity<ApiResponse<List<PondDto>>> getFeedingSchedule(
            @Parameter(description = "Farm ID")
            @RequestParam(required = false) Long farmId) {
        
        List<PondDto> ponds = pondService.getPondsNeedingFeeding(farmId);
        return ResponseEntity.ok(ApiResponse.success(ponds));
    }

    @PatchMapping("/{id}/feeding")
    @Operation(summary = "Record feeding", description = "Record that a pond has been fed")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST', 'FEEDING_STAFF')")
    public ResponseEntity<ApiResponse<PondDto>> recordFeeding(
            @Parameter(description = "Pond ID")
            @PathVariable Long id,
            
            @Parameter(description = "Feed amount (kg)")
            @RequestParam BigDecimal feedAmount,
            
            @Parameter(description = "Feed type")
            @RequestParam(required = false) String feedType) {
        
        PondDto updatedPond = pondService.recordFeeding(id, feedAmount, feedType);
        return ResponseEntity.ok(ApiResponse.success(updatedPond, "Feeding recorded successfully"));
    }

    // Harvest Management Endpoints
    @GetMapping("/harvest/schedule")
    @Operation(summary = "Get harvest schedule", description = "Get ponds scheduled for harvest")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST')")
    public ResponseEntity<ApiResponse<List<PondDto>>> getHarvestSchedule(
            @Parameter(description = "Farm ID")
            @RequestParam(required = false) Long farmId,
            
            @Parameter(description = "Start date")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            
            @Parameter(description = "End date")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<PondDto> ponds = pondService.getPondsScheduledForHarvest(farmId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(ponds));
    }

    @GetMapping("/harvest/ready")
    @Operation(summary = "Get ponds ready for harvest", description = "Get ponds that are ready for harvest")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST')")
    public ResponseEntity<ApiResponse<List<PondDto>>> getPondsReadyForHarvest(
            @Parameter(description = "Farm ID")
            @RequestParam(required = false) Long farmId) {
        
        List<PondDto> ponds = pondService.getPondsReadyForHarvest(farmId);
        return ResponseEntity.ok(ApiResponse.success(ponds));
    }

    // Analytics and Reporting Endpoints
    @GetMapping("/analytics/summary")
    @Operation(summary = "Get pond summary analytics", description = "Get comprehensive pond analytics")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST', 'ANALYST')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPondAnalytics(
            @Parameter(description = "Farm ID")
            @RequestParam(required = false) Long farmId) {
        
        Map<String, Object> analytics = pondService.getPondAnalytics(farmId);
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }

    @GetMapping("/analytics/performance")
    @Operation(summary = "Get performance metrics", description = "Get pond performance metrics")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST', 'ANALYST')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPerformanceMetrics(
            @Parameter(description = "Farm ID")
            @RequestParam(required = false) Long farmId) {
        
        Map<String, Object> metrics = pondService.getPerformanceMetrics(farmId);
        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    @GetMapping("/analytics/species-breakdown")
    @Operation(summary = "Get species breakdown", description = "Get breakdown of ponds by species")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST', 'ANALYST')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSpeciesBreakdown(
            @Parameter(description = "Farm ID")
            @RequestParam(required = false) Long farmId) {
        
        List<Map<String, Object>> breakdown = pondService.getSpeciesBreakdown(farmId);
        return ResponseEntity.ok(ApiResponse.success(breakdown));
    }

    // Maintenance and Operations Endpoints
    @GetMapping("/maintenance/needed")
    @Operation(summary = "Get ponds needing maintenance", description = "Get ponds that need maintenance")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST')")
    public ResponseEntity<ApiResponse<List<PondDto>>> getPondsNeedingMaintenance(
            @Parameter(description = "Farm ID")
            @RequestParam(required = false) Long farmId) {
        
        List<PondDto> ponds = pondService.getPondsNeedingMaintenance(farmId);
        return ResponseEntity.ok(ApiResponse.success(ponds));
    }

    @GetMapping("/overstocked")
    @Operation(summary = "Get overstocked ponds", description = "Get ponds that are over capacity")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST')")
    public ResponseEntity<ApiResponse<List<PondDto>>> getOverstockedPonds(
            @Parameter(description = "Farm ID")
            @RequestParam(required = false) Long farmId,
            
            @Parameter(description = "Capacity threshold percentage")
            @RequestParam(defaultValue = "100") BigDecimal threshold) {
        
        List<PondDto> ponds = pondService.getOverstockedPonds(farmId, threshold);
        return ResponseEntity.ok(ApiResponse.success(ponds));
    }

    // Bulk Operations Endpoints
    @PatchMapping("/bulk/status")
    @Operation(summary = "Bulk update status", description = "Update status for multiple ponds")
    @PreAuthorize("hasRole('FARM_MANAGER')")
    public ResponseEntity<ApiResponse<List<PondDto>>> bulkUpdateStatus(
            @Parameter(description = "Pond IDs")
            @RequestParam List<Long> pondIds,
            
            @Parameter(description = "New status")
            @RequestParam PondStatus status) {
        
        List<PondDto> updatedPonds = pondService.bulkUpdateStatus(pondIds, status);
        return ResponseEntity.ok(ApiResponse.success(updatedPonds, "Status updated for " + updatedPonds.size() + " ponds"));
    }

    @PostMapping("/bulk/water-quality-check")
    @Operation(summary = "Bulk water quality check", description = "Trigger water quality check for multiple ponds")
    @PreAuthorize("hasAnyRole('FARM_MANAGER', 'AQUACULTURE_SPECIALIST')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> bulkWaterQualityCheck(
            @Parameter(description = "Pond IDs")
            @RequestParam List<Long> pondIds) {
        
        Map<String, Object> results = pondService.bulkWaterQualityCheck(pondIds);
        return ResponseEntity.ok(ApiResponse.success(results, "Water quality check initiated for " + pondIds.size() + " ponds"));
    }
}
