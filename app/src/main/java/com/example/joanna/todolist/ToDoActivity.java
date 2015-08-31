package com.example.joanna.todolist;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ToDoActivity extends AppCompatActivity {
    ArrayList<ToDoItem> items;
  //  ArrayList<String> itemStrings;

    ToDoItemAdapter myAdapter;

    ArrayAdapter<String> itemStringsAdapter;
    ListView lvItems;
    EditText etDueDate;
    ToDoItemDatabase db;

    static final String TODO_ITEM_DETAIL_KEY = "ToDoItemDetailKey";
    static final String TODO_POSITION_KEY = "ToDoPosition";
    static final String TODO_ITEM_OBJECT_KEY = "ToDoItemObjectKey";
    static final String TAG = "ToDoActivity.java";

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        // get singleton instance of database
        db = ToDoItemDatabase.getInstance(this);
      //  db.deleteAllToDoItems();
        readItems();

        lvItems = (ListView)findViewById(R.id.lvItems);
        etDueDate = (EditText)findViewById(R.id.etToDoDate);
        myAdapter = new ToDoItemAdapter(this, items);
        lvItems.setAdapter(myAdapter);

        /**itemStringsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, itemStrings);
        lvItems.setAdapter(itemStringsAdapter);**/

        //Show action bar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setupListViewListener();

    }


    private void setupListViewListener() {
        // Long click is used to delete an item from the list
        lvItems.setOnItemLongClickListener(
                // onClickListener is an interface that only has 1 method, has to be overridden
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        db.deleteToDoItem(items.get(pos).getItem());
                        // itemStrings.remove(pos);
                        items.remove(pos);
                        //itemStringsAdapter.notifyDataSetChanged();
                        myAdapter.notifyDataSetChanged();
                        //    writeItems();

                        return true;
                    }
                }
        );

        // Click is used to enter edit activity
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // send data to edit item activity using Intent.putExtra(key, value);
                        Intent i = new Intent(ToDoActivity.this, EditItemActivity.class);
                        i.putExtra(TODO_POSITION_KEY, position);
                        //i.putExtra(TODO_ITEM_DETAIL_KEY, (String) lvItems.getItemAtPosition(position));
                        i.putExtra(TODO_ITEM_DETAIL_KEY, items.get(position).getItem());
                        i.putExtra(TODO_ITEM_OBJECT_KEY, items.get(position).getId());
                        // delete item and re-add edited text item instead of update
                        //db.deleteToDoItem((String) lvItems.getItemAtPosition(position));
                        db.deleteToDoItem(items.get(position).getItem());
                        startActivityForResult(i, REQUEST_CODE);
                    }
                }
        );
    }

    protected void onAddItem(View v) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (!itemText.isEmpty()) { //don't allow empty todoitems
            //   itemStringsAdapter.add(itemText);
            etNewItem.setText("");

            ToDoItem newItem = new ToDoItem(itemText, null, 0);
            long primaryKey = writeItem(newItem);
            newItem.setId(primaryKey);
            newItem.setDueDate("08/29/2015");
            //items.add(newItem);
            myAdapter.add(newItem);
        }

    }

    private void readItems() {
        try {
            items = db.getAllToDoItems();
            //itemStrings = new ArrayList<String>();
            /**for (ToDoItem tdi : items ) {
                //itemStrings.add(tdi.getItem());
                items.add(tdi);
            }**/
        } catch (Exception e) {
            //itemStrings = new ArrayList<String>();
            items = new ArrayList<ToDoItem>();
        }

    }

    private long writeItem(ToDoItem tdi) {
        long key;
        try {
            key = db.addOrUpdateToDoItem(tdi);
        } catch (Exception e) {
            e.printStackTrace();
            key = -1;
        }
        return key;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedText = data.getExtras().getString(TODO_ITEM_DETAIL_KEY);
            int itemPosition = data.getExtras().getInt(TODO_POSITION_KEY);
    //        ToDoItem tdi = (ToDoItem)data.getSerializableExtra(TODO_ITEM_OBJECT_KEY);
            ToDoItem tdi = items.get(itemPosition);
            tdi.setItem(editedText);
            //itemStrings.set(itemPosition, editedText);
            items.set(itemPosition,tdi);
            myAdapter.notifyDataSetChanged();
            writeItem(tdi);
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


}
