package bl.actions;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;

import bl.bank.Account;


public class GiveAuthorization extends CustomerAction {

	private final double COMMISSION = 1.5;
	private String name;
	
	public GiveAuthorization(String name, boolean isBanker){
		this.name= name;
		this.datePerformed = new Date();
		this.isBanker = isBanker;
		this.commission = COMMISSION;
	}
	
	@Override
	public String execute(Account theAccount) {		
		//saving the action
		theAccount.addToBalance(COMMISSION);
		theAccount.addActionToHistory(this);
				
		//adding authorization
		theAccount.addAuthorized(this.name);
		System.out.println("Gave Authorization to: "+name);
		return ", You added " + this.name + " to account #" + theAccount.getId();
	}

	@Override
	public String toString() {
		String isBankerAction = "ATM";
		if(isBanker)
			isBankerAction = "Banker";
		return "Authorization Action, Performed on " + datePerformed + " by " + isBankerAction + ". " + "Commission: " + commission;
	}

	@Override
	public double getAmount() {
		return 0;
	}

}
