package com.example.sanidhya.travelbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    final String STUDENT_TABLE_NAME = "tb";
    final String S_ID = "id";
    final String S_UNAME = "name";
    final String S_PWD = "pwd";
    final String TAG = "SQLiteError";
    SQLiteDatabase studentDatabase;


    void SInsert(String name, String mark) throws SQLiteException {

        ContentValues contentValues = new ContentValues();
        contentValues.put(S_UNAME, name);
        contentValues.put(S_PWD, mark);
        studentDatabase.insert(STUDENT_TABLE_NAME, null, contentValues);

    }

    void SUpdate(String roll, String name, String mark) throws SQLiteException{

        ContentValues contentValues = new ContentValues();
        if(name!=null)
            contentValues.put(S_UNAME, name);
        if(mark!=null)
            contentValues.put(S_PWD, mark);
        studentDatabase.update(STUDENT_TABLE_NAME, contentValues, S_ID + "=" + roll, null);

    }

    void SDelete(String roll) throws SQLiteException{
        studentDatabase.delete(STUDENT_TABLE_NAME, S_ID + "=" + roll, null);
    }

    Cursor SView(String roll) throws SQLiteException{
        return studentDatabase.rawQuery("SELECT * FROM " + STUDENT_TABLE_NAME + " WHERE name = ?", new String[]{roll});

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            try{
        studentDatabase = openOrCreateDatabase(STUDENT_TABLE_NAME, MODE_PRIVATE, null);

        studentDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + STUDENT_TABLE_NAME +
                "(" + S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                S_UNAME+ " VARCHAR(20) UNIQUE," +
                S_PWD + " VARCHAR(20));");
            }catch (Exception e){Toast.makeText(getApplicationContext(),"sql create error",Toast.LENGTH_LONG).show();}

            Button signup= (Button)findViewById(R.id.sign_up);
            signup.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    try{
                    String usr = String.valueOf(((EditText) findViewById(R.id.email_holder)).getText());
                    String pss = String.valueOf(((EditText) findViewById(R.id.password_holder)).getText());
                    SInsert(usr,pss);
                    Toast.makeText(getApplicationContext(),"Inserted successfully",Toast.LENGTH_SHORT).show();}
                    catch (Exception e){Toast.makeText(getApplicationContext(),"Sql insert error",Toast.LENGTH_SHORT).show();}
                }
            });

            Button login_butt=(Button) findViewById(R.id.login_butt);
            login_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String usr = String.valueOf(((EditText) findViewById(R.id.email_holder)).getText());
                    String pss = String.valueOf(((EditText) findViewById(R.id.password_holder)).getText());
                    Cursor c=SView(usr);
                    if(c.getCount()>0)
                    {
                        c.moveToFirst();
                        if (usr.equals(c.getString(1)) && pss.equals(c.getString(2))) {
                        Toast.makeText(getApplicationContext(), "Login success!", Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(MainActivity.this,OptionsActivity.class);
                            i.putExtra("usr_id",c.getString(0));
                        startActivity(i);
                        }
                        else{
                        Toast.makeText(getApplicationContext(), "Wrong credentials entered!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {Toast.makeText(getApplicationContext(),"No such user exists",Toast.LENGTH_SHORT).show();}
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"error on login",Toast.LENGTH_LONG).show();
                }
            }
        });

    }catch (Exception e){
            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
        }

}


}
