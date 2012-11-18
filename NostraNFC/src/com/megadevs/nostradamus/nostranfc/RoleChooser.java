package com.megadevs.nostradamus.nostranfc;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.activity_rolechooser)
public class RoleChooser extends Activity {

	private SpinnerAdapter adapter = new SpinnerAdapter() {
		
		@Override
		public void unregisterDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void registerDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 1;
		}
		
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View v = LayoutInflater.from(getBaseContext()).inflate(android.R.layout.simple_spinner_item, arg2, false);
			((TextView)v.findViewById(android.R.id.text1)).setText(getItem(arg0));
			return v;
		}
		
		@Override
		public int getItemViewType(int arg0) {
			return 0;
		}
		
		@Override
		public long getItemId(int arg0) {
			return 0;
		}
		
		@Override
		public String getItem(int arg0) {
			switch (arg0) {
			case 0:
				return "Protezione civile";
			case 1:
				return "Polizia";
			case 2:
				return "Vigili del fuoco";
			case 3:
				return "Croce rossa";
			case 4:
				return "Esercito";
			}
			return "";
		}
		
		@Override
		public int getCount() {
			return 5;
		}
		
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			View v = LayoutInflater.from(getBaseContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
			((TextView)v.findViewById(android.R.id.text1)).setText(getItem(position));
			return v;
		}
	};
	
	@ViewById(R.id.role_chooser)
	public Spinner roleChooser;
	
	@Pref
	public Prefs_ prefs;
	
	@AfterInject
	public void pre() {
		getActionBar().setLogo(R.drawable.logo1);
		getActionBar().setDisplayShowTitleEnabled(false);
	}
	
	@AfterViews
	public void init() {
		roleChooser.setAdapter(adapter);
	}
	
	@Click(R.id.btn_save)
	public void save() {
		prefs.role().put((String)roleChooser.getSelectedItem());
		finish();
	}

}
