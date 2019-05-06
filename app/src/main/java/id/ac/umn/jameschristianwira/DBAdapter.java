package id.ac.umn.jameschristianwira;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBAdapter extends SQLiteOpenHelper {
    public static String DB_PATH;

    public static String DB_NAME;
    public SQLiteDatabase db;
    public Context context;

    private static String TABLE_NAME = "users";
    public static final String COL_USERNAME = "user_name";
    public static final String COL_PASSWORD = "user_password";

    public DBAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);

        String packageName = context.getPackageName();
        DB_PATH = "/data/data/" + packageName + "/databases/";
        //DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        DB_NAME = name;
        this.context = context;
        openDatabase();
    }

    public SQLiteDatabase openDatabase(){
        Log.d(this.getClass().toString(), "openDatabase()");
        String path = DB_PATH + DB_NAME;
        if(db == null){
            createDatabase(path);
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        }
        return db;
    }

    public void createDatabase(String path){
        System.out.println("DB_NAME in createDatabase: " + DB_NAME);
        System.out.println("DB_PATH in createDatabase: " + DB_PATH);
        System.out.println("path in createDatabase: " + path);
        boolean dbExist = checkDB();
        if (!dbExist){
            this.getReadableDatabase();
            try {
                Log.d(this.getClass().toString(), "TRY copyDatabase()");
                copyDatabase();
            }
            catch (IOException e){
                Log.e(this.getClass().toString(), "Copying Error!");
                //throw new Error("Error copying database!");
            }
        }
        else {
            Log.i(this.getClass().toString(), "Database already exist!");
        }
    }

    private boolean checkDB(){
        String path = DB_PATH + DB_NAME;
        File dbFile = new File(path);
        if(dbFile.exists()) Log.i(this.getClass().toString(), "Database exist");
        else Log.i(this.getClass().toString(), "Database doesn't exist");
        return dbFile.exists();
    }

    private void copyDatabase() throws IOException{
        System.out.println("DB_NAME in copyDatabase: " + DB_NAME);

        InputStream externalDBStream = context.getAssets().open(DB_NAME);
        Log.d(this.getClass().toString(), "AFTER OPEN ASSET");
        //Log.d("Debug", externalDBStream.toString());

        String outFileName = DB_PATH + DB_NAME;
        Log.d(this.getClass().toString(), outFileName);

        OutputStream localDBStream = new FileOutputStream(outFileName);
        Log.d("Debug", localDBStream.toString());
        Log.d(this.getClass().toString(), "AFTER OutputStream");

        Log.d(this.getClass().toString(), "START LOOPING");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDBStream.read(buffer)) > 0){
            localDBStream.write(buffer,0, bytesRead);
        }
        Log.d(this.getClass().toString(), "AFTER LOOPING");

        localDBStream.flush();
        localDBStream.close();
        externalDBStream.close();
    }

    @Override
    public synchronized void close() {
        if(db != null){
            db.close();
        }
        super.close();
    }

    public Cursor getCredential(){
        Cursor cursor = db.query(TABLE_NAME, new String[] {COL_USERNAME, COL_PASSWORD}
        , null, null, null, null, null);
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
