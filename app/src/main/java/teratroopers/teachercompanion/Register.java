package teratroopers.teachercompanion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.Locale;
import android.util.*;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;

public class Register extends AppCompatActivity implements View.OnClickListener {

    mydbhelper mydb;
    Button b1;
    LinearLayout linearLayout;
    int i;
    String cname,str;
    static String[] classColNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout2);
        this.mydb = new mydbhelper((Context)this);
        back();
        createcards();
    }

    public void back(){
        Button back =(Button)findViewById(R.id.backpress);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void createcards(){
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,150);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
        lp1.setMargins(20, 20, 20, 10); //ltrd
        final Cursor res=mydb.getalldata();
        final Handler handler = new Handler();

        while (res.moveToNext()) {
            i = 0;
            cname = res.getString(0);
            b1 = new Button(this);
            b1.setText(res.getString(0));
            b1.setTag(res.getString(0));

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
                            new AlertDialog.Builder(Register.this)
                                    .setTitle("Do you want to export the class "+"  ?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                             convert(str);
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
        Intent intent=new Intent("teratroopers.teachercompanion.RegisterForm");
        intent.putExtra("name",str);
        intent.putExtra("value","register");
        startActivity(intent);
    }
    public void convert(String str){
        Cursor cursor = mydb.retrievetoxml(str);
        classColNames=cursor.getColumnNames();
        int a=cursor.getColumnCount();

        File sd = Environment.getExternalStorageDirectory();
        String csvFile = str+".xls";

        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            File file = new File(directory, csvFile);

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook;

            workbook = Workbook.createWorkbook(file, wbSettings);

            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);

            // column and row
            for(int i=0;i<a;i++) {
                sheet.addCell(new Label(i, 0, classColNames[i]));
            }
            if (cursor.moveToFirst()) {
                do {
                    int i = cursor.getPosition() + 1;
                    for(int b=0;b<a;b++) {
                        String rollno = cursor.getString(b);
                        sheet.addCell(new Label(b, i, rollno));
                    }
                } while (cursor.moveToNext());
            }

            //closing cursor
            cursor.close();
            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(),
                    "Data Exported to a Excel Sheet", Toast.LENGTH_SHORT).show();
        } catch(Exception e){
            e.printStackTrace();
          }
    }
}

