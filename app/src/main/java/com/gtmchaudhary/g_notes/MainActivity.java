package com.gtmchaudhary.g_notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    //SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView emptyListDisplayTextbox = (TextView)findViewById(R.id.emptyWindowText);

        //Data Source
        final DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<Note> notes = new ArrayList<Note>();
        notes = db.getAllNotes();

        //Instantiate RC-View
        RecyclerView rcView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcView.setLayoutManager(linearLayoutManager);

        //Adapter
        final rcAdapter myAdapter = new rcAdapter(this, notes);
        rcView.setAdapter(myAdapter);


        /************* Swipe-Down-To-Refresh ***************
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        myAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        //refreshNotes();
                    }
                }
        );
*/
        /***************Floating Action Button**************/
            FloatingActionButton myFab = (FloatingActionButton)findViewById(R.id.fab);
            myFab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                            Log.i(TAG, "fabPressed");
                            //Perform action on click
                            Intent intent = new Intent(getApplicationContext(), EditNotesActivity.class);
                        /*Initialize the Default values of UI elements of next activity*/
                            intent.putExtra("title", "Title");
                            intent.putExtra("content", "Note");
                            intent.putExtra("FLAG","fabPressed");

                            startActivity(intent);
                    }
                });

        /**********Toggling between textbox and recyclerView**********/
            if((myAdapter.getItemCount()) == 0){
                emptyListDisplayTextbox.setText("Nothing to show!");
                emptyListDisplayTextbox.setVisibility(View.VISIBLE);
                rcView.setVisibility(View.INVISIBLE);
            }
            else{
                emptyListDisplayTextbox.setVisibility(View.INVISIBLE);
                rcView.setVisibility(View.VISIBLE);
            }


        /************ Swipe Cards to Dismiss note *************/
        final ArrayList<Note> finalNotes = notes;
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(rcView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    View v = recyclerView.getLayoutManager().findViewByPosition(position);
                                    //Delete from DATABASE
                                    TextView noteTitle = (TextView)v.findViewById(R.id.noteTitle);
                                    TextView noteContent = (TextView)v.findViewById(R.id.noteContent);
                                    Note toBeDeletedNote = new Note(noteTitle.getText().toString(), noteContent.getText().toString());
                                    db.deleteNote(toBeDeletedNote);

                                    //Delete from ArrayList and Refresh the list
                                    finalNotes.remove(position);
                                }
                                myAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    View v = recyclerView.getLayoutManager().findViewByPosition(position);
                                    //Delete from DATABASE
                                    TextView noteTitle = (TextView)v.findViewById(R.id.noteTitle);
                                    TextView noteContent = (TextView)v.findViewById(R.id.noteContent);
                                    Note toBeDeletedNote = new Note(noteTitle.getText().toString(), noteContent.getText().toString());
                                    db.deleteNote(toBeDeletedNote);

                                    //Delete from ArrayList and Refresh the list
                                    finalNotes.remove(position);
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                        });
        rcView.addOnItemTouchListener(swipeTouchListener);


        db.closeDB();
    }
}
