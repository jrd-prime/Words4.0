package jrd.words4

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jrd.words4.firebase_auth.ActivityLogin
import jrd.words4.helpers.PrimeThreads
import jrd.words4.helpers.PrimeUtils
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
    /** VIEW */
    private var mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener
    private lateinit var uProfilePhoto: ImageView
    private lateinit var bnmView: BottomNavigationView
    /** Bitmap */
    private var profilePhoto: Bitmap? = null

    init {
        /** MAIN */
        dbFirestore = FirebaseFirestore.getInstance()
        authFirebase = FirebaseAuth.getInstance()


        mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)
        /** MAIN */
        task = DownloadImageTask()
        u = PrimeUtils(this)
        pThread = PrimeThreads(this)
        /** VIEW */
        uProfilePhoto = findViewById(R.id.uProfilePhoto)
        bnmView = findViewById(R.id.menu_bnm)


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
            val intent = Intent(this, ActivityLogin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
            finish()
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
        bnmView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onResume() {
        super.onResume()
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
