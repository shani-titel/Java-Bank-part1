package bl.actions;
import java.text.SimpleDateFormat;
import java.util.Date;

import bl.bank.Account;


public abstract class Action {
	protected double amount;
	protected Date datePerformed;
	protected Account theAccount;
	public abstract String execute(Account theAccount); //returns string for log
	public abstract double getAmount();
}
