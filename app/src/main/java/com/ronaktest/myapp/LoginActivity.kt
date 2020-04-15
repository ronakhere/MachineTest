package com.ronaktest.myapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.ronaktest.myapp.util.AppPreference
import com.ronaktest.myapp.util.Constants
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity  : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN = 9001
    private var TAG = LoginActivity::class.java.getName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)

        init()
    }

    private fun init() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        if (AppPreference.getInstance(this).getString(Constants.IsLogin).equals("true")) {
            login_btn.visibility = View.GONE
            Toast.makeText(this, "User Logged in", Toast.LENGTH_SHORT).show()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            login_btn.visibility = View.VISIBLE
            login_btn.setOnClickListener(View.OnClickListener { signIn() })
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result =
                Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            val statusCode = result!!.status.statusCode
            println("### result is sucess : " + result.isSuccess)

            AppPreference.getInstance(this)
                .putString(Constants.IsLogin, result.isSuccess.toString())

            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null)
                updateUI(account)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        var intent = Intent(this, MainActivity::class.java)

        AppPreference.getInstance(applicationContext).putString("NAME", account!!.displayName)
        AppPreference.getInstance(applicationContext).putString("EMAIL", account!!.email)
        AppPreference.getInstance(applicationContext).putString("PHOTO_URL", account!!.photoUrl.toString())

        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (AppPreference.getInstance(this).getString(Constants.IsLogin).equals("true")) {
            login_btn.visibility = View.GONE
            Toast.makeText(this, "User Logged in", Toast.LENGTH_SHORT).show()
        } else {
            login_btn.visibility = View.VISIBLE
            login_btn.setOnClickListener(View.OnClickListener { signIn() })
        }
    }
}