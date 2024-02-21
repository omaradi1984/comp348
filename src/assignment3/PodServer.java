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
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * The {@code PodServer} class implements a server for the Poem of the Day (PoD) protocol.
 * It listens for connections on a specified port and interacts with clients to provide
 * a selection of poems based on client input. The poems are loaded from a text file specified
 * at server startup. The server responds with a welcome message, lists available poems,
 * and waits for the client to select a poem. It then sends the selected poem to the client
 * or an error message if the input is invalid, before terminating the session.
 * 
 * <p>Usage to start the server: {@code java PodServer <poem file>}, where {@code <poem file>}
 * is the path to a text file containing poems separated by "---".</p>
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * I declare that this assignment is my own work and that all material previously written or published in any source by any other person has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any academic program. In submitting this assignment I give permission to copy it for assessment purposes only.
 * The {@code PodServer} class implements a server for the Poem of the Day (PoD) protocol.
 * It listens for connections on a specified port and interacts with clients to provide
 * a selection of poems based on client input. The poems are loaded from a text file specified
 * at server startup. The server responds with a welcome message, lists available poems,
 * and waits for the client to select a poem. It then sends the selected poem to the client
 * or an error message if the input is invalid, before terminating the session.
 * 
 * <p>Usage to start the server: {@code java PodServer <poem file>}, where {@code <poem file>}
 * is the path to a text file containing poems separated by "---".</p>
 */
public class PodServer {

    private static final int PORT = 6789;
    private static List<String> poems = new ArrayList<>();
    /**
     * Main method to run the PoD server. Accepts a file path as an argument to load poems
     * from and starts the server to listen for connections on a predefined port.
     * 
     * @param args The command line arguments, expecting a single argument for the poem file path.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java PodServer <poem file>");
            System.exit(1);
        }

        loadPoems(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Poem of the Day Server is running on port " + PORT);

            while (true) {
                Socket connectionSocket = serverSocket.accept();
                new Thread(new ClientHandler(connectionSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Loads poems from a specified file into memory. Each poem is expected to be separated
     * by a line containing "---". This method populates the {@code poems} list with the content
     * of each poem for later retrieval.
     * 
     * @param filePath The path to the file containing the poems.
     */
    private static void loadPoems(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder poem = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    poems.add(poem.toString());
                    poem = new StringBuilder();
                } else {
                    poem.append(line).append("\n");
                }
            }
            if (!poem.toString().isEmpty()) {
                poems.add(poem.toString());
            }
        } catch (IOException e) {
            System.out.println("Failed to load poems: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static class ClientHandler implements Runnable {
        private Socket connectionSocket;

        public ClientHandler(Socket socket) {
            this.connectionSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                 DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream())) {

                outToClient.writeBytes("Welcome to the Poem of the Day Server!\n");
                listPoems(outToClient);
                
                outToClient.flush();

                String selection = inFromClient.readLine();
                int poemIndex = Integer.parseInt(selection.trim()) - 1;
                if (poemIndex >= 0 && poemIndex < poems.size()) {
                	outToClient.writeBytes("\n"+poems.get(poemIndex)+"\n");
                } else {
                    outToClient.writeBytes("Error: Selection is outside the expected range.\n");
                }
                outToClient.flush();
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    connectionSocket.close();
                } catch (IOException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }
        /**
         * Sends a list of available poems to the client by extracting the first line (assumed to be the title)
         * of each poem. It also prompts the client to select a poem by number. This method is called after
         * a client connects and receives the welcome message.
         * 
         * @param outToClient The {@code DataOutputStream} to the client, used to send the list of poems and instructions.
         * @throws IOException If an I/O error occurs while writing to the stream.
         */
        private void listPoems(DataOutputStream outToClient) throws IOException {
            outToClient.writeBytes("Available Poems:\n");
            for (int i = 0; i < poems.size(); i++) {
                String title = poems.get(i).split("\n", 2)[0];
                outToClient.writeBytes((i + 1) + ". " + title + "\n");
            }
            outToClient.writeBytes("Please select a poem by number:\n");
            outToClient.flush();
        }
    }
}