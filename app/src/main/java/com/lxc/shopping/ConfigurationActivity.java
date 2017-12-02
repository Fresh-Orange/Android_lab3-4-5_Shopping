package com.lxc.shopping;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.SeekBar;

public class ConfigurationActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{

	Button btDone;
	SeekBar sbColor;
	View rootView;
	private int mAppWidgetId = -1;
	private AppWidgetManager appWidgetManager;
	int color = 0;
	//final int MAX_COLOR = 16777215;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		setResult(RESULT_CANCELED);

		sbColor = (SeekBar) findViewById(R.id.sb_color);
		sbColor.setOnSeekBarChangeListener(this);
		rootView = findViewById(R.id.config_whole);
		findViewById(R.id.bt_done).setOnClickListener(this);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			appWidgetManager = AppWidgetManager.getInstance(this);
		}
	}

	@Override
	public void onClick(View v) {
		if (mAppWidgetId != -1) {
			RemoteViews views = new RemoteViews(getPackageName(),
					R.layout.shopping_widget);
			Intent intent = new Intent(this, GoodsActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.wg_whole, pendingIntent);
			views.setImageViewResource(R.id.iv_goods, R.drawable.shoplist);
			views.setTextViewText(R.id.tv_msg, getString(R.string.appwidget_default_text));
			views.setTextColor(R.id.tv_msg, color);
			appWidgetManager.updateAppWidget(mAppWidgetId, views);
			Intent result = new Intent();
			result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, result);
			finish();
		}
	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		color = progress;
		color = color | 0xff000000;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		rootView.setBackgroundColor(color);
	}
}
