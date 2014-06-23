package bl.actions;
import java.util.Date;

import bl.bank.Account;


public class WithdrawOrDeposit extends CustomerAction {
	
	private final double COMMISSION = 2;
	
	public WithdrawOrDeposit(double amount, boolean isBanker){
		this.amount= amount;
		this.isBanker = isBanker;
		this.datePerformed = new Date();
		this.commission = COMMISSION;		
	}
	
	@Override
	public String toString() {
		String isBankerAction = "ATM";
		String isDepositString = "Deposit";
		double amountOfAction = amount;
		if(amount < 0)
		{
			amountOfAction*= -1;
			isDepositString = "Withdraw";
		}
		if(isBanker)
			isBankerAction = "Banker";
		return isDepositString + " Action, Performed on " + datePerformed + " by " + isBankerAction + ". " + "Amount: " + amountOfAction + ", Commission: " + commission;  
	}

	@Override
	public String execute(Account theAccount) {
		
		String returnStr;

		double balance = theAccount.getBalance() + amount;
		if(balance < 0){
			System.out.println("Not enough money!");
			returnStr = /*theCustomer.getCustName() + */", You failed to withdraw";
		}
		else
		{
			System.out.println("Old Balance: " + theAccount.getBalance());
			theAccount.addToBalance(amount-COMMISSION);
			theAccount.addActionToHistory(this);
			//write to log
			System.out.println("New Balance: " + theAccount.getBalance());
			returnStr = /*theCustomer.getCustName() + */", You made a withdraw/deposit";
		}
		return returnStr;
	}
	
	public double getAmount(){
		return this.amount;
	}


}
