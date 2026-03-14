package com.smartfarm.aquaculture.repository;

import com.smartfarm.aquaculture.domain.model.Pond;
import com.smartfarm.aquaculture.domain.model.PondStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PondRepository extends JpaRepository<Pond, Long> {
    
    // Basic queries
    Optional<Pond> findByCode(String code);
    List<Pond> findByFarmId(Long farmId);
    Page<Pond> findByFarmId(Long farmId, Pageable pageable);
    
    // Status-based queries
    List<Pond> findByStatus(PondStatus status);
    List<Pond> findByFarmIdAndStatus(Long farmId, PondStatus status);
    Page<Pond> findByFarmIdAndStatus(Long farmId, PondStatus status, Pageable pageable);
    
    // Species-based queries
    List<Pond> findByPrimarySpecies(String species);
    List<Pond> findByFarmIdAndPrimarySpecies(Long farmId, String species);
    
    // Water quality alerts
    @Query("SELECT p FROM Pond p WHERE p.oxygenLevel < :threshold OR " +
           "p.phLevel < :phMin OR p.phLevel > :phMax OR " +
           "p.temperature < :tempMin OR p.temperature > :tempMax")
    List<Pond> findPondsWithPoorWaterQuality(@Param("threshold") BigDecimal oxygenThreshold,
                                            @Param("phMin") BigDecimal phMin,
                                            @Param("phMax") BigDecimal phMax,
                                            @Param("tempMin") BigDecimal tempMin,
                                            @Param("tempMax") BigDecimal tempMax);
    
    // Overstocked ponds
    @Query("SELECT p FROM Pond p WHERE p.currentStock > p.capacity")
    List<Pond> findOverstockedPonds();
    
    @Query("SELECT p FROM Pond p WHERE p.currentStock > (p.capacity * :percentage)")
    List<Pond> findPondsOverCapacityThreshold(@Param("percentage") BigDecimal percentage);
    
    // Feeding schedule queries
    @Query("SELECT p FROM Pond p WHERE p.lastFeedingTime IS NULL OR " +
           "p.lastFeedingTime < :cutoffTime")
    List<Pond> findPondsNeedingFeeding(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    // Water testing queries
    @Query("SELECT p FROM Pond p WHERE p.lastWaterTest IS NULL OR " +
           "p.lastWaterTest < :cutoffTime")
    List<Pond> findPondsNeedingWaterTest(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    // Harvest-related queries
    @Query("SELECT p FROM Pond p WHERE p.expectedHarvestDate BETWEEN :startDate AND :endDate")
    List<Pond> findPondsScheduledForHarvest(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p FROM Pond p WHERE p.expectedHarvestDate <= :date AND p.status = 'ACTIVE'")
    List<Pond> findPondsReadyForHarvest(@Param("date") LocalDateTime date);
    
    // Stocking-related queries
    @Query("SELECT p FROM Pond p WHERE p.stockDate BETWEEN :startDate AND :endDate")
    List<Pond> findPondsStockedInPeriod(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
    
    // Performance analytics queries
    @Query("SELECT COUNT(p) FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE'")
    Long countActivePondsByFarm(@Param("farmId") Long farmId);
    
    @Query("SELECT SUM(p.currentStock) FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE'")
    BigDecimal sumCurrentStockByFarm(@Param("farmId") Long farmId);
    
    @Query("SELECT SUM(p.capacity) FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE'")
    BigDecimal sumCapacityByFarm(@Param("farmId") Long farmId);
    
    @Query("SELECT SUM(p.size) FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE'")
    BigDecimal sumSizeByFarm(@Param("farmId") Long farmId);
    
    @Query("SELECT AVG(p.growthRate) FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE' AND p.growthRate IS NOT NULL")
    BigDecimal averageGrowthRateByFarm(@Param("farmId") Long farmId);
    
    @Query("SELECT AVG(p.feedConversionRatio) FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE' AND p.feedConversionRatio IS NOT NULL")
    BigDecimal averageFeedConversionRatioByFarm(@Param("farmId") Long farmId);
    
    @Query("SELECT AVG(p.mortalityRate) FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE' AND p.mortalityRate IS NOT NULL")
    BigDecimal averageMortalityRateByFarm(@Param("farmId") Long farmId);
    
    @Query("SELECT SUM(p.estimatedYield) FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE' AND p.estimatedYield IS NOT NULL")
    BigDecimal totalEstimatedYieldByFarm(@Param("farmId") Long farmId);
    
    // Search functionality
    @Query("SELECT p FROM Pond p WHERE p.farmId = :farmId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.primarySpecies) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Pond> searchPondsByFarm(@Param("farmId") Long farmId,
                                @Param("searchTerm") String searchTerm,
                                Pageable pageable);
    
    // Advanced analytics
    @Query("SELECT p.primarySpecies, COUNT(p), SUM(p.currentStock), AVG(p.growthRate) " +
           "FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE' " +
           "GROUP BY p.primarySpecies")
    List<Object[]> getSpeciesBreakdownByFarm(@Param("farmId") Long farmId);
    
    @Query("SELECT p.status, COUNT(p) FROM Pond p WHERE p.farmId = :farmId GROUP BY p.status")
    List<Object[]> getStatusBreakdownByFarm(@Param("farmId") Long farmId);
    
    @Query("SELECT p.pondType, COUNT(p), SUM(p.size) FROM Pond p WHERE p.farmId = :farmId " +
           "GROUP BY p.pondType")
    List<Object[]> getPondTypeBreakdownByFarm(@Param("farmId") Long farmId);
    
    // Maintenance and operations
    @Query("SELECT p FROM Pond p WHERE p.status IN ('MAINTENANCE', 'REPAIR', 'CLEANING')")
    List<Pond> findPondsUnderMaintenance();
    
    @Query("SELECT p FROM Pond p WHERE p.farmId = :farmId AND " +
           "(p.status = 'MAINTENANCE' OR p.status = 'REPAIR' OR p.status = 'CLEANING')")
    List<Pond> findPondsUnderMaintenanceByFarm(@Param("farmId") Long farmId);
    
    // Custom queries for specific business rules
    @Query("SELECT p FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE' AND " +
           "p.currentStock IS NOT NULL AND p.currentStock > 0 AND " +
           "p.stockDate IS NOT NULL AND p.stockDate > :sinceDate")
    List<Pond> findActiveStockedPondsSince(@Param("farmId") Long farmId,
                                          @Param("sinceDate") LocalDateTime sinceDate);
    
    @Query("SELECT p FROM Pond p WHERE p.farmId = :farmId AND p.status = 'ACTIVE' AND " +
           "p.expectedHarvestDate IS NOT NULL AND p.expectedHarvestDate BETWEEN :startDate AND :endDate")
    List<Pond> findPondsForHarvestPlanning(@Param("farmId") Long farmId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);
}
