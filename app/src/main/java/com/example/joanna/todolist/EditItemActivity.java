package com.example.joanna.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    static final String TODO_ITEM_DETAIL_KEY = "ToDoItemDetailKey";
    static final String TODO_POSITION_KEY = "ToDoPosition";
    static final String TODO_DUE_DATE = "ToDoDueDate";

    Button btnSave;
    EditText etEditedText;
    EditText etEditDueDate;

    SimpleDateFormat dateFormatter;
    DatePickerDialog dueDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        findViewsById();

        //Show action bar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setText();
        setupButtonListener();
        setupDateListener();
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

    private void findViewsById() {
        etEditedText = (EditText)findViewById(R.id.etTextToEdit);
        btnSave = (Button)findViewById(R.id.btnSaveEdit);
        etEditDueDate = (EditText)findViewById(R.id.etEditItemDate);
        etEditDueDate.setInputType(InputType.TYPE_NULL);
        etEditDueDate.requestFocus();
    }

    // Sets multi-line edit text to string sent from main activity via Intent
    protected void setText() {
        String text = getIntent().getStringExtra(TODO_ITEM_DETAIL_KEY);
        String dueDate = getIntent().getStringExtra(TODO_DUE_DATE);
        etEditedText.setText(text);
        etEditDueDate.setText(dueDate);
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
                        data.putExtra(TODO_DUE_DATE, etEditDueDate.getText().toString());

                        setResult(RESULT_OK, data);
                        finish();
                    }
                }
        );
    }

    protected void setupDateListener() {
        etEditDueDate.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        dueDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String textToSet = dateFormatter.format(newDate.getTime());
                etEditDueDate.setText(textToSet);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if (v == etEditDueDate) {
            dueDateDialog.show();
        }
    }
}
