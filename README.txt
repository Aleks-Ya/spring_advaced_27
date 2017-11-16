Local run
mvn jetty:run

Base URL:
http://localhost:8080

Request example:
PUT localhost:8080/user
{
   "id":1,
   "name":"John",
   "email":"john@gmail.com",
   "birthday":"2000-07-03"
}