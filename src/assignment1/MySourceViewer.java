/**
 * title: MySourceViewer.java
 * description: A server source viewer java application.
 * date: December 11, 2023
 * @author Omar Zohouradi
 * @version 1.0
 * 
 * I declare that this assignment is my own work and that all material previously written or published in any source by any other person has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any academic program. In submitting this assignment I give permission to copy it for assessment purposes only.
 */

/**
 * DOCUMENTATION...
 */

/**                                                                               
 *
 *<H1>Source Viewer</H1>
 *
 *<H3>Purpose and Description</H3>
 *
 *<P>
 * An application that returns the lines that searches a web resource for a string provided by the user and returns the lines that contains it.
 *</P>
 *<P>
 * This program accepts a valid url/IP and a string in double quotes as arguments. It then searches the source and displays the lines where the string appears. 
 * Example: java mysourceviewer.java <url> "string"
 *</P>
 *<P>
 * This program uses the Java standard library that are available in almost all java versions 5, 8, 11 ...etc.
 *</P>
 *                                                                              
 *<DL>
 *<DT> Compiling and running instructions</DT>
 *<DT> Assuming SDK 1.3 (or later) and the CLASSPATH are set up properly.</DT>
 *<DT> Change to the directory containing the source code.</DT>
 *<DD> Compile:    javac MySourceViewer.java</DD>
 *<DD> Run:        java MySourceViewer</DD>
 *<DD> Document:   javadoc MySourceViewer.java</DD>
 *</DL>
 */
 /**
 *
 * <H3>Classes</H3>
 *
 *<P>
 * public class MySourceViewer {<BR>
 * This is the main public class for this application. It includes several java libraris and private static methods to process.
 * 
 * <H4>Libraries</H4>
 * 
 * import java.io.BufferedReader;
 * import java.io.IOException;
 * import java.io.InputStreamReader;
 * import java.net.URL;
 * import java.net.URLConnection;
 * import java.util.regex.Matcher;
 * import java.util.regex.Pattern;
 *</P>
 *
 *<H3>Methods</H3>
 *
 *<P>
 * public static void main(String args[]) {<BR>
 * This method is used to execute the application
 *</P>
 *
 **<P>
 * private static String cleanHtmlTags(String htmlString) {<BR>
 * This method is used to remove the HTML tags from a string and only return the text.
 *</P>
 *
 * <H3>GoodDocs Instance Variables</H3>
 * 
 *<P>
 * String serverAddress = args[0]; a variable to capture the target address (url)
 * String searchString = args[1]; a variable to capture the desired string
 *</P>
 *
 * <H3>Test Plan</H3>
 *
 *<P>
 * Run the application using the following syntax.
 * java MySourceViewer <url/IP> "String"
 * EXPECTED:
 * a user provides the url and the string to search. The application returns the lines that contain the string. 
 * ACTUAL:
 * application displays result as expected.
 *</P>
 */ 
/**
 * CODE...
 */
/** Java core packages */
package assignment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ozohouradi
 *
 */
public class MySourceViewer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 2) {
            System.out.println("Please type: java SearchStringInServer <URL or IP> <search string>");
            return;
        }
		//variables to store the arguments passed by the user.
		String serverAddress = args[0];
        String searchString = args[1];
        try {
        	//Creating a URL object and passing the url/IP
            URL url = new URL(serverAddress);
            //Openning a connection
            URLConnection connection = url.openConnection();
            //Creating a BufferedReader object and passing the connection inputstream warapped with an InputStreamReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                       
            String inputLine;
            //Creating a pattern object and compiling the search string.
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(searchString) + "\\b");
            
            //while the reader has a line to read
            while ((inputLine = reader.readLine()) != null) {
            	//a Matcher object to check if search string exists in line
            	Matcher matcher = pattern.matcher(inputLine);
            	//If matcher returns True, then the line is returned
                if (matcher.find()) {
                	//passing the line to the cleanHtmlTags method to remove the HTML tags and only return the text
                	String cleanedString = cleanHtmlTags(inputLine);
                    System.out.println("Line: " + cleanedString);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
		}
	private static String cleanHtmlTags(String htmlString) {
        if (htmlString == null) {
            return null;
        }

        // Regular expression to match HTML tags
        String regex = "<[^>]*>";

        // Replace HTML tags with an empty string
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlString);

        return matcher.replaceAll("");
    }
}
