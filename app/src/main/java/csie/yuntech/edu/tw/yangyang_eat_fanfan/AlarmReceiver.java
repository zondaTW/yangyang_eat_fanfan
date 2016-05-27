package csie.yuntech.edu.tw.yangyang_eat_fanfan;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "傻B!! 該吃飯飯拉~~~", Toast.LENGTH_LONG).show();
        //推送一条通知
        shownotification(context, intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void shownotification(Context context,Intent intent) {
        Intent notifyIntent = new Intent(context, yangyang_random_code.class);
        notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(context.getApplicationContext())
                .setSmallIcon(R.drawable.yangyang_icon)
                .setContentTitle("洋洋提醒你")
                .setContentText("傻B!! 該吃飯飯拉~~~")
                .setTicker("傻B!! 該吃飯飯拉~~~")//通知首次出现在通知栏，带上升动画效果的
                .setLights(0xff0000ff, 300, 700)
                .setSound(Uri.parse("android.resource://" + "csie.yuntech.edu.tw.yangyang_eat_fanfan" + "/raw/test"))// 設定音效效果
                .setPriority(Notification.PRIORITY_MAX) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setContentIntent(contentIntent).build();

        int defaults = 0;
        int flags = 0;
        defaults |= Notification.DEFAULT_VIBRATE;
        //defaults |= Notification.DEFAULT_SOUND;
        //notification.sound = Uri.parse("android.resource://" + "csie.yuntech.edu.tw.yangyang_eat_fanfan" + "/raw/test");
        //notification.sound = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6");

        //defaults |= Notification.DEFAULT_LIGHTS;
        //notification.ledARGB = 0xff0000ff;
        //notification.ledOnMS = 300; //亮的时间
        //notification.ledOffMS = 1000; //灭的时间
        flags |= Notification.FLAG_SHOW_LIGHTS;
        //flags |= Notification.FLAG_AUTO_CANCEL; //用戶單擊通知後自動消失
        // 設定通知效果
        notification.defaults |= defaults;
        notification.flags |= flags;
        notificationManager.notify(0, notification);
    }

}