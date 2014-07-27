/**
 * 
 */
package com.forfun.wecheck.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.forfun.wecheck.R;
import com.forfun.wecheck.cst.DatabaseCst;
import com.forfun.wecheck.database.DatabaseOpenHelper;

/**
 * @author Nick
 * 
 */
public class SearchFragment extends Fragment {

	private View _view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		_view = inflater.inflate(R.layout.search, container, false);

		Spinner spinner = (Spinner) _view.findViewById(R.id.search_category);

		// database handler
		DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(_view.getContext());

		Cursor cursor = dbHelper.getReadableDatabase().query(DatabaseCst.VIEW_CATEGORY, new String[] { DatabaseCst.COLUMN_NAME, DatabaseCst.COLUMN_ID }, null,
				null, null, null, DatabaseCst.COLUMN_ID);

		// Creating adapter for spinner
		SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(_view.getContext(), android.R.layout.simple_spinner_item, cursor, new String[] {
				DatabaseCst.COLUMN_NAME, DatabaseCst.COLUMN_ID }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);

		// Specify the layout to use when the list of choices appears
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spinner.setAdapter(dataAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Spinner spinner = (Spinner) _view.findViewById(R.id.search_category);

				Spinner subCategorySpinner = (Spinner) _view.findViewById(R.id.search_subCategory);

				// database handler
				DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(_view.getContext());

				Cursor cursor = dbHelper.getReadableDatabase().query(DatabaseCst.VIEW_SUB_CATEGORY,
						new String[] { DatabaseCst.COLUMN_NAME, DatabaseCst.COLUMN_ID }, "CATEGORY_ID = " + spinner.getSelectedItemId(), null, null, null,
						DatabaseCst.COLUMN_ID);

				// Creating adapter for spinner
				SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(_view.getContext(), android.R.layout.simple_spinner_item, cursor, new String[] {
						DatabaseCst.COLUMN_NAME, DatabaseCst.COLUMN_ID }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);

				// Specify the layout to use when the list of choices appears
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// Apply the adapter to the spinner
				subCategorySpinner.setAdapter(dataAdapter);

				subCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

						Spinner subCategorySpinner = (Spinner) _view.findViewById(R.id.search_subCategory);

						String filter = "SUB_CATEGORY_ID = " + subCategorySpinner.getSelectedItemId();

						setResultListAdaptor(filter);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		// search button handling
		Button searchButton = (Button) _view.findViewById(R.id.search_button);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				TextView searchTextView = (TextView) _view.findViewById(R.id.search_text);

				if (searchTextView.getText() != null) {

					String filter = "BRAND_EN || NAME_EN LIKE '%" + searchTextView.getText() + "%' OR BRAND_SC || NAME_SC LIKE '%" + searchTextView.getText()
							+ "%' OR BRAND_TC || NAME_TC LIKE '%" + searchTextView.getText() + "%'";

					setResultListAdaptor(filter);
				}

				InputMethodManager imm = (InputMethodManager) _view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(arg0.getWindowToken(), 0);
			}

		});

		return _view;
	}

	private void setResultListAdaptor(String filter) {
		ListView resultListView = (ListView) _view.findViewById(R.id.search_result);

		// database handler
		DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(_view.getContext());

		Cursor cursor = dbHelper.getReadableDatabase().query(DatabaseCst.VIEW_ITEM,
				new String[] { DatabaseCst.COLUMN_BRAND, DatabaseCst.COLUMN_NAME, DatabaseCst.COLUMN_ID }, filter, null, null, null, DatabaseCst.COLUMN_ID);

		SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(_view.getContext(), R.layout.search_result_row, cursor, new String[] {
				DatabaseCst.COLUMN_BRAND, DatabaseCst.COLUMN_NAME, DatabaseCst.COLUMN_ID }, new int[] { R.id.search_result_brand, R.id.search_result_name,
				R.id.search_result_price }, 0);

		// Apply the adapter to the spinner
		resultListView.setAdapter(dataAdapter);
		
		dbHelper.close();
	}
}
