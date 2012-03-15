package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.os.Bundle;

public class PolicyEdit extends Activity {
	
	public static final String APP_ID = "app_id";
	public static final String APP_NAME = "name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			setTitle(extras.getString(APP_ID));
		}
		
	}

}
