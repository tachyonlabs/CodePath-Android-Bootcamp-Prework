package com.tachyonlabs.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tachyonlabs.todoapp.R;
import com.tachyonlabs.todoapp.adapters.ItemsAdapter;
import com.tachyonlabs.todoapp.models.Item;
import com.tachyonlabs.todoapp.utils.TodoItemsDatabaseHelper;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<Item> todoItems;
    ItemsAdapter aTodoAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Here", "here");
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aTodoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // long-clicking an item deletes it
                // display a congratulations toast for completing a todolist item
                aToastToYou();
                // delete it from the database
                deleteItem(todoItems.get(position));
                // and from the ArrayList/screen
                todoItems.remove(position);
                aTodoAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // clicking an item brings up an editing screen
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("addOrEdit", "Edit");
                intent.putExtra("todoItemText", todoItems.get(position).text);
                intent.putExtra("whichItem", position);
                intent.putExtra("todoItemDate", todoItems.get(position).date);

                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("addOrEdit", "Add");
                intent.putExtra("todoItemText", "");
                intent.putExtra("todoItemDate", "");
                intent.putExtra("whichItem", -1);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    public void populateArrayItems() {
        readItems();
        aTodoAdapter = new ItemsAdapter(this, todoItems);
    }

    private void readItems() {
        // read all the items from the database
        todoItems = new ArrayList<Item>();
        TodoItemsDatabaseHelper todoDB = TodoItemsDatabaseHelper.getInstance(this);
        for (Item item : todoDB.getAllItems()) {
            todoItems.add(item);
        }
    }

    private void addItem(Item item) {
        // add a new item to the database
        TodoItemsDatabaseHelper todoDB = TodoItemsDatabaseHelper.getInstance(this);
        todoDB.addItem(item);
    }

    private void updateItem(Item item, String oldText) {
        // update an item in the database
        TodoItemsDatabaseHelper todoDB = TodoItemsDatabaseHelper.getInstance(this);
        todoDB.updateItem(item, oldText);
    }

    private void deleteItem(Item item) {
        // delete an item from the database
        TodoItemsDatabaseHelper todoDB = TodoItemsDatabaseHelper.getInstance(this);
        todoDB.deleteItem(item);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Item item = new Item();
            // Get the new or edited date
            item.date = data.getExtras().getString("todoItemDate");
            // and which item was being edited (or -1 to indicate a new item being added)
            int whichItem = data.getExtras().getInt("whichItem", 0);

            if (whichItem != -1) {
                // if we're editing an existing item rather than adding a new one
                // get the old text
                String oldText = todoItems.get(whichItem).text;
                // and the edited text
                item.text = data.getExtras().getString("todoItemText");
                // update the listview
                todoItems.set(whichItem, item);
                // notify the adapter that there's been a change
                aTodoAdapter.notifyDataSetChanged();
                // and persist the change by updating the database
                updateItem(item, oldText);
                // Toast the edited text to display temporarily on screen
                Toast.makeText(this, item.text, Toast.LENGTH_SHORT).show();
            } else {
                // or if it's a new item, add it
                item.text = data.getExtras().getString("todoItemText");
                aTodoAdapter.add(item);
                addItem(item);
            }
        }
    }

    public void checkBoxClick(View view) {
        // I'm sure this is not the best way to do this, but I've been having a frustrating time
        // trying to figure out how to detect checkboxes being checked as a listview event, and
        // came up with this alternate method which is at least simple and straightforward

        // get the checkbox
        final CheckBox checkBox = (CheckBox) view;
        // and get its todoitem
        RelativeLayout relativeLayout = (RelativeLayout) checkBox.getParent();
        final TextView textview = (TextView) relativeLayout.getChildAt(1);

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
                "All right!", "You did it!", "Good job!", "Awesome!"};

        Toast.makeText(this, congratsMessages[rand.nextInt(congratsMessages.length)], Toast.LENGTH_SHORT).show();
    }
}
