package com.megadevs.androiduserlibrarytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.megadevs.androiduserlibrary.AndroidUserLibrary;
import com.megadevs.androiduserlibrary.AndroidUserLibrary.OnSelectedAccountListener;

public class AndroidUserLibraryTestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_user_library_test);
        
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final AndroidUserLibrary library = AndroidUserLibrary.getInstance(AndroidUserLibraryTestActivity.this);
				library.init(new OnSelectedAccountListener() {
					@Override
					public void onComplete() {
						System.out.println(library.getOwnerName());
						System.out.println(library.getOwnerEmail());
						System.out.println(library.getOwnerID());						
					}
				});
			}
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_android_user_library_test, menu);
        return true;
    }
}
