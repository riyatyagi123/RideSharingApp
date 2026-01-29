package com.ridesharing.service;

import com.ridesharing.dao.BookingDAO;
import com.ridesharing.util.Session;

public class BookingService {

    private BookingDAO dao = new BookingDAO();

    public void bookRide(int rideId, int seats) {
        dao.bookRide(Session.userId, rideId, seats);
    }

    public void viewMyBookings() {
        dao.viewMyBookings(Session.userId);
    }
}
