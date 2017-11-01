package com.lxc.shopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lxc.shopping.animator.FadeItemAnimator;
import com.lxc.shopping.bean.GoodsItemBean;
import com.lxc.shopping.event.AddToShopListEvent;
import com.lxc.shopping.listener.recyItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GoodsActivity extends AppCompatActivity implements View.OnClickListener{
	List<GoodsItemBean> goodsItems = new ArrayList<>();
	List<GoodsItemBean> shopItems = new ArrayList<>();
	FloatingActionButton fab_shop;
	FloatingActionButton fab_main;
	FloatingActionButton fab_top;
	RecyclerView recyclerView;
	ListView listView;
	ShopItemAdapter listAdapter;
	GoodsItemAdapter recycleAdapter;

	static public String JUMP_DETAIL_INFO = "itemInfo";
	static public String JUMP_WIDGET_DETAIL_INFO = "widgetItemInfo";
	static public String LAUNCH_BROADCAST_INFO = "broadcastInfo";
	static public String LAUNCH_WIDGET_BROADCAST_INFO = "widgetBroadcastInfo";
	static public String LAUNCH_WIDGET_ACTION = "com.lxc.my.LAUNCH_WIDGET";
	private Boolean isHandled = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_goods);

		EventBus.getDefault().register(this);

		fab_shop = (FloatingActionButton) findViewById(R.id.fb_shoplist);
		fab_shop.setOnClickListener(this);
		fab_main = (FloatingActionButton) findViewById(R.id.fb_goods);
		fab_main.setOnClickListener(this);
		fab_top = (FloatingActionButton) findViewById(R.id.fb_top);
		fab_top.setOnClickListener(this);

		recycleAdapter = new GoodsItemAdapter(this, goodsItems, new recyItemClickListener() {
			@Override
			public void onClick(int position) {
				Intent i = new Intent(GoodsActivity.this, DetailActivity.class);
				i.putExtra(JUMP_DETAIL_INFO, goodsItems.get(position));
				startActivity(i);
			}

			@Override
			public void onLongClick(final int position) {
				goodsItems.remove(position);
				recycleAdapter.notifyItemRemoved(position);
				Toast.makeText(
						GoodsActivity.this,
						"移除第" + String.valueOf(position) + "个商品",
						Toast.LENGTH_SHORT).show();
			}
		});
		recyclerView = (RecyclerView) findViewById(R.id.rcv_goods);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(recycleAdapter);
		FadeItemAnimator itemAnimator = new FadeItemAnimator();
		itemAnimator.setRemoveDuration(1000);//时间长一点，比较能看出效果
		recyclerView.setItemAnimator(itemAnimator);

		listAdapter = new ShopItemAdapter(shopItems, this);
		listView = (ListView) findViewById(R.id.lv_shop_list);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//第一项是提示信息，不设置监听器
				if (position == 0)
					return;

				Intent i = new Intent(GoodsActivity.this, DetailActivity.class);
				i.putExtra(JUMP_DETAIL_INFO, shopItems.get(position));
				startActivity(i);
			}
		});
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				//第一项是提示信息，不设置监听器
				if (position == 0)
					return true;
				showDeleteDialog(position);
				return true;
			}
		});


		initData();

		sendHotBroadcast();

		boolean b = getIntent().getBooleanExtra(DetailActivity.JUMP_GOODS_INFO, false);
		if (b){//如果是通知栏发过来的
			showShoppingCart(true);
		}
	}


/*	@Override
	protected void onRestart() {
		super.onRestart();
		if (!isHandled){
			boolean b = getIntent().getBooleanExtra(DetailActivity.JUMP_GOODS_INFO, false);
			if (b){//如果是通知栏发过来的
				showShoppingCart(true);
			}
		}
		isHandled = false;
	}*/

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		boolean b = intent.getBooleanExtra(DetailActivity.JUMP_GOODS_INFO, false);
		if (b){//如果是通知栏发过来的
			showShoppingCart(true);
		}
		isHandled = true;
	}

	/**
	 * 展示删除的警告对话框
	 * @param position
	 */
	private void showDeleteDialog(final int position) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(GoodsActivity.this);
		builder.setTitle(R.string.delete_warning_title);
		String msg = getString(R.string.delete_warning_msg);
		msg = String.format(msg, goodsItems.get(position).name);
		builder.setMessage(msg);

		builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				shopItems.remove(position);
				listAdapter.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	private  void initData(){
		goodsItems.add(new GoodsItemBean("Enchated Forest", "¥ 5.00", "作者" ,"Johanna Basford", R.drawable.enchatedforest));
		goodsItems.add(new GoodsItemBean("Arla Milk", "¥ 59.00", "产地" ,"德国", R.drawable.arla));
		goodsItems.add(new GoodsItemBean("Devondale Milk", "¥ 79.00", "产地" ,"澳大利亚", R.drawable.devondale));
		goodsItems.add(new GoodsItemBean("Kindle Oasis", "¥ 2399.00", "版本" ,"8GB", R.drawable.kindle));
		goodsItems.add(new GoodsItemBean("waitrose 早餐麦片", "¥ 179.00", "重量" ,"2Kg", R.drawable.waitrose));
		goodsItems.add(new GoodsItemBean("Mcvitie's 饼干", "¥ 14.90", "产地" ,"英国", R.drawable.mcvitie));
		goodsItems.add(new GoodsItemBean("Ferrero Rocher", "¥ 132.59", "重量" ,"300g", R.drawable.ferrero));
		goodsItems.add(new GoodsItemBean("Maltesers", "¥ 141.43", "重量" ,"118g", R.drawable.maltesers));
		goodsItems.add(new GoodsItemBean("Lindt", "¥ 139.43", "重量" ,"249g", R.drawable.lindt));
		goodsItems.add(new GoodsItemBean("Borggreve", "¥ 28.90", "重量" ,"640g", R.drawable.borggreve));

		recycleAdapter.notifyDataSetChanged();

		shopItems.add(new GoodsItemBean("购物车", "价格", "", "", -1));
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.fb_shoplist:
				showShoppingCart(true);
				break;
			case R.id.fb_goods:
				showShoppingCart(false);
				break;
			case R.id.fb_top:
				recyclerView.smoothScrollToPosition(0);
				break;
		}
	}

	private void showShoppingCart(boolean b){
		if (b){
			fab_shop.setVisibility(View.GONE);
			fab_main.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
			fab_top.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
		else{
			fab_shop.setVisibility(View.VISIBLE);
			fab_main.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			fab_top.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
	}

	/**
	 * 发送产生“热卖”通知的广播
	 */
	private void sendHotBroadcast() {
		/*Intent intent = new Intent("com.lxc.my.LAUNCH");*/
		Intent intent = new Intent(LAUNCH_WIDGET_ACTION);
		Random random = new Random();
		int randIndex = random.nextInt(goodsItems.size());
		intent.putExtra(LAUNCH_WIDGET_BROADCAST_INFO, goodsItems.get(randIndex));
		sendOrderedBroadcast(intent, null);
	}

	@Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
	public void onMessageEvent(AddToShopListEvent event) {
		shopItems.add(event.getItemAdded());
		listAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
