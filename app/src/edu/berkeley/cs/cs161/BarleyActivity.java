package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.widget.TextView;
import android.os.Bundle;

public class BarleyActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Hello, Android. I am barley.");
        setContentView(tv);
    }
}