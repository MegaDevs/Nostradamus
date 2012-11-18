package com.megadevs.nostradamus.nostratooth;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref.Scope;

@SharedPref(value=Scope.APPLICATION_DEFAULT)
public interface Prefs {

	@DefaultBoolean(false)
	public boolean isEnabled();
	
}
