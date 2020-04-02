# TMDB App
---
## Authors: Raphael Dray
---
> This program is an Android Mobile Application. <br />
> It can be launched with an Android Simulator (included with Android Studio). <br />
> It fetches data from TMDB ([The Movie Database](https://www.themoviedb.org/)) API.


> It's written in __Java programming language including some libraries like so:__
+ [Picasso](https://square.github.io/picasso/)
+ [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview)
+ [Retrofit 2](https://square.github.io/retrofit/)
+ [RxJava 2/RxAndroid](https://github.com/ReactiveX/RxJava)
+ [Room](https://developer.android.com/jetpack/androidx/releases/room)
+ [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
+ [OkHttp 3 Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)
+ [CircleImageView](https://github.com/hdodenhof/CircleImageView)


---
## Last Release Version: 0.0.5
### Changelog:
> #### Version 0.0.5
> Added ViewModels implementation for handling popular movies get request. <br />
> Add RxJava call adapter into ViewModels to dispose and store subscriptions.
---
> #### Version 0.0.4
> Added Room DAO classes and refactored MovieModel and APIClient classes for handling Paging and Favorite. <br />
> Upgraded Java support version to Java 8 for including lambdas expression.
---
> #### Version 0.0.3
> Adding of API Management classes with the adaptation of RxJava2 in Retrofit2. <br />
> Adding of JsonDeserializer used by the GsonFactory to only retrieve the results array object of MovieModel in the response of the API. <br />
> Adding 5 requests to handle respectively:
> + Latest movies
> + Now Playing movies
> + Popular movies
> + Top Rated movies
> + Upcoming movies
---
> #### Version 0.0.2
> Adding of utils for network availability test and dates formatting.
---
> #### Version 0.0.1
> Adding of activity movie details xml file and synced with Gradle libraries written in the README. <br />
> Adding some classes to perform a multi-threaded blur on an image for the front of the application. <br />
> Filled the mainActivity class to build the activity movie details xml file in order to test this view. 