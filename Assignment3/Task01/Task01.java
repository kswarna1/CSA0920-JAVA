
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;

class InsufficientBalanceException extends Exception {
    InsufficientBalanceException(String message) {
        super(message);
    }
}

public class Task01 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        double balance = 10000;

        try {
            FileInputStream file =
                new FileInputStream("card-reader.txt");
            file.close();

            System.out.print("Enter account number: ");
            String account = sc.nextLine();

            System.out.print("Enter withdrawal amount: ");
            double amount = Double.parseDouble(sc.nextLine());

            if (amount <= 0) {
                throw new IllegalArgumentException(
                    "Amount must be positive");
            }

            if (amount > balance) {
                throw new InsufficientBalanceException(
                    "Insufficient account balance");
            }

            balance = balance - amount;

            System.out.println("Withdrawal successful");
            System.out.println("Account Number: " + account);
            System.out.println("Remaining Balance: " + balance);

        } catch (IOException e) {
            System.out.println("Card reader file is missing");

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid withdrawal amount: "
                               + e.getMessage());

        } catch (InsufficientBalanceException e) {
            System.out.println(e.getMessage());

        } finally {
            System.out.println("ATM transaction completed");
            sc.close();
        }
    }
}
