Introduction
------------
This project demonstrates a RESTful web service that allows managing two entities, a `Person` entity and a `Message` entity. A person can be the sender of a message or the receiver. The web service exposes end-points that allow clients to use `GET` and `POST` requests to manage these entities. In addition, there are other helpful queries that provide easier access to `Person` and `Message` entities such as the those listed below,

1. Get a list of messages by specifying a sender,
  
    `GET /messages?senderId=:senderId`,
    
    or by specifying a receiver, 
    
    `GET /messages?senderId=:senderId`,
  
    or by specifying both,
    
    `GET /messages?senderId=:senderId&receiverId=:receiverId`
      
  
2. Get back useful links in the response JSON to navigate to related resources, such as - get a link to the related person resources in the response of the `GET /messages` request. like so,

      ````
      [{
          "message": {
              "id": 80,
              ...
              "messageDesc": "This is message one"
          },
          "links": [
              ...
          {
              "rel": "sender",
              "href": "http://localhost:8080/persons/1187"
          }
              ...
      }]
      ````

How this web-service was developed.
-----------------------------------
This project was developed using Java and Spring framework. The Spring libraries used are Boot, Data JPA, MVC/Web, HATEOAS, Test, RestDocs.

The jar is deployed on AWS EC2 instance and uses RDS for storing the data.

API Reference.
-----------------
The API reference is available at [API Reference](http://htmlpreview.github.io/?https://github.com/omersalar/Rest-Demo/blob/master/target/generated-docs/APIDocumentation.html). The documenatation was prepared using Spring RestDocs.


:thumbsup: :heart: :clap:
