package bl.actions;

import bl.bank.Account;
import bl.bank.Customer;

public abstract class CustomerAction extends Action {
	
	protected double commission;
	protected boolean isBanker;
	protected Customer theCustomer;
	public abstract String execute(Account theAccount);
	@Override
	public abstract String toString();
	
}
