package com.ferry;

/**
 * Class Schedule
 * Menyimpan jadwal dan jenis layanan.
 */
public class Schedule {
    private String scheduleId;
    private String departureTime;
    private String checkInTime;
    private ServiceType serviceType;
    private int durationMinutes;

    public Schedule(String scheduleId, String departureTime,
                    String checkInTime, ServiceType serviceType,
                    int durationMinutes) {
        this.scheduleId = scheduleId;
        this.departureTime = departureTime;
        this.checkInTime = checkInTime;
        this.serviceType = serviceType;
        this.durationMinutes = durationMinutes;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
