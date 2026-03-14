package com.smartfarm.aquaculture.domain.model;

public enum PondStatus {
    ACTIVE("Active", "Pond is currently in use and operational"),
    INACTIVE("Inactive", "Pond is not currently in use"),
    MAINTENANCE("Maintenance", "Pond is under maintenance"),
    QUARANTINE("Quarantine", "Pond is under quarantine due to disease"),
    PREPARING("Preparing", "Pond is being prepared for stocking"),
    HARVESTING("Harvesting", "Pond is being harvested"),
    CLEANING("Cleaning", "Pond is being cleaned"),
    RESTING("Resting", "Pond is resting between cycles"),
    DRAINED("Drained", "Pond is drained"),
    REPAIR("Repair", "Pond needs repair");
    
    private final String displayName;
    private final String description;
    
    PondStatus(String displayName, String description) {
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
