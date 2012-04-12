package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FileSystem extends Activity {
	
	public static final String[] policies = new String[] {"Cannot read files", 
	"Cannot write to file", "Cannot execute files"};
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView lv = new ListView(this);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, policies));
        PolicyEdit.loadListViewWithPolicies(lv, policies);
        setContentView(lv);
    }
    
    
}
