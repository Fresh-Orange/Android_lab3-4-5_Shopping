package com.lxc.shopping.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lxc.shopping.DetailActivity;
import com.lxc.shopping.GoodsActivity;
import com.lxc.shopping.R;
import com.lxc.shopping.bean.GoodsItemBean;

public class additionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("activity_create", "receive");
		GoodsItemBean itemBean = (GoodsItemBean) intent.getSerializableExtra(DetailActivity.ADDITION_BROADCAST_INFO_ITEM);
		int id = intent.getIntExtra(DetailActivity.ADDITION_BROADCAST_INFO_INT, 0);
		Intent intent1 = new Intent(context, GoodsActivity.class);
		intent1.putExtra(DetailActivity.JUMP_GOODS_INFO, true);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setContentTitle("马上下单")
				.setContentText(itemBean.name + "已加入购物车")
				.setTicker("新消息")
				.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), itemBean.imageRes))
				.setSmallIcon(R.mipmap.cart)
				.setAutoCancel(true)
				.setColor(Color.parseColor("#303F9F"))
				.setContentIntent(pendingIntent);
		Notification notification = builder.build();
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(id, notification);
	}
}
