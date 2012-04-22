package net.openudid.android;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.bluetooth.BluetoothAdapter;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.Log;

public class OpenUDID {
	public final static String TAG = "OpenUDID";//for Log
	private static String _openUdid;
	private final static boolean _UseImeiFailback = true;// false if you don't wanna include READ_PHONE_STATE permission
	//we recommend adding BT permission over  READ_PHONE_STATE permission as the will be less privacy concerns
	private final static boolean _UseBlueToothFailback = true; // false if you don't wanna include BT permission or android 1.6
	private final static boolean LOG = true; //Display or not debug message
	/*public OpenUDID() {
		// TODO Auto-generated constructor stub
	}*/
	public static void syncContext(Context mContext){
		if(_openUdid==null){
			generateOpenUDIDInContext(mContext);
		}
	}
	public static String getOpenUDIDInContext() {
		
		return _openUdid;
	}
	/*
	 * Generate a new OpenUDID
	 */
	private static void generateOpenUDIDInContext(Context mContext) {
		if (LOG) Log.d(TAG, "Generating openUDID");
		//Try to get WIFI MAC
		generateWifiId(mContext);
		if(null!=_openUdid){
			return;
		}
		//Try to get the ANDROID_ID
		String _androidId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID).toLowerCase(); 
		if(null!=_androidId && _androidId.length()>14 && !_androidId.equals("9774d56d682e549c")/*android 2.2*/){
			_openUdid = "ANDROID:"+_androidId;
			return; 
		}
		
		if(_UseImeiFailback){
			_openUdid = null;
			generateImeiId(mContext);

			if(_openUdid != null){
				return;
			}
		}
		
		if (_UseBlueToothFailback ){
			_openUdid = null;
			generateBlueToothId();
			if(_openUdid == null){

				generateSystemId();
				
			}
		}else{
			generateSystemId();
		}
		
		
		Log.d(TAG,_openUdid);
		
		Log.d(TAG,"done");
    }
	
	private static void generateImeiId(Context mContext) {
		// TODO Auto-generated method stub
		try{
			TelephonyManager TelephonyMgr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
			String szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE

    	if(null!=szImei && ! szImei.substring(0, 3).equals("000")){
    		_openUdid = "IMEI:"+szImei;
    	}
		}catch(Exception ex){
		
		}
	}
	private static void generateBlueToothId() {
		try{
			BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
    	m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	String m_szBTMAC = m_BluetoothAdapter.getAddress();
    	if(null!=m_szBTMAC){
    		_openUdid = "BTMAC:"+m_szBTMAC;
    	}
		}catch(Exception ex){
		
		}
	}
	private static void generateWifiId(Context mContext){
		try{
			WifiManager wifiMan = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInf = wifiMan.getConnectionInfo();

			Log.d(TAG,String.format("%s",wifiInf.getMacAddress()));
		
			String macAddr = wifiInf.getMacAddress();
			if(macAddr!=null){
				_openUdid = "WIFIMAC:"+macAddr;
			}
		}catch(Exception ex){
		
		}
	}
	
	private static String Md5(String input){
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(input.getBytes(),0,input.length());
		byte p_md5Data[] = m.digest();
		
		String mOutput = new String();
		for (int i=0;i < p_md5Data.length;i++) {
			int b =  (0xFF & p_md5Data[i]);
			// if it is a single digit, make sure it have 0 in front (proper padding)
			if (b <= 0xF) mOutput+="0";
			// add number to string
			mOutput+=Integer.toHexString(b);
		}
		// hex string to uppercase
		return mOutput.toUpperCase();
	}
	
	private static void generateSystemId(){
		// only reach here for very narrow chances  (android 2.2 , no wifi , no bluetooth)
		// this always return sth.
		String fp = String.format("%s/%s/%s/%s:%s/%s/%s:%s/%s/%d-%s-%s-%s-%s",
				Build.BRAND,
				Build.PRODUCT,
				Build.DEVICE,
				Build.BOARD,
				Build.VERSION.RELEASE,
				Build.ID,
				Build.VERSION.INCREMENTAL,
				Build.TYPE,
				Build.TAGS,
				Build.TIME,
				Build.DISPLAY,Build.HOST,Build.MANUFACTURER,Build.MODEL);
		
		Log.d(TAG,fp);
		if(null!=fp){
			_openUdid = Md5(fp);
		}
	}

}
