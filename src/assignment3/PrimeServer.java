package assignment3;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
/**
 * The server application for the Prime Calculator.
 * It registers an instance of {@link PrimeCalculatorImpl} in the RMI registry for clients to invoke remotely.
 */
public class PrimeServer {
	/**
     * Main method to start the Prime Calculator server.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        
    	try {
            PrimeCalculator calculator = new PrimeCalculatorImp();
            LocateRegistry.createRegistry(1099); // RMI registry on port 1099
            Naming.rebind("rmi://localhost/PrimeCalculator", calculator);
            System.out.println("Prime Calculator Server is ready.");
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
