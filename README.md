# PLARS
A Personal, Location-Aware Recommendation System for restaurants in Calgary. 

## Background 
This is a project for the Advanced Geospatial Topics course at the University of Calgary. It provides restaurant recommendations based on user context, namely: 

``` recommendation index = rating * preference / distance ```

where:

* Recommendation index: restaurants with higher indeces are recommended to the user 
* Rating: the five-star restaurant rating as mined from Yelp 
* Preference: user preference for the restaurant type, i.e. "Italian", based on the proportion of previous user clicks over overall clicks 
* Distance: the distance from the user to the particular restaurant
