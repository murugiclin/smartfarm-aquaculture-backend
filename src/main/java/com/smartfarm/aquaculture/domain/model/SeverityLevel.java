package com.smartfarm.aquaculture.domain.model;

public enum SeverityLevel {
    LOW("Low", "Minor health issue, monitoring required"),
    MEDIUM("Medium", "Significant health issue, treatment needed"),
    HIGH("High", "Severe health issue, immediate treatment required"),
    CRITICAL("Critical", "Life-threatening condition, emergency response needed");
    
    private final String displayName;
    private final String description;
    
    SeverityLevel(String displayName, String description) {
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
