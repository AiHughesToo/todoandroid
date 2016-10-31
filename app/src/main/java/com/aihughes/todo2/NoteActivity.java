package com.aihughes.todo2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class NoteActivity extends AppCompatActivity {

    // creates a variable named noteslist that is a type known as a list-view
    private ListView notesList;
    // this establishes the notes array adapter and other variables
    private NotesArrayAdapter notesArrayAdapter;
    private ArrayList<Note> notesArray;
    private SharedPreferences notesPrefs;
    private int catNumber;
    String filename = "TodoItemsFile";
    Gson gson = new Gson();
    List<Note> noteLists = new ArrayList<>();
    //new Way vars
    private List<Category> categories = new ArrayList<>();
    // Stores categories + notes to pass to our custom adapter
    private ArrayList<Object> allItems = new ArrayList<>();
    // Custom adapter to display categories and notes to list view
    private CategoryAdapter categoryAdapter;
    //use this to set the position of the item we have clicked on so we can ref it later for deletion,
    private int oldNotePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // this is part of the first run feature  that uses the preferences
        notesPrefs = getPreferences(Context.MODE_PRIVATE);
        gson = new Gson();
        // this calls the method setupNotes
        setupNotes();

        // this line ties the variable to the actual view on the screen (Activity)
        notesList = (ListView)findViewById(R.id.list_view);
        updateAllItems();

        // Set our custom adapter to handle our notesList
        categoryAdapter = new CategoryAdapter(this, allItems);
        Collections.sort(noteLists);
        notesList.setAdapter(categoryAdapter);
        Collections.sort(noteLists);

        // this turns the view into a clickable element
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // if the element is clicked create the intent
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //this selects the note from the array at the position established by the variable named position.
                allItems.get(position);
                oldNotePosition = position;
                Note note = (Note) allItems.get(position);


                //create a new intent named intent  from NotesActivity to NoteDetailActivity. this opens the wormhole between the two.
                Intent intent = new Intent(NoteActivity.this, NoteDetailActivity.class);
                //this sends the information of the title through the wormhole. we tell the other side that the next bit of information is labeled Title and we GET the actual value
                intent.putExtra("Title", note.getTitle());
                //this sends the information of the text through the wormhole. we tell the other side that the next bit of information is labeled text and we GET the actual value
                intent.putExtra("Text", note.getText());
                intent.putExtra("Category", note.getCategory());
                intent.putExtra("DueDate", note.getDueDate());
                intent.putExtra("catID", note.getCatID());
                // we also send the index position. we will use this later.
                intent.putExtra("Index", position);

                //this actually fires the intent causing the wormhole to open and push the info
                startActivityForResult(intent, 1);

            }
        });

        //this is for the delete method. it sets the option for a long click and what to do if a long click happens.
        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //what to do if the view gets a long click
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //builds an alert box
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NoteActivity.this);
                // gives this box a title & message. the buttons are also established.
                alertBuilder.setTitle("Delete");
                alertBuilder.setMessage("You sure?");
                // if user clicks cancel then do nothing
                alertBuilder.setNegativeButton("Cancel", null);
                //if user clicks the delete then do some stuff
                alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Note note = (Note) allItems.get(position);

                        switch (note.getCategory()){
                            case "home":
                                catNumber = 0;
                                break;
                            case "work":
                                catNumber = 1;
                                break;
                            case "personal":
                                catNumber = 2;
                                break;
                            default:
                                catNumber = 3;
                                break;

                        }
                        categories.get(catNumber).notes.remove(note);
                        allItems.remove(position);
                        deleteFile(note.getTitle());
                        writeTodos();

                        updateAllItems();
                        categoryAdapter.notifyDataSetChanged();
                        writeTodos();

                    }
                });
                alertBuilder.create().show();
                return true;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this, NoteDetailActivity.class);
                intent.putExtra("Title", "");
                intent.putExtra("Text", "");
                intent.putExtra("Category", "");
                intent.putExtra("DueDate", "");
                intent.putExtra("catId", "");
                startActivityForResult(intent, 1);

            }

        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            int index = data.getIntExtra("Index", -1);

            Note note = new Note(data.getStringExtra("Title"),
                    data.getStringExtra("Text"), new Date(), data.getStringExtra("Category"),
                    data.getStringExtra("image_bg_src"), data.getStringExtra("DueDate"), data.getStringExtra("catID"));

            if (index == -1){
                switch (data.getStringExtra("Category")){
                    case "home":
                        catNumber = 0;
                        break;
                    case "work":
                        catNumber = 1;
                        break;
                    case "personal":
                        catNumber = 2;
                        break;
                    default:
                        catNumber = 3;
                        break;

                }
                categories.get(catNumber).notes.add(note);
                writeTodos();
                updateAllItems();
                categoryAdapter.notifyDataSetChanged();
            }else{
                Note oldnote = (Note) allItems.get(oldNotePosition);
                switch (note.getCategory()){
                    case "home":
                        catNumber = 0;
                        break;
                    case "work":
                        catNumber = 1;
                        break;
                    case "personal":
                        catNumber = 2;
                        break;
                    default:
                        catNumber = 3;
                        break;

                }
                categories.get(catNumber).notes.remove(oldnote);
                allItems.remove(oldNotePosition);
                deleteFile(oldnote.getTitle());
                writeTodos();

                updateAllItems();
                categoryAdapter.notifyDataSetChanged();

                switch (data.getStringExtra("Category")){
                    case "home":
                        catNumber = 0;
                        break;
                    case "work":
                        catNumber = 1;
                        break;
                    case "personal":
                        catNumber = 2;
                        break;
                    default:
                        catNumber = 3;
                        break;

                }

                categories.get(catNumber).notes.add(note);
               updateAllItems();
                categoryAdapter.notifyDataSetChanged();

            }

            writeTodos();


        }

    }

    private void setupNotes() {
        notesArray = new ArrayList<>();

        File filesDir = this.getFilesDir();
        File todoFile = new File(filesDir + File.separator + filename);
        if (todoFile.exists()){
            readTodos(todoFile);

        }else {
            //new way
            categories.add(new Category("Home", new ArrayList<Note>()));
            categories.add(new Category("Work", new ArrayList<Note>()));
            categories.add(new Category("Personal", new ArrayList<Note>()));
            categories.add(new Category("Other", new ArrayList<Note>()));


            categories.get(0).notes.add(new Note("Welcome", "You can enter new todo list items in one of 3 categories or you have the option of adding your own category. Press and hold a note to get option for deletion.", new Date(), "home", "mipmap-hdpi/ic_launcher.png", "1/1/1999", ""));

            writeTodos();
        }

    }

    private void readTodos(File todoFile) {
        FileInputStream inputStream = null;
        String todosText = "";
        try {
            inputStream = openFileInput(todoFile.getName());
            byte[] input = new byte[inputStream.available()];
            while (inputStream.read(input) != -1) {}
            todosText = new String(input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            Type collectionType = new TypeToken<List<Category>>(){}.getType();
            List<Category> categoryList = gson.fromJson(todosText, collectionType);
            categories = new LinkedList(categoryList);


        }
    }

    private void writeTodos() {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);

            String json = gson.toJson(categories);
            byte[] bytes = json.getBytes();
            outputStream.write(bytes);

            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception ignored) {}
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //no inspection Simple if statement
        if (id == R.id.action_settings) {

            Toast.makeText(getApplicationContext(), " You have died of dysentery.",
                    Toast.LENGTH_SHORT).show();
            return true;
        }else if (id == R.id.action_personal){

            Toast.makeText(getApplicationContext(), " You have died of dysentery.",
                    Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateAllItems() {
        allItems.clear();
        for(int i = 0; i < categories.size(); i++) {
            allItems.add(categories.get(i).getName());
            for(int j = 0; j < categories.get(i).notes.size(); j++) {
                allItems.add(categories.get(i).notes.get(j));
            }
        }
    }
}
