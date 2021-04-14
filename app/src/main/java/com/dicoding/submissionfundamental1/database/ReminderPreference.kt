package com.dicoding.submissionfundamental1.database

import android.content.Context

class ReminderPreference(context: Context) {

    companion object{
        const val PREFS_NAME = "reminder_pref"
        private const val IS_REMINDER = "isReminder"
    }

    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setReminder(reminder: Boolean){
        val editor = preference.edit()
        editor.putBoolean(IS_REMINDER, reminder)
        editor.apply()
    }

    fun getReminder(): Boolean = preference.getBoolean(IS_REMINDER, false)

}