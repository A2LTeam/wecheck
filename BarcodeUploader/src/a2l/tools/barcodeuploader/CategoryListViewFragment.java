package a2l.tools.barcodeuploader;

import java.util.ArrayList;
import java.util.List;

import a2l.tools.barcodeuploader.constant.ItemCst;
import a2l.tools.barcodeuploader.database.DataManager;
import a2l.tools.barcodeuploader.entity.Category;
import a2l.tools.barcodeuploader.entity.Entity;
import a2l.tools.barcodeuploader.listadapter.TranListCallBack;
import a2l.tools.barcodeuploader.listadapter.TransactionListAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryListViewFragment extends Fragment implements TranListCallBack, OnClickListener, OnItemClickListener {

	// Private constants
	private static final String DEBUG_TAG = "CategoryListViewFragment";

	private ListView mListView;

	private MessageReceiver mMessageReceiver;
	private TransactionListAdapter<Category> mTransactionListAdapter;

	// --------------------------------------------------------------------------------------------------
	// Fragment lifecycle
	// --------------------------------------------------------------------------------------------------
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(DEBUG_TAG, "onAttach");
	}

	// --------------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// --------------------------------------------------------------------------------------------------
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		mListView = (ListView) view.findViewById(R.id.list_view);
		mListView.setOnItemClickListener(this);
		// --------------------------------------------------------------------------------------------------
		// Load records
		loadTransactionRecords();

		return view;
	}

	// --------------------------------------------------------------------------------------------------
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	// --------------------------------------------------------------------------------------------------
	@Override
	public void onStart() {
		super.onStart();
		Log.d(DEBUG_TAG, "onStart");
	}

	// --------------------------------------------------------------------------------------------------
	@Override
	public void onResume() {
		super.onResume();
	}

	// --------------------------------------------------------------------------------------------------
	@Override
	public void onPause() {
		super.onPause();
	}

	// --------------------------------------------------------------------------------------------------
	@Override
	public void onStop() {
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);

		super.onStop();
		Log.d(DEBUG_TAG, "onStop");
	}

	// --------------------------------------------------------------------------------------------------
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	// --------------------------------------------------------------------------------------------------
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// --------------------------------------------------------------------------------------------------
	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(DEBUG_TAG, "onDetach");
	}

	// --------------------------------------------------------------------------------------------------
	// Custom functions
	// --------------------------------------------------------------------------------------------------
	private void loadTransactionRecords() {
		AsyncTask<Void, Void, List<Category>> loadingTask = new AsyncTask<Void, Void, List<Category>>() {

			// --------------------------------------------------------------------------------------------------
			@Override
			protected List<Category> doInBackground(Void... params) {
				List<Category> list = new ArrayList<Category>();
				DataManager dataManager = DataManager.getInstance(getActivity());
				dataManager.openDatabase();
				String query = "SELECT distinct(CATEGORY) CATEGORY FROM " + ItemCst.TABLE_NAME;
				Cursor cursor = dataManager.getSQLiteDatabase().rawQuery(query, null);
				while (cursor.moveToNext()) {
					Category category = new Category();
					category.setCategory(dataManager.getStringFromColumn(cursor, "CATEGORY"));
					list.add(category);
				}
				cursor.close();
				dataManager.closeDatabase();
				return list;
			}

			// --------------------------------------------------------------------------------------------------
			@Override
			protected void onPostExecute(List<Category> list) {
				if (mTransactionListAdapter == null) {
					mTransactionListAdapter = initTransactionList(list);
					mListView.setAdapter(mTransactionListAdapter);
				} else {
					mTransactionListAdapter.updateList(list);
				}

			}
		};
		loadingTask.execute();
	}

	private TransactionListAdapter<Category> initTransactionList(List<Category> list) {
		return new TransactionListAdapter<Category>(getActivity(), list, this);
	}

	// ==================================================================================================
	private class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
		}

	}

	@Override
	public SparseArray<View> constructViewHolder(View view) {
		SparseArray<View> viewHolder = new SparseArray<View>();
		viewHolder.put(R.id.category, view.findViewById(R.id.category));
		return viewHolder;
	}

	@Override
	public void setViewValue(SparseArray<View> viewHolder, Entity e) {
		Category category = (Category) e;
		((TextView) viewHolder.get(R.id.category)).setText(category.getCategory());
	}

	@Override
	public int getLayoutId() {
		return R.layout.category;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ListViewFragment listFragment = new ListViewFragment();
		listFragment.setCategory(((Category) mTransactionListAdapter.getItem(position)).getCategory());
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.addToBackStack("test");
		fragmentTransaction.replace(R.id.container, listFragment);
		fragmentTransaction.commit();
	}

}