package jrd.words4.helpers

import android.app.Activity


class PrimeThreads(activity: Activity) {

    val act = activity
    private var u: PrimeUtils

    init {
        u = PrimeUtils(act)
    }



}