/**
 * 
 */
package com.forfun.wecheck.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.forfun.wecheck.R;
import com.forfun.wecheck.fragment.FavoriteFragment;
import com.forfun.wecheck.fragment.HotFragment;
import com.forfun.wecheck.fragment.ScanFragment;
import com.forfun.wecheck.fragment.SearchFragment;

/**
 * @author Nick
 * 
 */
public class MainActivity extends FragmentActivity {

	private FragmentTabHost _tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		_tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		_tabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);

		_tabHost.addTab(_tabHost.newTabSpec("scan").setIndicator("Scan"), ScanFragment.class, null);
		_tabHost.addTab(_tabHost.newTabSpec("hot").setIndicator("Hot"), HotFragment.class, null);
		_tabHost.addTab(_tabHost.newTabSpec("favorite").setIndicator("Favorite"), FavoriteFragment.class, null);
		_tabHost.addTab(_tabHost.newTabSpec("search").setIndicator("Search"), SearchFragment.class, null);
		
		_tabHost.setCurrentTab(1);
	}
}
