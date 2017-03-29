import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by USER on 3/28/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db";
    public static final String TABLE_NAME = "fidelDictionary";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_fidel = "fidel";
    public static final String COLUMN_letters = "letters";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table fidelDictionary " +
                        "(id integer primary key,fidel text,letters text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS fidelDictionary");
        onCreate(db);
    }

    public boolean insertFidel (String fidel, String letters) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fidel", fidel);
        contentValues.put("letters", letters);
        db.insert("fidelDictionary", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from fidelDictionary where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateDictionary (Integer id, String fidel, String letters) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fidel", fidel);
        contentValues.put("letters", letters);
        db.update("fidelDictionary", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public ArrayList<String> getAllDictionaries() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashDictionary();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from fidelDictionary", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_fidel)));
            res.moveToNext();
        }
        return array_list;
    }
}
