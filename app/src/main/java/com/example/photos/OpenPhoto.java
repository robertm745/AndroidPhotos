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
    public static final String SEARCH_STATE = "searchState";
    public static final String ALBUM = "album";

    private int albumIndex;
    private int photoIndex;
    private Albums albums;
    private ListView listView;
    private boolean searchState;
    private Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_photo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.searchState = bundle.getBoolean(OpenPhoto.SEARCH_STATE);
            if (searchState)
                this.album = (Album) bundle.getSerializable(OpenPhoto.ALBUM);
            albumIndex = bundle.getInt(ALBUM_INDEX);
            photoIndex = bundle.getInt(PHOTO_INDEX);
            this.albums = (Albums) getIntent().getSerializableExtra(ALBUMS);
            setTitle(bundle.getString(ALBUM_NAME) + " Slideshow");
        }


        if (!searchState) {
            FloatingActionButton fab = findViewById(R.id.add_tag_fab);
            fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());
        }

        ImageView imageView = findViewById(R.id.slideshow_imageview);
        if (searchState)
            imageView.setImageURI(Uri.parse(album.getPhotos().get(photoIndex).getLocation()));
        else
            imageView.setImageURI(Uri.parse(albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).getLocation()));

        Button previous = findViewById(R.id.previous_button);
        previous.setOnClickListener(view -> {
            if (photoIndex > 0){
                photoIndex -= 1;
                if (searchState)
                    imageView.setImageURI(Uri.parse(album.getPhotos().get(photoIndex).getLocation()));
                else
                    imageView.setImageURI(Uri.parse(albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).getLocation()));
            }
        });

        Button next = findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (photoIndex < albums.getAlbums().get(albumIndex).getPhotos().size()-1){
                    photoIndex += 1;;
                    if (searchState)
                        imageView.setImageURI(Uri.parse(album.getPhotos().get(photoIndex).getLocation()));
                    else
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