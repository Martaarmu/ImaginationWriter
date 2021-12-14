# ¡Imagination Writer App!

## What do you need?
You only need Java [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

Some Spanish words to understand the GUI

## Freatures

- [x] You can register and login into the app. Also you can change your profile details.

- [x] You have the total control of your books and share with others authors

- [x] Passwords are encrypted with SHA256

- [x] You got a CRUD of Books, Parts, Chapters, Scenes and Characters

- [x] (Beta) You can export to PDF (without layout)

- [x] Upload local DB to remote MariaDB server and download DB from remote MariaDB

## How it works

Just double click on .jar file or .exe file.

**NOTE**: That data will missing for the first time because the generated H2 DataBase.

**NOTE**: Windows 10 or 11 is hardly recommended for run this app because of the GUI and icons.
### XML Configuration
Is under ImaginationWriter/src/main/resources/META-INF/persistence.xml

**Recommended** 

Touch only MariaDB configuration (ApplicationMariaDB persistence-unit)

1. javax.persistence.jdbc.url ➡ To specify remote MariaDB url(put the name of the database after last "/")
2. javax.persistence.jdbc.user ➡ User database that can modify data
3. javax.persistence.jdbc.password ➡ Password of the userdatabase

## ScreenShots
### Login
<img height="322" src="https://raw.githubusercontent.com/Varo95/ImaginationWriter/master/screenshots/Login.png" width="421"/>

### Register-Edit Profile
<img height="442" src="https://raw.githubusercontent.com/Varo95/ImaginationWriter/master/screenshots/Profile-Editor.png" width="437"/>

### Books View
<img height="577" src="https://raw.githubusercontent.com/Varo95/ImaginationWriter/master/screenshots/Books.png" width="665"/>

### Characters-Scene Editor View
<img height="540" src="https://raw.githubusercontent.com/Varo95/ImaginationWriter/master/screenshots/Characters-Scenes.png" width="751"/>

### Book Editor View
<img height="916" src="https://raw.githubusercontent.com/Varo95/ImaginationWriter/master/screenshots/Book-Editor.png" width="1487"/>

### Parts-Chapters Editor View
<img height="540" src="https://raw.githubusercontent.com/Varo95/ImaginationWriter/master/screenshots/Parts-Chapters.png" width="751"/>