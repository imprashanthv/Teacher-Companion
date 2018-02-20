package teratroopers.teachercompanion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.app.ActionBar.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;



public class ClassList extends AppCompatActivity implements View.OnClickListener {

    mydbhelper mydb;
    Button b1;
    LinearLayout linearLayout;
    int i;
    String cname;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        this.mydb = new mydbhelper((Context)this);
        createCards();
        back();
    }

    public void createCards(){
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,150);
        lp1.setMargins(20, 20, 20, 10); //ltrd
        final Cursor res=mydb.getalldata();
        while (res.moveToNext()) {
            i=0;
            cname=res.getString(0);
            b1 = new Button(this);
            b1.setText(res.getString(0));
            b1.setTag(res.getString(0));
            b1.setElevation(3.8f);
            b1.setBackgroundResource(R.drawable.backbutt);
            b1.setLayoutParams(lp1);
            b1.setGravity(Gravity.CENTER);
            linearLayout.addView(b1);
            b1.setOnClickListener(this);
            b1.setOnLongClickListener(
                    new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            str=view.getTag().toString();
                            new AlertDialog.Builder(ClassList.this)
                                    .setTitle("Do you want to delete the class "+str+"  ?")
                                    .setMessage("You can not undo the action")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Toast.makeText(getApplicationContext(),str+" is deleted from the records",Toast.LENGTH_SHORT).show();
                                            mydb.deleteclass(str);
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                           // Bundle configBundle = new Bundle();
                                           //onCreate(savedInstanceState);
                                        }})
                                    .setNegativeButton(android.R.string.no, null).show();
                            return true;
                        }

                    }

            );

        }

    }

    public void onClick(View v) {
        String str=v.getTag().toString();
        Intent intent=new Intent("teratroopers.teachercompanion.TakeAttendence");
        intent.putExtra("name",str);
        startActivity(intent);
    }
    public void back(){
        Button back =(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
