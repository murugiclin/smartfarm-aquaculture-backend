package com.smartfarm.aquaculture.domain.model;

public enum FishCategory {
    TILAPIA("Tilapia", "Freshwater fish known for fast growth"),
    CATFISH("Catfish", "Bottom-dwelling freshwater fish"),
    CARP("Carp", "Hardy freshwater fish family"),
    TROUT("Trout", "Cold-water freshwater fish"),
    SALMON("Salmon", "Anadromous fish, migrates between fresh and saltwater"),
    BASS("Bass", "Predatory freshwater fish"),
    MACKEREL("Mackerel", "Saltwater pelagic fish"),
    GROUPER("Grouper", "Saltwater reef fish"),
    SNAPPER("Snapper", "Saltwater fish found in tropical waters"),
    TUNA("Tuna", "Large saltwater pelagic fish"),
    SHRIMP("Shrimp", "Crustaceans, not fish but commonly farmed"),
    PRAWN("Prawn", "Larger crustaceans similar to shrimp"),
    LOBSTER("Lobster", "Large marine crustaceans"),
    CRAB("Crab", "Marine crustaceans with hard shells"),
    CLAM("Clam", "Bivalve mollusks"),
    OYSTER("Oyster", "Bivalve mollusks that produce pearls"),
    MUSSEL("Mussel", "Bivalve mollusks that attach to surfaces"),
    SCALLOP("Scallop", "Bivalve mollusks that can swim"),
    ABALONE("Abalone", "Marine gastropod mollusks"),
    SEAWEED("Seaweed", "Marine algae and plants"),
    OTHER("Other", "Species not fitting in other categories");
    
    private final String displayName;
    private final String description;
    
    FishCategory(String displayName, String description) {
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
