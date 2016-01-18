#Build It Bigger

##What is it?

This is an Android app with multiple flavors that uses multiple libraries 
and a Google Cloud Endpoints. The app consists of four modules. A Java 
library that provides jokes, a Google Could Endpoints (GCE) project that 
serves those jokes, an Android Library containing an
activity for displaying jokes, and an Android app that fetches jokes from 
the GCE module and passes them to the Android Library for display.

##Platform and Libraries Used

- Android SDK 14 or Higher
- Android Support AppCompat 23.1.0
- Google Appengine Endpoints 1.9.18
- Javax Servlet API 2.5

##Installation Instructions

This project uses the Gradle build automation system. To build this project, 
use the "gradlew build" command or use "Import Project" in Android Studio.

Please ensure that you add the project id for the GCE backend server 
fetching jokes in these files:
1. app/src/main/res/values/strings.xml
2. jokesbackend/src/main/webapp/WEB-INF/appengine-web.xml
3. app/src/androidTest/java/JokeAsyncTaskTest.java

##Attribution

The starter code for the app was provided by [Udacity](https://github.com/udacity/ud867/tree/master/FinalProject)