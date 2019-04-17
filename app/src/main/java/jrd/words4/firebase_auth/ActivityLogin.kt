package jrd.words4.firebase_auth


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.common.SignInButton
import com.google.android.material.snackbar.Snackbar
import jrd.words4.ActivityA
import jrd.words4.R
import jrd.words4.helpers.PrimeUtils
import jrd.words4.helpers.SnackHelper


class ActivityLogin : AppCompatActivity() {

    private var providers: ArrayList<AuthUI.IdpConfig>
    private lateinit var signInGoogle: SignInButton
    private lateinit var u: PrimeUtils

    init {
        providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
    }

    companion object {
        private const val RC_SIGN_IN: Int = 13
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        u = PrimeUtils(this)
        signInGoogle = findViewById(R.id.signInGoogle)
        signInGoogle.setSize(1)
        signInGoogle.setOnClickListener {
            u.l("Login -> Click")
            signIn()
        }

    }

    private fun signIn() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            //  val response = IdpResponse.fromResultIntent(data)
            //  u.l("ActivityRes - Responce : $response")

            if (resultCode == Activity.RESULT_OK) {
                u?.l("Login - Result OK")

                val intentToHome = Intent(this, ActivityA::class.java)
                intentToHome.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intentToHome)
                finish()
            } else {
                u?.l("Login - Result ERROR")
                val snackbar = Snackbar
                    .make(signInGoogle, "Проверьте доступ в интернет", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                SnackHelper().configSnackbar(this, snackbar)
                snackbar.show()
            }
        }
    }

    private fun finishActivity() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onBackPressed() {
        val snackbarOnClickListener: View.OnClickListener =
            View.OnClickListener {
                super.onBackPressed()
                finishActivity()
            }
        val snackbar = Snackbar
            .make(signInGoogle, "Выйти из приложения?", Snackbar.LENGTH_LONG)
            .setDuration(2700)
            .setAction("Да", snackbarOnClickListener)
        SnackHelper().configSnackbar(this, snackbar)
        snackbar.show()
    }

}

