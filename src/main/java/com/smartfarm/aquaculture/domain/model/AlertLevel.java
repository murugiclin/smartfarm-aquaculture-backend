package com.smartfarm.aquaculture.domain.model;

public enum AlertLevel {
    NORMAL("Normal", "All parameters are within optimal range"),
    WARNING("Warning", "Some parameters need attention"),
    CRITICAL("Critical", "Immediate action required"),
    EMERGENCY("Emergency", "Life-threatening conditions");
    
    private final String displayName;
    private final String description;
    
    AlertLevel(String displayName, String description) {
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
