# dwm-document-service

Service responsible for storage, retrieval and update of documents for users. This microservice uses restful design.

Technology : SpringBoot, H2 in memory database, maven, jpa

Scope of the service includes:
● Overview of currently attached documents for a user
● Document upload, download and modification functionality
● Documents with metadata like name and type , creation date and updated date
● Persist data across restarts

Validation : 
● Maximum allowed document upload size limit is 2MB
● Document name is unique per user


Swagger Link: http://localhost:8080/swagger-ui-document-api.html

Health Actuator :
http://<server>:8080/actuator/health
Example
http://localhost:8080/actuator/health  

How to run the service:

1.Run the app using maven : mvn spring-boot:run
   
The application can be accessed at http://localhost:8080.

2.Run the Springboot fat jar : java -jar documentdemo-0.0.1-SNAPSHOT.jar

3.Run the docker image of the app : docker run -p 8080:8080 banani9/document_service_1.1.0

To Test:

Run the application.Open up an api client like Postman and then send a POST request to the api.
for e.g To test the upload api : http://localhost:8080/api/document/upload
It needs two request parameter "documents" and "username". Chose body and then under form-data
chose KEY as "documents" and the type chosen is File, multiple documents can be added as VALUE.
chose KEY as "username" and the type chosen is Text, add any username of your choice as VALUE.


Endpoint/Interface summary:

1."/api/document/upload" : Used to upload the documents for a user with a given "documents" list and "username". 
2."/api/document/downloadFile" : Downloads document for a given "username" and "filename".
3."/api/document/update/{id}": Updates name or replaces a particular document of the provided document "id", 
takes request parameter as "name" or "document".
4."/api/document/documents" : Fetches all the documents for a user with given "username".  


Storage and Persistence:
1.The service uses H2 database to store the Documents information and metadata.
And the original document is stored in the current working space.

2.The service also persist the data across restart.


Future enhancement possibilities:
Store the files in Azure Blob Storage or AWS S3 buckets instead of in OS disk

