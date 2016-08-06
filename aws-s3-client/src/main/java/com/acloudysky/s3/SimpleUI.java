package com.acloudysky.s3;


import java.io.BufferedReader;
import java.io.IOException;

import com.acloudysky.s3.Utility;


/*** 
 * Display a selection menu for the user. Process the
 * user's input and call the proper method based on the user's selection.
 * Each method calls the related 
 * <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/Welcome.html" target="_blank">AWS S3 API</a>.
 * @author Michael Miele.
 *
 */
public class SimpleUI extends UserInterface {
	
	private StringBuilder menu;
	private static final String newline = System.getProperty("line.separator");
	
	private String bucketName, keyName;
	
	/**
	 * Instantiates SimpleUI class along with its superclass.
	 */
	SimpleUI() {
		super();
		// Display menu.
		Utility.displayMenu(Utility.getMenuEntries());
		
	}
	
	/**
	 * Read user input.
	 */
	private static String readUserInput(String msg) {
		
		// Open standard input.
		BufferedReader br = new BufferedReader(new java.io.InputStreamReader(System.in));

		String selection = null;
		
		//  Read the selection from the command-line; need to use try/catch with the
		//  readLine() method
		try {
			if (msg == null)
				System.out.print("\n>>> ");
			else
				System.out.print("\n" + msg);
			selection = br.readLine();
		} catch (IOException e) {
			System.out.println("IO error trying to read your input!");
			System.out.println(String.format("%s", e.getMessage()));
			System.exit(1);
		}
		
		return selection;

	}
	
	/**
	 * Execute the selected operation.
	 */
	private void performOperation(String operation) {
	
		// Select operation to perform.
		switch(operation) {
		
			case "cb": {
			
				try{
					// Create a bucket.
					do {
						System.out.println(String.format("%s", "The bucket name must follow the format: chowx-i-92aea747.d.eanitea.com"));
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
					} while(bucketName.isEmpty());
					BucketOperations.CreateBucket(bucketName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "lb": {
				try{
					// List the buckets contained in the account.
					BucketOperations.ListBuckets();
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "db": {
				try{
					// Delete a bucket.
					do {
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
					}while(bucketName.isEmpty());	
					BucketOperations.DeleteBucket(bucketName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "uo": {
				try{
					// Upload an object in the specified bucket.
					do {
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
						keyName = readUserInput("Key name: ").toLowerCase();	
					}while(bucketName.isEmpty() || keyName.isEmpty());
					ObjectOperations.UploadObject(bucketName, keyName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
				
			case "do": {
				try{
					// Download an object.
					do {
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
						keyName = readUserInput("Key name: ").toLowerCase();	
					}while(bucketName.isEmpty() || keyName.isEmpty());
					ObjectOperations.DownloadObject(bucketName, keyName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "lo": {
				try{
					// List objects contained in the specified bucket.
					do {
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
					}while(bucketName.isEmpty());
					ObjectOperations.ListObject(bucketName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "xo": {
				try{
					// Delete an object.
					do {
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
						keyName = readUserInput("Key name: ").toLowerCase();	
					}while(bucketName.isEmpty() || keyName.isEmpty());
					ObjectOperations.DeleteObject(bucketName, keyName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
				
			
			default: {
				System.out.println(String.format("%s is not allowed", operation));
				break;
			}
		}
				

	}
	
	
	/***
	 * Get user selection and call the related method.
	 * Loop indefinitely until the user exits the application.
	 */
	public void processUserInput() {
		
		while (true) {
			
			// Get user input.
			String userSelection = readUserInput(null).toLowerCase();	
			// Normalize user's input.
			String normalizedUserSelection = userSelection.trim().toLowerCase();
			

			try{
				// Exit the application.
				if ("x".equals(normalizedUserSelection)){
					break;
				}
				else
					if ("m".equals(normalizedUserSelection)) {
						// Display menu
						Utility.displayMenu(Utility.getMenuEntries());
						continue;
					}
				
			}
			catch (Exception e){
				// System.out.println(e.toString());
				System.out.println(String.format("Input %s is not allowed%n", userSelection));
				continue;
			}
			
			performOperation(normalizedUserSelection);
		}
		
	}
	
}
