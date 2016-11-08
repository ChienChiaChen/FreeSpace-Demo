package tw.chiachen.testfreespace;

import java.io.File;

/**
 * Created by Jason_Chien on 2016/11/7.
 */
public class ExternalStorage implements FreeSpace{
	private File file = null;
	public ExternalStorage() {
		file = new File (Util.getSDLocation ());
	}

	@Override
	public String getAvailableSpace() {
		String freeSpace=Util.humanReadableByteCount(getFile().getFreeSpace ());
		return freeSpace;
	}

	@Override
	public String getTotalSpace() {
		String totalSpace=Util.humanReadableByteCount(getFile().getTotalSpace ());
		return totalSpace;
	}


	private File getFile(){
		return this.file;
	}
}
