package csie.yuntech.edu.tw.yangyang_eat_fanfan;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by a on 2015/12/5.
 */
public class yangyang_setting_code extends Activity {
    private yangyang_ItemDAO_breakfast yangyang_ItemDAO_breakfast;
    private yangyang_ItemDAO_lunch yangyang_ItemDAO_lunch;
    private yangyang_ItemDAO_dinner yangyang_ItemDAO_dinner;
    private yangyang_ItemDAO_mealtime yangyang_ItemDAO_mealtime;
    private Button button_insert_restaurant;
    private ListView listView_restaurant;
    private RadioButton radioButton_breakfast, radioButton_lunch, radioButton_dinner;
    private Button update_time_button, close_time_button;
    private TextView breakfast_time, lunch_time, dinner_time;
    private ArrayAdapter adapter_breakfast, adapter_lunch, adapter_dinner;
    private ArrayList<String> breakfast_restaurant = new ArrayList<String>();
    private ArrayList<String> lunch_restaurant = new ArrayList<String>();
    private ArrayList<String> dinner_restaurant = new ArrayList<String>();
    private ArrayList<Integer> breakfast_id = new ArrayList<Integer>();
    private ArrayList<Integer> lunch_id = new ArrayList<Integer>();
    private ArrayList<Integer> dinner_id = new ArrayList<Integer>();
    private ArrayList<String> mealtime = new ArrayList<String>();
    private Date breakfast_start_time, breakfast_end_time, lunch_start_time, lunch_end_time, dinner_start_time, dinner_end_time;
    private final int TIME_DIALOG_ID = 1;
    private TimePickerDialog.OnTimeSetListener timePickerListener;
    public String update_mealtime_chose = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yangyang_setting_layout);

        TabHost tab = (TabHost) findViewById(R.id.tabHost);
        tab.setup();

        TabHost.TabSpec s = tab.newTabSpec("tag1");
        s.setContent(R.id.tab1);
        s.setIndicator("店家資訊");
        tab.addTab(s);

        s = tab.newTabSpec("tag2");
        s.setContent(R.id.tab2);
        s.setIndicator("時間設定");
        tab.addTab(s);

        s = tab.newTabSpec("tag3");
        s.setContent(R.id.tab3);
        s.setIndicator("研發團隊");
        tab.addTab(s);

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

        breakfast_time = (TextView) findViewById(R.id.textView_breakfast_time_range);
        lunch_time = (TextView) findViewById(R.id.textView_lunch_time_range);
        dinner_time = (TextView) findViewById(R.id.textView_dinner_time_range);

        breakfast_time.setText(mealtime.get(0) + " ~ " + mealtime.get(1));
        lunch_time.setText(mealtime.get(2) + " ~ " + mealtime.get(3));
        dinner_time.setText(mealtime.get(4) + " ~ " + mealtime.get(5));

        // 建立資料庫物件
        yangyang_ItemDAO_breakfast = new yangyang_ItemDAO_breakfast(getApplicationContext());
        yangyang_ItemDAO_lunch = new yangyang_ItemDAO_lunch(getApplicationContext());
        yangyang_ItemDAO_dinner = new yangyang_ItemDAO_dinner(getApplicationContext());
        //read table
        breakfast_restaurant = getArraylist_name("breakfast"); //從資料庫讀出來
        breakfast_id = getArraylist_id("breakfast");
        lunch_restaurant = getArraylist_name("lunch");
        lunch_id = getArraylist_id("lunch");
        dinner_restaurant = getArraylist_name("dinner");
        dinner_id = getArraylist_id("dinner");

        button_insert_restaurant = (Button) findViewById(R.id.button_insert_restaurant);
        radioButton_breakfast = (RadioButton) findViewById(R.id.radioButton_breakfast);
        radioButton_lunch = (RadioButton) findViewById(R.id.radioButton_lunch);
        radioButton_dinner = (RadioButton) findViewById(R.id.radioButton_dinner);


        listView_restaurant = (ListView) findViewById(R.id.listView_restaurant);
        adapter_breakfast = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, breakfast_restaurant);
        adapter_lunch = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, lunch_restaurant);
        adapter_dinner = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, dinner_restaurant);

        listView_restaurant.setAdapter(adapter_breakfast);


        button_insert_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //讓alertdialog 有edittext
                LayoutInflater inflater = LayoutInflater.from(yangyang_setting_code.this);
                final View input = inflater.inflate(R.layout.alertdialog_updata_use, null);
                final EditText editText_update_use = (EditText) input.findViewById(R.id.editText_update_use);

                new AlertDialog.Builder(yangyang_setting_code.this)
                        .setTitle("新增")
                                //.setIcon(android.R.drawable.star_big_on)
                        .setMessage("輸入你想新增的店家")
                        .setView(input)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //判斷有無輸入
                                if (editText_update_use.getText().toString() == null || "".equals(editText_update_use.getText().toString().trim())) {
                                    Toast.makeText(yangyang_setting_code.this, "傻B!! 沒輸入就想新增", Toast.LENGTH_SHORT).show();
                                } else {
                                    restaurant_insert(editText_update_use.getText().toString());
                                    Toast.makeText(yangyang_setting_code.this, "傻B!! 新增完成了", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        radioButton_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView_restaurant.setAdapter(adapter_breakfast);
            }
        });

        radioButton_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView_restaurant.setAdapter(adapter_lunch);
            }
        });

        radioButton_dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView_restaurant.setAdapter(adapter_dinner);
            }
        });


        listView_restaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String chose_name = null;
                if (radioButton_breakfast.isChecked()) {
                    chose_name = breakfast_restaurant.get(position);
                } else if (radioButton_lunch.isChecked()) {
                    chose_name = lunch_restaurant.get(position);
                } else if (radioButton_dinner.isChecked()) {
                    chose_name = dinner_restaurant.get(position);
                }
                final String edit_setText_use = chose_name;     //修改時，先在edittext上顯示用

                //讓alertdialog 有edittext
                LayoutInflater inflater = LayoutInflater.from(yangyang_setting_code.this);
                final View input = inflater.inflate(R.layout.alertdialog_updata_use, null);
                final EditText editText_update_use = (EditText) input.findViewById(R.id.editText_update_use);
                editText_update_use.setText(edit_setText_use);
                new AlertDialog.Builder(yangyang_setting_code.this)
                        .setTitle("你選擇" + chose_name)
                        .setMessage("修改 or 刪除")
                        .setNegativeButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //再跳出一個修改用的alertdialog
                                new AlertDialog.Builder(yangyang_setting_code.this)
                                        .setTitle("修改")
                                        .setMessage("想修改為")
                                        .setView(input)
                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //判斷有無輸入
                                                if (editText_update_use.getText().toString() == null || "".equals(editText_update_use.getText().toString().trim())) {
                                                    Toast.makeText(yangyang_setting_code.this, "傻B!! 沒輸入就想修改", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    restaurant_update(position, editText_update_use.getText().toString());
                                                    Toast.makeText(yangyang_setting_code.this, "傻B!! 修改完成了", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                            }
                        })
                        .setNeutralButton("刪除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(yangyang_setting_code.this)
                                        .setTitle("刪除")
                                        .setMessage("你確定要刪除" + edit_setText_use + "?")
                                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                restaurant_delete(position);
                                                Toast.makeText(yangyang_setting_code.this, "傻B!! 刪除完成了", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                            }
                        })

                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });


        breakfast_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(yangyang_setting_code.this)
                        .setTitle("修改早餐時間")
                        .setMessage("選擇要修改的時間")
                        .setNegativeButton(mealtime.get(0), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                update_mealtime_chose = "breakfast_start_time";
                                showDialog(TIME_DIALOG_ID);
                            }
                        })
                        .setNeutralButton(mealtime.get(1), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                update_mealtime_chose = "breakfast_end_time";
                                showDialog(TIME_DIALOG_ID);
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        lunch_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(yangyang_setting_code.this)
                        .setTitle("修改午餐時間")
                        .setNegativeButton(mealtime.get(2), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                update_mealtime_chose = "lunch_start_time";
                                showDialog(TIME_DIALOG_ID);
                            }
                        })
                        .setNeutralButton(mealtime.get(3), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                update_mealtime_chose = "lunch_end_time";
                                showDialog(TIME_DIALOG_ID);
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        dinner_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(yangyang_setting_code.this)
                        .setTitle("修改晚餐時間")
                        .setNegativeButton(mealtime.get(4), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                update_mealtime_chose = "dinner_start_time";
                                showDialog(TIME_DIALOG_ID);
                            }
                        })
                        .setNeutralButton(mealtime.get(5), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                update_mealtime_chose = "dinner_end_time";
                                showDialog(TIME_DIALOG_ID);
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        timePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                final String update_time = String.format("%02d:%02d",hourOfDay, minute);
                Date temp_start_time = null, temp_end_time = null;
                final SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                if (update_mealtime_chose == "breakfast_start_time")
                {
                    mealtime.set(0, update_time);
                    //時間比大小，防低能兒
                    try {
                        temp_start_time = df.parse(mealtime.get(0));
                        temp_end_time= df.parse(mealtime.get(1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (temp_start_time.after(temp_end_time))
                    {
                        mealtime.set(0, mealtime.get(1));
                        mealtime.set(1, update_time);
                    }
                    mealtime_update(1, mealtime.get(0), mealtime.get(1), "breakfast");
                }
                else if (update_mealtime_chose == "breakfast_end_time")
                {
                    mealtime.set(1, update_time);
                    //時間比大小，防低能兒
                    try {
                        temp_start_time = df.parse(mealtime.get(0));
                        temp_end_time= df.parse(mealtime.get(1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (temp_start_time.after(temp_end_time))
                    {
                        mealtime.set(1, mealtime.get(0));
                        mealtime.set(0, update_time);
                    }
                    mealtime_update(1, mealtime.get(0), mealtime.get(1), "breakfast");
                }
                else if (update_mealtime_chose == "lunch_start_time")
                {
                    mealtime.set(2, update_time);
                    //時間比大小，防低能兒
                    try {
                        temp_start_time = df.parse(mealtime.get(2));
                        temp_end_time= df.parse(mealtime.get(3));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (temp_start_time.after(temp_end_time))
                    {
                        mealtime.set(2, mealtime.get(3));
                        mealtime.set(3, update_time);
                    }
                    mealtime_update(2, mealtime.get(2), mealtime.get(3), "lunch");
                }
                else if (update_mealtime_chose == "lunch_end_time")
                {
                    mealtime.set(3, update_time);
                    //時間比大小，防低能兒
                    try {
                        temp_start_time = df.parse(mealtime.get(2));
                        temp_end_time= df.parse(mealtime.get(3));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (temp_start_time.after(temp_end_time))
                    {
                        mealtime.set(3, mealtime.get(2));
                        mealtime.set(2, update_time);
                    }
                    mealtime_update(2, mealtime.get(2), mealtime.get(3), "lunch");
                }
                else if (update_mealtime_chose == "dinner_start_time")
                {
                    mealtime.set(4, update_time);
                    //時間比大小，防低能兒
                    try {
                        temp_start_time = df.parse(mealtime.get(4));
                        temp_end_time= df.parse(mealtime.get(5));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (temp_start_time.after(temp_end_time))
                    {
                        mealtime.set(4, mealtime.get(5));
                        mealtime.set(5, update_time);
                    }
                    mealtime_update(3, mealtime.get(4), mealtime.get(5), "dinner");
                }
                else if (update_mealtime_chose == "dinner_end_time")
                {
                    mealtime.set(5, update_time);
                    //時間比大小，防低能兒
                    try {
                        temp_start_time = df.parse(mealtime.get(4));
                        temp_end_time= df.parse(mealtime.get(5));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (temp_start_time.after(temp_end_time))
                    {
                        mealtime.set(5, mealtime.get(4));
                        mealtime.set(4, update_time);
                    }
                    mealtime_update(3, mealtime.get(4), mealtime.get(5), "dinner");
                }
                update_mealtime_chose = null;
            }
        };




        update_time_button = (Button) findViewById(R.id.update_time_button);
        close_time_button = (Button) findViewById(R.id.close_time_button);
        update_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReminder(true);
            }
        });

        close_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReminder(false);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        switch (id)
        {
            case TIME_DIALOG_ID:
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                return new TimePickerDialog(this, timePickerListener, hour, minute, true);
        }
        return super.onCreateDialog(id);
    }


    private ArrayList<Integer> getArraylist_id(String meal) {
        Cursor cursor = null;
        if (meal == "breakfast")
        {
            cursor = yangyang_ItemDAO_breakfast.getAll();
        }
        else if (meal == "lunch")
        {
            cursor = yangyang_ItemDAO_lunch.getAll();
        }
        else if (meal == "dinner")
        {
            cursor = yangyang_ItemDAO_dinner.getAll();
        }


        //用arraylist存資料
        ArrayList<Integer> sNote = new ArrayList<Integer>();

        //取得資料表列數
        int rows_num = cursor.getCount();

        if(rows_num != 0)
        {
            cursor.moveToFirst();   //將指標移至第一筆資料
            for(int i=0; i<rows_num; i++)
            {
                int strValue = cursor.getInt(cursor.getColumnIndex("_id"));
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
        if (meal == "breakfast")
        {
            cursor = yangyang_ItemDAO_breakfast.getAll();
        }
        else if (meal == "lunch")
        {
            cursor = yangyang_ItemDAO_lunch.getAll();
        }
        else if (meal == "dinner")
        {
            cursor = yangyang_ItemDAO_dinner.getAll();
        }


        //用arraylist存資料
        ArrayList<String> sNote = new ArrayList<String>();

        //取得資料表列數
        int rows_num = cursor.getCount();

        if(rows_num != 0)
        {
            cursor.moveToFirst();   //將指標移至第一筆資料
            for(int i=0; i<rows_num; i++)
            {
                String strValue = cursor.getString(cursor.getColumnIndex("restaurant_name"));
                sNote.add(strValue);
                cursor.moveToNext();//將指標移至下一筆資料
            }
        }
        cursor.close(); //關閉Cursor
        return sNote;
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

    public void restaurant_insert(String change_name)
    {
        long number;
        if (radioButton_breakfast.isChecked())
        {
            number = yangyang_ItemDAO_breakfast.insert(change_name);    //將輸入進來的字串，加到資料庫以及店家的arraylist中，並將回傳的id，加到id的arraylist中
            breakfast_restaurant.add(change_name);
            breakfast_id.add((int) number);
            adapter_breakfast.notifyDataSetChanged();       //更新adapter
        }
        else if (radioButton_lunch.isChecked())
        {
            number = yangyang_ItemDAO_lunch.insert(change_name);        //將輸入進來的字串，加到資料庫以及店家的arraylist中，並將回傳的id，加到id的arraylist中
            lunch_restaurant.add(change_name);
            lunch_id.add((int) number);
            adapter_lunch.notifyDataSetChanged();            //更新adapter
        }
        else if (radioButton_dinner.isChecked())
        {
            number = yangyang_ItemDAO_dinner.insert(change_name);       //將輸入進來的字串，加到資料庫以及店家的arraylist中，並將回傳的id，加到id的arraylist中
            dinner_restaurant.add(change_name);
            dinner_id.add((int) number);
            adapter_dinner.notifyDataSetChanged();          //更新adapter
        }
    }

    public void restaurant_delete(int position)
    {
        if (radioButton_breakfast.isChecked())
        {
            yangyang_ItemDAO_breakfast.delete(breakfast_id.get(position));  //將傳送進的id對應的資料刪除，以及店家的arraylis、id的arraylist中的也一並刪除
            breakfast_restaurant.remove(position);
            breakfast_id.remove(position);
            adapter_breakfast.notifyDataSetChanged();      //更新adapter
        }
        else if (radioButton_lunch.isChecked())
        {
            yangyang_ItemDAO_lunch.delete(lunch_id.get(position));          //將傳送進的id對應的資料刪除，以及店家的arraylis、id的arraylist中的也一並刪除
            lunch_restaurant.remove(position);
            lunch_id.remove(position);
            adapter_lunch.notifyDataSetChanged();           //更新adapter
        }
        else if (radioButton_dinner.isChecked())
        {
            yangyang_ItemDAO_dinner.delete(dinner_id.get(position));        //將傳送進的id對應的資料刪除，以及店家的arraylis、id的arraylist中的也一並刪除
            dinner_restaurant.remove(position);
            dinner_id.remove(position);
            adapter_dinner.notifyDataSetChanged();          //更新adapter
        }
    }

    public void restaurant_update(int position, String change_name)
    {
        if (radioButton_breakfast.isChecked())
        {
            yangyang_ItemDAO_breakfast.update(breakfast_id.get(position), change_name);     //將傳送進的id對應的資料修改為傳送進來的字串，以及店家的arraylis也修改
            breakfast_restaurant.set(position, change_name);
            adapter_breakfast.notifyDataSetChanged();       //更新adapter
        }
        else if (radioButton_lunch.isChecked())
        {
            yangyang_ItemDAO_lunch.update(lunch_id.get(position), change_name);            //將傳送進的id對應的資料修改為傳送進來的字串，以及店家的arraylis也修改
            lunch_restaurant.set(position, change_name);
            adapter_lunch.notifyDataSetChanged();           //更新adapter
        }
        else if (radioButton_dinner.isChecked())
        {
            yangyang_ItemDAO_dinner.update(dinner_id.get(position), change_name);           //將傳送進的id對應的資料修改為傳送進來的字串，以及店家的arraylis也修改
            dinner_restaurant.set(position, change_name);
            adapter_dinner.notifyDataSetChanged();          //更新adapter
        }
    }

    public void mealtime_update(int position, String start_time, String end_time, String change_mealtime)
    {
        yangyang_ItemDAO_mealtime.update(position, start_time, end_time);
        if (change_mealtime == "breakfast")
        {
            breakfast_time.setText(mealtime.get(0) + " ~ " + mealtime.get(1));
        }
        else if(change_mealtime == "lunch")
        {
            lunch_time.setText(mealtime.get(2) + " ~ " + mealtime.get(3));
        }
        else if(change_mealtime == "dinner")
        {
            dinner_time.setText(mealtime.get(4) + " ~ " + mealtime.get(5));
        }
    }

    /**
     * Set the alarm
     *
     * @param b whether enable the Alarm clock or not
     */
    private void setReminder(boolean b) {

        // get the AlarmManager instance
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //建立意圖
        Intent intent = new Intent();

        //這裡的 this 是指當前的 Activity
        //AlarmReceiver.class 則是負責接收的 BroadcastReceiver
        intent.setClass(this, AlarmReceiver.class);

        // create a PendingIntent that will perform a broadcast
        PendingIntent pending;

        String temp;
        temp = mealtime.get(0);
        String[] breaktime = temp.split(":");
        temp = mealtime.get(2);
        String[] lunchtime = temp.split(":");
        temp = mealtime.get(4);
        String[] dinnertime = temp.split(":");
        if(b){
            // just use current time as the Alarm time.
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(breaktime[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(breaktime[1]));
            calendar.set(Calendar.SECOND, 0);
            // 以日期字串組出不同的 category
            intent.addCategory("breakfast");
            pending  = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // schedule an alarm
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pending);


            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(lunchtime[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(lunchtime[1]));
            calendar.set(Calendar.SECOND, 0);
            // 以日期字串組出不同的 category
            intent.addCategory("lunch");
            pending  = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // schedule an alarm
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pending);

            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dinnertime[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(dinnertime[1]));
            calendar.set(Calendar.SECOND, 0);
            // 以日期字串組出不同的 category
            intent.addCategory("dinner");
            pending  = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // schedule an alarm
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pending);
        }
        else{
            // cancel current alarm
            intent.addCategory("breakfast");
            pending  = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pending);

            intent.addCategory("lunch");
            pending  = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pending);

            intent.addCategory("dinner");
            pending  = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pending);

        }

    }

}
