package com.qualcomm.qnector;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class QNector extends Activity {
	private static String TAG = "QNector.java";
	private static AlertDialog.Builder builder;
    private String[] fileList;
    private File selectedFile;
	
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_qnector);

                showFileSelectorDialog(this);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                return super.onOptionsItemSelected(item);
        }
        
    	public Dialog showFileSelectorDialog(Context context) {
    		File path = new File(Environment.getExternalStorageDirectory() + "/qnector/");
    		try {
    			path.mkdirs();
    		} catch(SecurityException e) {
    			Log.e(TAG, e.toString());
    		}
    		if(path.exists()) {
    			FilenameFilter filter = new FilenameFilter() {
    				public boolean accept(File dir, String filename) {
    					File select = new File(dir, filename);
    					return filename.contains(".sch") || select.isDirectory();
    				}
    			};
    			fileList = path.list(filter);
    		}
    		else {
    			fileList= new String[0];
    		}
    		
    		Dialog dialog;
    		builder = new AlertDialog.Builder(context);
    		builder.setTitle("Select Schematic");
    		if(fileList == null) {
    			dialog = builder.create();
    			return dialog;
    		}
    		builder.setItems(fileList, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    selectedFile = new File(fileList[which]);
                    updateSchematic();
                }
            });
    		dialog = builder.show();
    		return dialog;
    	}
    	
    	private void updateSchematic(){
    		//TODO on dialog selection
    	}

}
