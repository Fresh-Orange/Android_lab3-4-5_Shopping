package com.lxc.shopping.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lxc.shopping.DetailActivity;
import com.lxc.shopping.GoodsActivity;
import com.lxc.shopping.R;
import com.lxc.shopping.bean.GoodsItemBean;

public class launchReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("activity_create", "receive");
		GoodsItemBean itemBean = (GoodsItemBean) intent.getSerializableExtra(GoodsActivity.LAUNCH_BROADCAST_INFO);
		Intent intent1 = new Intent(context, DetailActivity.class);
		intent1.putExtra(GoodsActivity.JUMP_DETAIL_INFO, itemBean);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), itemBean.imageRes);
		builder.setContentTitle("新商品热卖")
				.setContentText(itemBean.name + "仅售" + itemBean.price)
				.setTicker("新消息")
				.setLargeIcon(bm)
				.setSmallIcon(R.mipmap.fire_red)
				.setAutoCancel(true)
				.setContentIntent(pendingIntent)
				.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bm))
				.setColor(Color.parseColor("#f90101"));
		Notification notification = builder.build();
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(-1, notification);
	}
}
