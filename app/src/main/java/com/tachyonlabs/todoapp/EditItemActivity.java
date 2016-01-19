package com.tachyonlabs.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    int itemBeingEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // put the item text into the EditText widget
        EditText editItem = (EditText) findViewById(R.id.etEditItem);
        String textToEdit = getIntent().getStringExtra("todoItemText");
        itemBeingEdited = getIntent().getIntExtra("whichItem", 0);
        editItem.setText(textToEdit);
        // set the cursor to the end of the current text value
        editItem.setSelection(textToEdit.length());

//        Commenting out the pink email button
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void saveEditedItem(View view) {
        EditText editItem = (EditText) findViewById(R.id.etEditItem);
        Intent data = new Intent();
        // send the edited text (and the index of the edited item) back to the main activity
        data.putExtra("todoItemText", editItem.getText().toString());
        data.putExtra("whichItem", itemBeingEdited);
        setResult(RESULT_OK, data); // set result code and bundle data for response
        this.finish();
    }
}
