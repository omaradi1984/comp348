/**
 * 
 */
package assignment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ozohouradi
 *
 */
public class MyTelnetServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final int PORT = 23; // Telnet port
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Telnet Server is running on port " + PORT);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println(
						"Client connected: " + clientSocket.getInetAddress());

				Thread clientThread = new Thread(
						new ClientHandler(clientSocket));
				clientThread.start();
			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class ClientHandler implements Runnable {
		private Socket clientSocket;

		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			try (PrintWriter out = new PrintWriter(
					clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(
									clientSocket.getInputStream()))) {
				out.println(
						"Welcome to the Telnet Server! Type 'quit' to exit.");

				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					System.out.println("Client: " + inputLine);
					if (inputLine.trim().equalsIgnoreCase("quit")) {
						break;
					}
					// Echo back to client
					out.println("Server: " + inputLine);
				}
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
