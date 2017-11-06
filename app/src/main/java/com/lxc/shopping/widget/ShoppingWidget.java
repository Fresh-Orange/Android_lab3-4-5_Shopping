package com.lxc.shopping.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
	public static final String WIDGET_INIT_ACTION = "com.lxc.action.widgetInitClick";
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
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.shopping_widget);

		//推荐商品
		if (intent.getAction().equals(GoodsActivity.LAUNCH_WIDGET_ACTION)){
			GoodsItemBean itemBean = (GoodsItemBean) intent.getSerializableExtra(GoodsActivity.LAUNCH_WIDGET_BROADCAST_INFO);
			remoteViews.setImageViewResource(R.id.iv_goods, itemBean.imageRes);
			remoteViews.setTextViewText(R.id.tv_msg, itemBean.name + "仅售" + itemBean.price);
			for (int appWidgetId : appWidgetIds) {
				updateRemoteView(context,  remoteViews, RECOMMEND_TYPE, itemBean, appWidgetId, appWidgetManager);
			}
		}
		//回到初始状态
		else if (intent.getAction().equals(WIDGET_INIT_ACTION)){
			Log.d(TAG, "onReceive: init");
			remoteViews.setImageViewResource(R.id.iv_goods, R.drawable.shoplist);
			remoteViews.setTextViewText(R.id.tv_msg, context.getString(R.string.appwidget_default_text));
			for (int appWidgetId : appWidgetIds) {
				updateRemoteView(context,  remoteViews, INIT_TYPE, null, appWidgetId, appWidgetManager);
			}
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		for (int appWidgetId : appWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.shopping_widget);
			updateRemoteView(context,  remoteViews, INIT_TYPE, null, appWidgetId, appWidgetManager);
		}
	}

	/**
	 * 更新view
	 */
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
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.wg_whole, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
	}

}

