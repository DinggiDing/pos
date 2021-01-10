package com.example.albumtab_spinner;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<albumele> list;
    albumListAdapter adapter = null;

    public static SQLiteHelper sqLiteHelper;
    private String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnSet = findViewById(R.id.btnSet);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
                switch (menuitem.getItemId()) {
                    case R.id.dashboard: {
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    }

                    case R.id.home: {
                        return true;
                    }

                    case R.id.about: {
                        startActivity(new Intent(getApplicationContext(), About.class));
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return  false;
            }
        });

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new albumListAdapter(this, R.layout.album_items,list);
        gridView.setAdapter(adapter);

        sqLiteHelper = new SQLiteHelper(this, "AlbumDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS FOOD(Id INTEGER PRIMARY KEY AUTOINCREMENT, memo INTEGER, image VARCHAR, color INTEGER)");

        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM FOOD");
        list.clear();
        
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int memo = cursor.getInt(1);
            String image = cursor.getString(2);

            list.add(new albumele(memo, image, id));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {"Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0) {
                            // Delete
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM FOOD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while(c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetActivity.class);
                startActivityForResult(intent,888);
            }
        });
        
    }

    ImageView imageViewDog;
    private void showDialogDelete(final int idDog) {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);

        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Are you sure you want too this delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    sqLiteHelper.deleteData(idDog);
                    Toast.makeText(getApplicationContext(), "Delete successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("error",e.getMessage());
                }
                updatealbumList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    public void updatealbumList() {
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM FOOD");
        list.clear();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int memo = cursor.getInt(1);
            String image = cursor.getString(2);

            list.add(new albumele(memo, image, id));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 888) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        else if(requestCode == 777) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 777);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Glide.with(this).load(uri).into(imageViewDog);

        }
        else if(requestCode == 777 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            path = getPathFromURI(uri);
            Glide.with(this).load(uri).into(imageViewDog);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPathFromURI(Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();;
        return path;
    }
}