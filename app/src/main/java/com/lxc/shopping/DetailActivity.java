package com.lxc.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lxc.shopping.bean.GoodsItemBean;
import com.lxc.shopping.event.AddToShopListEvent;

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
	GoodsItemBean goodsItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getSupportActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_layout);
		tvName = (TextView) findViewById(R.id.tv_goods_name);
		tvPrice = (TextView) findViewById(R.id.tv_price);
		tvType = (TextView) findViewById(R.id.tv_type);
		tvMoreInfo = (TextView) findViewById(R.id.tv_more_info);
		ivGoods = (ImageView) findViewById(R.id.iv_goods);

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
		goodsItem = (GoodsItemBean) i.getSerializableExtra(GoodsActivity.JUMP_INFO);
		tvName.setText(goodsItem.name);
		tvPrice.setText(goodsItem.price);
		tvType.setText(goodsItem.type + " " + goodsItem.furtherInformation);
		//tvMoreInfo.setText(goodsItem.furtherInformation);
		ivGoods.setImageResource(goodsItem.imageRes);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.iv_back:
				if (isToShopList){
					AddToShopListEvent event = new AddToShopListEvent(goodsItem);
					EventBus.getDefault().post(event);
				}
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
		isToShopList = true;
		Toast.makeText(this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();//这里面已经进行finish了
		AddToShopListEvent event = new AddToShopListEvent(goodsItem);
		EventBus.getDefault().post(event);
	}
}
