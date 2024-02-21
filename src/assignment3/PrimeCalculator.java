package assignment3;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * 
 * I declare that this assignment is my own work and that all material previously written or published in any source by any other person has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any academic program. In submitting this assignment I give permission to copy it for assessment purposes only.
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
