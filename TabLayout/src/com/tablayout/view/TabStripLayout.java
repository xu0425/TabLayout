package com.tablayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TabStripLayout extends LinearLayout {

	private Paint mPaint = null;
	private RectF mRectF = null;

	public TabStripLayout(Context context) {
		this(context, null);
	}

	public TabStripLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TabStripLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setOrientation(LinearLayout.HORIZONTAL);
		setWillNotDraw(false);
	}

	/**
	 * Set the paint.
	 * 
	 * @param paint
	 */
	public void setPaint(Paint paint) {
		mPaint = paint;
	}

	/**
	 * Set the RectF.
	 * 
	 * @param rectF
	 */
	public void setRectF(RectF rectF) {
		mRectF = rectF;
		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mPaint != null && mRectF != null) {
			drawStrip(canvas, mPaint, mRectF);
		}
	}

	/**
	 * Draw strip.
	 * 
	 * @param canvas
	 * @param paint
	 * @param rectF
	 */
	protected void drawStrip(Canvas canvas, Paint paint, RectF rectF) {
		canvas.drawRect(rectF, paint);
	}

	/**
	 * Update strip position.
	 * 
	 * @param left
	 */
	public void updateHorizontalStrip(float left) {
		if (mRectF != null) {
			float width = mRectF.width();
			mRectF.left = left;
			mRectF.right = mRectF.left + width;
			postInvalidateOnAnimation();
		}
	}
}
