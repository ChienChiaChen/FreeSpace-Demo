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
		InternalSpace.setText(getInternalSpace ());
	}

	public void showExternalPath(View v){
		ExternalPath.setText (getExternalPath ());
	}

	public void showExternalSpace(View view) {
		ExternalSpace.setText (getExternalSpace ());
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
		File path = new File (getSDLocation ());
		showLog (path.getAbsolutePath ());
		return path.getAbsolutePath ();
	}

	public String getExternalSpace(){
		File path = new File (getSDLocation ());
		String freeSpace=humanReadableByteCount(path.getFreeSpace ());
		String totalSpace=humanReadableByteCount(path.getTotalSpace ());
		showLog (freeSpace+ " / "+totalSpace);
		return freeSpace + " / " + totalSpace;
	}


	public void showLog(String log) {
		Log.e (TAG, log);
	}
	public static String getSDLocation(){
		String sdfolder=null;
		HashSet<String> sdpaths=getExternalMounts ();
		if (!sdpaths.isEmpty ())
			for (String sdpath:sdpaths) {
				sdfolder = sdpath.substring (sdpath.lastIndexOf ("/") + 1);
				sdfolder="/storage/"+sdfolder;
			}
		return sdfolder;
	}

	public static HashSet<String> getExternalMounts() {
		final HashSet<String> out = new HashSet<String> ();
		String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
		String s = "";
		try {
			final Process process = new ProcessBuilder ().command ("mount")
					.redirectErrorStream (true).start ();
			process.waitFor ();
			final InputStream is = process.getInputStream ();
			final byte[] buffer = new byte[1024];
			while (is.read (buffer) != -1) {
				s = s + new String (buffer);
			}
			is.close ();
		} catch (final Exception e) {
			e.printStackTrace ();
		}
		// parse output
		final String[] lines = s.split ("\n");
		for (String line : lines) {
			if (!line.toLowerCase (Locale.US).contains ("asec")) {
				if (line.matches (reg)) {
					String[] parts = line.split (" ");
					for (String part : parts) {
						if (part.startsWith ("/"))
							if (!part.toLowerCase (Locale.US).contains ("vold"))
								out.add (part);
					}
				}
			}
		}
		return out;
	}

	public static String humanReadableByteCount(long bytes) {
		int unit = 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
//		String pre ="KMGTPE".charAt(exp-1) +"i";
		String pre ="KMGTPE".charAt(exp-1)+"" ;
		return String.format("%.3f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
