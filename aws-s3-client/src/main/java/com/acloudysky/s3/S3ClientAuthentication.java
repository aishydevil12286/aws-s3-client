package com.acloudysky.s3;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Creates an authenticated client which is allowed to use the S3 API. 
 * <p>
 * <b>Note</b>. You need to set up your AWS security credentials to be able to use the API. 
 * You do this by creating a file named "credentials" at ~/.aws/ (C:\Users\USER_NAME\.aws\ for Windows users) 
 * and saving the following lines in the file:
 * </p>
 *<pre>
 * [default]
 * aws_access_key_id = your access key
 * aws_secret_access_key = your secret key
 * </pre>
 * 
 * @author Michael Miele
 *
 */
public class S3ClientAuthentication implements IS3Client {

	// Local class types variables. 
	private String OS, userHome;
	public static AmazonS3 s3Client = null;
	public static String credentialsFilePath;
	
	public S3ClientAuthentication() {
		ArrayList<String> env = Utility.getEnvironment();
		Iterator<String> i = env.iterator();
		OS = i.next();
		userHome = i.next();
	
		// Determine credential directory file path.
    	if (OS.startsWith("Windows"))
		  credentialsFilePath = userHome.concat("\\" + ".aws\\credentials");
        else 
        	if (OS.startsWith("Mac"))
        		credentialsFilePath = "~/.aws/credentials";
    	// Test
    	// System.out.println(credentialsFilePath);
	}
		
	/**
	 * Creates authenticated client to access the S3 service through its API. 
	 * @return Authenticated client.
	 * @throws AmazonClientException Base exception class for any errors that occur while attempting to use an AWS client 
	 * to make service calls to Amazon Web Services
	 */
	public AmazonS3 getAuthenticatedS3Client() throws AmazonClientException {
		AWSCredentialsProvider credentialsProvider;
    	AWSCredentials credentials;
    	 
    	File credentialsFile = null;
    	s3Client = null;
        
    	  try {
    		  	credentialsProvider = new ProfileCredentialsProvider();
    		  	credentials = credentialsProvider.getCredentials();
     
          } catch (Exception e) {
          		credentialsFile = new File(System.getProperty("user.home"), ".aws/credentials");
          		throw new AmazonClientException(
                      "Cannot load the credentials from the credential profiles file. " +
                      "Please make sure that your credentials file is at the correct " +
                      "location " + credentialsFile.getAbsolutePath() + " and is in valid format.", e);
          }
     
        s3Client = new AmazonS3Client(credentials);
        return s3Client;
	}

	/**
	 * Sets the S3 service region.
	 * @param selectedRegion The region to use. 
	 */
	public void setS3Region(Regions selectedRegion) {
		Region currentRegion = Region.getRegion(selectedRegion);
        s3Client.setRegion(currentRegion);
	}

}
