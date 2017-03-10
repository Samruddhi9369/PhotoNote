package edu.csulb.android.photonote;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.listview);
        loadNotesList();
        registerForContextMenu(listView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadNotesList();
    }

    public void loadNotesList(){
        final Intent intent = new Intent(this,ViewPhotoActivity.class);
        NotesDatabaseHelper db = new NotesDatabaseHelper(getApplicationContext());
        final List<PhotoNote> notes = db.getAllNotes();
        listView.setAdapter(new CustomAdapter(this, R.layout.custom_row, notes));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String caption = notes.get(position).getCaption();
                intent.putExtra("caption",caption);
                String date = notes.get(position).getCreatedAt();
                intent.putExtra("date",date);
                String filePath = null;
                try {
                    filePath = notes.get(position).getFilePath();
                    intent.putExtra("filePath",filePath);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onClickFab(View view) {
        Intent intent = new Intent(this,AddPhotoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_unistall) {
            Uri packageUri = Uri.parse("package:edu.csulb.android.photonote");
            Intent uninstallIntent =
                    new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
            startActivity(uninstallIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteNote(info.position);
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
                loadNotesList();
                // TODO: Implement Delete
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deleteNote(int position){
        NotesDatabaseHelper db = new NotesDatabaseHelper(getApplicationContext());
        final List<PhotoNote> notes = db.getAllNotes();
        try {
            String filePath = notes.get(position).getFilePath();
            File file = new File(filePath);
            file.delete();
            db.deleteImage(filePath);
        } catch (IOException e) {
        e.printStackTrace();
    }
    }
}
