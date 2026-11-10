import java.math.BigDecimal;

public class FeeService {

    public String pay(String regNo, String amountText) {

        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Enter registration number");
        }

        BigDecimal amount;

        try {
            amount = new BigDecimal(amountText.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Enter a valid amount");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Amount must be positive");
        }

        return "Payment Successful!\n"
                + "Registration Number: " + regNo.trim()
                + "\nAmount Paid: Rs. " + amount.setScale(2);
    }
}