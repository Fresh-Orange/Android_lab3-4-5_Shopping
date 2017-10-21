package com.lxc.shopping.event;

import com.lxc.shopping.bean.GoodsItemBean;

/**
 * Created by LaiXiancheng on 2017/10/20.
 * Email: lxc.sysu@qq.com
 */

public class AddToShopListEvent {
	GoodsItemBean itemAdded;

	public AddToShopListEvent(GoodsItemBean itemAdded) {
		this.itemAdded = itemAdded;
	}

	public GoodsItemBean getItemAdded() {
		return itemAdded;
	}

	public void setItemAdded(GoodsItemBean itemAdded) {
		this.itemAdded = itemAdded;
	}
}
