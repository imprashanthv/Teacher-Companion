package teratroopers.teachercompanion;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import de.codecrafters.tableview.TableView;
import android.graphics.Color;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
public class RegisterForm extends AppCompatActivity {

    mydbhelper mydb;
    String cname,value;
    Button b;
    int a;
    Cursor result;
    Context context;

    static String[] classColNames;
    static String[][] classData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_register_form);
        mydb = new mydbhelper(this);
        Bundle bd = getIntent().getExtras();
         cname = bd.getString("name");
         value = bd.getString("value");
        button();
        if(value.equals("register")){
            result = mydb.retrievedata(cname);
            int c= result.getColumnCount();
            if(c>3){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            a=0;
        }
        else{
            try {
                result = mydb.retrievedatatodisplayattendance(value, cname);
                a=0;
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "required date attendance is not taken",
                        Toast.LENGTH_SHORT).show();
                a=1;
            }
        }
        if(a==0) {
            classColNames = result.getColumnNames();
            int b=result.getColumnCount();
            char st='d';
            for (int i=0;i<b;i++) {
                StringBuilder strb = new StringBuilder(classColNames[i]);
                String str = classColNames[i];
                char d=str.charAt(0);
                if(d==st) {
                    strb.deleteCharAt(0);
                    strb.deleteCharAt(0);
                    str.replace("d","");
                    str.replace("t","");
                    classColNames[i] = strb.toString();
                    str=classColNames[i];
                    if ( Character.isDigit(str.charAt(0)) ){
                        strb.insert(2, '/');
                        strb.insert(5, '/');
                        classColNames[i] = strb.toString();
                    }
                }

            }

            classData = new String[result.getCount()][result.getColumnNames().length];
            result.moveToFirst();
            for (int i = 0; i < result.getCount(); i++) {
                for (int j = 0; j < result.getColumnNames().length; j++) {
                    try {
                        classData[i][j] = result.getString(j);
                    } catch (NullPointerException npe) {
                        classData[i][j] = "";
                    }
                }
                result.moveToNext();
            }


            final TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);

            tableView.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, classColNames));
            tableView.setColumnCount(result.getColumnCount());
            tableView.setDataAdapter(new SimpleTableDataAdapter(this, classData));

            int colorEvenRows = getResources().getColor(R.color.white);
            int colorOddRows = getResources().getColor(R.color.gray);
            tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));

            //comment the following code if there's any error
            TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(this, result.getColumnCount(), 120);
            columnModel.setColumnWidth(0,100);
            columnModel.setColumnWidth(1,100);
            tableView.setColumnModel(columnModel);

            tableView.addDataClickListener(new TableDataClickListener() {
                @Override
                public void onDataClicked(int rowIndex, Object clickedData) {
                    Toast.makeText(getApplicationContext(), ((String[]) clickedData)[1], Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void button(){

       b=(Button)findViewById(R.id.button8);
        if(value.equals("register"))
        {
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    Intent intent=new Intent("teratroopers.teachercompanion.datepicker");
                    Bundle b = new Bundle();
                    b.putString("cname",cname);
                    intent.putExtras(b);
                    //intent.putExtra("name",cname);
                    //startActivity(intent);
                    startActivity(intent);
                    //finish();
                }
            });
        }
        else{
            b.setVisibility(View.INVISIBLE);
        }
    }
}


