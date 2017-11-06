package com.lxc.shopping;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lxc.shopping.bean.GoodsItemBean;
import com.lxc.shopping.event.AddToShopListEvent;
import com.lxc.shopping.receiver.additionReceiver;
import com.lxc.shopping.widget.additionWidgetReceiver;

import org.greenrobot.eventbus.EventBus;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{
	TextView tvName;
	TextView tvType;
	TextView tvPrice;
	TextView tvMoreInfo;
	ImageView ivBack;
	ImageView ivStar;
	ImageView ivShop;
	ImageView ivGoods;
	private boolean isStar = false;
	private boolean isToShopList = false;
	private int shopCnt = 0;
	GoodsItemBean goodsItem;
	ListView optionsListView;
	static public String JUMP_GOODS_INFO = "goodsInfo";
	static public String ADDITION_BROADCAST_INFO_ITEM = "broadcastInfoItem";
	static public String ADDITION_WIDGET_BROADCAST_INFO_ITEM = "widgetBroadcastInfoItem";
	static public String ADDITION_BROADCAST_INFO_INT = "broadcastInfoInt";
	static public String BROADCAST_ACTION = "com.lxc.my.ADDITION";
	static public String BROADCAST_WIDGET_ACTION = "com.lxc.my.WIDGET_ADDITION";
	additionReceiver receiver;
	additionWidgetReceiver widgetReceiver;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getSupportActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_layout);

		IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
		receiver = new additionReceiver();
		registerReceiver(receiver, intentFilter);

		IntentFilter widgetIntentFilter = new IntentFilter(BROADCAST_WIDGET_ACTION);
		widgetReceiver = new additionWidgetReceiver();
		registerReceiver(widgetReceiver, widgetIntentFilter);

		tvName = (TextView) findViewById(R.id.tv_goods_name);
		tvPrice = (TextView) findViewById(R.id.tv_price);
		tvType = (TextView) findViewById(R.id.tv_type);
		tvMoreInfo = (TextView) findViewById(R.id.tv_more_info);
		ivGoods = (ImageView) findViewById(R.id.iv_goods);
		optionsListView = (ListView) findViewById(R.id.lv_options);
		String[] optionNames = {
				getString(R.string.give_order),
				getString(R.string.share),
				getString(R.string.uninterested),
				getString(R.string.more_sale_info)};
		ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(this, R.layout.options_item, optionNames);
		optionsListView.setAdapter(optionAdapter);

		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(this);
		ivStar = (ImageView) findViewById(R.id.iv_star);
		ivStar.setOnClickListener(this);
		ivShop = (ImageView) findViewById(R.id.iv_shop);
		ivShop.setOnClickListener(this);

		initData();
	}

	private void initData() {
		Intent i = getIntent();
		goodsItem = (GoodsItemBean) i.getSerializableExtra(GoodsActivity.JUMP_DETAIL_INFO);
		tvName.setText(goodsItem.name);
		tvPrice.setText(goodsItem.price);
		tvType.setText(goodsItem.type + " " + goodsItem.furtherInformation);
		ivGoods.setImageResource(goodsItem.imageRes);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.iv_back:
				finish();
				break;
			case R.id.iv_star:
				changeStar();
				break;
			case R.id.iv_shop:
				addToShopList();
		}
	}


	/**
	 * 改变“收藏”状态
	 */
	private void changeStar(){
		if (isStar){
			ivStar.setImageResource(R.drawable.empty_star);
			isStar = false;
		}
		else{
			ivStar.setImageResource(R.drawable.full_star);
			isStar = true;
		}
	}

	/**
	 * 物品加入购物车
	 */
	private void addToShopList(){

		AddToShopListEvent event = new AddToShopListEvent(goodsItem);
		EventBus.getDefault().postSticky(event);

		Intent intent = new Intent(BROADCAST_WIDGET_ACTION);
		intent.putExtra(ADDITION_WIDGET_BROADCAST_INFO_ITEM, goodsItem);
		sendBroadcast(intent, null);

		shopCnt++;

		Toast.makeText(this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		unregisterReceiver(widgetReceiver);
	}
}
