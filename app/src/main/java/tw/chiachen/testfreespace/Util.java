package tw.chiachen.testfreespace;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Locale;

/**
 * Created by Jason_Chien on 2016/11/7.
 */
public class Util {
	private static final String ERROR = "ERROR";
	public static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static String getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return formatSize(availableBlocks * blockSize);
	}

	public static String getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return formatSize(totalBlocks * blockSize);
	}


	public static String formatSize(long size) {
		String suffix = null;

		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
				if (size>=1024) {
					suffix = "GB";
					size /=1024;
				}
			}
		}

		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}

		if (suffix != null) resultBuffer.append(suffix);
		return resultBuffer.toString();
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
		String pre ="KMGTPE".charAt(exp-1)+"" ;
		return String.format("%.3f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
