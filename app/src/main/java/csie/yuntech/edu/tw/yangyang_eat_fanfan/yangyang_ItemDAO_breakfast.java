package csie.yuntech.edu.tw.yangyang_eat_fanfan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by a on 2015/12/5.
 */
public class yangyang_ItemDAO_breakfast {
    // 表格名稱
    public static final String TABLE_NAME = "breakfast";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String RESTAURANT_NAME_COLUMN = "restaurant_name";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RESTAURANT_NAME_COLUMN + " TEXT NOT NULL" +
                    " );";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public yangyang_ItemDAO_breakfast(Context context) {
        db = yangyang_DBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public long insert(String restaurant_name) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(RESTAURANT_NAME_COLUMN, restaurant_name);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 回傳結果
        return id;
    }

    // 修改參數指定的物件
    public boolean update(int update_id, String restaurant_name) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(RESTAURANT_NAME_COLUMN, restaurant_name);


        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + update_id;

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long del_id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + del_id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where, null) > 0;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    // 讀取所有記事資料
    public Cursor getAll() {
        Cursor cursor = db.rawQuery( "select " + KEY_ID + "," + RESTAURANT_NAME_COLUMN  + " from " +
                TABLE_NAME, null);

        return cursor;
    }

    public void simple()
    {
        String[] simple_restaurant = new String[]{"微笑", "榕樹下", "晶品", "賞味家", "晨間", "活力YO", "洪爺", "科大", "三姊妹"};

        for (int i = 0; i < simple_restaurant.length; i++  )
        {
            this.insert(simple_restaurant[i]);
        }
    }
}
