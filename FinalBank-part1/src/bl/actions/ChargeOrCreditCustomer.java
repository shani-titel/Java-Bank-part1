package bl.actions;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;

import bl.bank.Account;


public class ChargeOrCreditCustomer extends Action {
	
	public ChargeOrCreditCustomer(double amount){
		this.amount = amount;
		this.datePerformed = new Date();
	}
	
	@Override
	public String execute(Account theAccount) {
		Scanner theScanner = new Scanner(System.in);
		theScanner.close();
		datePerformed = new Date();
		theAccount.addToBalance(amount);
		return ", You performed an charge or credit action.";
	}

	@Override
	public double getAmount() {
		return this.amount;
	}

}
