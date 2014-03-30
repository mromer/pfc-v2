package com.mromer.bikeclimber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mromer.bikeclimber.utils.GeocodeUtil;

public class AddressDialogFragment extends DialogFragment {
	
	private final String NO_ITEMS_FINDED = "No result for the search";

	int mNum;
	private ListView listView;
	private List<String> listSearch;
	private List<Address> listAddress;
	private HashMap<String, Object> resultGeocodeUtil;

	private String action;

	private String title;

	/**
	 * Create a new instance of MyDialogFragment, providing "num"
	 * as an argument.
	 * @param action2 
	 */
	public static AddressDialogFragment newInstance(int num, String title, String action) {
		AddressDialogFragment f = new AddressDialogFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		args.putString("action", action);
		args.putString("title", title);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");   
		action = getArguments().getString("action"); 
		title = getArguments().getString("title"); 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.select_address_dialog, container, false);		

		getDialog().setTitle(title);

		searchBtn(v);		
		listView(v);		

		return v;
	}

	private void listView(View v) {
		
		listView = (ListView) v.findViewById(R.id.searchList);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if (!listSearch.get(position).equals(NO_ITEMS_FINDED)){
					FormularioActivity activity = (FormularioActivity) getActivity();					
					activity.fillAddress(action, listSearch.get(position), listAddress.get(position));
		            dismiss();
				}				
			}
			
		});
	}

	private void searchBtn(View v) {
		TextView b = (TextView) v.findViewById(R.id.searchBtn);
		b.setOnClickListener(new OnClickListener() {			

			@Override
			public void onClick(View v) {
				listSearch = new ArrayList<String>();
				
				// Get text from edit text
				EditText searchEditText = (EditText) getView().findViewById(R.id.searchEditText);

				if (!searchEditText.getText().toString().isEmpty()) {
					
					GeocodeUtil gu = new GeocodeUtil();
					resultGeocodeUtil = gu.getAddresses(getActivity(), searchEditText.getText().toString());
					listAddress = (List<Address>) resultGeocodeUtil.get(GeocodeUtil.LIST_ADDRESS);
					listSearch = (List<String>) resultGeocodeUtil.get(GeocodeUtil.LIST_STRING);					
				}				
				
				if (listSearch.isEmpty()) {
					listSearch.add(NO_ITEMS_FINDED);
				}
				ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), 
						android.R.layout.simple_list_item_1, listSearch);
				
				listView.setAdapter(adapter);
			}
		});
	}
}