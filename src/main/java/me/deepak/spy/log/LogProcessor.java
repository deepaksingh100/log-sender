package me.deepak.spy.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Interface to process logs.
 * 
 * @author deepak
 */
public interface LogProcessor {

	/**
	 * Returns all logfile names (except same day) that startsWith access.log
	 * 
	 * @author deepak
	 */

	String[] getLogs();

	/**
	 * This method compress given logfiles to a file named log.zip
	 * 
	 * @author deepak
	 */

	boolean zipLogFiles(String[] logs);

	/**
	 * Returns apache2 log files path.
	 * 
	 * @author deepak
	 */

	String getLogPath();

	/**
	 * This method writes logfiles to zip file.
	 * 
	 * @author deepak
	 */

	default void writeToZipFile(String[] logs) throws IOException {

		try (ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(getZippedLogFilePath()))) {
			for (String log : logs) {
				File file = new File(new StringBuilder(getLogPath()).append(File.separator).append(log).toString());
				try (FileInputStream inputStream = new FileInputStream(file)) {
					ZipEntry zipEntry = new ZipEntry(log);
					outputStream.putNextEntry(zipEntry);
					byte[] bytes = new byte[512];
					int length;
					while ((length = inputStream.read(bytes)) >= 0) {
						outputStream.write(bytes, 0, length);
					}
					outputStream.closeEntry();
				}
			}
		}
	}

	/**
	 * Returns files that are successfully sent and to be deleted.
	 * 
	 * @author deepak
	 */

	default String[] filesToBeDeleted(String[] logs) {
		return logs;
	}

	/**
	 * Returns constant zipped file name "log.zip".
	 * 
	 * @author deepak
	 */

	default String getZippedLogFileName() {
		return Constants.ZIPPED_FILE_NAME.getName();
	}

	/**
	 * Returns zipped log file path. Returns
	 * "getLogPath(){seperator}getZippedLogFileName()"
	 * 
	 * @author deepak
	 */

	default String getZippedLogFilePath() {
		return new StringBuilder(getLogPath()).append(File.separator).append(getZippedLogFileName()).toString();
	}

	/**
	 * Deletes given log files from logPath folder.
	 * 
	 * @author deepak
	 */

	default void deleteLogs(String[] logs) {
		String logPath = getLogPath();
		for (String log : logs) {
			new File(new StringBuilder(logPath).append(File.separator).append(log).toString()).delete();
		}
	}

}
