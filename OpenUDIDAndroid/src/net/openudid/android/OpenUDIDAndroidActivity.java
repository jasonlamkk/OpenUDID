package net.openudid.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;

public class OpenUDIDAndroidActivity extends Activity {
    private TextView lblOpenUDID;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        OpenUDID.syncContext(getApplicationContext());
        lblOpenUDID = (TextView) findViewById(R.id.textView2);
        lblOpenUDID.setText( OpenUDID.getOpenUDIDInContext() );
    }
}