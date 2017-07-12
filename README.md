# PLARS
A Personal, Location-Aware Recommendation System for restaurants in Calgary. 

## Background 
This is a project for a geomatics course by MH Gonzales and X Zou for the University of Calgary. It served as a concept sketch for a restaurant recommendation algorithm based on user context. Namely: 

``` recommendation index = rating * preference / distance ```

where:

* Recommendation index: restaurants with higher indeces are recommended to the user 
* Rating: the five-star restaurant rating as mined from Yelp 
* Preference: user preference for the restaurant type, i.e. "Italian", based on the proportion of previous user clicks over overall clicks 
* Distance: the distance from the user to the particular restaurant

A more in-depth implementation involving statistical analysis of the smartphone sensor data is explored in [the following paper](http://ieeexplore.ieee.org/document/7746307/?reload=true). It also considers dynamically accounting for the user's mode of transport in the application. 
