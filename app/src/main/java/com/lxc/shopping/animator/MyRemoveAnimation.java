package com.lxc.shopping.animator;

/**
 * Created by LaiXiancheng on 2017/8/4.
 * item的消失动画效果，模拟电视关闭效果
 */
import android.graphics.Matrix;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

class MyRemoveAnimation extends Animation {
	private int halfWidth;
	private int halfHeight;

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		setDuration(getDuration());
		setFillAfter(true);
		//View的中心点
		halfWidth=width/2;
		halfHeight=height/2;
		//设置动画先加速后减速
		setInterpolator(new AccelerateDecelerateInterpolator());
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		Matrix matrix = t.getMatrix();
		//interpolatedTime是从0~1的一个变化
		// 前85%让动画缩小成一个点（宽高均为原来的0.1倍），后15%保持线的高度将这个点展开成一条线，变换的中心为View的中心
		if (interpolatedTime<0.85){
			float scale = (1-interpolatedTime/0.85f) < 0.1f ? 0.1f : (1-interpolatedTime/0.85f);
			matrix.preScale(scale, scale, halfWidth, halfHeight);
		}else{
			float scale = (interpolatedTime-0.85f)/0.15f < 0.1f ? 0.1f : (interpolatedTime-0.85f)/0.15f;
			matrix.setScale(scale , 0.1f,halfWidth,halfHeight);
		}

	}


}
