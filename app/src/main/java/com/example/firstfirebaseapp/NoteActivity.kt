package com.example.firstfirebaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        ListItemNOteTitle.text = intent.extras.getString("Title_Key")
        ListItemNOteBody.text = intent.extras.getString("Note_Key")
    }
}
