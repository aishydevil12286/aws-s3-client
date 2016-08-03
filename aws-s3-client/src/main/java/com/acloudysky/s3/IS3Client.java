package com.acloudysky.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;

/**
 * Defines fields and methods to implement the S3ClientAuthentication class. 
 * @see S3ClientAuthentication
 * @author Michael Miele
 */
public interface IS3Client {
	
	/**
	 * Create authenticated client to use the S3 service API. 
	 * @return Authenticated client.
	 */
	AmazonS3 getAuthenticatedS3Client() throws AmazonClientException;
		 
	/**
	 * Set the S3 service region.
	 * @param regionValue The region to use. 
	 */
	void setS3Region(Regions regionValue);
}