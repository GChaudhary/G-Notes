package com.gtmchaudhary.g_notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by gtmchaudhary on 9/27/2016.
 */
public class rcAdapter extends RecyclerView.Adapter<rcAdapter.ViewHolder> {

    public static final String TAG = "MyActivity";

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        public CardView cardView;
        public TextView noteTitle;
        public TextView noteContent;
        private ItemClickListener clickListener;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView)v.findViewById(R.id.cardView);
            noteTitle = (TextView)v.findViewById(R.id.noteTitle);
            noteContent = (TextView)v.findViewById(R.id.noteContent);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), false);
        }
        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }

    public Context mContext;
    ArrayList<Note> notes = new ArrayList<Note>();
    public rcAdapter(Context mContext, ArrayList<Note> notes) {
        this.mContext = mContext;
        //get handle of databaseCursor
        this.notes = notes;
    }

    @Override
    public rcAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_note, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.noteTitle.setText(notes.get(position).getNoteTitle());
        holder.noteContent.setText(notes.get(position).getNoteContent());

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(final View view, final int position, boolean isLongClick) {
                if (isLongClick) {


                    //********** LongClick ***********//
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Delete this note?")
                            .setCancelable(true)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Delete from DATABASE
                                    DatabaseHelper db = new DatabaseHelper(mContext);
                                    TextView noteTitle = (TextView)view.findViewById(R.id.noteTitle);
                                    TextView noteContent = (TextView)view.findViewById(R.id.noteContent);
                                    Note toBeDeletedNote = new Note(noteTitle.getText().toString(), noteContent.getText().toString());
                                    db.deleteNote(toBeDeletedNote);

                                    //Delete from ArrayList and Refresh the list
                                    notes.remove(position);
                                    notifyDataSetChanged();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {

                    //********** SingleClick ***********//

                    TextView noteTitle = (TextView)view.findViewById(R.id.noteTitle);
                    TextView noteContent = (TextView)view.findViewById(R.id.noteContent);

                    Intent intent = new Intent(mContext, EditNotesActivity.class);
                    intent.putExtra("title",noteTitle.getText().toString());
                    intent.putExtra("content",noteContent.getText().toString());
                    intent.putExtra("FLAG","cardPressed");

                    mContext.startActivity(intent);
                    Log.i("TAG", "editNote activity launched");
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notes.size();/*get number of entries in DataBase*/
    }
}