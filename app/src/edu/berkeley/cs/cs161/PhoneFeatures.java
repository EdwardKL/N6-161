package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PhoneFeatures extends Activity{
	
	public static final String[] policies = new String[] {"Cannot ACCESS_COARSE_LOCATION", 
	"Cannot ACCESS_FINE_LOCATION", "Cannot ADD_VOICEMAIL", "Cannot CALL_PHONE", 
	"Not CALL_PRIVILEGED", "Cannot use BLUETOOTH", "Cannot use CAMERA", 
	"Cannot DISABLE_KEYGUARD", "Cannot RECORD_AUDIO", "Cannot SEND_SMS"};
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView lv = new ListView(this);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, policies));
        PolicyEdit.loadListViewWithPolicies(lv, policies);
        setContentView(lv);
    }
}
