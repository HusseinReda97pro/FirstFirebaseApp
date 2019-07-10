package com.example.firstfirebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*

class Login : AppCompatActivity() {
    var auth:FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        LoginButton.setOnClickListener{


        }


        GoToSignupPage.setOnClickListener{
            var intent = Intent(this, Signup::class.java)
            startActivity(intent)

        }
    }
    private fun login(){
        var email    = LoginEmail.text.toString()
        var password = LoginPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            progressLogin.visibility = View.VISIBLE
            auth?.signInWithEmailAndPassword(email,password)?.addOnCompleteListener(this){task->

                if(task.isSuccessful){
                    progressLogin.visibility = View.GONE
                    verifyEmail()

                }else{
                    Toast.makeText(this,task.exception.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }
        else {
            Toast.makeText(this,"Email or password is Empty",Toast.LENGTH_LONG).show()
        }
    }
    private fun verifyEmail(){
        var user = auth?.currentUser
        if(user!!.isEmailVerified){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this,"Verify your Aucont",Toast.LENGTH_LONG).show()

        }
    }
}
