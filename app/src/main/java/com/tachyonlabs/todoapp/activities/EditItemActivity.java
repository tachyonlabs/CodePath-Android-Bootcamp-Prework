package com.tachyonlabs.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
        setTitle("Simple Todo - " + getIntent().getStringExtra("addOrEdit") + " Item");
        String textToEdit = getIntent().getStringExtra("todoItemText");
        editItem.setText(textToEdit);
        // set the cursor to the end of the current text value
        editItem.setSelection(textToEdit.length());
        // set the DatePicker to the previously selected date if there is one
        DatePicker datePicker = (DatePicker) findViewById(R.id.dpDatePicker);
        String dateToEdit = getIntent().getStringExtra("todoItemDate");

        if (dateToEdit.equals("No due date")) {
            RadioButton noDueDate = (RadioButton) findViewById(R.id.rdbNoDueDate);
            noDueDate.setChecked(true);
            datePicker.setVisibility(View.GONE);
        } else {
            RadioButton selectDueDate = (RadioButton) findViewById(R.id.rdbSelectDueDate);
            selectDueDate.setChecked(true);
            String[] dateParts = dateToEdit.split("-");
            datePicker.updateDate(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]) - 1, Integer.parseInt(dateParts[2]));
        }
        itemBeingEdited = getIntent().getIntExtra("whichItem", 0);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rdgDueDate);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // make the datepicker appear and disappear depending on whether they click
                // the "No due date" or "Select due date" radio buttons
                DatePicker datePicker = (DatePicker) findViewById(R.id.dpDatePicker);
                RadioButton radioButton = (RadioButton)findViewById(checkedId);
                if (radioButton.getText().toString().equals("No due date")) {
                    datePicker.setVisibility(View.GONE);
                } else {
                    datePicker.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void saveEditedItem(View view) {
        EditText editItem = (EditText) findViewById(R.id.etEditItem);
        DatePicker datePicker = (DatePicker) findViewById(R.id.dpDatePicker);
        RadioButton noDueDate = (RadioButton) findViewById(R.id.rdbNoDueDate);
        Intent data = new Intent();
        // send the edited text (and the index of the edited item) back to the main activity
        data.putExtra("todoItemText", editItem.getText().toString());
        // and the date (or lack of date)
        if (noDueDate.isChecked()) {
            data.putExtra("todoItemDate", "No due date");
        } else {
            // get the date from the datepicker, with leading zeroes for the month and day if necessary
            data.putExtra("todoItemDate", String.valueOf(datePicker.getYear()) + "-" + String.format("%02d", datePicker.getMonth() + 1) + "-" + String.format("%02d", datePicker.getDayOfMonth()));
        }
        data.putExtra("whichItem", itemBeingEdited);
        setResult(RESULT_OK, data); // set result code and bundle data for response
        this.finish();
    }

}
