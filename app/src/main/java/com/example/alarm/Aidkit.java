package com.example.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Aidkit extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    public Adapter mAdapter;
    public static final  int CONTACTLOADER = 0;
    Button addDoctor;
    BottomNavigationView bottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidkit);
        addDoctor=findViewById(R.id.doc_button);
        bottomMenu=findViewById(R.id.bottom_navigation);
        bottomMenu.setSelectedItemId(R.id.aidkit);

        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Aidkit.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.aidkit:
                        return true;
                }
                return false;
            }
        });

        ListView listView = findViewById(R.id.list);
        mAdapter = new Adapter(this, null);
        listView.setAdapter(mAdapter);

        // whenever we press a listview for updating
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Aidkit.this, EditorActivity.class);
                Uri newUri = ContentUris.withAppendedId(Contract.ContactEntry.CONTENT_URI, id);
                intent.setData(newUri);
                startActivity(intent);
            }
        });

        // get the loader running
        getLoaderManager().initLoader(CONTACTLOADER, null, this);

    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {Contract.ContactEntry._ID,
                Contract.ContactEntry.COLUMN_NAME,
                Contract.ContactEntry.COLUMN_EMAIL,
                Contract.ContactEntry.COLUMN_PICTURE,
                Contract.ContactEntry.COLUMN_PHONENUMBER,
                Contract.ContactEntry.COLUMN_TYPEOFCONTACT
        };

        return new CursorLoader(this, Contract.ContactEntry.CONTENT_URI,
                projection, null,
                null,
                null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);

    }

}