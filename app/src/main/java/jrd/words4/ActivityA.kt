package jrd.words4

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jrd.words4.firebase_auth.ActivityLogin
import jrd.words4.helpers.PrimeThreads
import jrd.words4.helpers.PrimeUtils
import jrd.words4.helpers.SnackHelper
import jrd.words4.progress.FGProgress
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.ExecutionException

class ActivityA : AppCompatActivity() {

    private var dbFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var authFirebase: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var u: PrimeUtils
    private lateinit var uri: Uri
    private lateinit var pThread: PrimeThreads
    private lateinit var task: DownloadImageTask
    private lateinit var navController : NavController
    /** VIEW */
    private var mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener
    private lateinit var uProfilePhoto: ImageButton
    private lateinit var bnmView: BottomNavigationView
    /** Bitmap */
    private var profilePhoto: Bitmap? = null

    init {
        /** MAIN */
        dbFirestore = FirebaseFirestore.getInstance()
        authFirebase = FirebaseAuth.getInstance()
        /** VIEW */
        mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnv_to_home -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bnv_to_prog-> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bnv_to_stat-> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)

        navController = Navigation.findNavController(this, R.id.navHost)
        bnmView = findViewById(R.id.menu_bnm)
        bnmView.selectedItemId = R.id.bnv_to_home
        /** MAIN */
        task = DownloadImageTask()
        u = PrimeUtils(this)
        pThread = PrimeThreads(this)
        /** VIEW */
        uProfilePhoto = findViewById(R.id.uProfilePhoto)

// TODO обработка был ли запуск заданий /
        if(false){
            }else{navController.navigate(R.id.FGHomeFirstRun)}


        u.l("onCreate")
        /** Init */
        if (authFirebase.currentUser != null) {
            u.l("User Online")
            uri = authFirebase.currentUser!!.photoUrl!!
            u.l("Download Photo -> Try get profile photo")
            try {
                profilePhoto = task.execute(uri.toString()).get()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } else {
            u.l("User Offline -> Login Activity")
            startIntentToLogin()
        }
        if (profilePhoto != null) {
            u.l("Set Profile Photo")
            uProfilePhoto.setImageBitmap(profilePhoto)
        } else if (authFirebase.currentUser != null) {
            u.l("Set Default Profile Photo")
            uProfilePhoto.setImageDrawable(getDrawable(R.drawable.u))
        }
        /** Init */
        /** Init */
        /** Init */
        /** Init */
        /** Init */
        /** Init */
        /** Init */
        uProfilePhoto.setOnClickListener {
            authFirebase.signOut()
            u.l("logout")
            startIntentToLogin()
        }
        bnmView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onResume() {
        super.onResume()
        u.l("onResume")

    }

    override fun onBackPressed() {
        val snackbarOnClickListener: View.OnClickListener =
            View.OnClickListener {
                super.onBackPressed()
                startIntentToDesktop()
            }
        val snackbar = Snackbar
            .make(bnmView, "Уже уходите?", Snackbar.LENGTH_LONG)
            .setDuration(2700)
            .setAction("Да", snackbarOnClickListener)
        SnackHelper().configSnackbar(this, snackbar)
        snackbar.show()
    }

    private fun startIntentToLogin() {
        val intent = Intent(this, ActivityLogin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
        finish()
    }

    private fun startIntentToDesktop() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    internal class DownloadImageTask : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg strings: String): Bitmap? {
            var url: URL? = null
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL(strings[0])
                urlConnection = url.openConnection() as HttpURLConnection
                val inputStream = urlConnection.inputStream
                return BitmapFactory.decodeStream(inputStream)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
            }
            return null
        }
    }
}
