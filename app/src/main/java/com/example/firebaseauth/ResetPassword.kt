package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        auth = FirebaseAuth.getInstance()
        btn_reset_password.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword(){
        if (et_current_password.text.trim().toString().isNotEmpty() &&
            et_new_password.text.trim().toString().isNotEmpty() &&
            et_confirm_password.text.trim().toString().isNotEmpty()){

            if(et_current_password.text.trim().toString().equals(et_confirm_password.text.trim().toString()) ){
                val user:FirebaseUser? = auth.currentUser
                if(user != null && user.email != null){
                    val credential = EmailAuthProvider.getCredential(user.email!!,et_current_password.text.trim().toString())
                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(baseContext, "Re-Authentication successful.", Toast.LENGTH_SHORT).show()
                            user!!.updatePassword(et_new_password.text.trim().toString())
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        user!!.updatePassword(et_new_password.text.trim().toString()).addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Toast.makeText(baseContext, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                                    auth.signOut()
                                                    startActivity(Intent(this, Login::class.java))
                                                    finish()
                                                }
                                            }
                                    }
                                }

                        }else{
                            Toast.makeText(baseContext, "Re-Authentication unsuccessful.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }else{
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }

            }


        }else{
            Toast.makeText(baseContext, "Please all the failed.", Toast.LENGTH_SHORT).show()
        }
    }
}
