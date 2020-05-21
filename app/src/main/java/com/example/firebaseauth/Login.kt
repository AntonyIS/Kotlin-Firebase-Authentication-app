package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup.*

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        btn_signup_user.setOnClickListener {
            login()
        }

    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    fun login(){
        if (et_email_s.text.trim().toString().isEmpty()){
            et_email_s.error = "Please enter email"
            et_email_s.requestFocus()
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(et_email_s.text.trim().toString()).matches()){
            et_email_s.error = "Please enter valid email"
            et_email_s.requestFocus()
        }
        if (et_password_s.text.trim().toString().isEmpty()){
            et_password_s.error = "Please enter email"
            et_password_s.requestFocus()
        }

        auth.signInWithEmailAndPassword(et_email_s.text.trim().toString(), et_password_s.text.trim().toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)

                }

            }

    }

    private fun updateUI(currentUser:FirebaseUser?){
        if (currentUser != null){
            if(currentUser.isEmailVerified){
                startActivity(Intent(this, Dashboard::class.java))
                finish()
            }else{
                Toast.makeText(baseContext, "Please verify you email address.", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(baseContext, "Login failed.", Toast.LENGTH_SHORT).show()
        }
    }
}
