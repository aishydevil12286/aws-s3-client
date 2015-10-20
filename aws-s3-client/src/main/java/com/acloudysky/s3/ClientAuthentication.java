package com.acloudysky.s3;
import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;


/***
 * Create Amazon S3 authorized client.
 * For more informations, see <a href="http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/getting-started-signup.html" 
 * target="_blank">Sign Up for Amazon Web Services and Get AWS Credentials</a>.
 * For lots more information on using the AWS SDK for Java, including information on
 * high-level APIs and advanced features, check out the <a href="http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/welcome.html" 
 * target="_blank">AWS SDK for Java</a> Developer's Guide.
 * Stay up to date with new features in the AWS SDK for Java by following the 
 * <a href="https://java.awsblog.com" target="_blank">AWS Java Developer Blog</a>.  
 * @author Michael Miele
 */
public class ClientAuthentication {

	static AmazonS3 s3Client;
	
    /***
     * Create authorized client to access the service.
     * Before you run this code, you need to set up your AWS security credentials to connect to AWS. 
     * You can do this by creating a file named "credentials" at ~/.aws/ (C:\Users\USER_NAME.aws\ for Windows users) 
     * and saving the following lines in the file:
     * <pre>
     * [default]
     * aws_access_key_id = your access key id
     * aws_secret_access_key = your secret key
     * </pre>
     * @return s3Client Authorized S3 client.
     * @throws AmazonClientException Issued if credential error is encountered.
     */
    public static AmazonS3 getAuthorizedS3Client()  throws AmazonClientException {
      
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

    public static void setS3Region(Region regionValue){
    	Region currentRegion = regionValue;
        s3Client.setRegion(currentRegion);
    }
}
