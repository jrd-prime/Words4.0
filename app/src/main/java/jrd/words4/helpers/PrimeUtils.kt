package jrd.words4.helpers

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log

class PrimeUtils(activityMain: Activity) {

    private val activity = activityMain
    private val context: Context = activity.applicationContext
    private val const = Constants
    private val pref = PreferenceManager.getDefaultSharedPreferences(context)
    private val status = pref.getBoolean(const.FIRST_RUN, true)

    fun checkFirstRun(): Boolean {

        l(pref.getBoolean(const.FIRST_RUN, true).toString())
        return pref.getBoolean(const.FIRST_RUN, true)
    }

    fun setFirstRun() {
            pref.edit().putBoolean(const.FIRST_RUN, false).apply()
            l("" + const.FIRST_RUN + " setted to FALSE")
    }
    fun checkHomeFirstRun(): Boolean {

        l(pref.getBoolean(const.HOME_FIRST_RUN, true).toString())
        return pref.getBoolean(const.HOME_FIRST_RUN, true)
    }

    fun setHomeFirstRun() {
        pref.edit().putBoolean(const.HOME_FIRST_RUN, false).apply()
        l("" + const.HOME_FIRST_RUN + " setted to FALSE")
    }

    fun l(msg: String) {
        val m = activity.localClassName + " /// " + msg
        Log.i("mInfo", m)
    }


}