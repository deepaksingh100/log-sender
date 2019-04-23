package me.deepak.spy.log;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LinuxLogProcessor implements LogProcessor {

	private static final String APACHE2_LOG_PATH = new StringBuilder(File.separator).append("var")
			.append(File.separator).append("log").append(File.separator).append("apache2").toString();

	@Override
	public String[] getLogs() {
		return new File(getLogPath()).list((File dir, String name) -> name.startsWith("access.log") && LocalDate.now()
				.compareTo(LocalDate.parse(name.split("-")[1], DateTimeFormatter.ofPattern("yyyy.MM.dd"))) != 0);
	}

	@Override
	public boolean zipLogFiles(String[] logs) {

		if (logs == null || logs.length == 0) {
			return false;
		}
		try {
			writeToZipFile(logs);
		} catch (IOException e) {
			System.err.println("Exception occured while zipping log files is : " + e);
			return false;
		}
		return true;
	}

	@Override
	public String getLogPath() {
		return APACHE2_LOG_PATH;
	}

}
