package logger;
import java.util.logging.Filter;
import java.util.logging.LogRecord;


public class BankerFilter implements Filter{
	
	private String id;
	
	public BankerFilter(String id) {
		this.id = id;
	}

	@Override
	public boolean isLoggable(LogRecord rec) {
		if(rec.getMessage().contains(id) && rec.getMessage().contains("Banker"))
			return true;
		else
			return false;
	}

}
