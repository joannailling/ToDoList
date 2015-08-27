package com.example.joanna.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    static final String TODO_ITEM_DETAIL_KEY = "ToDoItemDetailKey";
    static final String TODO_POSITION_KEY = "ToDoPosition";
    Button btnSave;
    EditText etEditedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etEditedText = (EditText)findViewById(R.id.etTextToEdit);
        btnSave = (Button)findViewById(R.id.btnSaveEdit);

        //Show action bar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setText();
        setupButtonListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    // Sets multi-line edit text to string sent from main activity via Intent
    protected void setText() {
        String text = getIntent().getStringExtra(TODO_ITEM_DETAIL_KEY);
        etEditedText.setText(text);
        etEditedText.setSelection(0, text.length());
    }

    // Click Listener for Save button
    protected void setupButtonListener() {
        // position of edited item is passed in intent from item click listener
        final int posOfEditedItem = getIntent().getIntExtra(TODO_POSITION_KEY, 0);

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent data = new Intent();
                        data.putExtra(TODO_POSITION_KEY, posOfEditedItem);
                        data.putExtra(TODO_ITEM_DETAIL_KEY, etEditedText.getText().toString());
                        setResult(RESULT_OK, data);

                        finish();
                    }
                }
        );
    }

}
