package me.deepak.spy.log;

/**
 * Factory of me.deepak.spy.log.LogProcessor. Currently provides object of
 * me.deepak.spy.log.LinuxLogProcessor & me.deepak.spy.log.WindowsLogProcessor.
 * 
 * @author deepak
 */

public final class LogProcessorFactory {

	private static final String OS_NAME = System.getProperty("os.name");

	private LogProcessorFactory() {
	}

	public static final LogProcessor getInstance() {
		if (OS_NAME.contains("Linux")) {
			return new LinuxLogProcessor();
		} else if (OS_NAME.contains("Windows")) {
			return new WindowsLogProcessor();
		}
		throw new IllegalArgumentException("UnSupported OS : " + OS_NAME);
	}
}
