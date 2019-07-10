package com.example.firstfirebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class Signup : AppCompatActivity() {
    var  auth:FirebaseAuth? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
            auth = FirebaseAuth.getInstance()
        SignupButton.setOnClickListener{
            register()

        }
    }
    private fun register(){
        val email = SignupEmail.text.toString()
        val password = SignupPassword.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            progressSignup.visibility = View.VISIBLE
            auth?.createUserWithEmailAndPassword(email,password)?.addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    progressSignup.visibility = View.GONE
                    sendVerficationMail()
                }else{

                    Toast.makeText(this,task.exception.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }
        else {
            Toast.makeText(this,"Email or password is Empty",Toast.LENGTH_LONG).show()
        }
    }

    private fun sendVerficationMail (){
        var user = auth?.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener{
            if (it.isSuccessful){
                var intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,"Error with Verify Aucont",Toast.LENGTH_LONG).show()

            }
        }

    }
}
