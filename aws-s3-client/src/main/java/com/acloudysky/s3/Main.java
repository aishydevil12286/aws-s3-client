package com.acloudysky.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.acloudysky.auth.AwsClientAuthencation;
import com.acloudysky.s3.Utility;


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

	private static AmazonS3  s3Client = null;
	
	// Selected S3 region. Enumerated value.
	private static Regions currentRegion; 
	
	// Selected region. String value such as "us-west-2".
	private static String region = null;
	
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
		
		
		
		String name = null, topic = null;
		
		// Display application menu.
		Utility.displayWelcomeMessage("AWS S3");
				
		// Read input parameters.
		try {
				region = args[0];
				// System.out.println(region);
		}
		catch (Exception e) {
			System.out.println("IO error trying to read application input! Assigning default values.");
			// Assign default values if none are passed.
			if (args.length==0) {
				region = "us-west-2";
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
			// Instantiate AwsClientAuthencation class.
			AwsClientAuthencation s3ClientAuth = new AwsClientAuthencation();
		
			// Obtain authenticated EC2 client.
			s3Client = s3ClientAuth.getAuthenticatedS3Client();
			
			// Set region.
			currentRegion = Utility.getRegion(region);
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
