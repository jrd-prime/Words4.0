package jrd.words4

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jrd.words4.firebase_auth.ActivityLogin
import jrd.words4.helpers.PrimeUtils
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL


class ActivityHome : AppCompatActivity() {

    private lateinit var dbFirestore: FirebaseFirestore
    private lateinit var authFirebase: FirebaseAuth
    private lateinit var u: PrimeUtils
    private lateinit var fab: FloatingActionButton
    private lateinit var imgToolbar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        u = PrimeUtils(this)
        if (intent.getBooleanExtra("Exit me", false)) {

            u.l(intent.getBooleanExtra("Exit me", false).toString())
            finish()
        } else {
            dbFirestore = FirebaseFirestore.getInstance()
            authFirebase = FirebaseAuth.getInstance()
            imgToolbar = findViewById(R.id.imgToolbar)
//            toolbar = findViewById(R.id.toolbar)
            fab = findViewById(R.id.fab)
            fab.setOnClickListener {
                Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            }


            val thread = Thread {
                try {

                    val phBit = authFirebase.currentUser?.photoUrl

                    val newurl = URL(phBit.toString())
                   val  mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream())
                    imgToolbar.setImageBitmap(mIcon_val)

                        val bitmap = mIcon_val
                        imgToolbar.setImageDrawable(bitmapToDrawable(bitmap))





                    u.l(phBit.toString())

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            thread.start()

            if (authFirebase.currentUser != null) {
                u.l("Авторизован -> ?")
                Snackbar.make(fab, "Вы авторизованы", Snackbar.LENGTH_LONG).setDuration(5000).show()
            } else {
                u.l("Не авторизован -> Login activity")
                val notLoggedIntent = Intent(this, ActivityLogin::class.java)
                startActivity(notLoggedIntent)
            }
        }

    }
    // Method to convert a bitmap to bitmap drawable
    private fun bitmapToDrawable(bitmap:Bitmap): BitmapDrawable {
        return BitmapDrawable(resources,bitmap)
    }
    private fun getImageBitmap(url: String): Bitmap? {
        var bm: Bitmap? = null
        try {
            val aURL = URL(url)
            val conn = aURL.openConnection()
            conn.connect()
            val inp = conn.getInputStream()
            val bis = BufferedInputStream(inp)
            bm = BitmapFactory.decodeStream(bis)
            bis.close()
            inp.close()
        } catch (e: IOException) {
            Log.e("mInfo", "Error getting bitmap", e)
        }

        return bm
    }

    override fun onResume() {

        u.l("resume")
        if (intent.getBooleanExtra("Exit me", false)) {

            u.l(intent.getBooleanExtra("Exit me", false).toString())
            finish()
        }
        super.onResume()
        if (authFirebase.currentUser != null) {
            u.l("Авторизован -> ?")
            Snackbar.make(fab, "Вы авторизованы", Snackbar.LENGTH_LONG).setDuration(5000).show()
        } else {
            u.l("Не авторизован -> Login activity")
            val notLoggedIntent = Intent(this, ActivityLogin::class.java)
            startActivity(notLoggedIntent)
        }
    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = authFirebase.currentUser
        //updateUI(currentUser)
    }




}
