package jrd.words4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jrd.words4.firebase_auth.ActivityLogin
import jrd.words4.helpers.PrimeThreads
import jrd.words4.helpers.PrimeUtils


class ActivityHome : AppCompatActivity() {

    private lateinit var dbFirestore: FirebaseFirestore
    private lateinit var authFirebase: FirebaseAuth
    private lateinit var u: PrimeUtils
    //  private lateinit var fab: FloatingActionButton
    private lateinit var imgToolbar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        u = PrimeUtils(this)


        dbFirestore = FirebaseFirestore.getInstance()
        authFirebase = FirebaseAuth.getInstance()
        imgToolbar = findViewById(R.id.uProfilePhoto)
        val uri: Uri? = authFirebase.currentUser?.photoUrl





        if (authFirebase.currentUser != null) {
            u.l("Авторизован -> ?")
            //  Snackbar.make(fab, "Вы авторизованы", Snackbar.LENGTH_LONG).setDuration(5000).show()
        } else {
            u.l("Не авторизован -> Login activity")
            val notLoggedIntent = Intent(this, ActivityLogin::class.java)
            startActivity(notLoggedIntent)
        }
    }


    override fun onResume() {
        val uri: Uri? = authFirebase.currentUser?.photoUrl


        if (uri != null) {
        //    PrimeThreads(this).downloadProfilePhoto(uri, imgToolbar)
        }
        u.l("resume")
        if (intent.getBooleanExtra("Exit me", false)) {

            u.l(intent.getBooleanExtra("Exit me", false).toString())
            finish()
        }
        super.onResume()
        if (authFirebase.currentUser != null) {
            u.l("Авторизован -> ?")
            //  Snackbar.make(fab, "Вы авторизованы", Snackbar.LENGTH_LONG).setDuration(5000).show()
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
