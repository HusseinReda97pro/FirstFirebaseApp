package com.example.firstfirebaseapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_note_model.view.*
import kotlinx.android.synthetic.main.edit_note_model.*
import kotlinx.android.synthetic.main.edit_note_model.view.*
import java.security.Key
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var NoteList:ArrayList<Note>? = null
    var mRef:DatabaseReference? = null
    var auth:FirebaseAuth? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var database:FirebaseDatabase= FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        mRef = database.getReference("Notes")
        NoteList = ArrayList()
        addButton.setOnClickListener{
            showDialogAddNote()
        }

        NoteListView.onItemClickListener = AdapterView.OnItemClickListener{parent,view,position,id ->
                var note = NoteList?.get(position)!!
                var intent =  Intent(this,NoteActivity::class.java)
                intent.putExtra("Title_Key",note.title)
                intent.putExtra("Note_Key",note.note)
                startActivity(intent)
        }

        NoteListView.onItemLongClickListener =  AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val alertBuilder = AlertDialog.Builder(this)
            val myView =layoutInflater.inflate(R.layout.edit_note_model,null)
            alertBuilder.setView(myView)
            val alertDialog= alertBuilder.create()
            alertDialog.show()

            var note = NoteList?.get(position)!!
            myView.title_update.setText(note.title)
            myView.note_update.setText(note.note)
            myView.UpdateNote.setOnClickListener{
                var childRef = mRef?.child(note.id.toString())
                val updatedNote = Note(note.id!!,myView.title_update.text.toString(),myView.title_update.text.toString()
                    ,getCurrentDate())
                childRef?.setValue(updatedNote)
                alertDialog.dismiss()
            }
            myView.DeleteNote.setOnClickListener{
                var childRef = mRef?.child(note.id.toString())?.removeValue()
                alertDialog.dismiss()

            }
               true
        }



    }
    override fun onStart(){
        super.onStart()
        if (auth?.currentUser == null){
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }
        mRef?.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                NoteList?.clear()
                for(note in p0?.children){
                    var myNote = note.getValue(Note::class.java)
                    NoteList!!.add(0,myNote!!)
                }
                val noteAdapter = NoteAdapter(applicationContext,NoteList!!)
                NoteListView.adapter = noteAdapter

            }


        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var id = item?.itemId
        if (id == R.id.Logout){
            FirebaseAuth.getInstance().signOut()
            var intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        return true
    }
    fun showDialogAddNote(){
        val alertBuilder = AlertDialog.Builder(this)
        val view =layoutInflater.inflate(R.layout.add_note_model,null)
        alertBuilder.setView(view)
        val alertDialog= alertBuilder.create()
        alertDialog.show()
        // to save data in firebase
        view.AddNote.setOnClickListener{
            val title = view.title_edit_text.text.toString()
            val note = view.note_edit_text.text.toString()
            if(title.isNotEmpty() && note.isNotEmpty()){
                var id =mRef!!. push().key.toString()
                var myNote = Note(id,title,note,getCurrentDate())
                mRef!!.child(id).setValue(myNote)
                alertDialog.dismiss()
            }else{
                Toast.makeText(this,"Emity!!",Toast.LENGTH_LONG).show()
            }
        }

    }
    fun getCurrentDate():String{
        val calender = Calendar.getInstance()
        val mdFormat = SimpleDateFormat("EEEE hh:mm a ")
        val strDate =mdFormat.format(calender.time)
        return  strDate
    }
}