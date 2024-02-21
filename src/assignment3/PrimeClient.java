package assignment3;

import java.rmi.Naming;
import java.util.Scanner;
/**
 * The client application for the Prime Calculator.
 * It allows users to specify a range and requests the server to find the largest prime number within that range.
 */
public class PrimeClient {
	  /**
     * Main method to run the Prime Calculator client.
     * It connects to the Prime Calculator server, sends a range for prime number calculation, and displays the result.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            PrimeCalculator calculator = (PrimeCalculator) Naming.lookup("rmi://localhost/PrimeCalculator");
            try (Scanner scanner = new Scanner(System.in)) {
				System.out.println("Enter the start of the range:");
				long start = scanner.nextLong();
				System.out.println("Enter the end of the range:");
				long end = scanner.nextLong();

				long largestPrime = calculator.findLargestPrime(start, end);
				if (largestPrime != -1) {
				    System.out.println("The largest prime number in the range is: " + largestPrime);
				} else {
				    System.out.println("There are no prime numbers in the given range.");
				}
			}
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
