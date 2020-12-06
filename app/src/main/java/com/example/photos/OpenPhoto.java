package com.example.photos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class OpenPhoto extends AppCompatActivity {

    public static final String ALBUM_NAME = "albumName";
    public static final String ALBUM_INDEX = "albumIndex";
    public static final String PHOTO_INDEX = "albumIndex";
    public static final String ALBUMS = "albums";

    private int albumIndex;
    private int photoIndex;
    private Albums albums;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_photo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            albumIndex = bundle.getInt(ALBUM_INDEX);
            photoIndex = bundle.getInt(PHOTO_INDEX);
            this.albums = (Albums) getIntent().getSerializableExtra(ALBUMS);
        }

        FloatingActionButton fab = findViewById(R.id.add_tag_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ImageView imageView = findViewById(R.id.slideshow_imageview);
        imageView.setImageURI(Uri.parse(albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).getLocation()));

        Button previous = findViewById(R.id.previous_button);
        previous.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (photoIndex > 0){
                    photoIndex -= 1;;
                    imageView.setImageURI(Uri.parse(albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).getLocation()));
                }
            }
        });

        Button next = findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (photoIndex < albums.getAlbums().get(albumIndex).getPhotos().size()-1){
                    photoIndex += 1;;
                    imageView.setImageURI(Uri.parse(albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).getLocation()));
                }

            }
        });



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

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


    }
}