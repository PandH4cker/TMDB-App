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
+ [Android Youtube Player](https://github.com/PierfrancescoSoffritti/android-youtube-player)


---
## Last Release Version: 0.0.8
### Changelog:
> #### Version 0.0.8
> Refactored file architectures', added GenreConverter as TypeAdapter for Room integration of the genre_ids. <br />
> Improved MovieAdapter to put to the MovieDetailsActivity the information from where it has been started. <br />
> Improved APIClient to handle multiple register type adapter, migrated the type to TypeToken instance. <br />
> Improved the model, added the Genre enumeration to handle genre_ids list from API response.
> 
---
> #### Version 0.0.7
> Adding connection between the main view and the movie details view. <br />
> Added Red Bookmark vector asset to set up a film in the favorite list. <br />
> Added Android Youtube Player to handle playing youtube video for movie trailers. <br />
> Improved movie details activity and refactored its design and created cast item for the future recycler view adapter for it. <br />
> Added Video Model to handle the response of the API when fetching the video trailer of the movie.
---
> #### Version 0.0.6
> Adding the first implementation of the main view and refactoring some code.
---
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