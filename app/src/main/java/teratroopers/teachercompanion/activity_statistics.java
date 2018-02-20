package teratroopers.teachercompanion;

import android.content.Context;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class activity_statistics extends AppCompatActivity implements View.OnClickListener {
    mydbhelper mydb;

    Button b1;
    LinearLayout linearLayout;
    int i;
    String cname;
    String str;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayoutstat);
        this.mydb = new mydbhelper((Context)this);
        context=this;
        createCards();
        back();
    }
    public void createCards(){
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,150);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
        lp1.setMargins(20, 20, 20, 10); //ltrd
        final Cursor res=mydb.getalldata();
        final Handler handler = new Handler();

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
                            new AlertDialog.Builder(getApplicationContext())
                                    .setTitle("Do you want to delete the class "+cname+"  ?")
                                    .setMessage("You can not undo the action")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Toast.makeText(getApplicationContext(),cname+" is deleted from the records",Toast.LENGTH_SHORT).show();
                                            mydb.deleteclass(str);
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

        //Toast.makeText(getActivity(),str+" clicked",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent("teratroopers.teachercompanion.statistics");
        intent.putExtra("name",str);
        startActivity(intent);
        //test1(str);

    }
    public void back(){
        Button back =(Button)findViewById(R.id.backstat);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void test1(String str){
        Cursor req = mydb.statistics1(str);
        req.moveToNext();
        String g = req.getString(0);
        test(str,g);
    }

    public void test(String str,String g){

        Cursor res= mydb.statistics(str);
       // Cursor req = mydb.statistics1(str);
        StringBuffer buffer = new StringBuffer();
        // buffer.append(date+":-\n");
        // Log.i("hello11",res.getString(2));
        res.moveToNext();
        int x=1;
        int y=Integer.parseInt(res.getString(1));
        buffer.append("ROLLNO  "+ "PRESENT \t"+"PERCENTAGE\n");
        buffer.append("\t\t"+res.getString(0) + "\t\t \t \t");
        buffer.append(y + "\t\t\t\t\t\t" + (y*100)/Integer.parseInt(g)+"%\n");
        int a=Integer.parseInt(res.getString(1));
       // String g = req.getString(0);
        while (res.moveToNext()) {
            x++;
            y=Integer.parseInt(res.getString(1));
            a=a+Integer.parseInt(res.getString(1));
            buffer.append("\t\t"+res.getString(0) + "\t\t\t \t");
            buffer.append(y + "\t\t\t\t\t\t" + (y*100)/Integer.parseInt(g)+"%\n");
        }
        buffer.append("\n");
       // buffer.append(x);
        buffer.append("\n");
       // buffer.append(a);
        buffer.append("\n");
        buffer.append("TOTAL NO OF CLASSES TAKEN :"+g+"\n");
        buffer.append("TOTAL CLASS PERCENTAGE IS:"+(a*100)/(x*Integer.parseInt(g))+"%");
        showmessage("CLASS STATISTICS", buffer.toString());
    }

    public void showmessage(String title,String Message) {
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
