package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Internet extends Activity {
	
	public static final String[] policyList = new String[] {"ACCESS_NETWORK_STATE", 
			"ACCESS_WIFI_STATE", "INTERNET"};
	
	public static String[] policies = new String[policyList.length];
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PolicyEdit.loadReadablePolicies(policies, policyList);
        
        ListView lv = new ListView(this);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, policies));
        PolicyEdit.loadListViewWithPolicies(lv, policyList);
        setContentView(lv);
    }
}
