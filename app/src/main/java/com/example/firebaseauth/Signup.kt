package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup.*

class Signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        btn_signup_user.setOnClickListener {
            signUp()
        }
        btn_login_user_.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun  updateUI(currentUser:FirebaseUser?){

    }

    fun signUp(){
        if(et_email_s.text.toString().isEmpty()){
                et_email_s.error = "Please enter email"
                et_email_s.requestFocus()
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(et_email_s.text.toString()).matches()){
            et_email_s.error = "Please enter valid email"
            et_email_s.requestFocus()
        }
        if(et_password_s.text.toString().isEmpty()){
            et_password_s.error = "Please enter password"
            et_password_s.requestFocus()
        }
        auth.createUserWithEmailAndPassword(et_email_s.text.toString(), et_password_s.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user:FirebaseUser? = auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, Login::class.java))
                                finish()
                            }
                        }

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }
    }
}
