package com.example.vitaly.database;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "DatabaseProject";
    private ListView lvMain;
    private EditText etName;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnReadActivityMain).setOnClickListener(this);
        findViewById(R.id.btnInsertActivityMain).setOnClickListener(this);
        etName = (EditText) findViewById(R.id.etNameActivityMain);
        etPassword = (EditText) findViewById(R.id.etPasswordActivityMain);
        lvMain = (ListView) findViewById(R.id.lvMainActivityMain);
        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Do you want to delete?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO content resolver + delete
                            }
                        });

                builder.create().show();

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().initLoader(1, null, this);
        //prepareAndShowListView();
    }

    private void prepareAndShowListView(Cursor cursor) {
        //Cursor cursor = getContentResolver().query(MyContentProvider.URI,null,null,null,null);
        if ((cursor==null)||(cursor.getCount()==0)) return;

        ArrayList<HashMap<String,String>> data = new ArrayList<>();

        cursor.moveToFirst();
        do{
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            Log.d(LOG_TAG,"name - "+name+" passw - "+password);

            HashMap<String,String> local = new HashMap<>();
            local.put("name",name);
            local.put("password",password);
            data.add(local);

        } while (cursor.moveToNext());

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"name","password"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

                /*SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        this,
                        android.R.layout.simple_list_item_2,
                        cursor,
                        new String[]{"name","password"},
                        new int[]{android.R.id.text1, android.R.id.text2},
                        Adapter.NO_SELECTION
                );*/
        lvMain.setAdapter(simpleAdapter);

    }

    @Override
    public void onClick(View v) {

        /*DBHelper helper = new DBHelper(this,DBHelper.CURRENT_VERSION);
        SQLiteDatabase database = helper.getWritableDatabase();
*/
        switch (v.getId()){
            case R.id.btnInsertActivityMain:

                ContentValues contentValues = new ContentValues();
                contentValues.put("name",etName.getText().toString());
                contentValues.put("password",etPassword.getText().toString());

                //database.insert(DBHelper.TABLE_NAME,null,contentValues);
                getContentResolver().insert(MyContentProvider.URI,contentValues);
                break;

            case R.id.btnReadActivityMain:
                //Cursor cursor = database.query(DBHelper.TABLE_NAME,null,null,null,null,null,null);
                //Cursor cursor = database.rawQuery("SELECT * FROM "+DBHelper.TABLE_NAME,null);
                //prepareAndShowListView();
                getSupportLoaderManager().initLoader(1,null,this);
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,MyContentProvider.URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        prepareAndShowListView(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
