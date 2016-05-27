package csie.yuntech.edu.tw.yangyang_eat_fanfan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by a on 2015/12/5.
 */
public class yangyang_random_code extends Activity {
    private yangyang_ItemDAO_breakfast yangyang_ItemDAO_breakfast;
    private yangyang_ItemDAO_lunch yangyang_ItemDAO_lunch;
    private yangyang_ItemDAO_dinner yangyang_ItemDAO_dinner;
    private yangyang_ItemDAO_mealtime yangyang_ItemDAO_mealtime;
    private Button button_random, button_google_map, button_call_yang;
    private TextView textView_restaurant;
    private int randNum;
    private int  rand_count = 0;
    private boolean google_map_flag = false;
    private ArrayList<String> breakfast_restaurant = new ArrayList<String>();
    private ArrayList<String> lunch_restaurant = new ArrayList<String>();
    private ArrayList<String> dinner_restaurant = new ArrayList<String>();
    private ArrayList<String> total_restaurant = new ArrayList<String>();
    private ArrayList<String> mealtime = new ArrayList<String>();
    private Date breakfast_start_time, breakfast_end_time, lunch_start_time, lunch_end_time, dinner_start_time, dinner_end_time, now_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yangyang_random_layout);

        // 建立資料庫物件
        yangyang_ItemDAO_mealtime = new yangyang_ItemDAO_mealtime(getApplicationContext());
        //read table
        mealtime = getArraylist_mealtime("mealtime"); //從資料庫讀出來
        //格式化3餐時間
        final SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        try {
            breakfast_start_time = df.parse(mealtime.get(0));
            breakfast_end_time = df.parse(mealtime.get(1));
            lunch_start_time = df.parse(mealtime.get(2));
            lunch_end_time = df.parse(mealtime.get(3));
            dinner_start_time = df.parse(mealtime.get(4));
            dinner_end_time = df.parse(mealtime.get(5));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //格式化現在時間
        Calendar calender_now_time = Calendar.getInstance();
        try {
            now_time = df.parse(calender_now_time.get(Calendar.HOUR_OF_DAY) + ":" + calender_now_time.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 建立資料庫物件
        yangyang_ItemDAO_breakfast = new yangyang_ItemDAO_breakfast(getApplicationContext());
        yangyang_ItemDAO_lunch = new yangyang_ItemDAO_lunch(getApplicationContext());
        yangyang_ItemDAO_dinner = new yangyang_ItemDAO_dinner(getApplicationContext());
        //read table
        breakfast_restaurant = getArraylist_name("breakfast"); //從資料庫讀出來
        lunch_restaurant = getArraylist_name("lunch");
        dinner_restaurant = getArraylist_name("dinner");

        //判斷3餐的時間，並依照現在時間，添加進TOTAL ARRAYLIST
        if (now_time.after(breakfast_start_time) && now_time.before(breakfast_end_time)) {
            total_restaurant.addAll(breakfast_restaurant);  //arraylist 相加成一個
        }

        if (now_time.after(lunch_start_time) && now_time.before(lunch_end_time)) {
            total_restaurant.addAll(lunch_restaurant);      //arraylist 相加成一個
        }

        if (now_time.after(dinner_start_time) && now_time.before(dinner_end_time)) {
            total_restaurant.addAll(dinner_restaurant);     //arraylist 相加成一個
        }


        button_random = (Button) findViewById(R.id.button_random);
        button_google_map = (Button) findViewById(R.id.button_google_map);
        button_call_yang = (Button) findViewById(R.id.button_call_yang);
        textView_restaurant = (TextView) findViewById(R.id.textView_restaurant);

        button_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total_restaurant.size() != 0)   //當total arraylist沒東西時，不執行
                {
                    //random太多吃，就只能吃屎，並將其他功能都關閉
                    if (rand_count < 5)
                    {
                        randNum = (int) (Math.random() * total_restaurant.size());     // 0 ~ restaurant的長度
                        textView_restaurant.setText(total_restaurant.get(randNum));
                        google_map_flag = true;                                  //當有random過時，google_map_flag = true，才可以使用GOOGLE MAP
                        rand_count ++;
                    }
                    else
                    {
                        textView_restaurant.setText("屎");
                        google_map_flag = false;
                    }

                } else {
                    Toast.makeText(yangyang_random_code.this, "傻B!! 就沒到吃飯時間，吃個屁喔!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_google_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView_restaurant.getText().toString() == "屎") {
                    Toast.makeText(yangyang_random_code.this, "傻B!! 吃屎去吧!!!", Toast.LENGTH_SHORT).show();
                }
                else if (google_map_flag) {
                    //Toast.makeText(yangyang_random_code.this, "傻B!! 我還沒做好啦!!!", Toast.LENGTH_SHORT).show();
                    Intent intent_googlemap = new Intent();
                    intent_googlemap.setClass(yangyang_random_code.this, yangyang_googlemap_code.class);

                    Bundle bundle=new Bundle();
                    bundle.putString("restaurant_name", textView_restaurant.getText().toString() );

                    intent_googlemap.putExtras(bundle);

                    startActivity(intent_googlemap);
                } else {
                    Toast.makeText(yangyang_random_code.this, "傻B!! 還沒決定要吃哪，就想出發??", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_call_yang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView_restaurant.getText().toString() == "屎") {
                    Toast.makeText(yangyang_random_code.this, "傻B!! 別打擾洋洋，乖乖去吃屎吧!!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    makeCall_yangyang();
                }
            }
        });
    }

    private ArrayList<String> getArraylist_mealtime(String mealtime) {
        Cursor cursor = null;
        cursor = yangyang_ItemDAO_mealtime.getAll();
        //用arraylist存資料
        ArrayList<String> sNote = new ArrayList<String>();

        //取得資料表列數
        int rows_num = cursor.getCount();

        if (rows_num != 0) {
            cursor.moveToFirst();   //將指標移至第一筆資料
            for (int i = 0; i < rows_num; i++) {
                String strValue = cursor.getString(cursor.getColumnIndex(yangyang_ItemDAO_mealtime.MEAL_START_TIME));
                sNote.add(strValue);
                strValue = cursor.getString(cursor.getColumnIndex(yangyang_ItemDAO_mealtime.MEAL_END_TIME));
                sNote.add(strValue);
                cursor.moveToNext();//將指標移至下一筆資料
            }
        }
        cursor.close(); //關閉Cursor
        return sNote;
    }

    public ArrayList<String> getArraylist_name(String meal) {
        Cursor cursor = null;
        //以傳送進來的的字串，決定現在要讀誰
        if (meal == "breakfast") {
            cursor = yangyang_ItemDAO_breakfast.getAll();
        } else if (meal == "lunch") {
            cursor = yangyang_ItemDAO_lunch.getAll();
        } else if (meal == "dinner") {
            cursor = yangyang_ItemDAO_dinner.getAll();
        }


        //用arraylist存資料
        ArrayList<String> sNote = new ArrayList<String>();

        //取得資料表列數
        int rows_num = cursor.getCount();

        if (rows_num != 0) {
            cursor.moveToFirst();   //將指標移至第一筆資料
            for (int i = 0; i < rows_num; i++) {
                String strValue = cursor.getString(cursor.getColumnIndex("restaurant_name"));
                sNote.add(strValue);
                cursor.moveToNext();//將指標移至下一筆資料
            }
        }
        cursor.close(); //關閉Cursor
        return sNote;
    }

    protected void makeCall_yangyang() {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);            //Intent 打電話
        phoneIntent.setData(Uri.parse("tel:09xxxxxxxx"));                   //Intent 值為電話

        try {
            startActivity(phoneIntent);
            finish();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(yangyang_random_code.this,
                    "撥號失敗,請稍後再試", Toast.LENGTH_SHORT).show();
        }
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yangyang_homepage, menu);
        return true;
    }
    */
}