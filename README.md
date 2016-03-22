# aws-s3-client
Java console application showing how to interact with Amazon S3.
For details see:
<ul>
	<li><a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html" target="_blank">Working with Amazon S3 Buckets</a></li>
	<li><a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingObjects.html" target="_blank">Working with Amazon S3 Objects</a></li>
</ul>

Also, see the companion blog post at this location: <a href="http://acloudysky.com/build-aws-s3-client-application/" target="_blank">Build AWS S3 
Client Application</a>.

<h2>Prerequisites</h2>
You must have Maven installed. The dependencies are satisfied by building the Maven package.

<h2>Security Credentials</h2>
You need to set up your AWS security credentials before the sample code is able to connect to AWS. You can do this by creating a file named "credentials" at ~/.aws/ (C:\Users\USER_NAME.aws\ for Windows users) and saving the following lines in the file:

<pre>
  [default]
    aws_access_key_id = &lt;your access key id&gt;
    aws_secret_access_key = &lt;your secret key&gt;
</pre>

<h2>Running the Example</h2>
The application connects to Amazon's <a href="http://aws.amazon.com/s3" target="_blank">Simple Storage Service (S3)</a>, and allows the user to create a bucket, upload an object into the bucket, download the object, delete the object and delete the bucket. All you need to do is run it by following these steps:
<ol>
	<li>From the project, create an executable JAR</li>
	<li>From a terminal window, go to the directory containing the JAR and execute a command similar to 
	the following:   
	<pre>
  	java -jar aws-s3-java.jar
	</pre>	
	<span style="background-color: #ffffcc; color:red">Alternatively, you can use a tool like Eclipse to build the application and run it</span>.
	</li>
</ol>

<p>
Assure that the pom.xml file has the following dependency, otherwise you'll get a runtime error when executing the jar. 
<code>
	<pre>
		&lt;dependency&gt;
			&lt;groupId&gt;joda-time&lt;/groupId&gt;
			&lt;artifactId&gt;joda-time&lt;/artifactId&gt;
			&lt;version&gt;2.9&lt;/version&gt;
		&lt;/dependency&gt;
	</pre>
	
</code>
</p>
<p>
	<b>Note</b>.  See <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html" target="_blank">Bucket Restrictions and Limitations</a>. See also <a href="http://acloudysky.com/cloud-application-common-tasks/#createrunnablejar" target="_blank">Create Runnable JAR</a>.
</p>

<h2>License</h2>
This sample application is distributed under the <a href="http://www.apache.org/licenses/LICENSE-2.0" target="_blank">Apache License, Version 2.0</a>.

