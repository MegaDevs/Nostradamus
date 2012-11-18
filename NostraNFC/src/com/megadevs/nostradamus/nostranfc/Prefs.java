package com.megadevs.nostradamus.nostranfc;

import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref.Scope;

@SharedPref(value=Scope.APPLICATION_DEFAULT)
public interface Prefs {

	public String role();
	
}
