package org.example.LLD;

import java.util.List;

//Vehicle{
//    String VehicleNumber;
//    String Type;
//}
//
//ParkingSpot{
//    int spotId;
//    String spotType;
//    boolean isAvailable;
//    isFree();
//    park();
//    unpark();
//}
//ParkingFloor{
//    int floorNumber;
//    List<ParkingSpots> spots;
//    List<ParkingSpot> getAvailableSpots();
//    ParkingSpot getNearestAvailableSpot(Vehicle vehicle);
//}
//Ticket{
//    String TicketId;
//    Vehicle vehicle;
//    Parking spot;
//    long startTime;
//}
//ParkingLot
//{
//    List<ParkingFloor> floors;
//    List<ParkingSpot> getAllAvailableSpots();
//    ParkingSpot findNearestSpot(Vehicle vehicle)
//    Ticket parkVehicle(Vehicle vehicle)
//    double unparkVehicle(String ticketId)
//}
import java.util.*;
import java.time.*;

class Vehicle {
    String vehicleNumber;
    int tyres;

    Vehicle(String vehicleNumber, int tyres) {
        this.vehicleNumber = vehicleNumber;
        this.tyres = tyres;
    }
}

class ParkingSpot {
    int spotId;
    boolean isAvailable;
    int spotType; // 1: Bike, 2: Car, 3: Truck
    Vehicle vehicle; // Vehicle parked in this spot

    ParkingSpot(int spotId, int spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.isAvailable = true;
    }

    public boolean isFree() {
        return isAvailable;
    }

    public void park(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.isAvailable = false;
    }

    public void unpark() {
        this.vehicle = null;
        this.isAvailable = true;
    }
}

class ParkingFloor {
    int floorNumber;
    List<ParkingSpot> spots;

    ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = spots;
    }

    public List<ParkingSpot> getAvailableSpots() {
        List<ParkingSpot> available = new ArrayList<>();
        for (ParkingSpot spot : spots) {
            if (spot.isFree()) {
                available.add(spot);
            }
        }
        return available;
    }

    public ParkingSpot getNearestAvailableSpot(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (spot.isFree() && spot.spotType >= vehicle.tyres) {
                // Simple logic: spotType >= tyres, e.g., Car requires 4 tyres
                return spot;
            }
        }
        return null;
    }
}

class Ticket {
    String ticketId;
    Vehicle vehicle;
    ParkingSpot spot;
    LocalDateTime startTime;

    Ticket(String ticketId, Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.startTime = LocalDateTime.now();
    }
}

public class ParkingLot {
    List<ParkingFloor> floors = new ArrayList<>();
    Map<String, Ticket> activeTickets = new HashMap<>();

    public ParkingLot(List<ParkingFloor> floors) {
        this.floors = floors;
    }

    public List<ParkingSpot> getAllAvailableSpots() {
        List<ParkingSpot> available = new ArrayList<>();
        for (ParkingFloor floor : floors) {
            available.addAll(floor.getAvailableSpots());
        }
        return available;
    }

    public ParkingSpot findNearestSpot(Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.getNearestAvailableSpot(vehicle);
            if (spot != null) return spot;
        }
        return null;
    }

    public Ticket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = findNearestSpot(vehicle);
        if (spot == null) {
            System.out.println("No available spot for vehicle " + vehicle.vehicleNumber);
            return null;
        }
        spot.park(vehicle);
        String ticketId = "TICKET-" + UUID.randomUUID().toString();
        Ticket ticket = new Ticket(ticketId, vehicle, spot);
        activeTickets.put(ticketId, ticket);
        System.out.println("Parked vehicle " + vehicle.vehicleNumber + " at spot " + spot.spotId);
        return ticket;
    }

    public double unparkVehicle(String ticketId) {
        Ticket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            System.out.println("Invalid ticket ID");
            return 0;
        }
        ticket.spot.unpark();
        activeTickets.remove(ticketId);

        LocalDateTime endTime = LocalDateTime.now();
        long minutes = Duration.between(ticket.startTime, endTime).toMinutes();
        double rate = 10; // Assume 10 currency units per minute
        double bill = rate * minutes;

        System.out.println("Unparked vehicle " + ticket.vehicle.vehicleNumber + ". Bill: " + bill);
        return bill;
    }

    public static void main(String[] args) {
        // Example setup
        List<ParkingSpot> floor1Spots = Arrays.asList(
                new ParkingSpot(1, 2),
                new ParkingSpot(2, 4),
                new ParkingSpot(3, 4)
        );
        ParkingFloor floor1 = new ParkingFloor(1, floor1Spots);

        ParkingLot parkingLot = new ParkingLot(Arrays.asList(floor1));

        Vehicle car1 = new Vehicle("ABC123", 4);
        Ticket ticket1 = parkingLot.parkVehicle(car1);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Wait simulation (or just unpark immediately)
        parkingLot.unparkVehicle(ticket1.ticketId);
    }
}
