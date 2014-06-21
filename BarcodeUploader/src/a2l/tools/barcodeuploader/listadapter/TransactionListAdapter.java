package a2l.tools.barcodeuploader.listadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import a2l.tools.barcodeuploader.entity.Entity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

//--------------------------------------------------------------------------------------------------
//	Inner class/interface
//==================================================================================================
//	This class can be refractor to a separate class if we need to reuse it again
public class TransactionListAdapter<E extends Entity> extends BaseAdapter {

	// Variables
	private Context mContext;
	private List<E> mList;
//	private Map<Integer, View> mViewHolder;
	private TranListCallBack mListAdapterCallBack;

	// --------------------------------------------------------------------------------------------------
	// Constructors
	// --------------------------------------------------------------------------------------------------
	public TransactionListAdapter(Context context, List<E> list, TranListCallBack callBack) {
		mContext = context;
		mList = new ArrayList<E>(list);
		mListAdapterCallBack = callBack;
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
			view = inflater.inflate(mListAdapterCallBack.getLayoutId(), null);
			SparseArray<View> mViewHolder = mListAdapterCallBack.constructViewHolder(view);
			view.setTag(mViewHolder);
		}
		
		E e = getItem(position);
		mListAdapterCallBack.setViewValue((SparseArray<View>) view.getTag(), e);

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
