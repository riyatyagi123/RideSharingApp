package com.ridesharing.model;

public class Ride {
    private int rideid;
    private String source;
    private String destination;
    private int seats;
    private double fare;
    private int createdBy;

    public int getRideid() { return rideid; }
    public void setRideid(int rideid) { this.rideid = rideid; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
}
