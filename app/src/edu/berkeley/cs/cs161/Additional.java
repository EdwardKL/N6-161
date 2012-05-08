package edu.berkeley.cs.cs161;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.berkeley.cs.cs161.db.SavedAppsSQLiteHelper.RegexType;

public class Additional extends ListActivity {
	private static final int IWhite = Menu.FIRST + 3;
	private static final int IBlack = Menu.FIRST + 2;
	private static final int FWhite = Menu.FIRST + 1;
	private static final int FBlack = Menu.FIRST;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additional);
		fillData();

        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        		Builder builder = new AlertDialog.Builder(parent.getContext());
        		builder.setTitle("Warning!");
        		builder.setMessage("Are you sure you would like to delete this regex?");
        		builder.setCancelable(true);
        		builder.setNeutralButton("Close", new OnClickListener() {
        			public void onClick(DialogInterface dialog, int id) {
        				dialog.cancel();
        			}
        		});
        		builder.setPositiveButton("Delete", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// remove from db
						startActivity(getIntent());
						finish();
					}
				});
        		AlertDialog help = builder.create();
        		help.show();
        	}
		});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, IWhite, 0, "Add to Internet WhiteList");
        menu.add(0, IBlack, 0, "Add to Internet BlackList");
        menu.add(0, FWhite, 0, "Add to File System WhiteList");
        menu.add(0, FBlack, 0, "Add to File System BlackList");
        return true;
	}
	
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	int temp = item.getItemId();
    	if (temp == IWhite || temp == IBlack || temp == FWhite || temp == FBlack) {
                addNewRegEx(temp);
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    private void addNewRegEx (int type) {
    	Intent i = new Intent(this, AddRegex.class);
    	i.putExtra("RegexType", type);
    	startActivityForResult(i, 0);
    }
    
    private void fillData() {
    	// Should use strings from database
    	ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"hi", "sup"});
        setListAdapter(aa);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	int typeId = intent.getExtras().getInt("RegexType");
    	RegexType type = RegexType.values()[typeId-Menu.FIRST];
    	String regex = intent.getExtras().getString("RegexString");
    	PolicyEdit.insertRegexIntoDb(regex, type);
    	fillData();
    }
}
