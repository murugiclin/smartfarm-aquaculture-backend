package com.smartfarm.aquaculture.domain.model;

public enum WaterType {
    FRESHWATER("Freshwater", "Lives in water with low salt content"),
    SALTWATER("Saltwater", "Lives in ocean water with high salt content"),
    BRACKISH("Brackish", "Lives in water with intermediate salt content"),
    ANADROMOUS("Anadromous", "Migrates from salt to freshwater to spawn"),
    CATADROMOUS("Catadromous", "Migrates from fresh to saltwater to spawn"),
    AMPHIDROMOUS("Amphidromous", "Migrates between fresh and saltwater not for breeding"),
    EURYHALINE("Euryhaline", "Tolerates wide range of salinity"),
    STENOHALINE("Stenohaline", "Requires narrow range of salinity");
    
    private final String displayName;
    private final String description;
    
    WaterType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}
