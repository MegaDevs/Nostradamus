//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.megadevs.nostradamus.nostratoothhelper;

import android.content.Context;
import android.content.SharedPreferences;
import com.googlecode.androidannotations.api.sharedpreferences.BooleanPrefEditorField;
import com.googlecode.androidannotations.api.sharedpreferences.BooleanPrefField;
import com.googlecode.androidannotations.api.sharedpreferences.EditorHelper;
import com.googlecode.androidannotations.api.sharedpreferences.SharedPreferencesHelper;

public final class Prefs_
    extends SharedPreferencesHelper
{


    public Prefs_(Context context) {
        super(context.getSharedPreferences((getLocalClassName(context)+"_Prefs"), 0));
    }

    private static String getLocalClassName(Context context) {
        String packageName = context.getPackageName();
        String className = context.getClass().getName();
        int packageLen = packageName.length();
        if (((!className.startsWith(packageName))||(className.length()<= packageLen))||(className.charAt(packageLen)!= '.')) {
            return className;
        }
        return className.substring((packageLen + 1));
    }

    public Prefs_.PrefsEditor_ edit() {
        return new Prefs_.PrefsEditor_(getSharedPreferences());
    }

    public BooleanPrefField isEnabled() {
        return booleanField("isEnabled", false);
    }

    public final static class PrefsEditor_
        extends EditorHelper<Prefs_.PrefsEditor_>
    {


        PrefsEditor_(SharedPreferences sharedPreferences) {
            super(sharedPreferences);
        }

        public BooleanPrefEditorField<Prefs_.PrefsEditor_> isEnabled() {
            return booleanField("isEnabled");
        }

    }

}
