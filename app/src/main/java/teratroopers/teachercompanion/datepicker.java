package teratroopers.teachercompanion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class datepicker extends AppCompatActivity {
    Button view;
    public String date,cname;
    mydbhelper mydb;
    RegisterForm rf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);
        this.mydb = new mydbhelper((Context)this);
        this.rf= new RegisterForm();
        Bundle b = getIntent().getExtras();
        cname = b.getString("cname");
        view();
    }
    public void view(){
        view= (Button) findViewById(R.id.button9);
        view.setOnClickListener(
                new
                        View.OnClickListener() {
                            public void onClick(View view) {
                                try{
                                    check();
                                }
                                // Log.i("in click","ckick");
                                // check();
                                catch(Exception e){
                                    Snackbar.make(view,"required date attendance is not taken until now",Snackbar.LENGTH_SHORT).show();
                                }


                            }
                        }
        );
    }
    public void check() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        String d, m;
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        if (day < 10)
            d = "0" + day;
        else
            d = String.valueOf(day);
        if (month < 10)
            m = "0" + month;
        else
            m = String.valueOf(month);

        date = "dt" + d + m + year;

        Intent intent=new Intent("teratroopers.teachercompanion.RegisterForm");
        intent.putExtra("name",cname);
        intent.putExtra("value",date);
        startActivity(intent);
    }
}
