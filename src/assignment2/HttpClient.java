/**
 * title: HttpClient.java
 * description: an HTTP client application.
 * date: December 25, 2023
 * @author Omar Zohouradi
 * @version 1.0
 *
 *I declare that this assignment is my own work and that all material previously written or published in any source by any other person has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any academic program. In submitting this assignment I give permission to copy it for assessment purposes only.
 */
/**
* DOCUMENTATION...
*/
/**                                                                               
 *
 *<H1>Http Client</H1>
 *
 *<H3>Purpose and Description</H3>
 *
 *<P>
 * An application that connects to an http server to request a resource.
 *</P>
 *<P>
 * This program uses the http protocol to interact with an http server and request resources using GET method.
 * For html pages, the application will parse on the console the html code of the resource. For multimedia resources, the application will download the resource
 * and save it in the directory path provided as an argument when running the application.
 * To run the application:
 * java httpclient.java http://<<IP>>:<<port>>/<<resource>> (optional) <<directory path>>
 * Example: java httpclient.java http://127.0.0.1:8000/testfile.html ./ (this will save the resource in the same folder as the application)
 *</P>
 *<DL>
 *<DT> Compiling and running instructions</DT>
 *<DT> Assuming SDK 1.8 (or later) and the CLASSPATH are set up properly.</DT>
 *<DT> Change to the directory containing the source code.</DT>
 *<DD> Compile:    javac httpclient.java</DD>
 *<DD> Run:        java httpclient http://<<IP>>:<<port>>/<<resource>> (optional) <<directory path>></DD>
 *<DD> Document:   javadoc httpclient.java</DD>
 *</DL>
 */

/**
 * CODE...
 */

/** Java core packages */
package assignment2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class HttpClient {
	protected static int PORT;
	protected static String resource = null;
	protected static String filePath = null;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PORT = Integer.parseInt(args[0]);
		resource = args[1];
		if(args.length == 3) {
			filePath = args[2];
		}
		
		if (args.length > 0) {
			try {
				URL url = new URL(resource);
				// Openning a connection
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();

				// Set the HTTP request method (GET in this case)
				connection.setRequestMethod("GET");

				// Get the response code
				int responseCode = connection.getResponseCode();
				System.out.println("Response Code: " + responseCode);
				if (responseCode == HttpURLConnection.HTTP_OK) {
					String contentType = connection.getContentType();
					System.out.println("File type: " + contentType);
					if (contentType.startsWith("text/html")) {
						// Read and print the response content
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(
										connection.getInputStream()));
						String line;
						StringBuilder response = new StringBuilder();
						while ((line = reader.readLine()) != null) {
							response.append(line);
						}
						reader.close();
						System.out.println(
								"Response Content:\n" + response.toString());
					} else {
						if (filePath.length() != 0) {
							int contentLength = connection.getContentLength();
							try (InputStream raw = connection
									.getInputStream()) {
								InputStream in = new BufferedInputStream(raw);
								byte[] data = new byte[contentLength];
								int offset = 0;
								int oldProgress = 0;
								int currentProgress = 0;
								while (offset < contentLength) {
									int bytesRead = in.read(data, offset,
											data.length - offset);
									if (bytesRead == -1)
										break;
									offset += bytesRead;
									oldProgress = (int) ((((double)offset) / ((double)contentLength)) * 100d);								
									if(currentProgress < oldProgress) {
										currentProgress = oldProgress;
										System.out.printf("Successfully downloaded: %d%%\n", currentProgress);
									}
								}
								String filename = url.getFile();
								filename = filePath + UUID.randomUUID()
										+ filename.substring(
												filename.lastIndexOf('/') + 1);
								try (FileOutputStream fout = new FileOutputStream(
										filename)) {
									fout.write(data);
									System.out.println(
											"File downloaded successfully: "
													+ filename);
									fout.flush();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							System.out.println(
									"Please run the application again and provide a destination path.");
							return;
						}
						// Close the connection
						connection.disconnect();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
