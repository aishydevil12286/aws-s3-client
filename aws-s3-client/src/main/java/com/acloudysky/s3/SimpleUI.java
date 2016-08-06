package com.acloudysky.s3;


import java.io.BufferedReader;
import java.io.IOException;


/*** 
 * Display a selection menu for the user. Process the
 * user's input and call the proper method based on the user's selection.
 * Each method calls the related 
 * <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/Welcome.html" target="_blank">AWS S3 API</a>.
 * @author Michael Miele.
 *
 */
public class SimpleUI {
	
	private StringBuilder menu;
	private static final String newline = System.getProperty("line.separator");
	
	private String bucketName, keyName;
	
	/**
	 * Initializes the menu that allows the user to make the allowed choices.
	 * It uses a StringBuilder to create the formatted menu.
	 */
	SimpleUI() {
		menu = new StringBuilder();
		menu.append(String.format("Select one of the following options:%n"));	
		menu.append(String.format("%s %s Create bucket.", newline, "b1"));
		menu.append(String.format("%s %s List buckets.", newline, "b2"));
		menu.append(String.format("%s %s Delete bucket.", newline, "b3"));
		
		menu.append(String.format("%s %s Upload object.", newline, "o1"));
		menu.append(String.format("%s %s Download object.", newline, "o2"));
		menu.append(String.format("%s %s List objects.", newline, "o3"));
		menu.append(String.format("%s %s Delete object.", newline, "o4"));
		
		menu.append(String.format("%s %s  Display menu.", newline, "m"));
		menu.append(String.format("%s %s  Exit application.", newline, "x"));
		menu.append(newline);		
		
	
		// Display menu.
		System.out.println(menu.toString());
		
		
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
	/***
	 * Get user selection and call the related method.
	 * Loop indefinitely until the user exits the application.
	 */
	public void processUserInput() {
		
		String in = null;
		while (true) {
			
			// Get user input.
			String selection = readUserInput(null).toLowerCase();	
			
			try{
				// Exit the application.
				if ("x".equals(selection))
					break;
				else
					if ("m".equals(selection)) {
						// Display menu
						System.out.println(menu.toString());
						continue;
					}
					else 
						// Read the input string.
						in = selection.trim();
	
			}
			catch (Exception e){
				// System.out.println(e.toString());
				System.out.println(String.format("Input %s is not allowed%n", selection));
				continue;
			}
			
			// Select action to perform.
			switch(in) {
			
				case "b1": {
				
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
				
				case "b2": {
					try{
						// List the buckets contained in the account.
						BucketOperations.ListBuckets();
					}
					catch (Exception e){
						System.out.println(String.format("%s", e.getMessage()));
					}
					break;
				}
				
				case "b3": {
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
				
				case "o1": {
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
					
				case "o2": {
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
				
				case "o3": {
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
				
				case "o4": {
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
					System.out.println(String.format("%s is not allowed", selection));
					break;
				}
			}
					
		}
		SimpleUI.Exit();
		
	}
	
	private static void Exit() {
		System.out.println("Bye!\n");
		return;
	}
}
