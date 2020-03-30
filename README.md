# Railway-Reservation-Sytem
**A Railway Reservation System which is purely in java.**

A JAVA application which uses JAVA API JDBC for the database purposes. The railway reservation
system provides train timing details, PNR number, seat number, booking and cancellation of different
class of coach of various types of reservation namely,Confirm Reservation for confirm seat. Waiting list
reservation.

***Steps to run this application as it should be:-***

**Installation**
1. Java - Because this application is built on it https://java.com/en/download/help/download_options.xml
2. MySQL - Database https://www.sitepoint.com/how-to-install-mysql/
3. JDBC Driver - to interact with database https://www.wavemaker.com/learn/app-development/services/database-services/download-jdbc-driver-jar

**Setup**
1. Create a user called 'root' and set the password to '1234'.
2. Create a new database called 'railway'. 
3. Create 3 tabels called 'user','train' and 'chart'.
4. The fields of 'user' table will be uname(string),pass(string),age(int),g(string),timestamp(timestamp),sno(int which should be auto incremented).
5. The fields of 'train' table will be tnum(int), tname(string), seats(int), bp(string), dp(string), fAC(int), sAC(int), tAC(int), sc(int), doj(date), dtime(string), atime(string), sno(int which should be auto incremented).
6. The fields of 'chart' table will be pnr(int), name(string), age(int), gender(string), seatno(int), coach(string), status(string), timestamp(timestamp), dot(date), sno(int which should be auto incremented), tnum(int).

**To run the file**
1. javac railway.java
2. java railway.
3. Enjoy!!
