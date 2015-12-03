<a href="https://www.linkedin.com/pub/jill-heske/13/836/635">
                <img src="https://static.licdn.com/scds/common/u/img/webpromo/btn_viewmy_160x33.png" width="160" height="33" border="0" alt="View Jill Heske's profile on LinkedIn"></a>
mo

## Synopsis

![Football scores icon](https://github.com/jheske/FootballScores/blob/master/app/src/main/res/drawable-xhdpi/ic_launcher.png?raw=true)

This repository contains **FootballScores**, an app for viewing football match scores and listing upcoming games.  The original version of the app was provided by Udacity as part of Project #3.  I made considerable modifications to fix bugs, port most components to Material AppCompat library widgets, add a home screen widget, and incorporate accessibility features.  In addition, I translated the app into Spanish to demonstrate internationalization.     


## Features

This project is designed to meet all of the requirements as per Udacity's rubric for P3.  Features may or may not be completed and include, but are not limited to:

Tabbed layout on home page allows user to view scores for past games and dates and times for upcoming games. 

Now uses Google's latest Material AppCompat and Design library widgets for most components, including Toolbar, TabLayout, ViewPager, RecyclerView, and Cardview.

Uses a SQLite database and a ContentProvider for storing and retrieving scores.

Offers a home screen widget to periodically automatically update and display latest scores.

## Screenshots

<img src="https://github.com/jheske/FootballScores/blob/master/portrait-scores.png?raw=true" alt="Football Scores main screen" width="250">  <img src="https://github.com/jheske/FootballScores/blob/master/spanish.png?raw=true" alt="Marcadores de Fútbol" width="250">  <img src="https://github.com/jheske/FootballScores/blob/master/widget.png?raw=true" alt="Football Scores widget" width="250">


## build.gradle

    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:23.0.0'
    compile 'com.android.support:design:23.0.0'
    compile 'com.android.support:recyclerview-v7:23.0.0'
    compile 'com.android.support:cardview-v7:23.0.0'
    compile 'com.facebook.stetho:stetho:1.1.1'
    compile 'com.jakewharton:butterknife:7.0.1'
    compile 'com.squareup.phrase:phrase:1.1.0'


## Icon 

http://www.iconarchive.com/show/button-ui-requests-2-icons-by-blackvariant/PopcornTime-icon.html


## Testing

This project has been tested on:

* Samsung Tab 4 running Android 4.4.2
* LG Optimus L90 running Android 4.4.2
* GenyMotion emulator running Android 5.1

			
## Installation

You can fork this repo or clone it using `git clone https://github.com/jheske/FootballScores.git`


## Required api key

All calls to api.football-data.org require a valid api key. 
 
**/api.football-data.org api keys may not be shared publicly so I have removed my key from this app**

In order to compile and run the app, you will need to acquire your own api key here [http://api.football-data.org/register](http://api.football-data.org/register).

    
Once you have your key, you must provide your own ApiKey.java class as follows:
    
    public class ApiKey {

    	private static final String apiKey = "your api key";

    	public static String getApiKey() {return apiKey;}

    	//Make ApiKey a utility class by preventing instantiation.
    	private ApiKey() {throw new AssertionError();}

    }

**REMEMBER to .gitignore ApiKey.java to keep it out of your repo.**


## Contributors

Jill Heske

## License

See LICENSE file at top level of repo.