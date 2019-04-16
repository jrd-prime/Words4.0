package jrd.words4.helpers


import android.content.Context
import com.google.android.material.snackbar.Snackbar
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import jrd.words4.R


class SnackHelper {

        fun configSnackbar(context: Context, snackbar: Snackbar) {
            addMargins(snackbar)
            setRoundBordersBg(context, snackbar)
            ViewCompat.setElevation(snackbar.view, 10F)
        }

        private fun addMargins(snack: Snackbar) {
            val params = snack.view.layoutParams as ViewGroup.LayoutParams

            snack.view.alpha = 0.9F
            snack.view.layoutParams = params
        }

        private fun setRoundBordersBg(context: Context, snackbar: Snackbar) {
            // API 21
            snackbar.view.background = context.getDrawable(R.drawable.bg_snack)
        }

}