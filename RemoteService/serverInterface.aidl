// serverInterface.aidl
package com.example.admd_hw2;

// Declare any non-default types here with import statements

interface serverInterface {
   String getMovies();
   void postRating(String title,String rating);

}