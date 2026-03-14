package com.smartfarm.aquaculture.domain.model;

public enum TemperaturePreference {
    COLD_WATER("Cold Water", "Prefers temperatures below 20°C"),
    COOL_WATER("Cool Water", "Prefers temperatures between 15-25°C"),
    WARM_WATER("Warm Water", "Prefers temperatures between 20-30°C"),
    TROPICAL("Tropical", "Prefers temperatures above 25°C"),
    EURYTHERMAL("Eurythermal", "Tolerates wide temperature ranges"),
    STENOTHERMAL("Stenothermal", "Requires narrow temperature ranges");
    
    private final String displayName;
    private final String description;
    
    TemperaturePreference(String displayName, String description) {
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
