NY Times

This application gives the latest news taken from The NewYork Times Developer API.  

<h2>Architecture Design Pattern</h2>

MVVM Clean Architecture with SOLID PRINCIPLES.<br>
This client application uses Flow, Live-data, Coroutines, View binding and other Jetpack components to handle data opertions.<br>

<h2>Dependency Injection</h2>
Dagger-Hilt 

<h2>Remote Network</h2>
Retrofit2 for remote network calls to get<br/>
  1. Most popular news<br/>
  2. Top articles<br/>
  3. Movie reviews<br/>
  4. Top articles based on category - arts, fashion, health, books and so on.<br/>

<h2>Local Data Source</h2>
Room Database for saving news article.

<h2>Unit and UI Testing</h2>
Junit, Mockito , MockWebserver are used for writing test cases for modules and Espresso for UI Testing.

<h2>4. Min SDK Version</h2>

Minimum Android API 21

<h2>5. Credentials to run with user defined API KEY</h2>

You can replace your own api-key in the project file - gradle.properties.<br/>
Just follow these steps.<br/>

Step 1 : Login to https://developer.nytimes.com/<br/>
Step 2 : Navigate to Apps and create new app.<br/>
Step 3 : Enable API - Most Popular API, Movie Reviews API, Top Stories API.<br/>
Step 4 : Copy the API key and replace in the project directory -> gradle.properties ->(field) API_KEY<br/>
Step 5 : Rebuild Project and Voila you are good to go!<br/>

<h2>5. References</h2>

The NewYork Times Developer portal - https://developer.nytimes.com/<br/>

Please navigate to WIKI to know more about this application!


