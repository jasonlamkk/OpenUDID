package net.openudid.reader;

import net.openudid.android.OpenUDID;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


/**
 * to test read consistency
 * */
public class OpenUDIDReaderActivity extends Activity {
    private TextView lblOpenUDID;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lblOpenUDID = (TextView) findViewById(R.id.textView2);
        /*
        try {
			Context openContext = this.createPackageContext("net.openudid", Context.MODE_WORLD_READABLE );
			SharedPreferences mPreferences =  openContext.getSharedPreferences("openudid_prefs", Context.MODE_WORLD_READABLE);
			String _keyInPref = mPreferences.getString("openudid", null);
	        lblOpenUDID.setText( _keyInPref );
	        
		} catch (NameNotFoundException e1) {
		//without install / reference to net.openudid.android, reading Preferences will fail
			lblOpenUDID.setText( e1.toString() );
		}*/
        
        // after link to net.openudid.android project
        OpenUDID.syncContext(this);
        lblOpenUDID.setText(OpenUDID.getOpenUDIDInContext());
    }
}