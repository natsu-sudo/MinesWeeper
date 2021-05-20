package com.coding.minesweeper

import android.content.Context
import android.content.SharedPreferences


//this class contain all the User shared Preferences which we used in App
class UserSharedPreferences {
    companion object{
        fun initializeSharedPreferencesForBestTime(context: Context): SharedPreferences {
            return context.getSharedPreferences(Constants.BEST_TIME,Context.MODE_PRIVATE)
        }

        fun initializeSharedPreferencesForLastGamePlayed(context: Context): SharedPreferences {
            return context.getSharedPreferences(Constants.LAST_PLAYED,Context.MODE_PRIVATE)
        }
    }
}