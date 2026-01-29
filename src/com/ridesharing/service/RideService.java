package com.ridesharing.service;

import com.ridesharing.dao.RideDAO;
import com.ridesharing.util.Session;

public class RideService {

    private RideDAO dao = new RideDAO();

    public void createRide(String s, String d, int seats, double fare) {
        dao.createRide(s, d, seats, fare, Session.userId);
    }

    public void viewAllRides() {
        dao.viewAllRides();
    }

    public void viewMyRides() {
        dao.viewMyRides(Session.userId);
    }

    public void cancelMyRide(int rideId) {
        dao.cancelMyRide(rideId, Session.userId);
    }
}
