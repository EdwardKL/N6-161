package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Internet extends Activity {
	
	public static final String[] policies = new String[] {"Cannot ACCESS_NETWORK_STATE", 
			"Cannot ACCESS_WIFI_STATE", "Cannot CHANGE_NETWORK_STATE", "Cannot CHANGE_WIFI_MULTICAST_STATE",
			"Cannot CHANGE_WIFI_STATE", "No INTERNET"};
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView lv = new ListView(this);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, policies));
        PolicyEdit.loadListViewWithPolicies(lv, policies);
        setContentView(lv);
    }
}
