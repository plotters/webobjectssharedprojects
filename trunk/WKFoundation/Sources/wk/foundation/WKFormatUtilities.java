package wk.foundation;

public class WKFormatUtilities {
	public static final long BYTE_BASE = 1024l;

	/**
	 * Takes a byte value and returns a friendly B, KB, MB or GB value
	 * @param bytes
	 */
	public static String userFriendlyByteCountString(long bytes) {
		if (bytes / BYTE_BASE < 1) {
			return bytes + " B";
		} //~ if (bytes / BYTE_BASE < 0.1)

		bytes = bytes/BYTE_BASE;
		if (bytes / BYTE_BASE < 1) {
			return bytes + " KB";
		} //~ if (bytes / BYTE_BASE < 1)

		bytes = bytes/BYTE_BASE;
		if (bytes / BYTE_BASE < 1) {
			return bytes + " MB";
		} //~ if (bytes / BYTE_BASE < 1)

		bytes = bytes/BYTE_BASE;
		return bytes + " GB";
	}
}
