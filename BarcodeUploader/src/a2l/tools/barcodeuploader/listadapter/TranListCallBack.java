package a2l.tools.barcodeuploader.listadapter;

import a2l.tools.barcodeuploader.entity.Entity;
import android.util.SparseArray;
import android.view.View;

public interface TranListCallBack {
	public SparseArray<View> constructViewHolder(View view);

	public void setViewValue(SparseArray<View> viewHolder, Entity e);
	
	public int getLayoutId();
}
