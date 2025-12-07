Parking Lot LLD refers to the Low-Level Design of a Parking Lot Management System.
**Design a Parking Lot System.**
You need to implement the classes and interactions required to operate a multi-level parking lot.
The system should support:

**Functional Requirements**
Requirements

1 The parking lot contains multiple floors, each having multiple spots.

2 Spot types:

    Two-wheeler
    
    Four-wheeler

3 When a vehicle enters:

    Find the nearest available suitable spot.
    
    Generate a parking ticket.

4 When a vehicle exits:

    Calculate the fee (e.g., ₹10 per hour for 2-wheeler, ₹20 for 4-wheeler).
    
    Mark the spot as available again.

5 Add APIs:

    parkVehicle(Vehicle v)
    
    unparkVehicle(String ticketId)
    
    getAvailableSpots()

############### IDENTIFY CLASSES, ATTRIBUTES, ACTIONS   ########
Vehicle{
String VehicleNumber;
String Type;
}

ParkingSpot{
int spotId;
String spotType;
boolean isAvailable;
isFree();
park();
unpark();
}
ParkingFloor{
int floorNumber;
List<ParkingSpots> spots;
List<ParkingSpot> getAvailableSpots();
ParkingSpot getNearestAvailableSpot(Vehicle vehicle);
}
Ticket{
String TicketId;
Vehicle vehicle;
Parking spot;
long startTime;
}
ParkingLot
{
List<ParkingFloor> floors;
List<ParkingSpot> getAllAvailableSpots();
ParkingSpot findNearestSpot(Vehicle vehicle)
Ticket parkVehicle(Vehicle vehicle)
double unparkVehicle(String ticketId)
}
