package com.ridesharing.main;

import java.util.Scanner;
import com.ridesharing.service.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService us = new UserService();
        RideService rs = new RideService();
        BookingService bs = new BookingService();

        while (true) {
            System.out.println("\n1 Register  2 Login  3 Exit");
            int ch = sc.nextInt();

            if (ch == 1) {
                System.out.print("Name: ");
                String n = sc.next();
                System.out.print("Mobile: ");
                String m = sc.next();
                System.out.print("Password: ");
                String p = sc.next();
                us.register(n, m, p);
            }

            else if (ch == 2) {
                System.out.print("Mobile: ");
                String m = sc.next();
                System.out.print("Password: ");
                String p = sc.next();

                if (us.login(m, p)) {

                    while (true) {
                        System.out.println("\n1 Create Ride");
                        System.out.println("2 View All Rides");
                        System.out.println("3 Book Ride");
                        System.out.println("4 My Rides");
                        System.out.println("5 My Bookings");
                        System.out.println("6 Cancel Ride");
                        System.out.println("7 Logout");

                        int c = sc.nextInt();

                        if (c == 1) {
                            System.out.print("Source: ");
                            String s = sc.next();
                            System.out.print("Destination: ");
                            String d = sc.next();
                            System.out.print("Seats: ");
                            int seats = sc.nextInt();
                            System.out.print("Fare: ");
                            double fare = sc.nextDouble();
                            rs.createRide(s, d, seats, fare);
                        }
                        else if (c == 2) rs.viewAllRides();
                        else if (c == 3) {
                            System.out.print("Ride ID: ");
                            int r = sc.nextInt();
                            System.out.print("Seats: ");
                            int st = sc.nextInt();
                            bs.bookRide(r, st);
                        }
                        else if (c == 4) rs.viewMyRides();
                        else if (c == 5) bs.viewMyBookings();
                        else if (c == 6) {
                            System.out.print("Ride ID: ");
                            rs.cancelMyRide(sc.nextInt());
                        }
                        else break;
                    }
                } else {
                    System.out.println("Login Failed");
                }
            }
            else break;
        }
        sc.close();
    }
}
