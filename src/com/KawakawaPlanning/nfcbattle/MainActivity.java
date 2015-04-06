package com.KawakawaPlanning.nfcbattle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.SoundPool;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String LOG_TAG = MainActivity.class.getSimpleName();
	
	private NfcAdapter mNfcAdapter;
	TextView textview1;
	TextView textview2;
	boolean flag = false;
	int id1;
	int id2;
	SoundPool SP;
	int SoundID;
	Context con = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textview1 = (TextView)findViewById(R.id.textView1);
		textview2 = (TextView)findViewById(R.id.textView2);
		
		Intent _intent = new Intent(this,MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, _intent,PendingIntent.FLAG_UPDATE_CURRENT);

	}


	@Override
	protected void onResume(){
		super.onResume();
		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		if(mNfcAdapter == null){
			Toast.makeText(getApplicationContext(),"この機種では利用できません。", Toast.LENGTH_SHORT);
			finish();
			return;
		}
		if(!mNfcAdapter.isEnabled()){
			Toast.makeText(getApplicationContext(),"NFCが有効ではありません。", Toast.LENGTH_SHORT);
			finish();
			return;
		}
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()),0);
		IntentFilter[] intentFilter = new IntentFilter[]{
			new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
		};
		
		String[][] techList = new String[][]{
				{
					android.nfc.tech.NfcA.class.getName(),
					android.nfc.tech.NfcB.class.getName(),
					android.nfc.tech.IsoDep.class.getName(),
					android.nfc.tech.MifareClassic.class.getName(),
					android.nfc.tech.MifareUltralight.class.getName(),
					android.nfc.tech.NdefFormatable.class.getName(),
					android.nfc.tech.NfcV.class.getName(),
					android.nfc.tech.NfcF.class.getName(),
				}
			};
			mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, techList);
			
		
	}
	
	private String getIdm(Intent intent) {
		String idm = null;
		StringBuffer idmByte = new StringBuffer();
		byte[] rawIdm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		if (rawIdm != null) {
			for (int i = 0; i < rawIdm.length; i++) {
				if( i == 0){
					idmByte.append(Integer.toHexString(rawIdm[i] & 0xff));
				}else{
					idmByte.append(":" + Integer.toHexString(rawIdm[i] & 0xff));
				}
				
			}
			idm = idmByte.toString();
		}
		return idm;
	}

	@Override
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
 
		Log.w("kp",getIdm(intent));
		//▼▼▼▼ここから
		String action = intent.getAction();
		if(TextUtils.isEmpty(action)){
			return;
		}
 
		if(!action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)){
			return;
		}
 
		byte[] rawId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		Log.v("kp",rawId.toString());
		String id = "nothing";
		id = bytesToString(rawId);
		Log.v("kp",id);
		id = id.substring(id.length()-5);

		
		if (flag == false){
			id1 = Integer.parseInt(id);
			textview1.setText(id1+"");
			flag = true;
			Toast.makeText(con, "1Pの読み取りができました。", Toast.LENGTH_SHORT).show();
			
		}else{
			Toast.makeText(con, "2Pの読み取りができました。", Toast.LENGTH_SHORT).show();
			id2 = Integer.parseInt(id);
			textview2.setText(id2+"");	
			if (id1 > id2){
				new AlertDialog.Builder(this)
                .setTitle("結果")
                .setMessage("1Pの勝ち！！" +
                		"もう一度遊びますか？")
                .setPositiveButton(
                  "はい", 
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  
                    	textview1.setText("1Pの数字が表示されます");                    	
                    	textview2.setText("2Pの数字が表示されます");
                    }
                  })
                  .setNegativeButton(
                  "いいえ", 
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  
                    	finish();
                    }
                  })
                .show();
			}else if ( id1 < id2){
				new AlertDialog.Builder(this)
                .setTitle("結果")
                .setMessage("2Pの勝ち！！" +
                		"もう一度遊びますか？")
                .setPositiveButton(
                  "はい", 
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  
                    	textview1.setText("1Pの数字が表示されます");                    	
                    	textview2.setText("2Pの数字が表示されます");
                    }
                  })
                  .setNegativeButton(
                  "いいえ", 
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  
                    	finish();
                    }
                  })
                .show();
				
			}else if(id1 == id2){
				new AlertDialog.Builder(this)
                .setTitle("結果")
                .setMessage("同点です！！" +
                		"もう一度遊びますか？")
                .setPositiveButton(
                  "はい", 
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  
                    	textview1.setText("1Pの数字が表示されます");                    	
                    	textview2.setText("2Pの数字が表示されます");
                    }
                  })
                  .setNegativeButton(
                  "いいえ", 
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  
                    	finish();
                    }
                  })
                .show();
			}
			flag = false;
		    
		}
		
		//▲▲▲▲▲ここまで
	}
	public String bytesToString(byte[] bytes){
		StringBuilder buffer = new StringBuilder();
		boolean isFirst = true;
		
		for(byte b : bytes){
			if(isFirst){
				isFirst = false;
			}else{
//				buffer.append("-");
			}
			buffer.append(Integer.toString(b & 0xff));
		}
		return buffer.toString();
	}
}

