package com.aihughes.todo2;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by alexhughes on 10/23/16.
 */

public class NoteDetailActivity extends AppCompatActivity {
    //establish the variables edittext because we need to be able to type.
    private EditText noteTitle;
    private EditText noteText;
    private TextView noteCategory;
    private ImageView noteImage;
    private EditText noteDueDate;
    private Button saveButton;
    //the index variable is used to help us find the note in the array later on.
    private int index;


    @Override
    // this seems standard for each view that we create.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this is the template for this view
        setContentView(R.layout.activity_note_detail);
        // this connects the variables in this file (activity) to the visible items in the template
        noteTitle = (EditText)findViewById(R.id.note_title);
        noteText = (EditText)findViewById(R.id.note_text);
        noteCategory = (EditText)findViewById(R.id.Category_editText);
        noteImage = (ImageView)findViewById(R.id.detail_imageView);
        noteDueDate = (EditText)findViewById(R.id.txtdate);
        saveButton = (Button)findViewById(R.id.save_button);


        // because an intent was used to get here we want to use the same way to get back.
        Intent intent = getIntent();
        //this fills in the info from the main screen. if we click on an item this caries over the info.
        noteTitle.setText(intent.getStringExtra("Title"));
        noteText.setText(intent.getStringExtra("Text"));
        noteCategory.setText(intent.getStringExtra("Category").toLowerCase());
        noteDueDate.setText(intent.getStringExtra("DueDate"));
        index =  intent.getIntExtra("Index", -1);





        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //again we want to go back to the main screen by the same way (intent) we got here.
                Intent intent = getIntent();
                //this puts the updated info into the intent to go back. this is how we send updated info
                intent.putExtra("Title", noteTitle.getText().toString());
                intent.putExtra("Category", noteCategory.getText().toString().toLowerCase());
                intent.putExtra("Text", noteText.getText().toString());
                intent.putExtra("DueDate", noteDueDate.getText().toString());
                intent.putExtra("Index", index);
                //this fires the action of the intent
                setResult(RESULT_OK, intent);
                //this closes the window
                finish();
            }
        });

        EditText txtDate=(EditText)findViewById(R.id.txtdate);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    DatePick dialog = new DatePick(view);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");

                }
            }
        });

    }
}
