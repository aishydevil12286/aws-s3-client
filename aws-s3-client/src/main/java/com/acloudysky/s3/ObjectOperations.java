package com.acloudysky.s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import com.amazonaws.services.s3.model.S3ObjectSummary;

/***
 * Performs S3 object operations. 
 * Each method calls the related AWS s3 API. 
 * <p>
 * For more information, see 
 * <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingObjects.html" target="_blank">Working with Amazon S3 Objects</a>.
 * </p>
 * @author Michael Miele
 *
 */
public class ObjectOperations {
	
	// Authorized client
	private static AmazonS3 s3Client;
	
	
	/***
	 * Initializes global status variables.
	 * @param authorizedClient Client authorized to access the S3 service.
	 */
	public static void InitObjectOperations(AmazonS3 authorizedClient) {
		s3Client = authorizedClient;
	}
	
	
	/****
	 * Utilities
	 */
	
	 /**
	  * Creates a temporary test file with text data to demonstrate uploading a file
	  * to Amazon S3
	  * @return A newly created temporary file with text data.
	  * @throws IOException
	  */
    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("01234567890112345678901234\n");
        writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
        writer.write("01234567890112345678901234\n");
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.close();

        return file;
    }
    
    /**
     * Displays the contents of the specified input stream as text.
     * @param input The input stream to display as text.
     * @throws IOException
     */
    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }
	    
    /***
     * Uploads an object to a bucket 
     * <b>Notes</b> 
     * <ul>
     *   <li>You can easily upload a file to S3, or upload directly an 
     * 		InputStream if you know the length of the data in the stream </li>
     *   <li>You can also specify your own metadata when uploading to S3, which allows you 
     * 		to set a variety of options like content-type and content-encoding, plus additional 
     * 		metadata specific to your applications</li>
     * </ul>
     * @param bucketName The name of the bucket to hold the object
     * @param keyName The key name for the object to upload
     * @throws IOException Error encountered while uploading the object
     */
	public static void UploadObject(String bucketName, String keyName) throws IOException {			
		
		try {
	            // Upload an object to your bucket. 
	            System.out.println("Uploading a new object to S3 from a file\n");
	            s3Client.putObject(new PutObjectRequest(bucketName, keyName, createSampleFile()));
          
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
			catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which means the client encountered "
	                    + "a serious internal problem while trying to communicate with S3, "
	                    + "such as not being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
        }
    }
	
    
	/***
	 * Downloads an object. 
	 * <b>Notes</b>
	 * <ul>
	 * 	<li>When you download an object, you get all of the object's metadata and a stream 
	 * 	from which to read the contents</li>
	 * 	<li>It's important to read the contents of the stream as quickly as possible 
	 * 	since the data is streamed directly from Amazon S3 and your network connection 
	 * 	remains open until you read all the data or close the input stream</li>
	 * 	<li>GetObjectRequest also supports several other options, including conditional 
	 * 	downloading of objects based on modification times, ETags, and selectively 
	 * 	downloading a range of an object</li>
	 * </ul>
	 * @param bucketName The name of the bucket that contains the object
	 * @param keyName The name of the object to download
	 * @throws IOException Error encountered while downloading the object
	 */
	public static void DownloadObject(String bucketName, String keyName) throws IOException {
	
		try {
		
            /*
             * Download an object - When you download an object, you get all of
             * the object's metadata and a stream from which to read the contents.
             * It's important to read the contents of the stream as quickly as
             * possibly since the data is streamed directly from Amazon S3 and your
             * network connection will remain open until you read all the data or
             * close the input stream.
             *
             * GetObjectRequest also supports several other options, including
             * conditional downloading of objects based on modification times,
             * ETags, and selectively downloading a range of an object.
             */
            System.out.println("Downloading an object");
            S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));
            System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
            displayTextInputStream(object.getObjectContent());
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
		catch (AmazonClientException ace) {
     		System.out.println("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
     		System.out.println("Error Message: " + ace.getMessage());
     	}
    }
	
	/***
	 * Lists objects contained in the specified bucket.
	 * @param bucketName The name of the bucket that contains the objects
	 * @throws IOException Error encountered while listing the objects
	 */
	public static void ListObject(String bucketName) throws IOException {			
		
		try {
			 	System.out.println("Listing objects");
			   
	            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	            	.withBucketName(bucketName);
	            	// .withPrefix("m");
	            ObjectListing objectListing;            
	            do {
	                objectListing = s3Client.listObjects(listObjectsRequest);
	                for (S3ObjectSummary objectSummary : 
	                	objectListing.getObjectSummaries()) {
	                    System.out.println(" - " + objectSummary.getKey() + "  " +
	                            "(size = " + objectSummary.getSize() + 
	                            ")");
	                }
	                listObjectsRequest.setMarker(objectListing.getNextMarker());
	            } while (objectListing.isTruncated());
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
		catch (AmazonClientException ace) {
     		System.out.println("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
     		System.out.println("Error Message: " + ace.getMessage());
     	}	
 	}
	
	/***
	 * Deletes object in a non-versioned bucket
	 * @param bucketName he name of the bucket that contains the object
	 * @param keyName The name of the object to delete
	 * @throws IOException Error encountered while deleting the object
	 */
	public static void DeleteObject(String bucketName, String keyName) throws IOException {
		
		try {
            System.out.println("Delete object");
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
      
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
		catch (AmazonClientException ace) {
     		System.out.println("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
     		System.out.println("Error Message: " + ace.getMessage());
     	}
    }
	
}
