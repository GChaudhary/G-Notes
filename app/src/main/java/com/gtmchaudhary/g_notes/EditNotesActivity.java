package com.gtmchaudhary.g_notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class EditNotesActivity extends AppCompatActivity {

    DatabaseHelper db;
    Note note;
    EditText noteTitle;
    EditText noteContent;

    public final String TAG = "MyActivity";
    public String flag = new String();
    public String initNoteTitle = new String();
    public String initNoteContent = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        noteTitle = (EditText)findViewById(R.id.editNote_Title);
        noteContent = (EditText)findViewById(R.id.editNote_Content);

        // Retrieve the Intent
        Intent intent = getIntent();
        flag = intent.getStringExtra("FLAG");
        initNoteTitle = intent.getStringExtra("title");
        initNoteContent = intent.getStringExtra("content");
        Log.i(TAG, "onCreate \nTitle- "+initNoteTitle+"\nContent- "+initNoteContent);

        flag = intent.getStringExtra("FLAG");
        if(flag.equals("fabPressed")){
            noteTitle.setHint("Title");
            noteContent.setHint("Note");

            noteTitle.setText(null);
            noteContent.setText(null);
        }
        else if(flag.equals("cardPressed")){
            noteTitle.setText(initNoteTitle);
            noteContent.setText(initNoteContent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_edit_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saveNote:
                // save action
                onSaveBtnPressed();
                returnToHOME();
                Log.i(TAG, "OnActionSave");
                return true;
            case R.id.action_discardNote:
                // discard found
                onDiscardBtnPressed();
                returnToHOME();
                Log.i(TAG, "OnActionDiscard");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSaveBtnPressed(){

        noteTitle = (EditText)findViewById(R.id.editNote_Title);
        noteContent = (EditText)findViewById(R.id.editNote_Content);

        if(flag.equals("fabPressed")){
            if(   (!noteTitle.getText().toString().trim().isEmpty())  ||  (!noteContent.getText().toString().trim().isEmpty())   ){
                Log.i(TAG, "fabPressed -> onSaveBtnPressed()");
                saveNoteToDB();
            }
        }
        else if(flag.equals("cardPressed")){
            if((noteTitle.getText().toString().trim().isEmpty())&&(noteContent.getText().toString().trim().isEmpty())) {
                Log.i(TAG, "cardPressed -> onSaveBtnPressed() -> discardNoteFromDB");
                discardNoteFromDB();
            }
            else {
                Log.i(TAG, "cardPressed -> onSaveBtnPressed() -> updateNoteOfDB");
                updateNoteOfDB();
            }
        }

    }

    public void onDiscardBtnPressed(){

        if(flag.equals("cardPressed")){
            Log.i(TAG, "cardPressed -> onDiscardBtnPressed()");
            discardNoteFromDB();
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        onSaveBtnPressed();
        //super.onBackPressed();
        returnToHOME();
    }

    private void saveNoteToDB() {
        // Save note to Database
        db = new DatabaseHelper(getApplicationContext());
        noteTitle = (EditText)findViewById(R.id.editNote_Title);
        noteContent = (EditText)findViewById(R.id.editNote_Content);
        note = new Note(noteTitle.getText().toString(), noteContent.getText().toString());
        db.createNote(note);
        db.closeDB();
        Log.i(TAG, "Note SAVED !..");
    }

    private void discardNoteFromDB(){
        // Discard note from Database
        db = new DatabaseHelper(getApplicationContext());
        note = new Note(initNoteTitle, initNoteContent);
        db.deleteNote(note);
        db.closeDB();
        Log.i(TAG, "Note DELETED !..");
    }


    public void updateNoteOfDB(){
        db = new DatabaseHelper(getApplicationContext());
        noteTitle = (EditText)findViewById(R.id.editNote_Title);
        noteContent = (EditText)findViewById(R.id.editNote_Content);
        note = new Note(initNoteTitle, initNoteContent);
        db.updateNote(note, noteTitle.getText().toString(),noteContent.getText().toString());
        db.closeDB();
        Log.i(TAG, "Note UPDATED !..");
    }

    public void returnToHOME(){
        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
        finish();
    }

}
