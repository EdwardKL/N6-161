package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PhoneInfo extends Activity {
	
	public static final String[] policyList = new String[] {"MODIFY_PHONE_STATE", 
	"READ_PHONE_STATE", "PROCESS_OUTGOING_CALLS"};
	
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
