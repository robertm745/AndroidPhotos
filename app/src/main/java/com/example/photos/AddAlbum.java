package com.example.photos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddAlbum extends AppCompatActivity {

    public static final String ALBUM_NAME = "albumName";

    private EditText albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        albumName = findViewById(R.id.album_name);

    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
    public void save(View view) {
        // gather all data from text fields
        String name = albumName.getText().toString();

        // pop up dialog if errors in input, and return
        // name and year are mandatory
        if (name == null || name.length() == 0) {
            Toast.makeText(getApplicationContext(), "Name of album required",
                    Toast.LENGTH_SHORT).show();
            return; // does not quit activity, just returns from method
        }

        // make Bundle
        Bundle bundle = new Bundle();
        bundle.putString(ALBUM_NAME, name);

        // send back to caller
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish(); // pops activity from the call stack, returns to parent

    }




}