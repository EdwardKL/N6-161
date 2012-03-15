package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Internet extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Internet tab");
        setContentView(textview);
    }
}
