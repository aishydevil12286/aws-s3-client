package com.acloudysky.s3;

import com.acloudysky.s3.ClientAuthentication;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;


/***
 *Instantiate the S3 authorized service client, initialize the operations and the UI classes.  
 *Before running the code, you need to set up your AWS security credentials. You can do this by creating a 
 *file named "credentials" at ~/.aws/ (C:\Users\USER_NAME.aws\ for Windows users) and saving the following lines in 
 *the file:
 *<pre>
 *[default]
 *aws_access_key_id = your access key id
 *aws_secret_access_key = your secret key
 *</pre>
 *For more information, see <a href="http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html" target="_blank">Providing AWS Credentials in the AWS SDK for Java</a> 
 *and <a href="https://console.aws.amazon.com/iam/home?#security_credential" target="_blank">Welcome to Identity and Access Management</a>.
 *<b>WARNING</b>: To avoid accidental leakage of your credentials, DO NOT keep the credentials file in your source directory.
 * @author Michael Miele
 *
 */
public class Main {

	/**
	 * Instantiate the S3 client, initialize the operation classes. 
	 * Instantiate the SimpleUI class to display the selection menu and process the user's input. 
	 * @see SimpleUI#SimpleUI()
	 * @see BucketOperations#InitBucketOperations(AmazonS3)
	 * @see ObjectOperations#InitObjectOperations(AmazonS3)
	 * @param args; 
	 *  args[0] = Your name
	 *  args[1] = Greeting message
	 * 
	 */
	public static void main(String[] args) {
		
		AmazonS3 s3Client = null;
		String name = null, topic = null;
		
		// Read input parameters.
		try {
				name = args[0];
				topic = args[1];
		}
		catch (Exception e) {
			System.out.println("IO error trying to read application input! Assigning default values.");
			// Assign default values if none are passed.
			if (args.length==0) {
				name = "User";
				topic = "AWS S3 client console application";
			}
			else {
				System.out.println("IO error trying to read application input!");
				System.exit(1); 
			}
		}
		
		// Print greeting message.
		String startGreetings = String.format("Hello %s let's start %s", name, topic);
		System.out.println(startGreetings);
		
		try {
        	// Obtain authenticated S3 client.
			s3Client = ClientAuthentication.getAuthorizedS3Client();
			// Set region.
			Region usEast1 = Region.getRegion(Regions.US_EAST_1);
	        ClientAuthentication.setS3Region(usEast1);
		} 
		
		catch (AmazonServiceException ase) {
        	StringBuffer err = new StringBuffer();
        	err.append(("Caught an AmazonServiceException, which means your request made it "
                      + "to Amazon S3, but was rejected with an error response for some reason."));
       	   	err.append(String.format("%n Error Message:  %s %n", ase.getMessage()));
       	   	err.append(String.format(" HTTP Status Code: %s %n", ase.getStatusCode()));
       	   	err.append(String.format(" AWS Error Code: %s %n", ase.getErrorCode()));
       	   	err.append(String.format(" Error Type: %s %n", ase.getErrorType()));
       	   	err.append(String.format(" Request ID: %s %n", ase.getRequestId()));
        	
    	} 
		
		if (s3Client != null) {
			
			// Initialize the BucketOperations class to handle related REST API calls.
			BucketOperations.InitBucketOperations(s3Client);
			
			// Initialize the ObjectOperations class to handle related REST API calls.
			ObjectOperations.InitObjectOperations(s3Client);
			
			// Instantiate the SimpleUI class and display menu.
			SimpleUI sui = new SimpleUI();
	
			// Start processing user's input.
			sui.processUserInput();
		}
		else 
			String.format("Error %s", "Main: authorized S3 client object is null.");
	}
	
}
