package edu.berkeley.cs.cs161;

import edu.berkeley.cs.cs161.db.SavedAppsSQLiteHelper;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class Internet extends Activity {
	
	public static final String[] policies = new String[] {"Restrict Internet access completely", 
			"Restrict downloading privileges", "Restrict uploading privileges"};
	
	String appId;
	SavedAppsSQLiteHelper sqliteHelper;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView lv = new ListView(this);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, policies));
        lv.setItemsCanFocus(false);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        setContentView(lv);
        
        Bundle extras = getIntent().getExtras();
        appId = extras.getString(PolicyEdit.APP_ID);
        
        sqliteHelper = new SavedAppsSQLiteHelper(this);
        String[] permissions;
		try {
			permissions = sqliteHelper.getAppPermissions(appId);
			
	        for (int i = 0; i < policies.length; i++) {
	        	for (String permission : permissions) {
	        		if (permission.equals(policies[i])) {
	        			lv.setItemChecked(i, true);
	        			break;
	        		}
	        	}
	        }
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        lv.setItemChecked(0, true);
        lv.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	CheckedTextView check = (CheckedTextView)v;
	        	if (!check.isChecked()) {
	        		try {
						sqliteHelper.addPermissionToApp(appId, policies[position]);
					} catch (Exception e) {
						((ListView)parent).setItemChecked(position, false);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	} else {

	        		try {
						sqliteHelper.removePermissionFromApp(appId, policies[position]);
					} catch (Exception e) {
						check.setChecked(false);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
		});
    }
}
