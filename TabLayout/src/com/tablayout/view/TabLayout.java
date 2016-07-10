package com.tablayout.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * TabLayout for title tab.
 * 
 * @author xu(QQ:1396847724)
 * @since 2016.7.1
 * @version 1.0
 * 
 */
public class TabLayout extends HorizontalScrollView implements OnClickListener {
	// Default normalColor.
	private int mNormalColor = Color.WHITE;
	// Default selectedColor.
	private int mSelectedColor = Color.parseColor("#fb9505");
	// Default width.
	private int mTabWidth = getDipSize(60);
	// Child views.
	private TabStripLayout mTabStripLayout;
	private TextView mLastSelectedTab;
	// Out call back.
	private OnSelectedCallBack mOnSelectedCallBack;

	// Out to user interface.
	public interface OnSelectedCallBack {
		public void selected(int position);
	}

	public TabLayout(Context context) {
		this(context, null);
	}

	public TabLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		TabStripLayout tabStrip = new TabStripLayout(context);
		// Initialise paint.
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(mSelectedColor);
		tabStrip.setPaint(paint);
		// Add tab strip view.
		addTabStrip(tabStrip);
	}

	private void addTabStrip(TabStripLayout tabStrip) {
		if (getChildCount() > 0) {
			removeAllViews();
		}
		int width = LayoutParams.WRAP_CONTENT;
		int height = LayoutParams.MATCH_PARENT;
		addView(tabStrip, width, height);
		mTabStripLayout = tabStrip;
	}

	/**
	 * Set tab normal color.
	 * 
	 * @param normalColor
	 */
	public void setNormalColor(int normalColor) {
		mNormalColor = normalColor;
	}

	/**
	 * Set tab selected color.
	 * 
	 * @param selectedColor
	 */
	public void setSelectedColor(int selectedColor) {
		mSelectedColor = selectedColor;
	}

	/**
	 * Set tab width.
	 * 
	 * @param tabWidth
	 * @param isPx
	 *            Whether using pixel for unit.
	 */
	public void setTabWidth(int tabWidth, boolean isPx) {
		if (isPx) {
			mTabWidth = tabWidth;
		} else {
			mTabWidth = getDipSize(tabWidth);
		}
	}

	/**
	 * Add tab to strip
	 * 
	 * @param index
	 *            The index of tab.
	 * @param title
	 *            The title of tab.
	 */
	public void addTab(int index, String title) {
		int textSize = 15;
		TextView tab = new TextView(getContext());
		tab.setText(title);
		tab.setTextSize(textSize);
		tab.setTextColor(mNormalColor);
		tab.setGravity(Gravity.CENTER);
		tab.setTypeface(Typeface.DEFAULT_BOLD);
		tab.setOnClickListener(this);
		tab.setFocusable(true);
		tab.setTag(index);
		tab.setLayoutParams(createTabParams());
		// Add tab to strip.
		mTabStripLayout.addView(tab);
	}

	private LinearLayout.LayoutParams createTabParams() {
		int width = mTabWidth;
		int height = LinearLayout.LayoutParams.MATCH_PARENT;
		return new LinearLayout.LayoutParams(width, height);
	}

	/**
	 * Initialise strip since tabs added.
	 */
	public void initStrip() {
		if (mTabStripLayout.getChildCount() <= 0) {
			mTabStripLayout.setRectF(new RectF());
			return;
		}
		// Select the first tab.
		mLastSelectedTab = (TextView) mTabStripLayout.getChildAt(0);
		mLastSelectedTab.setTextColor(mSelectedColor);
		post(new Runnable() {
			@Override
			public void run() {
				int w = mTabWidth;
				int h = getHeight();
				float left = 0;
				float top = h - getDipSize(2);
				float right = w;
				float bottom = h;
				RectF rectF = new RectF(left, top, right, bottom);
				// Initialise the first strip.
				mTabStripLayout.setRectF(rectF);
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode == MeasureSpec.AT_MOST) {// wrap_content
			int specWidth = MeasureSpec.getSize(widthMeasureSpec);
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(specWidth,
					MeasureSpec.EXACTLY);// match_parent
		}
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode == MeasureSpec.AT_MOST) {
			int minSize = getDipSize(50);// Minimum size 50dp
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(minSize,
					MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private int getDipSize(int value) {
		int unit = TypedValue.COMPLEX_UNIT_DIP;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		value = (int) TypedValue.applyDimension(unit, value, metrics);
		return value;
	}

	@Override
	public void onClick(View view) {
		if (mLastSelectedTab == view) {
			return;
		}
		// Set selected.
		mLastSelectedTab.setTextColor(mNormalColor);
		TextView currentTab = (TextView) view;
		currentTab.setTextColor(mSelectedColor);
		mLastSelectedTab = currentTab;
		// Call back.
		int position = (int) (view.getTag());
		if (mOnSelectedCallBack != null) {
			mOnSelectedCallBack.selected(position);
		}
	}

	/**
	 * Select tab by position.
	 * 
	 * @param position
	 *            The index of selected tab.
	 */
	public void selectTab(int position) {
		View view = mTabStripLayout.getChildAt(position);
		view.performClick();
	}

	/**
	 * Select tab by position and position offset.
	 * 
	 * @param position
	 *            The index of selected tab.
	 * @param positionOffset
	 *            The position offset.
	 */
	public void selectTab(int position, float positionOffset) {
		int lastPosition = mTabStripLayout.indexOfChild(mLastSelectedTab);
		if (lastPosition < 0) {
			return;
		}
		int nextPosition = -1;
		float nextP = 0;// Percent.
		float offset = 0;
		if (lastPosition == position) {// Move right.
			nextPosition = lastPosition + 1;
			nextP = positionOffset;
			offset = mLastSelectedTab.getWidth() * nextP;
		} else if (lastPosition > position) {// Move left.
			nextPosition = lastPosition - 1;
			nextP = 1f - positionOffset;
			offset = -mLastSelectedTab.getWidth() * nextP;
		}
		View nextTab = mTabStripLayout.getChildAt(nextPosition);
		if (nextTab != null) {
			float left = mLastSelectedTab.getLeft() + offset;
			int width = nextTab.getMeasuredWidth();
			int parentWidth = getWidth();
			float newPosition = left - parentWidth / 2 + width / 2;
			// Update position.
			scrollTo((int) newPosition, 0);
			mTabStripLayout.updateHorizontalStrip(left);
		}
	}

	/**
	 * Set outside callback.
	 * 
	 * @param callBack
	 */
	public void setOnSelectedCallBack(OnSelectedCallBack callBack) {
		mOnSelectedCallBack = callBack;
	}
}
