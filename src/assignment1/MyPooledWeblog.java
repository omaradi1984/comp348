/**
 * title: MyPooledWeblog.java
 * description: a web log processor java application.
 * date: December 11, 2001
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
 *<H1>Weblog Processor</H1>
 *
 *<H3>Purpose and Description</H3>
 *
 *<P>
 * An application that processes a web log and provides stats based on user input in the form of arguments.
 *</P>
 *<P>
 * This program processes web server log files and provides the following stats based on user input:
 * 1. Count number of accesses by each remotehost
 * 2. Count total bytes transmitted
 * 3. Count total bytes by remotehost
 * The user will need to provide the log file name and a number 1-3 representing any of the options above. and example command line:
 * java mypooledweblog.java <<filename>> <<option>>
 * java mypooledweblog.java access_log 1
 *</P>
 *<P>
 * This program uses the Java standard library that are available in almost all java versions 5, 8, 11 ...etc.
 *</P>
 *                                                                              
 *<DL>
 *<DT> Compiling and running instructions</DT>
 *<DT> Assuming SDK 1.3 (or later) and the CLASSPATH are set up properly.</DT>
 *<DT> Change to the directory containing the source code.</DT>
 *<DD> Compile:    javac MyPooledWeblog.java</DD>
 *<DD> Run:        java MyPooledWeblog</DD>
 *<DD> Document:   javadoc MyPooledWeblog.java</DD>
 *</DL>
 */

 /**
 *
 * <H3>Classes</H3>
 *
 *<P>
 * public class MyPooledWeblog {<BR>
 * This is the main public class for this application. It includes several java libraris and private static methods to process.
 * Libraries:
 * import java.io.BufferedReader;
 * import java.io.File;
 * import java.io.IOException;
 * import java.io.InputStreamReader;
 * import java.nio.charset.StandardCharsets;
 * import java.nio.file.Files;
 * import java.nio.file.Path;
 * import java.util.ArrayList;
 * import java.util.HashMap;
 * import java.util.HashSet;
 * import java.util.Iterator;
 * import java.util.Set;
 *</P>
 *<H3>Methods</H3>
 *<P>
   private static HashMap<String, Integer> accessByHostCalculator(ArrayList<ArrayList<String>> result) {<BR>
   This method calculates the total number of accesses by remotehost. It accepts a 2d arraylist of strings representing
   the results of reading the access log.
 *</P>
 *<P>
   private static HashMap<String, Integer> bytesByHostCalculator(ArrayList<ArrayList<String>> result) {<BR>
   This method calculates the total bytes transmitted by remotehost. It accepts a 2d arraylist of strings representing
   the results of reading the access log.
*</P>
*<P>
   private static int bytesCalculator(ArrayList<ArrayList<String>> result) {<BR>
   This method calculates the total bytes transmitted by all remotehosts. It accepts a 2d arraylist of strings representing
   the results of reading the access log.
*</P>
*<P>
   private static ArrayList<ArrayList<String>> readToArray(Path filePath) {<BR>
   This method accepts the file path, reads the web log using buffered reader, breaks the lines into substrings and saves the remotehost, statusCode and bytes information in a 2d arraylist.
*<P>
   private static int findNthOccurrence(String str, char ch, int N) {<BR>
   This method is used to find the Nth occurance of a certain character in a string. and returns the index of the occrance.
   It is used as part of the extraction process of the information from the line.
*</P>
   private static int charCounter(String str, char ch) {<BR>
   This method count the number of occurances of a certain character in a string. It returns an integer of the number of counts.   
 *</P>
 *<P>
   public static void main(String args[]) {<BR>
   This method is used to execute the application
 *</P>
 * <H3>GoodDocs Instance Variables</H3>
 *
 *<P>
 * int option = 0; this will store the option number passed as an argument by the user
 * final Path filePath = new File(args[0]).toPath(); this will store the file name to be processed.
 *</P>
 */

/**
 *
 * <H3>Test Plan</H3>
 *
 *<P>
 * Run the application.
 * EXPECTED:
 * If the user passed 1, the application will count number of accesses by each remotehost
 * If the user passed 2, the application will count total bytes transmitted
 * If the user passed 3, the application will count total bytes by remotehost
 * ACTUAL:
 *    application displays result as expected.
 *</P>
 */ 

/**
 * CODE...
 */

/** Java core packages */
package assignment1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author omara
 *
 */
public class MyPooledWeblog {
	//Main method to execute the application
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		// variables to store the user input passed as arguments.
		int option = 0;
		final Path filePath = new File(args[0]).toPath();
		
		//Try catch block to validate if the option input is a valid Integer format
		try {
			option = Integer.parseInt(args[1]);
		}catch (NumberFormatException e) {
	        System.err.println("Argument" + args[1] + " must be an integer.");
	        System.exit(1);
	    }
		
		//reading log entries and saving data to nx3 matrix
		ArrayList<ArrayList<String>> result = readToArray(filePath);
		
		switch(option){
		case 1:
			//option 1: count number of accesses by each remote host
			HashMap<String, Integer> accessByHost = accessByHostCalculator(result);
			System.out.println("Total number of access by remote host: ");
			accessByHost.forEach((t, u) -> System.out.println("Remote host: " + t + " || " + "Total count of accesses: " + u));
			break;
		case 2:
			//Option 2: count total bytes transmitted.
			int totalBytes = bytesCalculator(result);
			System.out.println("Total bytes transmitted: " + totalBytes);
			break;
		case 3:
			//option 3: count total bytes by remote host
			HashMap<String, Integer> bytesByHost = bytesByHostCalculator(result);
			System.out.println("Total bytes transmitted by remote host: ");
			bytesByHost.forEach((t, u) -> System.out.println("Remote host: " + t + " || " + "Total bytes transmitted: " + u));
			break;
		}
	}
	private static HashMap<String, Integer> accessByHostCalculator(ArrayList<ArrayList<String>> result) {
			// TODO Auto-generated method stub
			HashMap<String, Integer> accessByHost = new HashMap<String, Integer>();
			Set<String> set = new HashSet<String>();
			//Looping through the 2d arraylist and the number of access by remotehost. Storing the remotehost in a Set. This will store unique values.
			for(ArrayList<String> col : result) {
				set.add(col.get(0));
			}
			Iterator<String> iterator = set.iterator();
			//iterate through the Set
			while(iterator.hasNext()) {
				int count = 0;
				String tempAddress = iterator.next().toString();
				//Compare the Set value to the value in the original list
				for(ArrayList<String> col : result) {
					String address = col.get(0);
					//if both values match and the status code is 200, then count.
					if(tempAddress.equals(address) && col.get(1).equals("200")) {
						count += 1;
					}
				}
				// adding the uniqeue remotehost as key and total count as value
				accessByHost.put(tempAddress, count);
			}			
		return accessByHost;
	}
	
	private static HashMap<String, Integer> bytesByHostCalculator(ArrayList<ArrayList<String>> result) {
		// TODO Auto-generated method stub
			HashMap<String, Integer> bytesByHostname = new HashMap<String, Integer>();
			Set<String> set = new HashSet<String>();
			
			for(ArrayList<String> col : result) {
				set.add(col.get(0));
				//bytesByHostname.put(col.get(0), Integer.parseInt(col.get(2)));
			}
			Iterator<String> iterator = set.iterator();
			
			while(iterator.hasNext()) {
				int sum = 0;
				String tempAddress = iterator.next().toString();
				//System.out.println(tempAddress);
				for(ArrayList<String> col : result) {
					String address = col.get(0);
					if(tempAddress.equals(address)) {
						sum += Integer.parseInt(col.get(2));
					}
				}
				bytesByHostname.put(tempAddress, sum);
			}			
		return bytesByHostname;
	}		
	
	private static int bytesCalculator(ArrayList<ArrayList<String>> result) {
		//Calculating Total bytes transmitted.
				int sum = 0;
				for(ArrayList<String> col : result) {
					sum += Integer.parseInt(col.get(2));
				}
		return sum; 
	}
	
	private static ArrayList<ArrayList<String>> readToArray(Path filePath) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		
		if(Files.exists(filePath)) {
			try(BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8))){
				String line;
				
				while((line = br.readLine()) != null) {
					result.add(line);
				}
			}catch (IOException e) {
		        e.printStackTrace();
		    }
			for(String line : result) {
				int beginIndex, endIndex;
				String address, statusCode, bytes, remainingSubstring;
				ArrayList<String> columns = new ArrayList<String>();
				
				if(line.lastIndexOf("-") == line.length()-1) {
					beginIndex = line.indexOf(' ');
					address = line.substring(0, beginIndex);
					remainingSubstring = line.substring(beginIndex);
					
					beginIndex = findNthOccurrence(remainingSubstring, ' ', charCounter(remainingSubstring, ' ')-1);
					endIndex = remainingSubstring.lastIndexOf(" ");
					statusCode = line.substring(beginIndex, endIndex);
					columns.add(address);
					columns.add(statusCode);
					columns.add("0");
					rows.add(columns);
				}else {
					beginIndex = line.indexOf(' ');
					address = line.substring(0, beginIndex);
					remainingSubstring = line.substring(beginIndex);
					
					beginIndex = findNthOccurrence(remainingSubstring, '"', 2)+2;
					endIndex = remainingSubstring.lastIndexOf(' ');
					statusCode = remainingSubstring.substring(beginIndex, endIndex);
					
					beginIndex = remainingSubstring.lastIndexOf(' ')+1;
					bytes = remainingSubstring.substring(beginIndex);
					
					columns.add(address);
					columns.add(statusCode);
					columns.add(bytes);
					rows.add(columns);
				}
			
			}
		}else {
			System.out.println("File doesn't exist in the path provided! Please try again.");
			System.exit(1);
		}
		return rows;
		
	}

	private static int findNthOccurrence(String str, char ch, int N) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                count++;
                if (count == N) {
                    return i;
                }
            }
        }
        return -1;
    }
	
	private static int charCounter(String str, char ch) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }
}