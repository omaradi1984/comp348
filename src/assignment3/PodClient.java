/**
 * 
 */
package assignment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/**
 * 
 * I declare that this assignment is my own work and that all material previously written or published in any source by any other person has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any academic program. In submitting this assignment I give permission to copy it for assessment purposes only.
 * The {@code PodClient} class is designed to connect to the Poem of the Day (PoD) server,
 * display a list of available poems to the user, and allow the user to select a poem to be displayed.
 * It handles communication with the server using TCP/IP sockets and processes server responses
 * to provide a simple interactive experience.
 * <p>
 * This client assumes the server is running on localhost and listening on port 6789. Adjustments
 * are needed if the server configuration differs.
 * </p>
 */
public class PodClient {

    private static final String SERVER_ADDRESS = "localhost"; // Server address
    private static final int SERVER_PORT = 6789; // Server port
    /**
     * The main method establishes a connection to the PoD server, receives and displays
     * the list of available poems, waits for the user's selection, and displays the selected
     * poem or an error message based on the user's input.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Display the welcome message from the server
            System.out.println("Connected to Poem of the Day Server.");
            String responseLine;
            
            while ((responseLine = in.readLine()) != null && !responseLine.isEmpty()) {
                System.out.println(responseLine);
                if (responseLine.startsWith("Please select a poem by number:")) {
                    break; // Stop reading from server and wait for user input
                };
            }
            
            // Send user's poem selection to the server
            System.out.print("Your choice: ");
            String poemNumber = scanner.nextLine();
            out.println(poemNumber);
            out.flush();
            // Read and display the poem or error message from the server
            while ((responseLine = in.readLine()) != null) {
            	System.out.println(responseLine);
            }
            System.out.println("\nEND OF LINE");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

