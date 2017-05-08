package com.example.sanidhya.travelbuddy;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sanidhya on 28-03-2017.
 */

public class WalletActivity extends AppCompatActivity {

    final String STUDENT_TABLE_NAME = "tb";
    final String ITEM = "ITEM";
    final String EXPENSE = "EXPENSE";
    final String ID = "ID";


    private String[] groceryItems;
    protected String[] groceryPrices;

    SQLiteDatabase studentDatabase;
    final Context context = this;

    void showToast(CharSequence text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    void SInsert(String itm, String exp,String roll) throws SQLiteException {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM, itm);
        contentValues.put(EXPENSE, exp);
        studentDatabase.insert(STUDENT_TABLE_NAME+roll, null, contentValues);

    }

    void SUpdate(String roll, String itm, String exp) throws SQLiteException{

        ContentValues contentValues = new ContentValues();
        if(itm!=null)
            contentValues.put(ITEM, itm);
        if(exp!=null)
            contentValues.put(EXPENSE, exp);
        studentDatabase.update(STUDENT_TABLE_NAME+roll, contentValues, ITEM + "=" + itm, null);

    }

    void SDelete(String roll,String itm) throws SQLiteException{
        try{
            String query="DELETE FROM "+(STUDENT_TABLE_NAME+roll)+" WHERE ITEM= '"+itm+"'";
            studentDatabase.execSQL(query);}
        catch (Exception e){showToast(e.toString());}
    }

    Cursor SView(String roll) throws SQLiteException{
        return studentDatabase.rawQuery("SELECT * FROM " + (STUDENT_TABLE_NAME+roll),null);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        try {
            groceryItems=new String[100];
            groceryPrices=new String[100];
            TextView info= (TextView) findViewById(R.id.info);


            Bundle bundle =getIntent().getExtras();
            final String id = bundle.getString("usr_id");

            EditText s=(EditText) findViewById(R.id.budget);
            TextView p=(TextView) findViewById(R.id.prices);

            try{
                studentDatabase = openOrCreateDatabase(STUDENT_TABLE_NAME, MODE_PRIVATE, null);

                studentDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + STUDENT_TABLE_NAME + id +
                        "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                        ITEM+ " VARCHAR(20) NOT NULL," +
                        EXPENSE + " VARCHAR(20) NOT NULL);");
                //studentDatabase.execSQL("DROP TABLE IF EXISTS "+STUDENT_TABLE_NAME+id);
            }catch (Exception e){Toast.makeText(getApplicationContext(),"sql create error",Toast.LENGTH_LONG).show();}



            Button add_expense =(Button) findViewById(R.id.set_wall);
            add_expense.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    // custom dialog

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.setTitle("Add Item");
                    dialog.show();




                    final EditText item=(EditText) dialog.findViewById(R.id.Item);
                    final EditText expense=(EditText) dialog.findViewById(R.id.expense);
                    Button cancelBut=(Button) dialog.findViewById(R.id.cancel_butt);
                    Button dialogButton= (Button) dialog.findViewById(R.id.add_butt);
                    try{
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String itm=item.getText().toString();
                            String exp= expense.getText().toString();
                            if(itm.equals("") || exp.equals(""))
                            {

                                Toast.makeText(getApplicationContext(),"Please insert correct values",Toast.LENGTH_SHORT).show();
                            }else
                            {

                                SInsert(itm,exp,id);
                                //Values added
                                showToast("entry added");
                                dialog.dismiss();

                            }

                        }
                    });}catch (Exception e){Toast.makeText(getApplicationContext(),e.toString()+"bahr",Toast.LENGTH_LONG).show();}
                    cancelBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }});


                }
            });


            Button remove_expense =(Button) findViewById(R.id.remove_expense);
            remove_expense.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    // custom dialog

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.setTitle("Remove Item");
                    dialog.show();


                    //final TextView x=(TextView)findViewById(R.id.textView2);
                    //x.setVisibility(TextView.INVISIBLE);

                    final EditText item=(EditText) dialog.findViewById(R.id.Item);
                    final EditText expense=(EditText) dialog.findViewById(R.id.expense);
                    expense.setVisibility(EditText.INVISIBLE);
                    Button cancelBut=(Button) dialog.findViewById(R.id.cancel_butt);
                    Button dialogButton= (Button) dialog.findViewById(R.id.add_butt);
                    try{
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String itm=item.getText().toString();
                                //String exp= expense.getText().toString();
                                if(itm.equals(""))
                                {

                                    Toast.makeText(getApplicationContext(),"Please insert correct values",Toast.LENGTH_SHORT).show();
                                }else
                                {

                                    SDelete(id,itm);
                                    //Values removed
                                    showToast("entry deleted");
                                    dialog.dismiss();

                                }

                            }
                        });}catch (Exception e){Toast.makeText(getApplicationContext(),e.toString()+"bahr",Toast.LENGTH_LONG).show();}
                    cancelBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }});


                }
            });


            //groceryItems[0]=" ";
            //listview
            String in="";
            String pr="";
            double sum=0.0;
            int cnt=0;
            try{Cursor c= SView(id);
                c.moveToFirst();
                do{
                    groceryItems[cnt]=c.getString(1);
                    groceryPrices[cnt]=c.getString(2);
                    //showToast(groceryItems[cnt]);
                    in+=groceryItems[cnt]+"\n";
                    pr+=groceryPrices[cnt]+"\n";
                    sum+= Double.parseDouble(c.getString(2));
                    cnt++;
                }while (c.moveToNext());


                }

            catch (Exception e){showToast(e.toString()+"andar");}
            info.setText(in);
            p.setText(pr);
            //showToast(String.valueOf(sum));
            s.setText(String.valueOf(sum));



        }catch (Exception e){//Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }
}
