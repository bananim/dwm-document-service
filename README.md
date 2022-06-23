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

1.Run the app using maven
    mvn spring-boot:run
The application can be accessed at http://localhost:8080.

2.Run the Springboot fat jar:
    java -jar documentdemo-0.0.1-SNAPSHOT.jar

3. Run the docker image of the app
docker run -p 8080:8080 banani9/document_service_1.0.0

Endpoint/Interface summary:

1."/api/document/upload" : Used to upload the documents for a user
2."/api/document/downloadFile" : Downloads document for a given username and filename.
3."/api/document/update/{id}": Updates name or replaces a particular document of the provided id.
4."/api/document/documents" : Fetches all the documents for a user.


Storage and Persistence:
1.The service uses H2 database to store the Documents information and metadata.
And the original document is stored in the current working space.

2.The service also persist the data across restart.


Future enhancement possibilities:
Store the files in Azure Blob Storage or AWS S3 buckets instead of in OS disk

