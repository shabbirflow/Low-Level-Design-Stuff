public class ParkingRate {
    public double calculate(double hours, Vehicle v, ParkingSpot s) {
        int hrs = (int) Math.ceil(hours);
        double fee = 0;
        if (hrs >= 1)
            fee += 4;
        if (hrs >= 2)
            fee += 3.5;
        if (hrs >= 3)
            fee += 3.5;
        if (hrs > 3)
            fee += (hrs - 3) * 2.5;
        return fee;
    }
}
/**
 * The calculate method uses a tiered pricing model:
 * - First hour: $4.00
 * - Second and third hours: $3.50 each
 * - Subsequent hours: $2.50 each
 * - All durations are rounded up to the nearest whole hour.
 * Note: The Vehicle and ParkingSpot parameters are currently placeholders for
 * future rate logic.
 */
