package com.tachyonlabs.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.tachyonlabs.todoapp.R;

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
        EditText editDate = (EditText) findViewById(R.id.etEditDate);
        setTitle("Simple Todo - " + getIntent().getStringExtra("addOrEdit") + " Item");
        String textToEdit = getIntent().getStringExtra("todoItemText");
        String dateToEdit = getIntent().getStringExtra("todoItemDate");
        itemBeingEdited = getIntent().getIntExtra("whichItem", 0);
        editItem.setText(textToEdit);
        editDate.setText(dateToEdit);
        // set the cursor to the end of the current text value
        editItem.setSelection(textToEdit.length());

    }

    public void saveEditedItem(View view) {
        EditText editItem = (EditText) findViewById(R.id.etEditItem);
        EditText editDate = (EditText) findViewById(R.id.etEditDate);
        Intent data = new Intent();
        // send the edited text (and the index of the edited item) back to the main activity
        data.putExtra("todoItemText", editItem.getText().toString());
        data.putExtra("todoItemDate", editDate.getText().toString());
        data.putExtra("whichItem", itemBeingEdited);
        setResult(RESULT_OK, data); // set result code and bundle data for response
        this.finish();
    }
}
