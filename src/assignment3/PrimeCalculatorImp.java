package assignment3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
/**
 * Implementation of the {@link PrimeCalculator} remote interface.
 * This class provides the functionality to find the largest prime number within a specified range.
 */
@SuppressWarnings("serial")
public class PrimeCalculatorImp extends UnicastRemoteObject implements PrimeCalculator {
	 /**
     * Constructs a {@code PrimeCalculatorImpl} instance and exports it on an anonymous port.
     *
     * @throws RemoteException If exporting the object fails.
     */
    protected PrimeCalculatorImp() throws RemoteException {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long findLargestPrime(long start, long end) throws RemoteException {
        for (long num = end; num >= start; num--) {
            if (isPrime(num)) {
                return num;
            }
        }
        return -1; // Return -1 if no prime is found
    }
    /**
     * Checks if a number is prime.
     *
     * @param number The number to check.
     * @return {@code true} if the number is prime; {@code false} otherwise.
     */
    private boolean isPrime(long number) {
        if (number <= 1) return false;
        for (long i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}
