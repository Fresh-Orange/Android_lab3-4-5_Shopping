package com.lxc.shopping.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.lxc.shopping.DetailActivity;
import com.lxc.shopping.GoodsActivity;
import com.lxc.shopping.R;
import com.lxc.shopping.bean.GoodsItemBean;

public class additionWidgetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(),
				"com.lxc.shopping.widget.ShoppingWidget"));
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.shopping_widget);
		//添加到购物车
		if (intent.getAction().equals(DetailActivity.BROADCAST_WIDGET_ACTION)){
			GoodsItemBean itemBean = (GoodsItemBean) intent.getSerializableExtra(DetailActivity.ADDITION_WIDGET_BROADCAST_INFO_ITEM);
			remoteViews.setImageViewResource(R.id.iv_goods, itemBean.imageRes);
			remoteViews.setTextViewText(R.id.tv_msg, itemBean.name + "已加入购物车");
			//设置点击跳转事件
			Intent clickIntent = new Intent();
			clickIntent.putExtra(DetailActivity.JUMP_GOODS_INFO, true);
			clickIntent.setClass(context, GoodsActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.wg_whole, pendingIntent);

			appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		}
	}
}
