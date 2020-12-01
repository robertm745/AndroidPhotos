package com.example.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
class Albums implements Serializable {
    private ArrayList<Album> albums;

    public Albums() {
        this.albums = new ArrayList<Album>();
    }

    public ArrayList<Album> getAlbums() {
        return this.albums;
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    public void deleteAlbum(Album album){
        this.albums.remove(album);
    }

    public static Albums readAlbums(Context context) throws IOException, ClassNotFoundException {
        FileInputStream fIn = context.openFileInput("data.dat");
        ObjectInputStream ois = new ObjectInputStream(fIn);
        Albums albumList = (Albums) ois.readObject();
        ois.close();
        return albumList;
    }

    public static void write(Albums albumList, Context context) throws IOException {
        FileOutputStream fOut = context.openFileOutput("data.dat", MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fOut);
        oos.writeObject(albumList);
        oos.close();
    }

}


public class MainActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.album_list);
        Albums albums = null;

        try {
           albums = Albums.readAlbums(this);
        } catch (IOException | ClassNotFoundException e){
            albums = new Albums();
            try {
                Albums.write(albums, this);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album, albums.getAlbums()));

        registerForContextMenu(listView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlbum();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.album_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Albums albums = null;
        try {
            albums = Albums.readAlbums(this);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        switch (item.getItemId()) {
            case R.id.rename:
                Toast.makeText(this, "You have chosen the " + getResources().getString(R.string.rename) +
                                " context menu option for " + albums.getAlbums().get((int)info.id),
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                Toast.makeText(this, getResources().getString(R.string.delete) + "d " +
                                albums.getAlbums().get((int)info.id),
                        Toast.LENGTH_SHORT).show();
                Album toDelete = albums.getAlbums().get((int)info.id);
                albums.deleteAlbum(toDelete);

                try {
                    Albums.write(albums, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    listView.setAdapter(
                            new ArrayAdapter<Album>(this, R.layout.album, Albums.readAlbums(this).getAlbums()));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void addAlbum(){
        Intent intent = new Intent(this, AddAlbum.class);
        startActivityForResult(intent, 1);
    }

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
        String name = bundle.getString(AddAlbum.ALBUM_NAME);
        try {
            Albums albums = Albums.readAlbums(this);
            albums.addAlbum(new Album(name));
            Albums.write(albums, this);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        // redo the adapter to reflect change^K
        try {
            listView.setAdapter(
                    new ArrayAdapter<Album>(this, R.layout.album, Albums.readAlbums(this).getAlbums()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showAlbum(int pos) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}