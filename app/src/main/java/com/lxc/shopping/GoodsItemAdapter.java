package com.lxc.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxc.shopping.bean.GoodsItemBean;
import com.lxc.shopping.listener.recyItemClickListener;

import java.util.List;

/**
 * Created by LaiXiancheng on 2017/10/19.
 * Email: lxc.sysu@qq.com
 */

public class GoodsItemAdapter extends RecyclerView.Adapter<GoodsItemAdapter.GoodsVH> {

	private List<GoodsItemBean> goodsList;//列表对应的数据
	private Context context;
	private recyItemClickListener itemClickListener;//自定义的接口

	public GoodsItemAdapter(Context context, List<GoodsItemBean> goodsList, recyItemClickListener itemClickListener) {
		this.goodsList = goodsList;
		this.context = context;
		this.itemClickListener = itemClickListener;
	}

	@Override
	public GoodsVH onCreateViewHolder(ViewGroup parent, int viewType) {
		//直接在此处将布局文件inflate成View
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false);
		return new GoodsVH(view);
	}

	@Override
	public void onBindViewHolder(final GoodsVH holder, int position) {
		//直接在这里设置数据
		String name = goodsList.get(position).name;
		holder.tvFirstLetter.setText(name.substring(0,1));
		holder.tvName.setText(name);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				itemClickListener.onClick(holder.getAdapterPosition());
			}
		});
		holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				itemClickListener.onLongClick(holder.getAdapterPosition());
				return true;//消费长按事件
			}
		});
	}

	@Override
	public int getItemCount() {
		return goodsList.size();
	}

	class GoodsVH extends RecyclerView.ViewHolder{
		TextView tvName;
		TextView tvFirstLetter;
		GoodsVH(View view) {
			super(view);
			tvFirstLetter = view.findViewById(R.id.tv_first_letter);
			tvName = view.findViewById(R.id.tv_name);
		}
	}

}
