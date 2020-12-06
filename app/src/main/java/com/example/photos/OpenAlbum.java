package com.example.photos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class OpenAlbum extends AppCompatActivity {
    public static final String ALBUM_NAME = "albumName";
    public static final String ALBUM_INDEX = "albumIndex";
    public static final String ALBUMS = "albums";

    public static final int GALLERY_REQUEST_CODE = 123;

    private int albumIndex;
    private Albums albums;
    private ImageAdapter myImgAdapter;
    private GridView gridview;

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
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(myImgAdapter);

        registerForContextMenu(gridview);

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Photo p;
        switch (item.getItemId()) {
            case R.id.photo_delete:
                Toast.makeText(this, getResources().getString(R.string.delete) + " " + albums.getAlbums().get(albumIndex).getPhotos().get((int) info.id), Toast.LENGTH_SHORT).show();
                p = albums.getAlbums().get(albumIndex).getPhotos().remove((int) info.id);


                try {
                    Albums.write(albums, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myImgAdapter.setAlbum(albums.getAlbums().get(albumIndex));
                gridview.setAdapter(myImgAdapter);


                return true;
            case R.id.photo_move:
                Album temp = albums.getAlbums().get(albumIndex);
                movePhoto((int) info.id);
                    /*
                if (!albums.getAlbums().contains(temp)) {
                    albums.addAlbum(temp);
                    albumIndex = albums.getAlbumIndex(temp);
                    try {
                        Albums.write(albums, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                     */
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void movePhoto(int photoIndex) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an album");

        Album temp = albums.getAlbums().remove(albumIndex);

        ListAdapter adapter = new ArrayAdapter<Album>(getApplicationContext(), R.layout.move_list_row, albums.getAlbums()) {
            TextView txt;
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.move_list_row, null);
                    txt = (TextView) convertView.findViewById(R.id.row_name);
                } else {
                    txt = (TextView) convertView;
                }
                txt.setText(albums.getAlbums().get(position).toString());
                return convertView;
            }
        };

        builder.setAdapter(adapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int destIndex) {
                        Photo p = temp.getPhotos().remove(photoIndex);
                        albums.getAlbums().get(destIndex).addPhoto(p);
                        Toast.makeText(getApplicationContext(), "Moved " + p.toString() + " to " + albums.getAlbums().get(destIndex).getName(), Toast.LENGTH_SHORT).show();
                        albums.getAlbums().add(temp);
                        try {
                            Albums.write(albums, getApplicationContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        myImgAdapter.setAlbum(temp);
                        gridview = (GridView) findViewById(R.id.gridview);
                        gridview.setAdapter(myImgAdapter);


                    }
                });
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                albums.getAlbums().add(temp);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && intent != null){
            Uri imageData = intent.getData();
            albums.getAlbums().get(albumIndex).addPhoto(new Photo(imageData));
            myImgAdapter = new ImageAdapter(this);
            myImgAdapter.setAlbum(albums.getAlbums().get(albumIndex));
            gridview = (GridView) findViewById(R.id.gridview);
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