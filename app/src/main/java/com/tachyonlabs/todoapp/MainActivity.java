package com.tachyonlabs.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> todoItems;
    ArrayAdapter<String> aTodoAdapter;
    ListView lvItems;
    EditText etEditText;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aTodoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // display a congratulations toast for completing a todolist item
                aToastToYou();
                // long-clicking an item deletes it
                todoItems.remove(position);
                aTodoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // clicking an item brings up an editing screen
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("todoItemText", todoItems.get(position));
                intent.putExtra("whichItem", position);
                startActivityForResult(intent, REQUEST_CODE);
            }

        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    public void populateArrayItems() {
        readItems();
        // using my own checkbox and textview layout instead of android.R.layout.simple_list_item_1
        aTodoAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.tvTodoItem, todoItems);
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            // if todo.txt doesn't already exist
            todoItems = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        aTodoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Get the edited text
            String editedItemText = data.getExtras().getString("todoItemText");
            // and which item was being edited
            int whichItem = data.getExtras().getInt("whichItem", 0);
            // replace that item's text with the edited text
            todoItems.set(whichItem, editedItemText);
            // notify the adapter that there's been a change
            aTodoAdapter.notifyDataSetChanged();
            // and persist the change by writing the items to the file
            writeItems();
            // Toast the name to display temporarily on screen
            Toast.makeText(this, editedItemText, Toast.LENGTH_SHORT).show();
        }
    }

    public void checkBoxClick(View view) {
        // I'm sure this is not the best way to do this, but I've been having a frustrating time
        // trying to figure out how to detect checkboxes being checked as a listview event, and
        // came up with this alternate method which is at least simple and straightforward

        // get the checkbox
        final CheckBox checkBox = (CheckBox) view;
        // and get its todoitem
        LinearLayout linearLayout = (LinearLayout) checkBox.getParent();
        final TextView textview = (TextView) linearLayout.getChildAt(1);

        // Wait half a second so they can actually see the check appear before the todoitem is deleted
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // uncheck the checkbox so the check doesn't get propagated to the
                // next todoitem upon deletion
                checkBox.setChecked(false);
                // and delete the todoitem by programmatically long-clicking
                textview.performLongClick();
            }
        }, 500);
    }

    public void aToastToYou() {
        // displays one of a number of congratulations toasts
        Random rand = new Random();
        String[] congratsMessages = {"Congratulations!", "Great!", "Good work!", "Nice one!",
                "Way to go!", "Another one bites the dust!", "Getting things done!", "Yay!",
                "All right!", "You did it!", "Good job!"};

        Toast.makeText(this, congratsMessages[rand.nextInt(congratsMessages.length)], Toast.LENGTH_SHORT).show();
    }
}
