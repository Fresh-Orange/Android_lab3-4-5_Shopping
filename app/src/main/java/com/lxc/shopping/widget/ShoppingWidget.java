package com.lxc.shopping.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.lxc.shopping.DetailActivity;
import com.lxc.shopping.GoodsActivity;
import com.lxc.shopping.R;
import com.lxc.shopping.bean.GoodsItemBean;

/**
 * Implementation of App Widget functionality.
 */
public class ShoppingWidget extends AppWidgetProvider {

	public static final String TAG = "ImgAppWidgetProvider";
	public static final String WIDGET_CLICK_ACTION = "com.lxc.action.widgetClick";
	public static final String WIDGET_RECOMMEND_ACTION = "com.lxc.action.widgetRecommend";
	private final int INIT_TYPE = 0;
	private final int RECOMMEND_TYPE = 1;
	private final int ADD_TYPE = 2;
	private int cur_type = 0;
	private int[] appWidgetIds;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(),
				"com.lxc.shopping.widget.ShoppingWidget"));
		//添加到购物车状态
		if (intent.getAction().equals(DetailActivity.BROADCAST_WIDGET_ACTION)){
			cur_type = ADD_TYPE;
			GoodsItemBean itemBean = (GoodsItemBean) intent.getSerializableExtra(DetailActivity.ADDITION_WIDGET_BROADCAST_INFO_ITEM);

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.shopping_widget);
			remoteViews.setImageViewResource(R.id.iv_goods, itemBean.imageRes);
			remoteViews.setTextViewText(R.id.tv_msg, itemBean.name + "已加入购物车");

			for (int appWidgetId : appWidgetIds) {
				updateRemoteView(context,  remoteViews, ADD_TYPE, itemBean, appWidgetId, appWidgetManager);
			}
		}
		//推荐状态
		else if (intent.getAction().equals(GoodsActivity.LAUNCH_WIDGET_ACTION)){
			cur_type = RECOMMEND_TYPE;
			GoodsItemBean itemBean = (GoodsItemBean) intent.getSerializableExtra(GoodsActivity.LAUNCH_WIDGET_BROADCAST_INFO);

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.shopping_widget);
			remoteViews.setImageViewResource(R.id.iv_goods, itemBean.imageRes);
			remoteViews.setTextViewText(R.id.tv_msg, itemBean.name + "仅售" + itemBean.price);

			for (int appWidgetId : appWidgetIds) {
				updateRemoteView(context,  remoteViews, RECOMMEND_TYPE, itemBean, appWidgetId, appWidgetManager);
			}
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		this.appWidgetIds = appWidgetIds.clone();
		for (int appWidgetId : appWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.shopping_widget);
			updateRemoteView(context,  remoteViews, cur_type, null, appWidgetId, appWidgetManager);
		}
	}

	public void updateRemoteView(Context context, RemoteViews remoteViews, int type,
								 GoodsItemBean item,
								 int appWidgetId, AppWidgetManager appWidgetManager) {
		Intent clickIntent = new Intent();
		if (type == INIT_TYPE){//初始状态
			clickIntent.setClass(context, GoodsActivity.class);
		}
		else if (type == RECOMMEND_TYPE){//推荐商品状态
			clickIntent.putExtra(GoodsActivity.JUMP_DETAIL_INFO, item);
			clickIntent.setClass(context, DetailActivity.class);
		}
		else if (type == ADD_TYPE){//添加到购物车状态
			clickIntent.putExtra(DetailActivity.JUMP_GOODS_INFO, true);
			clickIntent.setClass(context, GoodsActivity.class);
		}

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.wg_whole, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
	}




	@Override
	public void onEnabled(Context context) {
		// Enter relevant functionality for when the first widget is created
	}

	@Override
	public void onDisabled(Context context) {
		// Enter relevant functionality for when the last widget is disabled
	}
}

