package com.gtmchaudhary.g_notes;

/**
 * Created by gtmchaudhary on 9/28/2016.
 */
public class Note {
    public int id;
    public String noteTitle;
    public String noteContent;

    public Note(){
    }

    public Note(String noteTitle, String noteContent) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle_db(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent_db(String noteContent) {
        this.noteContent = noteContent;
    }
}
