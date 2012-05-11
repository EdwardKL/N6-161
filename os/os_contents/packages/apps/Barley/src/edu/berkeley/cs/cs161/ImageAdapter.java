package edu.berkeley.cs.cs161;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<ResolveInfo> ris;
    private PackageManager pk;


    public ImageAdapter(Context c, List<ResolveInfo> ris) {
        mContext = c;
        pk = c.getPackageManager();
        this.ris = ris;
    }

    public int getCount() {
        return ris.size();
    }
 
    public Object getItem(int position) {
        return ris.get(position);
    }
  
    public Object getItemAtPosition(int position){
    	return getItem(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
    	ImageView imageView;
	// if (convertView == null) {  // if it's not recycled, initialize some attributes
        	LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	v = li.inflate(R.layout.grid_item, null);
            imageView = (ImageView) v.findViewById(R.id.icon_image);
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 0);
            imageView.setImageDrawable(ris.get(position).loadIcon(pk));
            TextView tv = (TextView) v.findViewById(R.id.icon_text);
            tv.setText(ris.get(position).activityInfo.name);
	    //} else {
            //v = (View) convertView;
	    // }
        
        //imageView.setImageResource(ris.get(position).getIconResource());
        return v;
    }

}