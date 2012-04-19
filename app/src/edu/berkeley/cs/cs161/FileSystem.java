package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FileSystem extends Activity {
	
	public static final String[] policies = new String[] {"Cannot AUTHENTICATE_ACCOUNTS", 
		"Cannot DELETE_PACKAGES", "Cannot INSTALL_PACKAGES", "Cannot GET_ACCOUNTS", 
		"Cannot READ_CALENDAR", "Cannot READ_CONTACTS", "Cannot READ_PROFILE", 
		"Cannot READ_SMS", "Cannot WRITE_CALENDAR", "Cannot WRITE_CONTACTS", 
		"Cannot WRITE_PROFILE", "Cannot WRITE_SMS"};
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView lv = new ListView(this);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, policies));
        PolicyEdit.loadListViewWithPolicies(lv, policies);
        setContentView(lv);
    }
    
    
}
