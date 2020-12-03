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
    public static final String ALBUM_INDEX = "albumIndex";
    public static final String ALBUMS = "albums";

    private int albumIndex;
    private Albums albums;
    private EditText albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        albumName = findViewById(R.id.album_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            albumIndex = bundle.getInt(ALBUM_INDEX);
            albumName.setText(bundle.getString(ALBUM_NAME));
            this.albums = (Albums) getIntent().getSerializableExtra(ALBUMS);
        }
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


        if (albums.contains(name) && (albumIndex == -1 || !name.equalsIgnoreCase(getIntent().getExtras().getString(ALBUM_NAME)))) {
            Toast.makeText(getApplicationContext(), "Duplicate album name", Toast.LENGTH_SHORT).show();
            return;
        }


        // make Bundle
        Bundle bundle = new Bundle();
        bundle.putString(ALBUM_NAME, name);
        bundle.putInt(ALBUM_INDEX, albumIndex);

        // send back to caller
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish(); // pops activity from the call stack, returns to parent

    }




}