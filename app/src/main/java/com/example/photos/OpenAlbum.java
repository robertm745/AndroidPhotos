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
    public static final String SEARCH_STATE = "state";
    public static final String ALBUM = "album";

    public static final int GALLERY_REQUEST_CODE = 123;
    public static final int OPEN_PHOTO = 1;

    private int albumIndex;
    private Albums albums;
    private ImageAdapter myImgAdapter;
    private GridView gridview;
    private boolean searchState;
    private Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        setTitle(bundle.getString(ALBUM_NAME));
        this.searchState = bundle.getBoolean(OpenAlbum.SEARCH_STATE);
        albumIndex = bundle.getInt(ALBUM_INDEX);
        this.albums = (Albums) getIntent().getSerializableExtra(ALBUMS);
        if (searchState)
            this.album = (Album) bundle.getSerializable(OpenAlbum.ALBUM);
        else
            this.album = albums.getAlbums().get(albumIndex);




        // change album here
        myImgAdapter = new ImageAdapter(this);
        if (searchState)
            myImgAdapter.setAlbum(this.album);
        else
            myImgAdapter.setAlbum(albums.getAlbums().get(albumIndex));
        gridview = findViewById(R.id.gridview);
        gridview.setAdapter(myImgAdapter);

        registerForContextMenu(gridview);

        // change album here
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                if (gridview.getAdapter().getCount() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(OpenPhoto.ALBUM, album);
                    bundle.putString(OpenPhoto.ALBUM_NAME, album.getName());
                    bundle.putInt(OpenPhoto.PHOTO_INDEX, pos);
                    if (searchState){
                        bundle.putBoolean(OpenPhoto.SEARCH_STATE, searchState);
                        Intent intent = new Intent(getApplicationContext(), OpenPhoto.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, OPEN_PHOTO);
                    } else {
                        bundle.putInt(OpenPhoto.ALBUM_INDEX, albumIndex);
                        Intent intent = new Intent(getApplicationContext(), OpenPhoto.class);
                        intent.putExtras(bundle);
                        intent.putExtra(AddAlbum.ALBUMS, albums);
                        startActivityForResult(intent, OPEN_PHOTO);
                    }

                }
            }
        });

        // change album here
        FloatingActionButton fab = findViewById(R.id.fab);
        if (!searchState) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT );
                    startActivityForResult(Intent.createChooser(intent, "Pick a photo"), GALLERY_REQUEST_CODE);
                }
            });
        } else {
            fab.hide();
        }
    }

    // change album here
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (!searchState) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.photo_menu, menu);
        }
    }

    // change album here
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.photo_delete:
                Toast.makeText(this, "Deleted " + albums.getAlbums().get(albumIndex).getPhotos().get(info.position), Toast.LENGTH_SHORT).show();
                albums.getAlbums().get(albumIndex).getPhotos().remove(info.position);
                try {
                    Albums.write(albums, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myImgAdapter.setAlbum(albums.getAlbums().get(albumIndex));
                gridview.setAdapter(myImgAdapter);
                return true;
            case R.id.photo_move:
                movePhoto(info.position);

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
            public View getView(int position, View textView, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                if (textView == null) {
                    textView = inflater.inflate(R.layout.move_list_row, null);
                    txt = textView.findViewById(R.id.row_name);
                } else {
                    txt = (TextView) textView;
                }
                txt.setText(albums.getAlbums().get(position).toString());
                return textView;
            }
        };

        builder.setAdapter(adapter, (dialog, destIndex) -> {
                    Photo p = temp.getPhotos().remove(photoIndex);
                    albums.getAlbums().get(destIndex).addPhoto(p);
                    Toast.makeText(getApplicationContext(), "Moved " + p.toString() + " to " + albums.getAlbums().get(destIndex).getName(), Toast.LENGTH_SHORT).show();
                    albums.addAlbum(temp);
                    try {
                        Albums.write(albums, getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myImgAdapter.setAlbum(albums.getAlbums().get(albums.getAlbumIndex(temp)));
                    gridview = findViewById(R.id.gridview);
                    gridview.setAdapter(myImgAdapter);
                });

        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", (dialog, which) -> albums.getAlbums().add(temp));
        AlertDialog alert = builder.create();
        alert.show();
    }


    // change album here possibly
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && intent != null){
            Uri imageData = intent.getData();
            albums.getAlbums().get(albumIndex).addPhoto(new Photo(imageData));
            myImgAdapter = new ImageAdapter(this);
            myImgAdapter.setAlbum(albums.getAlbums().get(albumIndex));
            gridview = findViewById(R.id.gridview);
            gridview.setAdapter(myImgAdapter);
            try {
                Albums.write(albums, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && intent != null) {
            Bundle bundle = intent.getExtras();
            albums = (Albums) bundle.get(ALBUMS);
        }



    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}