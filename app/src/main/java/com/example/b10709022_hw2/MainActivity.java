package com.example.b10709022_hw2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.b10709022_hw2.data.WaitlistDbHelper;
import com.example.b10709022_hw2.data.WaitlistContract;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    static RecyclerView rv;
    static WaitlistDbHelper wtDbHelper;
    static SQLiteDatabase Db;
    RvAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wtDbHelper = new WaitlistDbHelper(this);
        Db = wtDbHelper.getWritableDatabase();

        ShapeDrawable s = new ShapeDrawable();
        s.setShape(new OvalShape());
        //s.getPaint().setColor(getResources().getColor(R.color.pink));

        rvAdapter = new RvAdapter(this,getGuest(),s);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(rvAdapter);

        final Context context = this;
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final long id = (long)viewHolder.itemView.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Would you like to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeGuest(id);
                                rvAdapter.reset(getGuest());
                                System.out.println("sout : swiped");
                                wtDbHelper.printTable();
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rvAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);

                AlertDialog a = builder.create();
                a.show();
            }
        }).attachToRecyclerView(rv);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        setColor(sp.getString("color","pink"));
        sp.registerOnSharedPreferenceChangeListener(this);
    }
    private Cursor getGuest(){
        return Db.query(WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP);
    }

    private void removeGuest(long id){
        //String[] a = {MyDbContact.WaitList._ID,id+""};
        //myDb.delete(MyDbContact.WaitList.TABLE_NAME,"?=?",a);
        Db.execSQL(String.format("delete from %s where %s = %s",WaitlistContract.WaitlistEntry.TABLE_NAME,
                WaitlistContract.WaitlistEntry._ID,
                id+""));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.add){
            Intent intent = new Intent(this, AddActivity.class);
            startActivityForResult(intent,100);
            return true;
        }
        if(item.getItemId() == R.id.setting){
            Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        if(request==100&&response==101) {
            rvAdapter.reset(getGuest());
        }
    }
    private void setColor(String color){
        switch (color){
            case "red":
                rvAdapter.setColor(getColor(R.color.red));
                break;
            case "blue":
                rvAdapter.setColor(getColor(R.color.blue));
                break;
            case "green":
                rvAdapter.setColor(getColor(R.color.green));
                break;
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("color")){
            setColor(sharedPreferences.getString(key,"pink"));
        }
    }
}
