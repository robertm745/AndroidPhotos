package com.example.photos;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SearchPhotos extends AppCompatActivity {

    public static final String ALBUMS = "albums";
    public static final int OPEN_ALBUM = 1;

    private Albums albums;

    private RadioButton radioButtonLocation1;
    private RadioButton radioButtonPerson1;
    private RadioButton radioButtonLocation2;
    private RadioButton radioButtonPerson2;
    private RadioButton singleradioButton;
    private RadioButton andradioButton;
    private RadioButton orradioButton2;
    private EditText search_query1;
    private EditText search_query2;
    private TextView search_prompttag2;
    private TextView search_promptvalue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioButtonLocation1 = findViewById(R.id.radioButtonLocation1);
        radioButtonPerson1 = findViewById(R.id.radioButtonPerson1);
        radioButtonLocation2 = findViewById(R.id.radioButtonLocation2);
        radioButtonPerson2 = findViewById(R.id.radioButtonPerson2);
        singleradioButton = findViewById(R.id.singleradioButton);
        andradioButton = findViewById(R.id.andradioButton);
        orradioButton2 = findViewById(R.id.orradioButton2);
        search_query1 = findViewById(R.id.search_query1);
        search_query2 = findViewById(R.id.search_query2);
        search_prompttag2 = findViewById(R.id.search_prompttag2);
        search_promptvalue2 = findViewById(R.id.search_promptvalue2);

        singleradioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonLocation2.setVisibility(View.INVISIBLE);
                radioButtonPerson2.setVisibility(View.INVISIBLE);
                search_query2.setVisibility(View.INVISIBLE);
                search_prompttag2.setVisibility(View.INVISIBLE);
                search_promptvalue2.setVisibility(View.INVISIBLE);
            }
        });

        andradioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonLocation2.setVisibility(View.VISIBLE);
                radioButtonPerson2.setVisibility(View.VISIBLE);
                search_query2.setVisibility(View.VISIBLE);
                search_prompttag2.setVisibility(View.VISIBLE);
                search_promptvalue2.setVisibility(View.VISIBLE);
            }
        });

        orradioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonLocation2.setVisibility(View.VISIBLE);
                radioButtonPerson2.setVisibility(View.VISIBLE);
                search_query2.setVisibility(View.VISIBLE);
                search_prompttag2.setVisibility(View.VISIBLE);
                search_promptvalue2.setVisibility(View.VISIBLE);
            }
        });

        this.albums = (Albums) getIntent().getSerializableExtra(ALBUMS);


    }

    public void search(View v) {
        if (singleradioButton.isChecked()) {
            String query = search_query1.getText().toString();
            String complete;
            String type;
            if (radioButtonLocation1.isChecked()){
                complete = "location|" + query;
                type = "location";
            } else {
                complete = "person|" + query;
                type = "person";
            }
            if (query == null || query.length() == 0) {
                Toast.makeText(getApplicationContext(), "null/empty", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(getApplicationContext(), "Searching for " + complete, Toast.LENGTH_SHORT).show();
                Album results = new Album("Search Results");
                for (Album a : albums.getAlbums()) {
                    for (Photo p : a.getPhotos()) {
                        for (String val : p.getTags()){
                            String[] split = val.split("\\|");
                            if (split[0].equals(type) && split[1].startsWith(query)){
                                results.addPhoto(p);
                            }
                        }

                    }
                }
                openAlbum(results);

            }



        } else if (andradioButton.isChecked()){
            String query1 = search_query1.getText().toString();
            String query2 = search_query2.getText().toString();
            String complete1;
            String complete2;
            String type1;
            String type2;
            if (radioButtonLocation1.isChecked()){
                complete1 = "location|" + query1;
                type1 = "location";
            } else {
                complete1 = "person|" + query1;
                type1 = "person";
            }
            if (radioButtonLocation2.isChecked()){
                complete2 = "location|" + query2;
                type2 = "location";
            } else {
                complete2 = "person|" + query2;
                type2 = "person";
            }
            if (query1 == null || query1.length() == 0 || query2 == null || query2.length() == 0) {
                Toast.makeText(getApplicationContext(), "null/empty", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(getApplicationContext(), "Searching for " + complete1 + " AND " + complete2, Toast.LENGTH_SHORT).show();
                Album results = new Album("Search Results");
                boolean foundone = false;
                boolean foundtwo = false;
                for (Album a : albums.getAlbums()) {
                    for (Photo p : a.getPhotos()) {
                        foundone = false;
                        foundtwo = false;
                        for (String val : p.getTags()){
                            String[] split = val.split("\\|");
                            if (split[0].equals(type1) && split[1].startsWith(query1) || split[0].equals(type2) && split[1].startsWith(query2)){
                                if (foundone) foundtwo = true;
                                else foundone = true;
                            }
                        }

                        if (foundone && foundtwo) results.addPhoto(p);

                    }
                }
                openAlbum(results);


            }



        } else if (orradioButton2.isChecked()){
            String query1 = search_query1.getText().toString();
            String query2 = search_query2.getText().toString();
            String complete1;
            String complete2;
            String type1;
            String type2;
            if (radioButtonLocation1.isChecked()){
                complete1 = "location|" + query1;
                type1 = "location";
            } else {
                complete1 = "person|" + query1;
                type1 = "person";
            }
            if (radioButtonLocation2.isChecked()){
                complete2 = "location|" + query2;
                type2 = "location";
            } else {
                complete2 = "person|" + query2;
                type2 = "person";
            }
            if (query1 == null || query1.length() == 0 || query2 == null || query2.length() == 0) {
                Toast.makeText(getApplicationContext(), "null/empty", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(getApplicationContext(), "Searching for " + complete1 + " OR " + complete2, Toast.LENGTH_SHORT).show();
                Album results = new Album("Search Results");
                for (Album a : albums.getAlbums()) {
                    for (Photo p : a.getPhotos()) {
                        for (String val : p.getTags()){
                            String[] split = val.split("\\|");
                            if (split[0].equals(type1) && split[1].startsWith(query1) || split[0].equals(type2) && split[1].startsWith(query2)){
                                results.addPhoto(p);
                                break;
                            }
                        }
                    }
                }
                openAlbum(results);
            }
        }




//        String location = locationText.getText().toString();
//        String person = personText.getText().toString();
//        boolean found, byPerson = true, byLocation = true;
//        if (location == null || location.length() == 0) {
//            Toast.makeText(getApplicationContext(), "byLocation is null/empty", Toast.LENGTH_SHORT).show();
//            byLocation = false;
//        }
//        if (person == null || person.length() == 0) {
//            Toast.makeText(getApplicationContext(), "byPerson is null/empty", Toast.LENGTH_SHORT).show();
//            byPerson = false;
//        }
//        if (!byPerson && !byLocation) {
//            Toast.makeText(getApplicationContext(), "Please enter at least one Location or Person value to search", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Toast.makeText(getApplicationContext(), "Searching for " + locationText.getText().toString() + " " + personText.getText().toString(), Toast.LENGTH_SHORT).show();
//        Album results = new Album("Search Results");
//        for (Album a : albums.getAlbums()) {
//            for (Photo p : a.getPhotos()) {
//                found = false;
//                if (byLocation) {
//                    for (String locTag : p.getTags()) {
//                        if (locTag.startsWith(location)) {
//                            found = true;
//                            break;
//                        }
//                    }
//                    if (found) {
//                        results.addPhoto(p);
//                        continue;
//                    }
//                }
//                if (byPerson) {
//                    for (String per : p.getTags()) {
//                        if (per.startsWith(person)){
//                            found = true;
//                            break;
//                        }
//                    }
//                    if (found)
//                        results.addPhoto(p);
//                }
//            }
//        }
//        openAlbum(results);
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

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

}