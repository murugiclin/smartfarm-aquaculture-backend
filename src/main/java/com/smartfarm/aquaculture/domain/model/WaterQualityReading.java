package com.smartfarm.aquaculture.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "water_quality_readings", indexes = {
    @Index(name = "idx_wqr_pond_id", columnList = "pondId"),
    @Index(name = "idx_wqr_timestamp", columnList = "timestamp"),
    @Index(name = "idx_wqr_alert_level", columnList = "alertLevel")
})
@EntityListeners(AuditingEntityListener.class)
public class WaterQualityReading {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Pond ID is required")
    @Column(nullable = false)
    private Long pondId;
    
    @NotNull(message = "Timestamp is required")
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @NotNull(message = "Oxygen level is required")
    @DecimalMin(value = "0.0", message = "Oxygen level cannot be negative")
    @Digits(integer = 3, fraction = 2, message = "Oxygen level must have valid format")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal oxygenLevel; // mg/L
    
    @NotNull(message = "pH level is required")
    @DecimalMin(value = "0.0", message = "pH level cannot be negative")
    @Digits(integer = 3, fraction = 2, message = "pH level must have valid format")
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal phLevel;
    
    @NotNull(message = "Temperature is required")
    @DecimalMin(value = "0.0", message = "Temperature cannot be negative")
    @Digits(integer = 3, fraction = 2, message = "Temperature must have valid format")
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal temperature; // Celsius
    
    @DecimalMin(value = "0.0", message = "Ammonia cannot be negative")
    @Digits(integer = 3, fraction = 3, message = "Ammonia must have valid format")
    @Column(precision = 6, scale = 3)
    private BigDecimal ammonia; // mg/L
    
    @DecimalMin(value = "0.0", message = "Nitrite cannot be negative")
    @Digits(integer = 3, fraction = 3, message = "Nitrite must have valid format")
    @Column(precision = 6, scale = 3)
    private BigDecimal nitrite; // mg/L
    
    @DecimalMin(value = "0.0", message = "Nitrate cannot be negative")
    @Digits(integer = 4, fraction = 2, message = "Nitrate must have valid format")
    @Column(precision = 6, scale = 2)
    private BigDecimal nitrate; // mg/L
    
    @DecimalMin(value = "0.0", message = "Alkalinity cannot be negative")
    @Digits(integer = 4, fraction = 1, message = "Alkalinity must have valid format")
    @Column(precision = 5, scale = 1)
    private BigDecimal alkalinity; // mg/L as CaCO3
    
    @DecimalMin(value = "0.0", message = "Hardness cannot be negative")
    @Digits(integer = 4, fraction = 1, message = "Hardness must have valid format")
    @Column(precision = 5, scale = 1)
    private BigDecimal hardness; // mg/L as CaCO3
    
    @DecimalMin(value = "0.0", message = "Turbidity cannot be negative")
    @Digits(integer = 3, fraction = 1, message = "Turbidity must have valid format")
    @Column(precision = 4, scale = 1)
    private BigDecimal turbidity; // NTU
    
    @DecimalMin(value = "0.0", message = "Salinity cannot be negative")
    @Digits(integer = 2, fraction = 1, message = "Salinity must have valid format")
    @Column(precision = 3, scale = 1)
    private BigDecimal salinity; // ppt
    
    @Column(length = 50)
    private String readingMethod; // manual, sensor, lab_test
    
    @Column(length = 100)
    private String sensorId;
    
    @Column(length = 500)
    private String notes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertLevel alertLevel = AlertLevel.NORMAL;
    
    @Column(length = 20)
    private String recordedBy;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Version
    private Long version;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pondId", insertable = false, updatable = false)
    private Pond pond;
    
    // Constructors
    public WaterQualityReading() {}
    
    public WaterQualityReading(Long pondId, BigDecimal oxygenLevel, BigDecimal phLevel, BigDecimal temperature) {
        this.pondId = pondId;
        this.timestamp = LocalDateTime.now();
        this.oxygenLevel = oxygenLevel;
        this.phLevel = phLevel;
        this.temperature = temperature;
        this.calculateAlertLevel();
    }
    
    // Business logic methods
    public void calculateAlertLevel() {
        if (isCritical()) {
            this.alertLevel = AlertLevel.CRITICAL;
        } else if (isWarning()) {
            this.alertLevel = AlertLevel.WARNING;
        } else {
            this.alertLevel = AlertLevel.NORMAL;
        }
    }
    
    public boolean isCritical() {
        return (oxygenLevel != null && oxygenLevel.compareTo(BigDecimal.valueOf(3.0)) < 0) ||
               (phLevel != null && (phLevel.compareTo(BigDecimal.valueOf(5.5)) < 0 || 
                                  phLevel.compareTo(BigDecimal.valueOf(9.0)) > 0)) ||
               (temperature != null && (temperature.compareTo(BigDecimal.valueOf(15.0)) < 0 || 
                                       temperature.compareTo(BigDecimal.valueOf(35.0)) > 0)) ||
               (ammonia != null && ammonia.compareTo(BigDecimal.valueOf(1.0)) > 0) ||
               (nitrite != null && nitrite.compareTo(BigDecimal.valueOf(0.5)) > 0);
    }
    
    public boolean isWarning() {
        return (oxygenLevel != null && oxygenLevel.compareTo(BigDecimal.valueOf(5.0)) < 0) ||
               (phLevel != null && (phLevel.compareTo(BigDecimal.valueOf(6.5)) < 0 || 
                                  phLevel.compareTo(BigDecimal.valueOf(8.5)) > 0)) ||
               (temperature != null && (temperature.compareTo(BigDecimal.valueOf(20.0)) < 0 || 
                                       temperature.compareTo(BigDecimal.valueOf(30.0)) > 0)) ||
               (ammonia != null && ammonia.compareTo(BigDecimal.valueOf(0.5)) > 0) ||
               (nitrite != null && nitrite.compareTo(BigDecimal.valueOf(0.2)) > 0);
    }
    
    public boolean isOptimal() {
        return oxygenLevel != null && oxygenLevel.compareTo(BigDecimal.valueOf(6.0)) >= 0 &&
               oxygenLevel.compareTo(BigDecimal.valueOf(8.0)) <= 0 &&
               phLevel != null && phLevel.compareTo(BigDecimal.valueOf(7.0)) >= 0 &&
               phLevel.compareTo(BigDecimal.valueOf(8.0)) <= 0 &&
               temperature != null && temperature.compareTo(BigDecimal.valueOf(25.0)) >= 0 &&
               temperature.compareTo(BigDecimal.valueOf(28.0)) <= 0;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPondId() { return pondId; }
    public void setPondId(Long pondId) { this.pondId = pondId; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public BigDecimal getOxygenLevel() { return oxygenLevel; }
    public void setOxygenLevel(BigDecimal oxygenLevel) { 
        this.oxygenLevel = oxygenLevel;
        calculateAlertLevel();
    }
    
    public BigDecimal getPhLevel() { return phLevel; }
    public void setPhLevel(BigDecimal phLevel) { 
        this.phLevel = phLevel;
        calculateAlertLevel();
    }
    
    public BigDecimal getTemperature() { return temperature; }
    public void setTemperature(BigDecimal temperature) { 
        this.temperature = temperature;
        calculateAlertLevel();
    }
    
    public BigDecimal getAmmonia() { return ammonia; }
    public void setAmmonia(BigDecimal ammonia) { 
        this.ammonia = ammonia;
        calculateAlertLevel();
    }
    
    public BigDecimal getNitrite() { return nitrite; }
    public void setNitrite(BigDecimal nitrite) { 
        this.nitrite = nitrite;
        calculateAlertLevel();
    }
    
    public BigDecimal getNitrate() { return nitrate; }
    public void setNitrate(BigDecimal nitrate) { 
        this.nitrate = nitrate;
        calculateAlertLevel();
    }
    
    public BigDecimal getAlkalinity() { return alkalinity; }
    public void setAlkalinity(BigDecimal alkalinity) { this.alkalinity = alkalinity; }
    
    public BigDecimal getHardness() { return hardness; }
    public void setHardness(BigDecimal hardness) { this.hardness = hardness; }
    
    public BigDecimal getTurbidity() { return turbidity; }
    public void setTurbidity(BigDecimal turbidity) { this.turbidity = turbidity; }
    
    public BigDecimal getSalinity() { return salinity; }
    public void setSalinity(BigDecimal salinity) { this.salinity = salinity; }
    
    public String getReadingMethod() { return readingMethod; }
    public void setReadingMethod(String readingMethod) { this.readingMethod = readingMethod; }
    
    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public AlertLevel getAlertLevel() { return alertLevel; }
    public void setAlertLevel(AlertLevel alertLevel) { this.alertLevel = alertLevel; }
    
    public String getRecordedBy() { return recordedBy; }
    public void setRecordedBy(String recordedBy) { this.recordedBy = recordedBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public Pond getPond() { return pond; }
    public void setPond(Pond pond) { this.pond = pond; }
}
