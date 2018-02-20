package teratroopers.teachercompanion;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakeAttendence extends AppCompatActivity {

    mydbhelper mydb;
    int sroll,eroll;
    TextView disbutton;
    Button presbutton;
    Button absbutton;
    int total;
    int droll;
    int a,b;
    String k,pres;
    String date;
    String cname;
    public Context context;
    TextView tv;
    int count=0;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);
        context=this;
        mydb =new mydbhelper(this);
        Bundle b = getIntent().getExtras();
        cname = b.getString("name");

        vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        check();

        //goToClass gtc=new goToClass(cname);
        getValues(cname);
        display();
        presentButton();
        absentButton();
        buttonclickfordisplayingvalues();
    }

    public void getValues(String name) {

        disbutton = (TextView) findViewById(R.id.textView2);
        Cursor res = mydb.getcname(name);
        res.moveToNext();
        sroll = Integer.parseInt(res.getString(0));
        droll=sroll;
        Log.i("sroll",String.valueOf(sroll));
        res.moveToLast();
        eroll = Integer.parseInt(res.getString(0));
        tv=(TextView)findViewById(R.id.count);
        k=String.valueOf((eroll-sroll)+1);
        pres="0";
        tv.setText(pres+"/"+k+" present");
    }
    public void display(){
        String number=Integer.toString(droll);
        disbutton.setText(number);
    }

    public void presentButton(){
        total=(eroll-sroll)+1;
        presbutton=(Button)findViewById(R.id.present);
        presbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            if(b==1) {
                                vibrator.vibrate(50);
                            }

                        count++;
                        pres=String.valueOf(count);
                        tv.setText(pres+"/"+k+" present");
                        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                        date = sdf.format(new Date());
                        date = "dt" + date;
                        if(droll<eroll) {
                            if (droll == sroll) {
                                Log.i("first:","droll=sroll");
                                        mydb.alterTable(date,cname,sroll,eroll);
                            }
                            mydb.registerData(date,cname, droll, 1,sroll,eroll);
                            droll++;
                            display();
                        }
                        else if(droll==eroll){
                            mydb.registerData(date,cname, droll, 1,sroll,eroll);
                            // disbutton.setText("Attendance complete");
                            presbutton.setClickable(false);
                            absbutton.setClickable(false);
                            Snackbar.make(view,"Attendance Complete",Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Return",new View.OnClickListener(){
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                        }
                                    }).show();
                        }
                    }
                }
        );
    }

    public void absentButton(){
        a=sroll;
        total=(eroll-sroll)+1;
        absbutton=(Button)findViewById(R.id.absent);
        absbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                           if(b==1) {
                               vibrator.vibrate(50);
                           }

                        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                        date = sdf.format(new Date());
                        date="dt"+date;
                        if(droll<eroll) {
                            if (droll == sroll) {
                                mydb.alterTable(date,cname,sroll,eroll);
                            }
                            mydb.registerData(date,cname,droll,0,sroll,eroll);
                            droll++;
                            display();
                        }
                        else if(droll==eroll){
                            mydb.registerData(date,cname, droll, 0,sroll,eroll);
                            absbutton.setClickable(false);
                            presbutton.setClickable(false);
                            Snackbar.make(view,"Attendance Complete",Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Return",new View.OnClickListener(){
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                        }
                                    }).show();
                        }
                    }
                }
        );
    }

    public void buttonclickfordisplayingvalues(){
        FloatingActionButton  butt = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        butt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                        date = sdf.format(new Date());
                        date="dt"+date;
                        try {
                            Cursor res = mydb.retrievedatatodisplayattendance(date, cname);
                            StringBuffer buffer = new StringBuffer();
                            while (res.moveToNext()) {

                                buffer.append(res.getString(0) + "=");
                                buffer.append(res.getString(2) + "\t" + res.getString(1) + "\n");
                                //buffer.append("Ending Roll :" + res.getString(2) + "\n");
                            }
                            showmessage("Data", buffer.toString());
                        }catch (Exception e){
                            showmessage(":(","Attendance not taken");
                        }
                    }
                }
        );
    }

    public void showmessage(String title,String Message) {
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Today's Attendance");
        builder.setIcon(R.drawable.book);
        builder.setMessage(Message);
        builder.show();

    }
    public void check(){
        b=mydb.vibration1();
    }

}




