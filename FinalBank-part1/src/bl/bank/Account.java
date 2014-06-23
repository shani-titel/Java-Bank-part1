package bl.bank;
import java.util.ArrayList;
import java.util.List;

import bl.actions.CustomerAction;


public class Account {
	public static final int STUDENT_DISCOUNT = 20;
	public static final int SOLDIER_DISCOUNT = 15;
	public static final int BUSINESS_DISCOUNT = 10;
	
	public enum AccountType{Soldier, Student, Business};
	
	static int accountNumber= 0; //for account indexing in the bank
	
	private int id;
	private int discount;
	private double balance;
	public AccountType aType;
	private List<CustomerAction> actionHistory;
	private List<String> authorized;
	
	public Account(AccountType aType) {
		super();
		this.aType = aType;
		this.balance = 0;
		this.id = ++accountNumber;
		setDiscount();
		actionHistory = new ArrayList();
		authorized = new ArrayList();
	}
	
	public Account(AccountType aType, double balance) {
		this(aType);
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "Account [id=" + id + ", balance=" + balance + ", aType="
				+ aType + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getAuthorized() {
		return authorized;
	}

	public void setAuthorized(List<String> authorized) {
		this.authorized = authorized;
	}
	
	public void addAuthorized(String newAuthorized) {
		this.authorized.add(newAuthorized);
	}

	public List<CustomerAction> getActionHistory() {
		return actionHistory;
	}

	public void setActionHistory(List<CustomerAction> actionHistory) {
		this.actionHistory = actionHistory;
	}
	
	public void addActionToHistory(CustomerAction action) {
		this.actionHistory.add(action);
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double amount) {
		this.balance = amount;
	}
	
	public void addToBalance(double amount) {
		this.balance+= amount;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount() {
		if(aType != null){
			if( aType == AccountType.Student)
				this.discount = STUDENT_DISCOUNT;
			else if(aType == AccountType.Soldier)
				this.discount = SOLDIER_DISCOUNT;
			else if(aType == AccountType.Business)
				this.discount = BUSINESS_DISCOUNT;
			else
				this.discount = 0;
			}		
	}

	public AccountType getaType() {
		return aType;
	}

	public void setaType(AccountType aType) {
		this.aType = aType;
	}
	
	

	
}
