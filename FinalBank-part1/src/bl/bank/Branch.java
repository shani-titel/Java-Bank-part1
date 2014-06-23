package bl.bank;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bl.actions.CustomerAction;


public class Branch {
	private int id;
	private String address;
	private Date closingTime;
	private List<Account> accounts;
	private List<Banker> bankers;
	SimpleDateFormat dateFormat;
	
	
	public Branch(int id, String address){
		this.id = id;
		this.address = address;
		this.bankers = new ArrayList<>();
		this.accounts = new ArrayList<>();
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	}
	
	public void closeSystem(){
		double income =0;
		double outcome =0;
		for(int i=0; i<accounts.size(); i++)
		{
			Account curAccount = accounts.get(i);
			for(int j=0; j < curAccount.getActionHistory().size(); j++)
			{
				CustomerAction curAction = curAccount.getActionHistory().get(j);
				double amount = curAction.getAmount();
				if(amount > 0)
					income += amount;
				else
					outcome -= amount;
			}
		}
		
		System.out.println("Today Money Income: " + income);
		System.out.println("Today Money Outcome: " + outcome);
	}
	
	public void addBanker(Banker b){
		this.bankers.add(b);
	}
	
	public void addAccount(Account a){
		this.accounts.add(a);
	}


	@Override
	public String toString() {
		return "Branch [id=" + id + ", address=" + address + ", closingTime="
				+ closingTime + "]";
	}
	
	
}
