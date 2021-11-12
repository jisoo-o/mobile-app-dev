package com.example.sqltest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sqltest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Movie Information DB");
    }

    public class myDBHelper extends SQLiteOpenHelper{
        public myDBHelper(Context context){
            super(context, "movieDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table movieTBL (mTitle char(20), mName char(20), mYear integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists movieTBL;");
            onCreate(db);
        }

    }

    public void initializeDB(View view){
        myDBHelper myHelper = new myDBHelper(this);
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        myHelper.onUpgrade(sqlDB,1,2);
        sqlDB.close();
        Toast.makeText(getApplicationContext(),"Initialized",Toast.LENGTH_LONG).show();
    }

    public void insertDB(View view){
        myDBHelper myHelper = new myDBHelper(this);
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        sqlDB.execSQL("insert into movieTBL values(' "+ binding.editText1.getText().toString() +" ',' "+ binding.editText2.getText().toString() +" '," +binding.editText3.getText().toString()+");");
        sqlDB.close();
        Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_LONG).show();
    }

    public void searchDB(View view){
        myDBHelper myHelper = new myDBHelper(this);
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from movieTBL;",null);

        String string1 = "Movie Title" +System.lineSeparator();
        String string2 = "Director" +System.lineSeparator();
        String string3 = "Released Year" +System.lineSeparator();

        string1 += "============"+System.lineSeparator();
        string2 += "============"+System.lineSeparator();
        string3 += "============"+System.lineSeparator();

        while(cursor.moveToNext()){
            string1 += cursor.getString(0)+System.lineSeparator();
            string2 += cursor.getString(1)+System.lineSeparator();
            string3 += cursor.getString(2)+System.lineSeparator();
        }

        binding.textView3.setText(string1);
        binding.textView4.setText(string2);
        binding.textView5.setText(string3);

        cursor.close();
        sqlDB.close();


    }


}