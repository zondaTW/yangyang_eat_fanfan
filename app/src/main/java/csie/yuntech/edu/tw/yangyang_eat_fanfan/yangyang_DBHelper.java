package csie.yuntech.edu.tw.yangyang_eat_fanfan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by a on 2015/12/5.
 */
public class yangyang_DBHelper extends SQLiteOpenHelper {
    // 資料庫名稱
    public static final String DATABASE_NAME = "yangyang_eat_fanfan.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;
    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;

    // 建構子，在一般的應用都不需要修改
    public yangyang_DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new yangyang_DBHelper(context, DATABASE_NAME, null, VERSION).getWritableDatabase();
        }
        return database;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //不存在時，建表，並加入預設SIMPLE
        if (!this.tabIsExist(yangyang_ItemDAO_breakfast.TABLE_NAME))
        {
            // 建立應用程式需要的表格
            db.execSQL(yangyang_ItemDAO_breakfast.CREATE_TABLE);
            breakfast_simple(db);
        }

        if (!this.tabIsExist(yangyang_ItemDAO_lunch.TABLE_NAME))
        {
            // 建立應用程式需要的表格
            db.execSQL(yangyang_ItemDAO_lunch.CREATE_TABLE);
            lunch_simple(db);
        }

        if (!this.tabIsExist(yangyang_ItemDAO_dinner.TABLE_NAME))
        {
            // 建立應用程式需要的表格
            db.execSQL(yangyang_ItemDAO_dinner.CREATE_TABLE);
            dinner_simple(db);
        }

        if(!this.tabIsExist(yangyang_ItemDAO_mealtime.TABLE_NAME))
        {
            // 建立應用程式需要的表格
            db.execSQL(yangyang_ItemDAO_mealtime.CREATE_TABLE);
            mealtime_simple(db);
        }
    }

    private void breakfast_simple(SQLiteDatabase db) {
        String[] simple_restaurant = new String[]{"微笑", "榕樹下", "晶品", "賞味家", "晨間", "活力YO", "洪爺", "科大", "三姊妹"};
        ContentValues values =new ContentValues();
        for (int i = 0; i < simple_restaurant.length; i++  )
        {
            values.put(yangyang_ItemDAO_breakfast.RESTAURANT_NAME_COLUMN, simple_restaurant[i]);
            db.insert(yangyang_ItemDAO_breakfast.TABLE_NAME, yangyang_ItemDAO_breakfast.KEY_ID, values);
        }
    }

    private void lunch_simple(SQLiteDatabase db) {
        String[] simple_restaurant = new String[]{"常景", "大吉祥", "鐵板燒便當", "FOLLOW ME", "男滷肉飯", "紫軒", "醬人", "磐石", "莊媽媽", "五井", "木火", "雲饌", "周師父", "老夫子" , "鍋貼王"};
        ContentValues values =new ContentValues();
        for (int i = 0; i < simple_restaurant.length; i++) {
            values.put(yangyang_ItemDAO_lunch.RESTAURANT_NAME_COLUMN, simple_restaurant[i]);
            db.insert(yangyang_ItemDAO_lunch.TABLE_NAME, yangyang_ItemDAO_lunch.KEY_ID, values);
        }
    }

    private void dinner_simple(SQLiteDatabase db) {
        String[] simple_restaurant = new String[]{"常景", "大吉祥", "鐵板燒便當", "FOLLOW ME", "男滷肉飯", "紫軒", "醬人", "磐石", "莊媽媽", "五井", "木火", "雲饌", "周師父", "老夫子" , "鍋貼王"};
        ContentValues values =new ContentValues();
        for (int i = 0; i < simple_restaurant.length; i++  )
        {
            values.put(yangyang_ItemDAO_dinner.RESTAURANT_NAME_COLUMN, simple_restaurant[i]);
            db.insert(yangyang_ItemDAO_dinner.TABLE_NAME, yangyang_ItemDAO_dinner.KEY_ID, values);
        }
    }

    private void mealtime_simple(SQLiteDatabase db) {
        String[] simple_mealtime = new String[]{"00:00", "11:30", "10:00", "15:00", "15:00", "23:59"};
        ContentValues values =new ContentValues();
        for (int i = 0; i < simple_mealtime.length; i += 2 )
        {
            values.put(yangyang_ItemDAO_mealtime.MEAL_START_TIME, simple_mealtime[i]);
            values.put(yangyang_ItemDAO_mealtime.MEAL_END_TIME, simple_mealtime[i+1]);
            db.insert(yangyang_ItemDAO_mealtime.TABLE_NAME, yangyang_ItemDAO_mealtime.KEY_ID, values);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 刪除原有的表格
        db.execSQL("DROP TABLE IF EXISTS " + yangyang_ItemDAO_lunch.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + yangyang_ItemDAO_breakfast.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + yangyang_ItemDAO_dinner.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + yangyang_ItemDAO_mealtime.TABLE_NAME);
        // 呼叫onCreate建立新版的表格
        onCreate(db);
    }

    /**
     * 判断某张表是否存在
     * @param tabName 表名
     * @return
     */
    public boolean tabIsExist(String tabName){
        boolean result = false;
        if(tabName == null){
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();//此this是继承SQLiteOpenHelper类得到的
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        finally {
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }
        return result;
    }
}
