package jrd.words4.firebase_auth


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.SignInButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import jrd.words4.ActivityHome
import jrd.words4.R
import jrd.words4.helpers.PrimeUtils
import jrd.words4.helpers.SnackHelper


class ActivityLogin : AppCompatActivity() {

    private lateinit var providers: ArrayList<AuthUI.IdpConfig>
    private lateinit var signInGoogle: SignInButton
    private lateinit var u: PrimeUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        u = PrimeUtils(this)
        signInGoogle = findViewById(R.id.signInGoogle)
        signInGoogle.setSize(1)
        signInGoogle.setOnClickListener {
            u.l("SignInButton Click")
            signIn()
        }
        providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
    }

    private fun signIn() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            Companion.RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Companion.RC_SIGN_IN) {

            u.l("ActivityRes - Code OK")
            val response = IdpResponse.fromResultIntent(data)
            u.l("ActivityRes - Responce : $response")

            if (resultCode == Activity.RESULT_OK) {
                u.l("ActivityRes - Result OK")

                val intentToHome = Intent(this,ActivityHome::class.java)
                intentToHome.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intentToHome)
                finish()
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // ...
            } else {
                u.l("ActivityRes - Result ERROR")
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Snackbar
                    .make(signInGoogle, "Проверьте доступ в интернет", Snackbar.LENGTH_LONG).setDuration(5000)

                    .show()
            }
        }
    }


    override fun onBackPressed() {


        val snackbarOnClickListener: View.OnClickListener =
            View.OnClickListener {
                super.onBackPressed()
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

        val snackbar = Snackbar.make(
            signInGoogle,
            "Пора кормить кота!", Snackbar.LENGTH_SHORT
        )
        SnackHelper().configSnackbar(this, snackbar)
//snackbar.config(this) if you're using Kotlin
        snackbar.show()


//        Snackbar
//            .make(signInGoogle, "Выйти из приложения?", Snackbar.LENGTH_LONG).setDuration(5000)
//            .setAction("Да", snackbarOnClickListener)
//            .show()

    }

    companion object {
        private const val RC_SIGN_IN: Int = 13
    }
}

