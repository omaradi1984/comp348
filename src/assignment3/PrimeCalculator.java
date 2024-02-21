package assignment3;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Defines the remote interface for calculating the largest prime number within a specified range.
 * This interface extends {@link java.rmi.Remote} to allow its methods to be invoked from another JVM.
 */
public interface PrimeCalculator extends Remote {
	 /**
     * Finds the largest prime number within the given range.
     *
     * @param start The start of the range (inclusive).
     * @param end The end of the range (inclusive).
     * @return The largest prime number within the specified range, or -1 if no prime number is found.
     * @throws RemoteException If a remote invocation error occurs.
     */
	long findLargestPrime(long start, long end) throws RemoteException;
}
