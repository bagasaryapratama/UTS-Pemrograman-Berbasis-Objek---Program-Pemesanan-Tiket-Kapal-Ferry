package com.ferry;

/**
 * Class TariffService
 * Menentukan tarif otomatis dari sistem.
 */
public final class TariffService {
    private static final long ROUNDING_UNIT = 1000L;
    private static final long NON_VEHICLE_BASE_TARIFF = 25_000L;

    private TariffService() {
    }

    /**
     * Menghitung tarif tiket secara otomatis.
     */
    public static long calculateTariff(Route route, Vehicle vehicle,
                                       ServiceType serviceType) {
        long baseTariff = (vehicle == null)
                ? NON_VEHICLE_BASE_TARIFF
                : vehicle.getBaseTariff();

        double routeMultiplier = getRouteMultiplier(route);
        double calculated = baseTariff
                * routeMultiplier
                * serviceType.getTariffMultiplier();

        return Math.round(calculated / ROUNDING_UNIT) * ROUNDING_UNIT;
    }

    private static double getRouteMultiplier(Route route) {
        return 1.00;
    }
}
