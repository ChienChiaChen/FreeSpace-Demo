package tw.chiachen.testfreespace;

import android.nfc.Tag;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
	public static final String TAG="MainActivity";
	public TextView InternalPath, InternalSpace, ExternalPath, ExternalSpace;
	FreeSpace freeSpace = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_main);
		init();

	}

	public void init(){
		InternalPath = (TextView) findViewById (R.id.InternalPath);
		InternalSpace= (TextView) findViewById (R.id.InternalSpace);
		ExternalPath = (TextView) findViewById (R.id.ExternalPath);
		ExternalSpace= (TextView) findViewById (R.id.ExternalSpace);
	}

	public void showInternalPath(View v){
		InternalPath.setText (getInternalPath());
	}
	public void showInternalSpace(View v){
		freeSpace = new InternalStorage ();
		InternalSpace.setText(freeSpace.getAvailableSpace ());
	}

	public void showExternalPath(View v){
		ExternalPath.setText (getExternalPath ());
	}

	public void showExternalSpace(View view) {
		freeSpace = new ExternalStorage ();
		ExternalSpace.setText (freeSpace.getAvailableSpace ());
	}


	public String getInternalPath(){
		File path = Environment.getDataDirectory();
		showLog (path.getAbsolutePath ());
		return path.getAbsolutePath ();
	}

	public String getInternalSpace(){
		String freeSpace = Util.getAvailableInternalMemorySize ();
		String totalSpace = Util.getTotalInternalMemorySize ();
		showLog (freeSpace+ " / "+totalSpace);
		return freeSpace + " / " + totalSpace;
	}
	public String getExternalPath(){
		File path = new File (Util.getSDLocation ());
		showLog (path.getAbsolutePath ());
		return path.getAbsolutePath ();
	}


	public void showLog(String log) {
		Log.e (TAG, log);
	}
}
