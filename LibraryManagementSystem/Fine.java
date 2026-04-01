public class Fine {
    public static double collectFine(String memberId, int days) {
        double amount = days * 1.0; // $1 per late day
        System.out.printf("Collected fine of $%.2f from member %s\n", amount, memberId);
        return amount;
    }
}
