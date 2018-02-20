package teratroopers.teachercompanion;

import  android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class mydbhelper extends SQLiteOpenHelper {

    private static final String DATABSE_NAME="student.sqLiteDatabase";
    private static  String TABLE_NAME;
    private static final String cTABLE_NAME="cTABLE";
    private static final String settings="Settings";
    private static final String COL1="rollnos";
    private static final String COL2="studnames";
    private static final String COL3="count";
    private static final String CTCOL1="classname";
    private static final String CL1="key";
    private static final String CTCOL2="total";
    private List<Integer> list = new ArrayList<>();
    private List<Integer> li = new ArrayList<>();
    private List<String> lis = new ArrayList<>();

    Cursor req;
    boolean k=false;
    int g,count,i;


    public mydbhelper(Context context) {

        super(context,DATABSE_NAME, null, 1);
    }

    public SQLiteDatabase sqLiteDatabase;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
        ContentValues contentvalues = new ContentValues();
        Log.i("info", sqLiteDatabase.toString());
        sqLiteDatabase.execSQL("create table " + cTABLE_NAME + "(" + CTCOL1 + " TEXT, " + CTCOL2 + " INTEGER);");
        sqLiteDatabase.execSQL("create table " + settings + "(" + CL1 + " INTEGER);");
        sqLiteDatabase.execSQL("INSERT INTO Settings VALUES(0)");
        sqLiteDatabase.execSQL("INSERT INTO Settings VALUES(5)");
        sqLiteDatabase.execSQL("INSERT INTO Settings VALUES(7)");
        sqLiteDatabase.execSQL("INSERT INTO Settings VALUES(9)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData( int sr, int er) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        for(int i=sr;i<=er;i++) {
            contentvalues.put(COL1, i);
            contentvalues.put(COL2,"");
            contentvalues.put(COL3,0);
            long result = sqLiteDatabase.insert(TABLE_NAME, null, contentvalues);
            if (result == -1) {
                k= false;
            }
            else {
                k= true;
            }
        }
        return k;
    }
    public Cursor getalldata() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from " + cTABLE_NAME, null);
        return res;

    }
    public Cursor getcname(String cname){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor result=sqLiteDatabase.rawQuery("Select "+COL1+" from "+cname,null);
        return result;
    }

    public boolean dbname(String cname, int sr, int er){   //test.java activity (class add)
        ContentValues contentvalues = new ContentValues();
        sqLiteDatabase=this.getWritableDatabase();
        TABLE_NAME=cname;
        Log.i("tname",TABLE_NAME);

        sqLiteDatabase.execSQL("create table if not exists "+TABLE_NAME+"("+COL1+" INTEGER,"+COL2+ " TEXT, "+COL3+" INTEGER);");
        boolean c=checkclassname();
        if(c==true) {
            contentvalues.put(CTCOL1,TABLE_NAME);
            contentvalues.put(CTCOL2,0);
            sqLiteDatabase.insert(cTABLE_NAME, null, contentvalues);

            Log.i("class table insertion:", "success");
            k = insertData(sr, er);
            Log.i("our nikhil:", "success");
            return k;
        }
        else return false;
    }

    public boolean checkclassname(){ //checks class name for existence

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Log.i("came to checkclassname","success");
        Cursor result = sqLiteDatabase.rawQuery("Select count(*) from " + cTABLE_NAME + " where " + CTCOL1 + "=" + "'"+TABLE_NAME+"'", null);
        result.moveToNext();
        int k=Integer.parseInt(result.getString(0));
        Log.i("value of k:",String.valueOf(k));
        return k == 0 ? true : false;
    }

    public void deleteclass(String classname){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        //sqLiteDatabase.execSQL("delete TABLE IF EXISTS " + classname);
        sqLiteDatabase.delete(classname,null,null);
        sqLiteDatabase.delete(cTABLE_NAME,CTCOL1+"="+"'"+classname+"'",null);
        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + classname);
        }
        catch (Exception e){
            Log.i("deleteclass","exception");
        }
    }


    public void alterTable(String date,String cname,int sroll,int eroll){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        //ContentValues contentValues = new ContentValues();
        boolean k=isFieldExist(cname,date);
        if(k) {
            try{
                sqLiteDatabase.execSQL("alter table " + cname + " add "+date+" INTEGER");
                Log.i("table altered:", "success");
                for(int i=sroll;i<=eroll;i++) {
                    sqLiteDatabase.execSQL("UPDATE " + cname + " SET " + date + " = " + "0" + " WHERE " + COL1 + " = " + i);
                }
            }
            catch (Exception e){
                Log.i("Attendance taken:","finish");
                e.printStackTrace();
            }
        }
        else {
            Log.i("column alreadyis taken:","finish");
        }
    }

    public Cursor retrievedatatodisplayattendance(String date,String classname){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
         Cursor result = sqLiteDatabase.rawQuery("Select " + COL1 + "," + COL3 + "," + date + " from " + classname, null) ;
            return result;

    }
    public Cursor retrievetoxml(String classname){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("Select * from "+classname,null);
        return  result;
    }

    public void registerData(String date,String cname,int droll,int i,int sroll,int eroll){
        li.add(droll);
        list.add(i);
        g=droll;
        if(g==eroll) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            for(i=0;i<(eroll-sroll)+1;i++) {
                if(sroll==li.get(i)){
                    count=0;
                }
                if(count==0){
                    sqLiteDatabase.execSQL("UPDATE " + cTABLE_NAME + " SET " + CTCOL2 + " = " + CTCOL2 + " + " + 1 + " WHERE " + CTCOL1 + " = " + "'" +cname + "'");
                    count++;
                }
                sqLiteDatabase.execSQL("UPDATE " + cname + " SET " + date + " = " + date + " + " + list.get(i) + " WHERE " + COL1 + " = " + li.get(i));
                sqLiteDatabase.execSQL("UPDATE " + cname + " SET " + COL3 + " = " + COL3 + " + " + list.get(i) + " WHERE " + COL1 + " = " + li.get(i));
            }
        }
    }

    public boolean isFieldExist(String tableName, String fieldName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("PRAGMA table_info("+tableName+")",null);
        int i= res.getColumnIndex(fieldName);
        res.close();

        return i == -1 ? true : false;
    }
    public Cursor retrievedata(String cname){
         SQLiteDatabase db = this.getWritableDatabase();
        req = db.rawQuery("PRAGMA table_info("+cname+")",null);
        req.moveToLast();
        int a=req.getInt(0);
        Log.i("msg",Integer.toString(a));
        if(a>4){
            for(int i=0;i<3;i++) {
                lis.add(req.getString(1));
                req.moveToPrevious();
            }
            req.close();
            i=0;
            req = db.rawQuery("Select " + COL1  + "," + COL3 + "," + lis.get(i+2)+ "," + lis.get(i+1) + "," +lis.get(i)+ " from " + cname, null);
        }
        else{
            i=0;
            a=a-2;
            if(a==2){
                for(int i=0;i<2;i++) {
                    lis.add(req.getString(1));
                    req.moveToPrevious();
                }
                req.close();
                req = db.rawQuery("Select " + COL1  + "," + COL3 + "," + lis.get(i+1)+ "," +lis.get(i) + " from " + cname, null);
            }
            else if(a==1){
                lis.add(req.getString(1));
                req.close();
                req = db.rawQuery("Select " + COL1  + "," + COL3 + "," + lis.get(i) + " from " + cname, null);
            }
            else{
                req.close();
                req=db.rawQuery("Select " + COL1 +  "," + COL3 + " from " + cname, null);
            }
        }
        return req;
    }
    public Cursor statistics(String cname){
        SQLiteDatabase db = this.getWritableDatabase();
        req =db.rawQuery("Select "+COL1+"," + COL3 + " from " + cname,null);
        return req;
    }
    public Cursor statistics1(String cname){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("Select " +CTCOL2 + " from " + cTABLE_NAME + " where " + CTCOL1 + "=" + "'"+cname+"'", null);
        return result;
    }
    public void vibration(int a){
        if(a==1){
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("UPDATE " + settings + " SET " + CL1 + "= 1 where " + CL1 + "= 0");
            }
            catch (Exception e){
              e.printStackTrace();
            }
        }
        else{
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("UPDATE " + settings + " SET " + CL1 + "= 0 where " + CL1 + "= 1");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public int vibration1(){
        SQLiteDatabase db = this.getWritableDatabase();
        int a;
        Cursor c = db.rawQuery("select * from "+ settings ,null);
        c.moveToNext();
        a = c.getInt(0);
        c.close();
        return  a;
    }
    public int bt(int a,boolean b){
        if(b){
          try {

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE " + settings + " SET " + CL1 + "= "+a+" where " + CL1 + "= 5");
              Log.i("if","true");
              return 1;
          }
          catch (Exception e){
             return 0;
          }
        }
        else{
            SQLiteDatabase db = this.getWritableDatabase();
            int d=check1();
            if(d==a) {
                db.execSQL("UPDATE " + settings + " SET " + CL1 + "= 5 where " + CL1 + "= " + d);
                return 1;
            }
            else{
                return 0;
            }

        }
    }
    public int check1(){
        SQLiteDatabase db = this.getWritableDatabase();
        int d=5;
        try {
            Cursor c = db.rawQuery("select * from " + settings, null);
            c.moveToNext();
            c.moveToNext();
            d = c.getInt(0);
            c.close();
        }
        catch (Exception e){}
        return d;
    }
    public void password(int a){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE " + settings + " SET " + CL1 + "= "+ a +" where " + CL1 + "= 7");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public int password1(String a) {
        SQLiteDatabase db = this.getWritableDatabase();
        int d;
        Cursor c = db.rawQuery("select * from " + settings, null);
        c.moveToNext();
        c.moveToNext();
        c.moveToNext();
        d = c.getInt(0);
        int b=Integer.parseInt(a);
        if(b==d){
            d=1;
        }
        else{
            d=0;
        }
        c.close();
        return d;

    }
    public void password2(String s){
        SQLiteDatabase db = this.getWritableDatabase();
        int a=Integer.parseInt(s);
             try {
                 db.execSQL("UPDATE " + settings + " SET " + CL1 + "= 7 where " + CL1 + "= " + a);
             }catch(Exception e){
                 e.printStackTrace();
             }
    }
    public void phoneNumber(long num){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try{
            sqLiteDatabase.execSQL("UPDATE "+settings+" SET "+CL1+"= "+num+" where "+CL1+" = 9");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkNumber(){
        boolean isExists=true;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res=sqLiteDatabase.rawQuery("select * from "+settings,null);
        res.moveToPosition(3);
        if(res.getInt(0)==9) {
            isExists = false;
            Log.i("yeah","i was here");
        }
        Log.i("number",String.valueOf(res.getLong(0)));
        return isExists;
    }
    public long getphoneNumber(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor res=sqLiteDatabase.rawQuery("select * from "+settings,null);
        res.moveToPosition(3);
        long num=res.getLong(0);
        return num;
    }

    //1.vibration
    //2.lock/unlock (pin)
    //3.otp
    //4.phone number

}



