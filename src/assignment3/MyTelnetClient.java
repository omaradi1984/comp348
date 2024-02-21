/**
 * 
 */
package assignment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author ozohouradi
 *
 */
public class MyTelnetClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final String SERVER_ADDRESS = "localhost"; // Change this to the address
													// of your Telnet server
		final int SERVER_PORT = 23; // Telnet port

		try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
				PrintWriter out = new PrintWriter(socket.getOutputStream(),
						true);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(
						new InputStreamReader(System.in))) {
			System.out.println("Connected to the Telnet server.");
			System.out.println("Type 'quit' to exit.");

			String userInput;
			while ((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				if (userInput.equalsIgnoreCase("quit")) {
					break;
				}
				System.out.println("Server: " + in.readLine());
			}
		} catch (UnknownHostException e) {
			System.err.println("Error: Unknown host " + SERVER_ADDRESS);
		} catch (IOException e) {
			System.err.println("Error: Couldn't get I/O for the connection to "
					+ SERVER_ADDRESS);
		}
	}
}
