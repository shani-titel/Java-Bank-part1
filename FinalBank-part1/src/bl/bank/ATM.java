package bl.bank;
import java.io.IOException;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import logger.ATMFilter;
import logger.BankLogger;


public class ATM extends ServiceGiver {
	private String address;
	
	public ATM(int id, String address, BankLogger theLogger) throws SecurityException, IOException{
		this.id = id;
		this.address = address;
		waitingCustomers = new LinkedList<Customer>();
		myBankLogger = theLogger;
		serviceGiverHanlder = new FileHandler("ATM ID_" + this.id + ".xml", true);
		serviceGiverHanlder.setFormatter(new SimpleFormatter());
		serviceGiverHanlder.setFilter(new ATMFilter(String.valueOf(this.id)));
		
		myBankLogger.getTheLogger().addHandler(serviceGiverHanlder);
	}
	
	@Override
	public String toString() {
		return "ATM [id= " + id +" Address= " + address +  "]";
	}

	@Override
	public void addCustomerToQueue(Customer c) {
		c.setIsWaiting(true);
		waitingCustomers.add(c);
		System.out.println(c+" is added to Line for ATM");
		
		//log
		myBankLogger.getTheLogger().log(Level.INFO, "Customer " + c.getCustName() + " is now in line for ATM #" + this.id);
		
		c.setCurrentHandlingServiceGiver(this);
		if(!(c.isAlive()))
			c.start();
		
		synchronized (mutex) {
			if (waitingCustomers.size() == 1) {
				myBankLogger.getTheLogger().log(Level.INFO, "The line is no longer empty -> notifying ATM #" + this.id);
				mutex.notify(); // to let know there is a Customer waiting
			}
		}
	}

	
	
	@Override
	public void stopThread() {
		isRunning = false;
	}

	@Override
	public void run() {
		myBankLogger.getTheLogger().log(Level.INFO, "ATM #" + this.id + " started working!");
		while(isRunning){
			if(!waitingCustomers.isEmpty()){
				notifyCustomer();
			}
			else {
				synchronized (mutex) {
					try {
						myBankLogger.getTheLogger().log(Level.INFO, "ATM #" + this.id + " line is empty.");
						mutex.wait(); // wait till there is a Customer
						myBankLogger.getTheLogger().log(Level.INFO, "ATM #" + this.id + " recieved a message there is a customer waiting.");
					} catch (InterruptedException e) {
						e.printStackTrace();
						isRunning = false;
					}
				}
			}
		}
		myBankLogger.getTheLogger().log(Level.INFO, "ATM #" + this.id + " finished working.");
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
					myBankLogger.getTheLogger().log(Level.INFO, "ATM #" + this.id + " notifying customer: " + firstCustomer.getCustName());
					synchronized (firstCustomer) {
						firstCustomer.notifyAll();
					}
					notifyCustomer = true;
				}
			}
			synchronized (this) {
				try {
					//System.out.println("Banker waits that  Customer " + firstCustomer.getCustName() + will announce it is finished");
					myBankLogger.getTheLogger().log(Level.INFO, "ATM #" + this.id + " is waiting for customer: " + firstCustomer.getCustName()
							+ " to announce he is finished");
					firstCustomer.setIsWaiting(false);//important! do not move from this location!
					wait(); // wait till the Customer finishes
					//System.out.println("Banker was announced that  Customer " + firstCustomer.getCustName() + " is finished");
					myBankLogger.getTheLogger().log(Level.INFO, "ATM #" + this.id + " announced that customer: " + firstCustomer.getCustName()
							+ " is finished");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public void close() {
		stopThread();
		synchronized (mutex) {
			mutex.notifyAll();
		}
		System.out.println("ATM is stopped from main");
	}

}
