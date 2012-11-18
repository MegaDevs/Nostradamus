package com.megadevs.nostradamus.nostratooth;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface Prefs {

	@DefaultBoolean(false)
	public boolean isEnabled();
	
}
