package com.ferry;

/**
 * Class Route
 * Menyimpan pelabuhan asal dan tujuan.
 */
public class Route {
    private String routeId;
    private String origin;
    private String destination;

    public Route(String routeId, String origin, String destination) {
        this.routeId = routeId;
        this.origin = origin;
        this.destination = destination;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }
}
