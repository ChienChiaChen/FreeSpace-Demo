package tw.chiachen.testfreespace;

import android.os.Environment;

import java.io.File;

/**
 * Created by Jason_Chien on 2016/11/7.
 */
public class InternalStorage implements FreeSpace {
	private File file=null;

	public InternalStorage() {
		file = Environment.getExternalStorageDirectory ();
	}

	@Override
	public String getAvailableSpace() {
		return Util.humanReadableByteCount(getFile ().getUsableSpace ());
	}

	@Override
	public String getTotalSpace() {
		return Util.humanReadableByteCount(getFile ().getTotalSpace ());
	}

	private File getFile(){
		return this.file;
	}

}
