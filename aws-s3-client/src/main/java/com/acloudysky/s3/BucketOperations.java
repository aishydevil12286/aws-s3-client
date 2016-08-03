package com.acloudysky.s3;

import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;

import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.amazonaws.services.s3.model.ObjectListing;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;


import java.util.Iterator;
import java.util.List;


/***
 * Performs S3 bucket operations. 
 * Each method calls the related AWS S3 API. 
 * <p>
 * For more information, see 
 * <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html" target="_blank">Working with Amazon S3 Buckets</a>.
 * </p>
 * @author Michael Miele
 *
 */
public class BucketOperations {

	// Authorized client
	private static AmazonS3 s3Client;
	
	/***
	 * Initializes global status variables.
	 * @param authorizedClient Client authorized to access the S3 service.
	 */
	public static void InitBucketOperations(AmazonS3 authorizedClient) {
		s3Client = authorizedClient;
	}
	
    /**
     * Creates a bucket 
     * <b>Notes</b>
     *  <ul>
     *		<li>Amazon S3 bucket names are globally unique. 
     *  		Once a bucket name has been taken by any user, you can't create
     *  		another bucket with that name.</li>
     *		<li>You can optionally specify a location for your bucket if you want to
     *   		keep your data closer to your applications or users.</li>
     * 	</ul>
     * @param bucketName The name of the bucket to create
     * @throws IOException Error encountered while creating the bucket
     */
	public static void CreateBucket(String bucketName) throws IOException {			
		
		try {
            	System.out.println("Creating bucket " + bucketName + "\n");
            	// Create the bucket.
            	s3Client.createBucket(bucketName);
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
	
	
	/**
	 * Lists the buckets in the client's account.
	 * @throws IOException Error encountered while listing buckets
	 */
	public static void ListBuckets() throws IOException {
		
		try {
				List<Bucket> bucketList = s3Client.listBuckets();
				if (!bucketList.isEmpty()) {
					System.out.println("Listing buckets");
				
				// Iterate to get the buckets in the list
				for (Iterator<Bucket> iterator = 
						s3Client.listBuckets().iterator(); iterator.hasNext(); )
				{
					Bucket bucket = iterator.next();
					System.out.println(" - " + bucket.getName());
				}
			}
			else	
				System.out.println(String.format("%s", "The account does not contain any bucket."));

        } catch (AmazonServiceException ase) {
        	StringBuffer buffer = new StringBuffer();
        	
        	buffer.append(("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason."));
        	buffer.append(String.format("%n Error Message:  %s %n", ase.getMessage()));
        	buffer.append(String.format(" HTTP Status Code: %s %n", ase.getStatusCode()));
        	buffer.append(String.format(" AWS Error Code: %s %n", ase.getErrorCode()));
        	buffer.append(String.format(" Error Type: %s %n", ase.getErrorType()));
        	buffer.append(String.format(" Request ID: %s %n", ase.getRequestId()));
        	
        	System.out.print(buffer.toString());
        	
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

	
	/**
	 * Deletes a bucket
	 * The name of the bucket to delete
	 * @param bucketName The name of the bucket to delete
	 * @throws IOException Error encountered while deleting the bucket
	 */
	public static void DeleteBucket(String bucketName) throws IOException {	
		try {
			
			 	System.out.println(String.format("Deleting bucket %s %n", bucketName));
	            ObjectListing objectListing = s3Client.listObjects(bucketName);
	     
	            while (true) {
	                for ( Iterator<?> iterator = objectListing.getObjectSummaries().iterator(); iterator.hasNext(); ) {
	                    S3ObjectSummary objectSummary = (S3ObjectSummary) iterator.next();
	                    s3Client.deleteObject(bucketName, objectSummary.getKey());
	                }
	     
	                if (objectListing.isTruncated()) {
	                    objectListing = s3Client.listNextBatchOfObjects(objectListing);
	                } else {
	                    break;
	                }
	            };
	            VersionListing list = s3Client.listVersions(new ListVersionsRequest().withBucketName(bucketName));
	            for ( Iterator<?> iterator = list.getVersionSummaries().iterator(); iterator.hasNext(); ) {
	                S3VersionSummary s = (S3VersionSummary)iterator.next();
	                s3Client.deleteVersion(bucketName, s.getKey(), s.getVersionId());
	            }
	            /*
	             * Delete the bucket - A bucket must be completely empty before it can be
	             * deleted, so remember to delete any objects from your buckets before
	             * you try to delete them.
	             */
	            s3Client.deleteBucket(bucketName);
            
        } catch (MultiObjectDeleteException  ase) {
        	StringBuffer buffer = new StringBuffer();
        	
        	buffer.append(("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason."));
        	buffer.append(String.format("%n Error Message:  %s %n", ase.getMessage()));
        	buffer.append(String.format(" HTTP Status Code: %s %n", ase.getStatusCode()));
        	buffer.append(String.format(" AWS Error Code: %s %n", ase.getErrorCode()));
        	buffer.append(String.format(" Error Type: %s %n", ase.getErrorType()));
        	buffer.append(String.format(" Request ID: %s %n", ase.getRequestId()));
        	
        	System.out.print(buffer.toString());
        	
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
		
	
}
