/**
 * 
 */
package com.forfun.wecheck.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost.TabSpec;

import com.forfun.wecheck.R;
import com.forfun.wecheck.database.DatabaseOpenHelper;
import com.forfun.wecheck.fragment.FavoriteFragment;
import com.forfun.wecheck.fragment.HotFragment;
import com.forfun.wecheck.fragment.ScanFragment;
import com.forfun.wecheck.fragment.SearchFragment;

/**
 * activity handling for application 
 * 
 * @author Nick
 * 
 */
public class MainActivity extends FragmentActivity {

	private FragmentTabHost _tabHost = null;
	private DatabaseOpenHelper _dbHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initializeTab();
		openDatabase();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeDatabase();
	}

	private void initializeTab() {
		_tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		_tabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);

		TabSpec tabSpecScan = _tabHost.newTabSpec("scan");
		_tabHost.addTab(tabSpecScan.setIndicator(getText(R.string.tab_scan), getResources().getDrawable(R.drawable.ic_tab_selector_scan)), ScanFragment.class,
				null);

		TabSpec tabSpecHot = _tabHost.newTabSpec("hot");
		_tabHost.addTab(tabSpecHot.setIndicator(getText(R.string.tab_hot), getResources().getDrawable(R.drawable.ic_tab_selector_hot)), HotFragment.class, null);

		TabSpec tabSpecFavorite = _tabHost.newTabSpec("favorite");
		_tabHost.addTab(tabSpecFavorite.setIndicator(getText(R.string.tab_favorite), getResources().getDrawable(R.drawable.ic_tab_selector_favorite)),
				FavoriteFragment.class, null);

		TabSpec tabSpecSearch = _tabHost.newTabSpec("search");
		_tabHost.addTab(tabSpecSearch.setIndicator(getText(R.string.tab_search), getResources().getDrawable(R.drawable.ic_tab_selector_search)),
				SearchFragment.class, null);

		_tabHost.setCurrentTab(1);

		// after testing, either the text or the image show, cannot show both
		// ((ImageView)
		// _tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.icon)).setImageResource(R.drawable.ic_tab_selector_scan);
		// ((ImageView)
		// _tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.icon)).setVisibility(View.VISIBLE);
		// ((TextView)
		// _tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setVisibility(View.VISIBLE);
	}

	private void openDatabase() {
		_dbHelper = new DatabaseOpenHelper(this);
	}

	private void closeDatabase() {
		_dbHelper.close();
	}
}
