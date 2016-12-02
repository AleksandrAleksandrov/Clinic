package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aleksandr.aleksandrov.clinic.Resources;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "clinicDB";

    public static final String SPECIALIZATIONS_TABLE = "specializations";
    public static final String REMIND_TABLE = "reminds";
    public static final String SEND_APPEAL_AGAIN = "sendAppeal";

    public static final String SPECIALIZATION_COLUMN = "specialization";
    public static final String DATE_COLUMN = "date";
    public static final String SUCCESS = "success";

    //private DBQueryManager queryManager;

    public DBHelper(Context context) {
        //Конструктор суперкласса. гастроинтеролог 53 эндокринолог 54 кардиолог 55
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //queryManager = new DBQueryManager(getReadableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Создаем таблицу с полями.
        db.execSQL("create table " + REMIND_TABLE + " (" + Resources._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SPECIALIZATION_COLUMN + " string,"
                + DATE_COLUMN + " long"
                + ");");
        db.execSQL("create table " + SPECIALIZATIONS_TABLE + " ("
                + "id integer,"//id instead of spec_id
                + "name string"//name instead of spec_name
                + ");");
        db.execSQL("create table " + SEND_APPEAL_AGAIN + " (" + Resources._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Resources.TAG_ID_SPEC + " string,"
                + Resources.TAG_APPSPECIALIZATION_NAME + " string,"
                + Resources.TAG_APP_DATE + " string,"
                + Resources.TAG_APP_TIME + " string,"
                + Resources.TAG_APP_ONAME + " string,"
                + Resources.TAG_APP_OEMAIL + " string,"
                + Resources.TAG_APP_OPHONE + " string,"
                + Resources.TAG_APP_DESCRIPTION + " string,"
                + Resources.TAG_TIME_STAMP + " long"
                + ");");

    }

//    public DBQueryManager query() {
//        return queryManager;
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}