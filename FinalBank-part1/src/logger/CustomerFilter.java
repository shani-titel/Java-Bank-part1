package logger;
import java.util.logging.Filter;
import java.util.logging.LogRecord;


public class CustomerFilter implements Filter {

	private String name;
	
	public CustomerFilter(String cName){
		name = cName;
	}
	
	@Override
	public boolean isLoggable(LogRecord rec) {
		if(rec.getMessage().contains(name) && !rec.getMessage().toLowerCase().contains("banker"))
			return true;
		else
			return false;
	}
	
}
