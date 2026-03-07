import java.util.*;

public class Exit {
    private int id;

    public Exit(int id) {
        this.id = id;
    }

    public void validateTicket(ParkingTicket t) {
        Date now = new Date();
        t.setExitTime(now);
        double hrs = (now.getTime() - t.getEntryTime().getTime()) / 3600000.0;
        double fee = ParkingLot.getInstance().rate.calculate(hrs, t.getVehicle(),
                ParkingLot.getInstance().getSpot(t.getSlotNo()));
        t.setAmount(fee);
        System.out.printf("Ticket %d | Parked: %.2f hrs | Fee: $%.2f\n", t.getTicketNo(), hrs, fee);
        Payment p = (fee > 10) ? new CreditCard(fee) : new Cash(fee);
        p.initiateTransaction();
        ParkingLot.getInstance().freeSlot(t.getSlotNo());
        t.setStatus(TicketStatus.PAID);
    }

    /*
     * The Exit class represents an exit point in the parking lot.
     * 
     * - validateTicket(ParkingTicket t): This method validates a parking ticket by:
     * - Setting the exit time to the current time.
     * - Calculating the parking duration in hours.
     * - Computing the parking fee based on the duration, vehicle type, and spot
     * type.
     * - Setting the ticket amount to the calculated fee.
     * - Printing a formatted message showing the ticket number, parking duration,
     * and fee.
     * - Creating a payment instance (either CreditCard or Cash) based on the fee.
     * - Initiating the payment transaction.
     * - Freeing the parking spot associated with the ticket.
     * - Setting the ticket status to PAID.
     */
}
