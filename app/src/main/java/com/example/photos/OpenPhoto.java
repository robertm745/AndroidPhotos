package com.example.photos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class OpenPhoto extends AppCompatActivity {

    public static final String ALBUM_NAME = "albumName";
    public static final String ALBUM_INDEX = "albumIndex";
    public static final String PHOTO_INDEX = "photoIndex";
    public static final String ALBUMS = "albums";
    public static final String SEARCH_STATE = "searchState";
    public static final String ALBUM = "album";

    public static final int ADD_TAG = 1;

    private int albumIndex;
    private int photoIndex;
    private Albums albums;
    private ListView listView;
    private boolean searchState;
    private Album album;
    private TextView caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_photo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        caption = findViewById(R.id.textView2);
        caption.setText("");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.searchState = bundle.getBoolean(OpenPhoto.SEARCH_STATE);
            photoIndex = bundle.getInt(PHOTO_INDEX);
            if (searchState) {
                this.album = (Album) bundle.getSerializable(OpenPhoto.ALBUM);
            } else {
                albumIndex = bundle.getInt(ALBUM_INDEX);
                this.albums = (Albums) getIntent().getSerializableExtra(ALBUMS);
                this.album = albums.getAlbums().get(albumIndex);
            }

            setTitle(bundle.getString(ALBUM_NAME) + " Slideshow");
        }

        FloatingActionButton fab = findViewById(R.id.add_tag_fab);
        if (!searchState) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addTag();
                }
            });
        } else {
            fab.hide();
        }

        ImageView imageView = findViewById(R.id.slideshow_imageview);
        imageView.setImageURI(Uri.parse(album.getPhotos().get(photoIndex).getLocation()));
        caption.setText(album.getPhotos().get(photoIndex).getCaption());
        listView = findViewById(R.id.tag_listview);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.album, album.getPhotos().get(photoIndex).getTags()));
        registerForContextMenu(listView);

        Button previous = findViewById(R.id.previous_button);
        previous.setOnClickListener(view -> {
            if (photoIndex > 0){
                photoIndex -= 1;
                    imageView.setImageURI(Uri.parse(album.getPhotos().get(photoIndex).getLocation()));
                    caption.setText(album.getPhotos().get(photoIndex).getCaption());
                    listView.setAdapter(
                            new ArrayAdapter<String>(this, R.layout.album, album.getPhotos().get(photoIndex).getTags()));

            }
        });

        Button next = findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (photoIndex < album.getPhotos().size()-1){
                    photoIndex += 1;
                        imageView.setImageURI(Uri.parse(album.getPhotos().get(photoIndex).getLocation()));
                        caption.setText(album.getPhotos().get(photoIndex).getCaption());
                        listView.setAdapter(
                                new ArrayAdapter<String>(getApplicationContext(), R.layout.album, album.getPhotos().get(photoIndex).getTags()));

                }

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra(ALBUMS, albums);
                setResult(RESULT_OK,intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        // gather all info passed back by launched activity
        String name = bundle.getString(AddTag.TAG_NAME);
        if (requestCode == ADD_TAG) {
//            int indexAlbum = bundle.getInt(AddTag.ALBUM_INDEX);
//            int indexPhoto = bundle.getInt(AddTag.PHOTO_INDEX);
            albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).addTag(name);
//            System.out.println("getting back" + indexPhoto + "  "+ indexAlbum);
        }

        try {
            Albums.write(albums, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // redo the adapter to reflect change^K

            listView.setAdapter(
                    new ArrayAdapter<String>(this, R.layout.album, albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).getTags()));


    }

    public void addTag(){
        Intent intent = new Intent(this, AddTag.class);
        intent.putExtra(AddTag.ALBUMS, albums);
        intent.putExtra(AddTag.ALBUM_INDEX,albumIndex);
        intent.putExtra(AddTag.PHOTO_INDEX, photoIndex);
        startActivityForResult(intent, ADD_TAG);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (!searchState){
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.tag_menu, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                Toast.makeText(this, getResources().getString(R.string.delete) + "d " +
                                albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).getTags().get((int)info.id),
                        Toast.LENGTH_SHORT).show();
                albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).getTags().remove((int)info.id);

                try {
                    Albums.write(albums, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                listView.setAdapter(new ArrayAdapter<String>(this, R.layout.album, albums.getAlbums().get(albumIndex).getPhotos().get(photoIndex).getTags()));

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}