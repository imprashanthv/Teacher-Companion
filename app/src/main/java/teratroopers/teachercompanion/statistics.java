package teratroopers.teachercompanion;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class statistics extends AppCompatActivity {

    mydbhelper mydb;
    String cname;
    TextView view1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics2);
        mydb =new mydbhelper(this);
        Bundle b = getIntent().getExtras();
        cname = b.getString("name");
        values(cname);
    }
    public void values(String name){
        view1 = (TextView) findViewById(R.id.textView25);
        test1(name);

    }
    public void test1(String str){
        Cursor req = mydb.statistics1(str);
        req.moveToNext();
        String g = req.getString(0);
        test(str,g);
    }

    public void test(String str,String g){
        try {
            Cursor res = mydb.statistics(str);
            // Cursor req = mydb.statistics1(str);
            StringBuffer buffer = new StringBuffer();
            TextView cn=(TextView)findViewById(R.id.classnid);
            cn.setText(str);
            res.moveToNext();
            int x = 1;
            int y = Integer.parseInt(res.getString(1));
            buffer.append("ROLLNO  " + "PRESENT \t" + "PERCENTAGE\n");
            buffer.append("\t\t" + res.getString(0) + "\t\t \t\t \t");
            buffer.append(y + "\t\t\t\t\t\t\t" + (y * 100) / Integer.parseInt(g) + "%\n");
            int a = Integer.parseInt(res.getString(1));
            // String g = req.getString(0);
            while (res.moveToNext()) {
                x++;
                y = Integer.parseInt(res.getString(1));
                a = a + Integer.parseInt(res.getString(1));
                buffer.append("\t\t" + res.getString(0) + "\t\t\t \t\t");
                buffer.append(y + "\t\t\t\t\t\t\t" + (y * 100) / Integer.parseInt(g) + "%\n");


                //buffer.append("Ending Roll :" + res.getString(2) + "\n");
            }
            buffer.append("\n");
            buffer.append("TOTAL NO OF CLASSES TAKEN :" + g + "\n");
            buffer.append("TOTAL CLASS PERCENTAGE IS:" + (a * 100) / (x * Integer.parseInt(g)) + "%");
            buffer.append("\n\n\n\n\n\n\n\n");
            // showmessage("CLASS STATISTICS", buffer.toString());
            view1.setText(buffer.toString());
        }
        catch (Exception e){

        }
    }
}
