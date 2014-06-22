package a2l.tools.barcodeuploader;

import java.util.ArrayList;
import java.util.List;

import a2l.tools.barcodeuploader.constant.ItemCst;
import a2l.tools.barcodeuploader.database.DataManager;
import a2l.tools.barcodeuploader.entity.Entity;
import a2l.tools.barcodeuploader.entity.Item;
import a2l.tools.barcodeuploader.listadapter.TranListCallBack;
import a2l.tools.barcodeuploader.listadapter.TransactionListAdapter;
import a2l.tools.barcodeuploader.scanner.CameraTestActivity;
import android.app.Activity;
import android.app.Fragment;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewFragment extends Fragment implements TranListCallBack, OnItemClickListener {

	// Private constants
	private static final String DEBUG_TAG = "TransactionFragment";

	private ListView mListView;
	private String category;

	private MessageReceiver mMessageReceiver;
	private TransactionListAdapter<Item> mTransactionListAdapter;

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
		AsyncTask<Void, Void, List<Item>> loadingTask = new AsyncTask<Void, Void, List<Item>>() {

			// --------------------------------------------------------------------------------------------------
			@Override
			protected List<Item> doInBackground(Void... params) {
				List<Item> list = new ArrayList<Item>();
				DataManager dataManager = DataManager.getInstance(getActivity());
				dataManager.openDatabase();
				String query = "SELECT * FROM " + ItemCst.TABLE_NAME + " WHERE CATEGORY = '" + getCategory()
						+ "' ORDER BY BRAND";
				Cursor cursor = dataManager.getSQLiteDatabase().rawQuery(query, null);
				while (cursor.moveToNext()) {
					Item item = new Item();
					item.setBrand(dataManager.getStringFromColumn(cursor, ItemCst.FIELD_BRAND));
					item.setName(dataManager.getStringFromColumn(cursor, ItemCst.FIELD_NAME));
					item.setCategory(dataManager.getStringFromColumn(cursor, ItemCst.FIELD_CATEGORY));
					item.setItemCode(dataManager.getStringFromColumn(cursor, ItemCst.FIELD_CODE));
					item.setBarcode(dataManager.getStringFromColumn(cursor, ItemCst.FIELD_BARCODE));
					list.add(item);
				}
				cursor.close();
				dataManager.closeDatabase();
				return list;
			}

			// --------------------------------------------------------------------------------------------------
			@Override
			protected void onPostExecute(List<Item> list) {
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

	private TransactionListAdapter<Item> initTransactionList(List<Item> list) {
		return new TransactionListAdapter<Item>(getActivity(), list, this);
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
		viewHolder.put(R.id.name, view.findViewById(R.id.name));
		viewHolder.put(R.id.brand, view.findViewById(R.id.brand));
		viewHolder.put(R.id.itemCode, view.findViewById(R.id.itemCode));
		viewHolder.put(R.id.barcode, view.findViewById(R.id.barcode));
		return viewHolder;
	}

	@Override
	public void setViewValue(SparseArray<View> viewHolder, Entity e) {
		Item item = (Item) e;
		((TextView) viewHolder.get(R.id.brand)).setText(item.getBrand());
		((TextView) viewHolder.get(R.id.name)).setText(item.getName());
		((TextView) viewHolder.get(R.id.itemCode)).setText(item.getItemCode());
		((TextView) viewHolder.get(R.id.barcode)).setText(item.getBarcode());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		System.out.println("Hello");
		Intent intent = new Intent();
		intent.setClass(getActivity(), CameraTestActivity.class);
		intent.putExtra(ItemCst.CODE, ((TextView) view.findViewById(R.id.itemCode)).getText());
		// startActivity(intent);
		startActivityForResult(intent, R.id.list_view);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case R.id.list_view:
			loadTransactionRecords();
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.list_detail;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}