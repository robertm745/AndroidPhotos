package com.example.photos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenAlbum extends AppCompatActivity {
    public static final String ALBUM_NAME = "albumName";
    public static final String ALBUM_INDEX = "albumIndex";
    public static final String ALBUMS = "albums";

    public static final int GALLERY_REQUEST_CODE = 123;

    private int albumIndex;
    private Albums albums;
    private ImageAdapter myImgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            albumIndex = bundle.getInt(ALBUM_INDEX);
            this.albums = (Albums) getIntent().getSerializableExtra(ALBUMS);
        }

        setTitle(bundle.getString(ALBUM_NAME));

        myImgAdapter = new ImageAdapter(this);
        myImgAdapter.setAlbum(albums.getAlbums().get(albumIndex));
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(myImgAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT );
                startActivityForResult(Intent.createChooser(intent, "Pick a photo"), GALLERY_REQUEST_CODE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && intent != null){
            Uri imageData = intent.getData();
            albums.getAlbums().get(albumIndex).addPhoto(new Photo(imageData));
            myImgAdapter = new ImageAdapter(this);
            myImgAdapter.setAlbum(albums.getAlbums().get(albumIndex));
            GridView gridview = (GridView) findViewById(R.id.gridview);
            gridview.setAdapter(myImgAdapter);
            try {
                Albums.write(albums, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}