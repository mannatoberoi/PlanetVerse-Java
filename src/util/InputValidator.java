
package util;

import java.util.Scanner;

public class InputValidator {

    private static Scanner scanner = new Scanner(System.in);

    // Read integer
    public static int readInt(String message) {

        while (true) {
            try {
                System.out.print(message);
                int value = Integer.parseInt(scanner.nextLine());
                return value;

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Read decimal
    public static double readDouble(String message) {

        while (true) {
            try {
                System.out.print(message);
                double value = Double.parseDouble(scanner.nextLine());

                if (value < 0 || value > 10) {
                    System.out.println("Value must be between 0 and 10.");
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid decimal number.");
            }
        }
    }

    // Read text
    public static String readString(String message) {

        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be empty.");
        }
    }

    // Read email
    public static String readEmail(String message) {

        while (true) {
            String email = readString(message);

            if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                return email;
            }

            System.out.println("Please enter a valid email address.");
        }
    }
}
