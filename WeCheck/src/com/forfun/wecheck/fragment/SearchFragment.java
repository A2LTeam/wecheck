/**
 * 
 */
package com.forfun.wecheck.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.forfun.wecheck.R;
import com.forfun.wecheck.cst.DatabaseCst;
import com.forfun.wecheck.database.DatabaseOpenHelper;

/**
 * @author Nick
 * 
 */
public class SearchFragment extends Fragment {

	private static final String COLUMN_ID = "_id";
	private View _view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		_view = inflater.inflate(R.layout.search, container, false);

		Spinner spinner = (Spinner) _view.findViewById(R.id.search_category);

		// database handler
		DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(_view.getContext());

		Cursor cursor = dbHelper.getReadableDatabase().query(DatabaseCst.TABLE_CATEGORY, new String[] { "NAME_EN", COLUMN_ID }, null, null, null, null,
				COLUMN_ID);

		// Creating adapter for spinner
		SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(_view.getContext(), android.R.layout.simple_spinner_item, cursor, new String[] { "NAME_EN",
				COLUMN_ID }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);

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

				Cursor cursor = dbHelper.getReadableDatabase().query(DatabaseCst.TABLE_SUB_CATEGORY, new String[] { "NAME_EN", COLUMN_ID },
						"CATEGORY_ID = " + spinner.getSelectedItemId(), null, null, null, COLUMN_ID);

				// Creating adapter for spinner
				SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(_view.getContext(), android.R.layout.simple_spinner_item, cursor, new String[] {
						"NAME_EN", COLUMN_ID }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);

				// Specify the layout to use when the list of choices appears
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// Apply the adapter to the spinner
				subCategorySpinner.setAdapter(dataAdapter);
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
				ListView resultListView = (ListView) _view.findViewById(R.id.search_result);

				Spinner subCategorySpinner = (Spinner) _view.findViewById(R.id.search_subCategory);

				// database handler
				DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(_view.getContext());

				Cursor cursor = dbHelper.getReadableDatabase().query(DatabaseCst.TABLE_ITEM, new String[] { "NAME_EN", COLUMN_ID },
						"SUB_CATEGORY_ID = " + subCategorySpinner.getSelectedItemId(), null, null, null, COLUMN_ID);

				SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(_view.getContext(), android.R.layout.simple_spinner_item, cursor, new String[] {
						"NAME_EN", COLUMN_ID }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);

				// Apply the adapter to the spinner
				resultListView.setAdapter(dataAdapter);
			}

		});

		return _view;
	}
}
