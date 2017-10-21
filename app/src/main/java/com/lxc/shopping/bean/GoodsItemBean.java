package com.lxc.shopping.bean;

import java.io.Serializable;

/**
 * Created by LaiXiancheng on 2017/10/19.
 * Email: lxc.sysu@qq.com
 */

public class GoodsItemBean implements Serializable{
	public String name;
	public String price;
	public String type;
	public String furtherInformation;
	public int imageRes;

	public GoodsItemBean(String name, String price, String type, String furtherInformation, int imageRes) {
		this.name = name;
		this.price = price;
		this.type = type;
		this.furtherInformation = furtherInformation;
		this.imageRes = imageRes;
	}
}
