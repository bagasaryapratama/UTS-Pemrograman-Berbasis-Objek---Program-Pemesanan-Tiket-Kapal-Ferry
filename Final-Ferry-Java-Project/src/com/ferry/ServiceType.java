package com.ferry;

/**
 * Enum ServiceType
 * Dua pilihan layanan yang tersedia.
 */
public enum ServiceType {
    REGULER("Reguler", "Lebih murah, waktu perjalanan lebih lama", 1.00, 150),
    EXPRESS("Express", "Lebih cepat, tarif lebih mahal", 1.50, 90);

    private final String displayName;
    private final String description;
    private final double tariffMultiplier;
    private final int durationMinutes;

    ServiceType(String displayName, String description,
                double tariffMultiplier, int durationMinutes) {
        this.displayName = displayName;
        this.description = description;
        this.tariffMultiplier = tariffMultiplier;
        this.durationMinutes = durationMinutes;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public double getTariffMultiplier() {
        return tariffMultiplier;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
