package com.megadevs.nostradamus.nostratoothhelper;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref.Scope;

@SharedPref(value=Scope.APPLICATION_DEFAULT)
public interface Prefs {

	@DefaultBoolean(false)
	public boolean isEnabled();
	
}
