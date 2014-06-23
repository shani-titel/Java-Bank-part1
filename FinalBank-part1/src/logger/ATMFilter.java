package logger;
import java.util.logging.Filter;
import java.util.logging.LogRecord;


public class ATMFilter implements Filter{
	
	private String id;
	
	public ATMFilter(String id) {
		this.id = id;
	}
	
	@Override
	public boolean isLoggable(LogRecord rec) {
		if(rec.getMessage().contains(id) && rec.getMessage().contains("ATM"))
			return true;
		else
			return false;
	}

}
