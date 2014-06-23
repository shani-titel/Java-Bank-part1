package logger;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Date;

public class BankLogger {
	private static Logger theLogger = Logger.getLogger("BankLogger");
	
	public BankLogger(){
		//theLogger.setUseParentHandlers(false); //disables log to console //bring back later
		theLogger.log(Level.INFO, "The Bank Logger was created on: " + new Date());
	}

	public Logger getTheLogger() {
		return theLogger;
	}

	public static void setTheLogger(Logger theLogger) {
		BankLogger.theLogger = theLogger;
	}
	
	
}
