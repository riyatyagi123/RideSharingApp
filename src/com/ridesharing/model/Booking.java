package com.ridesharing.model;

public class Booking {
    private int bookingid;
    private int userid;
    private int rideid;
    private int seatsBooked;
    private double total;

    public int getBookingid() { return bookingid; }
    public void setBookingid(int bookingid) { this.bookingid = bookingid; }

    public int getUserid() { return userid; }
    public void setUserid(int userid) { this.userid = userid; }

    public int getRideid() { return rideid; }
    public void setRideid(int rideid) { this.rideid = rideid; }

    public int getSeatsBooked() { return seatsBooked; }
    public void setSeatsBooked(int seatsBooked) { this.seatsBooked = seatsBooked; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
