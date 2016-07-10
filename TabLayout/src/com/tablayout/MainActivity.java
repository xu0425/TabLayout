package com.tablayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tablayout.view.TabLayout;
import com.tablayout.view.TabLayout.OnSelectedCallBack;

public class MainActivity extends FragmentActivity {

	private TabLayout mTabLayout;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		initView();
		setListener();
	}

	private void findView() {
		mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	private void initView() {
		CustomPagerAdapter adapter = new CustomPagerAdapter(
				getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
		for (int i = 0; i < adapter.getCount(); i++) {
			String title = adapter.getPageTitle(i).toString();
			mTabLayout.addTab(i, title);
		}
		mTabLayout.initStrip();
	}

	private void setListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mTabLayout.selectTab(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				mTabLayout.selectTab(position, positionOffset);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		mTabLayout.setOnSelectedCallBack(new OnSelectedCallBack() {
			@Override
			public void selected(int position) {
				mViewPager.setCurrentItem(position);
			}
		});
	}

	class CustomPagerAdapter extends FragmentPagerAdapter {
		String[] titles = { "头条", "新闻", "热点", "体育", "娱乐", "科技", "订阅", "财经",
				"广州" };

		public CustomPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public Fragment getItem(int position) {
			CustomFragment fragment = new CustomFragment();
			Bundle bundle = new Bundle();
			bundle.putString("title", titles[position]);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getCount() {
			return titles.length;
		}
	}

	public class CustomFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Bundle bundle = getArguments();
			String title = bundle.getString("title");
			TextView tv = new TextView(getActivity());
			tv.setText(title);
			tv.setBackgroundColor(Color.argb((int) (Math.random() * 255), // alpha
					(int) (Math.random() * 255), // red
					(int) (Math.random() * 255), // green
					(int) (Math.random() * 255))); // blue
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(20);
			return tv;
		}
	}
}
