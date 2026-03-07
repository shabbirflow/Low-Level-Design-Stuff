public class Entrance {
    private int id;

    public Entrance(int id) {
        this.id = id;
    }

    public ParkingTicket getTicket(Vehicle v) {
        return ParkingLot.getInstance().parkVehicle(v);
    }

    /*
     * The Entrance class represents an entrance point in the parking lot.
     * 
     * - getTicket(Vehicle v): This method requests a parking ticket for a vehicle
     * by calling the 'parkVehicle' method on the singleton instance of the
     * ParkingLot class.
     */
}
