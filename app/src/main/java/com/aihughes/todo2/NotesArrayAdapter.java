package com.aihughes.todo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by alexhughes on 10/23/16.
 */

// this is a custom adapter the <note> here makes the array use the note class to construct the array.
public class NotesArrayAdapter extends ArrayAdapter<Note> {
    private int resource;
    // this establishes the array notes
    private ArrayList<Note> notes;
    // this will be used to inflate the note into the main screen
    private LayoutInflater inflater;
    // this will be used to format our date
    private SimpleDateFormat formatter;

    public NotesArrayAdapter(Context context, int resource, ArrayList<Note> objects)
    {
        //im not sure why we are using resource here
        super(context, resource, objects);
        this.resource = resource;
        this.notes = objects;

        //
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //here is where we actually give the date a format. we can change it to include the hour.
        formatter = new SimpleDateFormat("MM/dd/yyyy");


    }
    @Override
    //this is the get view method that we call somewhere need to find out where.
    public View getView(int position, View convertView, ViewGroup parent) {
        View notesRow = inflater.inflate(resource, parent, false);
        // here we tie the variables to the visible items in the template (resource file) by the id of the view.
        TextView noteTitle = (TextView)notesRow.findViewById(R.id.note_title);
        TextView noteText = (TextView)notesRow.findViewById(R.id.note_text);
        TextView noteDate = (TextView)notesRow.findViewById(R.id.note_date);
        //TextView noteCategory = (TextView)notesRow.findViewById(R.id.category_textView);
        TextView noteDueDate = (TextView)notesRow.findViewById(R.id.due_date);
        // this gets a note from the array and makes it available for the view.
        //NoteClass noteInstance = notes(array) from a position yet to be determined.
        Note note = notes.get(position);
        // this is the method get title on line 20 of the note java file.
        noteTitle.setText(note.getTitle());
        noteText.setText(note.getText());
       // noteCategory.setText(note.getCategory());
        noteDate.setText(formatter.format(note.getDate()));
        noteDueDate.setText(note.getDueDate());

        return notesRow;
    }


    //this is important to change the information in the array after an edit.
    public void updateAdapter(ArrayList<Note> notes){
        this.notes = notes;
        super.notifyDataSetChanged();
    }
}

