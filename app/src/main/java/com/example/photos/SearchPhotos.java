package com.example.photos;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchPhotos extends AppCompatActivity {

    public static final String ALBUM_INDEX = "albumIndex";
    public static final String ALBUMS = "albums";
    public static final int OPEN_ALBUM = 1;

    private EditText locationText;
    private EditText personText;
    private Albums albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.locationText = findViewById(R.id.tag_loc_val);
        this.personText = findViewById(R.id.tag_per_val);
        this.albums = (Albums) getIntent().getSerializableExtra(SearchPhotos.ALBUMS);


    }

    public void search(View v) {
        String location = locationText.getText().toString();
        String person = personText.getText().toString();
        boolean found, byPerson = true, byLocation = true;
        if (location == null || location.length() == 0) {
            Toast.makeText(getApplicationContext(), "byLocation is null/empty", Toast.LENGTH_SHORT).show();
            byLocation = false;
        }
        if (person == null || person.length() == 0) {
            Toast.makeText(getApplicationContext(), "byPerson is null/empty", Toast.LENGTH_SHORT).show();
            byPerson = false;
        }
        if (!byPerson && !byLocation) {
            Toast.makeText(getApplicationContext(), "Please enter at least one Location or Person value to search", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getApplicationContext(), "Searching for " + locationText.getText().toString() + " " + personText.getText().toString(), Toast.LENGTH_SHORT).show();
        Album results = new Album("Search Results");
        for (Album a : albums.getAlbums()) {
            for (Photo p : a.getPhotos()) {
                found = false;
                if (byLocation) {
                    for (String locTag : p.getLocationTags()) {
                        if (locTag.startsWith(location)) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        results.addPhoto(p);
                        continue;
                    }
                }
                if (byPerson) {
                    for (String per : p.getPersonTags()) {
                        if (per.startsWith(person)){
                            found = true;
                            break;
                        }
                    }
                    if (found)
                        results.addPhoto(p);
                }
            }
        }
        openAlbum(results);
    }

    public void openAlbum(Album album) {
        if (albums.getAlbums().size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(OpenAlbum.ALBUM, album);
            bundle.putInt(OpenAlbum.ALBUM_INDEX, -1);
            bundle.putString(OpenAlbum.ALBUM_NAME, album.getName());
            bundle.putBoolean(OpenAlbum.SEARCH_STATE, true);
            Intent intent = new Intent(this, OpenAlbum.class);
            intent.putExtras(bundle);
            intent.putExtra(OpenAlbum.ALBUMS, albums);
            startActivityForResult(intent, OPEN_ALBUM);
        }
    }

}