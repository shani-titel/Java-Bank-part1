package bl.bank;
import java.io.IOException;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import logger.BankLogger;
import logger.BankerFilter;

import bl.actions.ChargeOrCreditCustomer;


public class Banker extends ServiceGiver {
	private ChargeOrCreditCustomer chargeAction;
	private ChargeOrCreditCustomer creditAction;
	
	public Banker(int id, BankLogger theLogger) throws SecurityException, IOException{
		waitingCustomers = new LinkedList<Customer>();
		mutex = new Object();
		this.id = id;
		myBankLogger = theLogger;
		serviceGiverHanlder = new FileHandler("Banker ID_" + this.id + ".xml", true);
		serviceGiverHanlder.setFormatter(new SimpleFormatter());
		serviceGiverHanlder.setFilter(new BankerFilter(String.valueOf(this.id)));
		
		myBankLogger.getTheLogger().addHandler(serviceGiverHanlder);
	}
	
	@Override
	public void addCustomerToQueue(Customer c) {
		c.setIsWaiting(true);
		waitingCustomers.add(c);
		
		//System.out.println(c+" is added to Line for " + this);
		myBankLogger.getTheLogger().log(Level.INFO, "Customer " + c.getCustName() + " is now in line for banker #" + this.id);
		
		c.setCurrentHandlingServiceGiver(this);//save current service giver
		
		if(!(c.isAlive()))//start customer thread
			c.start(); 
		
		//notify banker the line isn't empty
		synchronized (mutex) {
			if (waitingCustomers.size() == 1) {
				myBankLogger.getTheLogger().log(Level.INFO, "The line is no longer empty -> notifying banker #" + this.id);
				mutex.notify(); // to let know there is a Customer waiting
			}
		}
	}
	
	public int getId(){
		return this.id;
	}

	@Override
	protected void notifyCustomer() {
		Customer firstCustomer = waitingCustomers.poll();
		
		if (firstCustomer != null) {
			boolean notifyCustomer = false;
			while (!notifyCustomer) {
				// make sure the customer is already in 'waiting' state
				// (synchronization)
				if (firstCustomer.getState() == State.WAITING) {
					//System.out.println("Banker notifies Customer " + firstCustomer.getCustName());
					myBankLogger.getTheLogger().log(Level.INFO, "Banker #" + this.id + " notifying customer: " + firstCustomer.getCustName());
					synchronized (firstCustomer) {
						firstCustomer.notifyAll();
					}
					notifyCustomer = true;
				}
			}
			synchronized (this) {
				try {
					//System.out.println("Banker waits that  Customer " + firstCustomer.getCustName() + will announce it is finished");
					myBankLogger.getTheLogger().log(Level.INFO, "Banker #" + this.id + " is waiting for customer: " + firstCustomer.getCustName()
							+ " to announce he is finished");
					firstCustomer.setIsWaiting(false);//important! do not move from this location!
					wait(); // wait till the Customer finishes
					//System.out.println("Banker was announced that  Customer " + firstCustomer.getCustName() + " is finished");
					myBankLogger.getTheLogger().log(Level.INFO, "Banker #" + this.id + " announced that customer: " + firstCustomer.getCustName()
							+ " is finished");
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Override
	public void run() {
		//System.out.println("In Banker::run");
		myBankLogger.getTheLogger().log(Level.INFO, "Banker #" + this.id + " started working!");
		while(isRunning){
			if(!waitingCustomers.isEmpty()){
				notifyCustomer();
			}
			else {
				synchronized (mutex) {
					try {
						//System.out.println("Banker has no Customers waiting");
						myBankLogger.getTheLogger().log(Level.INFO, "Banker #" + this.id + " line is empty.");
						mutex.wait(); // wait till there is a Customer
											// waiting
						//System.out.println("Banker received a message there is a Customer waiting");
						myBankLogger.getTheLogger().log(Level.INFO, "Banker #" + this.id + " recieved a message there is a customer waiting.");
					} catch (InterruptedException e) {
						e.printStackTrace();
						isRunning = false;
					}
				}
			}
		}
		//System.out.println("Banker finished running");
		myBankLogger.getTheLogger().log(Level.INFO, "Banker #" + this.id + " finished working.");
	}
	
	public void chargeCustomer(double amount, Customer c){
		this.chargeAction = new ChargeOrCreditCustomer(-amount);
		this.chargeAction.execute(c.getCurrAccount());
		System.out.println("Banker charged " + c + " for amount: " + amount);
		myBankLogger.getTheLogger().log(Level.INFO, "Banker #" + this.id + " charged " + c + " for amount: " + amount);
	}
	
	public void creditCustomer(double amount, Customer c){
		this.creditAction = new ChargeOrCreditCustomer(amount);
		this.creditAction.execute(c.getCurrAccount());
		System.out.println("Banker credited " + c + " for amount: " + amount);
		myBankLogger.getTheLogger().log(Level.INFO, "Banker #" + this.id + " credited " + c + " for amount: " + amount);
	}
	
	@Override
	public void stopThread() {
		isRunning = false;
	}

	@Override
	public String toString() {
		return "Banker [id=" + id + "]";
	}

	@Override
	public void close() {
		stopThread();
		synchronized (mutex) {
			mutex.notifyAll();
		}
		
		System.out.println("Banker is stopped from main");
	}
	
	public BankLogger getTheBankLogger(){
		return this.myBankLogger;
	}
	
}
