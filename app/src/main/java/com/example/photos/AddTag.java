package com.example.photos;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddTag extends AppCompatActivity {

    public static final String ALBUM_INDEX = "albumIndex";
    public static final String PHOTO_INDEX = "photoIndex";
    public static final String ALBUMS = "albums";

    public static final String TAG_NAME = "tagName";

    private int albumIndex;
    private int photoIndex;
    private Albums albums;
    private EditText tagName;
    private RadioButton tagTypeLocation;
    private RadioButton tagTypePerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tag);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tagName = findViewById(R.id.tag_name);
        tagTypeLocation = findViewById(R.id.radioButton);
        tagTypePerson = findViewById(R.id.radioButton2);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            albumIndex = (Integer) bundle.getInt(ALBUM_INDEX);
            photoIndex = (Integer) bundle.getInt(PHOTO_INDEX);
            System.out.println(photoIndex);
            this.albums = (Albums) getIntent().getSerializableExtra(ALBUMS);
        }

    }

    public void cancelTag(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
    public void saveTag(View view) {
        // gather all data from text fields
        String name = tagName.getText().toString();
        if (name == null || name.length() == 0) {
            Toast.makeText(getApplicationContext(), "Tag value required",
                    Toast.LENGTH_SHORT).show();
            return; // does not quit activity, just returns from method
        }
        if (tagTypeLocation.isChecked()){
            name = "location|" + name;
        } else if (tagTypePerson.isChecked()){
            name = "person|" + name;
        } else {
            Toast.makeText(getApplicationContext(), "Tag type required",
                    Toast.LENGTH_SHORT).show();
            return; // does not quit activity, just returns from method
        }

        if (albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).getTags().contains(name)) {
            Toast.makeText(getApplicationContext(), "Duplicate tag", Toast.LENGTH_SHORT).show();
            return;
        }


        // make Bundle
        Bundle bundle = new Bundle();
        bundle.putString(TAG_NAME, name);
        bundle.putInt(ALBUM_INDEX, albumIndex);
        bundle.putInt(PHOTO_INDEX, albumIndex);
        bundle.putSerializable(ALBUMS, albums);


        // send back to caller
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish(); // pops activity from the call stack, returns to parent

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}