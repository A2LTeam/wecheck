package com.forfun.wecheck.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.forfun.wecheck.R;
import com.forfun.wecheck.database.DataManager;

public class ItemDetailFragment extends Fragment {

	private ListView mListView;
	private ListAdapter<ShopItem> mListAdapter;
	private String itemName;
	private int itemId;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		Bundle args = getArguments();
		if (args != null) {
			itemName = args.getString("itemName");
			itemId = args.getInt("itemId");
		}
		
		View view = inflater.inflate(R.layout.shop_item, container, false);

		mListView = (ListView) view.findViewById(R.id.shop_item_detail);
		((TextView) view.findViewById(R.id.item_name)).setText(itemName);
		loadTransactionRecords();

		return view;
	}

	// --------------------------------------------------------------------------------------------------
	// Custom functions
	// --------------------------------------------------------------------------------------------------
	private void loadTransactionRecords() {
		AsyncTask<Void, Void, List<ShopItem>> loadingTask = new AsyncTask<Void, Void, List<ShopItem>>() {

			// --------------------------------------------------------------------------------------------------
			@Override
			protected List<ShopItem> doInBackground(Void... params) {
				List<ShopItem> list = new ArrayList<ShopItem>();
//				ShopItem shopItem = new ShopItem();
//				shopItem.setPrice(new BigDecimal("1345.12"));
//				shopItem.setShopName("Hello World");
//				list.add(shopItem);
				DataManager dataManager = DataManager.getInstance(getActivity());
				dataManager.openDatabase();
				String query = "SELECT S.NAME, SI.PRICE FROM SHOP_ITEM SI LEFT JOIN VW_SHOP S ON SI.SHOP_ID = S._ID WHERE SI.ITEM_ID = " + itemId;
				Cursor cursor = dataManager.getSQLiteDatabase().rawQuery(query, null);
				while (cursor.moveToNext()) {
					ShopItem shopItem = new ShopItem();
					shopItem.setShopName(dataManager.getStringFromColumn(cursor, "NAME"));
					shopItem.setPrice(dataManager.getFloatFromColumn(cursor, "PRICE"));
					list.add(shopItem);
				}
				cursor.close();
				dataManager.closeDatabase();
				return list;
			}

			// --------------------------------------------------------------------------------------------------
			@Override
			protected void onPostExecute(List<ShopItem> list) {
				if (mListAdapter == null) {
					mListAdapter = new ListAdapter<>(getActivity(), list);
					mListView.setAdapter(mListAdapter);
				} else {
					mListAdapter.updateList(list);
				}

			}
		};
		loadingTask.execute();
	}

	private class ListAdapter<E extends ShopItem> extends BaseAdapter {

		// Variables
		private Context mContext;
		private List<E> mList;

		// --------------------------------------------------------------------------------------------------
		// Constructors
		// --------------------------------------------------------------------------------------------------
		public ListAdapter(Context context, List<E> list) {
			mContext = context;
			mList = new ArrayList<E>(list);
		}

		// --------------------------------------------------------------------------------------------------
		// BaseAdpater functions
		// --------------------------------------------------------------------------------------------------
		@Override
		public int getCount() {
			return mList.size();
		}

		// --------------------------------------------------------------------------------------------------
		@Override
		public E getItem(int position) {
			return mList.get(position);
		}

		// --------------------------------------------------------------------------------------------------
		@Override
		public long getItemId(int position) {
			return 0;
		}

		// --------------------------------------------------------------------------------------------------
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (convertView == null) {
				// No available recycled view, create a new one
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.shop_item_row, null);
				ViewHolder mViewHolder = new ViewHolder();
				mViewHolder.setPrice((TextView) view.findViewById(R.id.shop_name));
				mViewHolder.setShop((TextView) view.findViewById(R.id.item_price));
				view.setTag(mViewHolder);
			}
			ViewHolder mViewHolder = (ViewHolder) view.getTag();
			ShopItem e = getItem(position);
			mViewHolder.getShop().setText(e.getShopName());
			mViewHolder.getPrice().setText(e.getPrice().toString());
			return view;
		}

		// --------------------------------------------------------------------------------------------------
		// Custom functions
		// --------------------------------------------------------------------------------------------------
		public void updateList(List<E> list) {
			mList.clear();
			mList.addAll(list);
			notifyDataSetChanged();
		}
	}

	private class ViewHolder {
		private TextView shop;
		private TextView price;

		public TextView getShop() {
			return shop;
		}

		public void setShop(TextView shop) {
			this.shop = shop;
		}

		public TextView getPrice() {
			return price;
		}

		public void setPrice(TextView price) {
			this.price = price;
		}

	}

	private class ShopItem {
		private String shopName;
		private Float price;

		public String getShopName() {
			return shopName;
		}

		public void setShopName(String shopName) {
			this.shopName = shopName;
		}

		public Float getPrice() {
			return price;
		}

		public void setPrice(Float price) {
			this.price = price;
		}

	}
}
