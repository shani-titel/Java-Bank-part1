package bl.bank;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import bl.actions.Action;

import logger.BankLogger;
import logger.CustomerFilter;


public class Customer extends Thread{
	
	private static int idGenerator=0;
	private int id;
	private String name;
	private List<Account> accounts;
	private BankLogger theBankLogger;
	private FileHandler customerHanlder;
	private Action customerAction;
	private Account currentAccount;
	private ServiceGiver currentHandlingServiceGiver;
	private boolean isWaiting;
	
	public Customer(){
		super();
	}
	
	public Customer(String name, List<Account> accounts, BankLogger theLogger) throws SecurityException, IOException {
		super();
		id = ++idGenerator;
		this.name = name;
		this.accounts = accounts;
		theBankLogger = theLogger;
		customerHanlder = new FileHandler("Customer ID_" + this.id + "_Name_" + this.name + ".xml", true);
		customerHanlder.setFormatter(new SimpleFormatter());
		customerHanlder.setFilter(new CustomerFilter(this.name));
		
		theBankLogger.getTheLogger().addHandler(customerHanlder);
		
		this.setName(name);
	}
	
	public void addAccount(Account theAccount){
		accounts.add(theAccount);
		theBankLogger.getTheLogger().log(Level.INFO, this.name + ", An account was added. Account #" + theAccount.getId());
	}
	
	public void printAccouns(){
		for(Account tempAccount: this.accounts){
			System.out.println(tempAccount);
		}
	}
	
	public void setAccountForAction(int accountID){
		if(!this.accounts.isEmpty()){
			for(Account tempAccount: this.accounts){
				if(tempAccount.getId() == accountID){
					this.currentAccount = tempAccount;
					return;
				}
			}
		}
		
	}
	
	public void applyAction(Action theAction){
		this.customerAction = theAction;
	}
	
	@Override
	public void run() {
		while (isWaiting) {
			
			System.out.println("Customer " + this.name + " is running");
			theBankLogger.getTheLogger().log(Level.INFO,
					name + ", You are now running.");

			try {
				synchronized (this) {
					System.out.println(this + " is waiting in queue");
					theBankLogger.getTheLogger().log(Level.INFO,
							name + ", You are waiting.");
					System.out.println(this + " is going for a walk");
					this.wait();
					System.out.println(this + " was notifyed it's his turn");
					theBankLogger.getTheLogger().log(Level.INFO,
							name + ", It's your turn!.");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				// isRunning = false;
			}

			// execute requested action
			String resultToLog = this.name
					+ customerAction.execute(currentAccount);

			// write action details to log
			theBankLogger.getTheLogger().log(Level.INFO, resultToLog);
			// end writing action to LOG

			// notify service giver that finished
			System.out.println(this+" notify to service giver that he finished");
			synchronized (currentHandlingServiceGiver) {
				currentHandlingServiceGiver.notify();
			}
		}
		System.out.println("Customer " + this + " has finished running");
		theBankLogger.getTheLogger().log(Level.INFO,
				name + ", You have finished running.");
	}

	@Override
	public String toString() {
		//return "Customer [id=" + id + ", name=" + name + "]";
		return this.name + " id: " + this.id;
	}

	public int getCustId() {
		return id;
	}

	public void setCustId(int id) {
		this.id = id;
	}

	public String getCustName() {
		return name;
	}

	public void setCustName(String name) {
		this.name = name;
	}
	
	public ServiceGiver getCurrentHandlingServiceGiver() {
		return currentHandlingServiceGiver;
	}

	public void setCurrentHandlingServiceGiver(
			ServiceGiver currentHandlingServiceGiver) {
		this.currentHandlingServiceGiver = currentHandlingServiceGiver;
	}

	public Account getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}

	public BankLogger getTheBankLogger() {
		return theBankLogger;
	}

	public void setTheBankLogger(BankLogger theBankLogger) {
		this.theBankLogger = theBankLogger;
	}
	
	public void setIsWaiting(boolean iswait){
		this.isWaiting = iswait;
	}
	
	public boolean getIsWaiting(){
		return this.isWaiting;
	}
	public Account getCurrAccount(){
		return this.currentAccount;
	}
}
