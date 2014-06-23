package bl.actions;
import java.util.Date;
import java.util.logging.Level;

import bl.bank.Account;
import bl.bank.Customer;


public class GetInfoPage extends CustomerAction {

	private final double COMMISSION = 1.5;
	
	public GetInfoPage(Customer c){
		theCustomer = c; 
	}
	
	@Override
	public String execute(Account theAccount) {
		
		//saving the action
		this.datePerformed = new Date();
		this.isBanker = isBanker;
		this.commission = COMMISSION;
		theAccount.addToBalance(COMMISSION);
		theAccount.addActionToHistory(this);
		
		//printing info page
		System.out.println("Actions performed until " + this.datePerformed.toString() + ":");
		System.out.println("____________________________________________");
		if(theAccount.getActionHistory().size() == 0)
			System.out.println("No Actions In Account!");
		else
		{
			for(int i=0; i<theAccount.getActionHistory().size(); i++)
			{
				System.out.println(i+1 +") " + theAccount.getActionHistory().get(i).toString());
			}
		}
		return theCustomer.getCustName() + ", You printed an Information Page";
	}

	@Override
	public String toString() {
		String isBankerAction = "ATM";
		if(isBanker)
			isBankerAction = "Banker";
		return "Information Page Action, Performed on " + datePerformed + " by " + isBankerAction + ". " + "Commission: " + commission;  
	}

	@Override
	public double getAmount() {
		return 0;
	}



}
