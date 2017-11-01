package com.lxc.shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lxc.shopping.bean.GoodsItemBean;

import java.util.List;

/**
 * Created by LaiXiancheng on 2017/10/19.
 * Email: lxc.sysu@qq.com
 */

public class ShopItemAdapter extends BaseAdapter {
	private List<GoodsItemBean> goodsList;
	private Context context;

	public ShopItemAdapter(List<GoodsItemBean> goodsList, Context context) {
		this.goodsList = goodsList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return goodsList.size();
	}

	@Override
	public Object getItem(int position) {
		return goodsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View itemView;
		final ViewHolder holder;
		//是否可以重用
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			itemView = inflater.inflate(R.layout.shop_item, parent, false); //加载布局，创建View
			holder=new ViewHolder(itemView);
			itemView.setTag(holder);
		} else {
			itemView = convertView;
			holder= (ViewHolder) itemView.getTag();
		}

		//设置数据
		String name = goodsList.get(position).name;
		char ch = name.charAt(0);
		if (ch>='A' && ch<='Z'  ||  ch>='a' && ch<='z')
			holder.tvFirstLetter.setText(name.substring(0,1));
		else
			holder.tvFirstLetter.setText("*");
		holder.tvName.setText(name);
		String price = goodsList.get(position).price;
		holder.tvPrice.setText(price);

		return itemView;
	}

	private static class ViewHolder {
		TextView tvName;
		TextView tvFirstLetter;
		TextView tvPrice;

		ViewHolder(View itemView) {
			tvName= itemView.findViewById(R.id.tv_name);
			tvPrice= itemView.findViewById(R.id.tv_price);
			tvFirstLetter= itemView.findViewById(R.id.tv_first_letter);
		}
	}
}
