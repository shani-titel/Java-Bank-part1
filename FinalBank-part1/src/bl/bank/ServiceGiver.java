package bl.bank;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.logging.FileHandler;

import logger.BankLogger;

public abstract class ServiceGiver implements Runnable {
	
	protected int id;
	protected Queue<Customer> waitingCustomers;
	//protected Customer currCustomer; NOT IN USE?
	protected static Object mutex;
	protected BankLogger myBankLogger;
	protected FileHandler serviceGiverHanlder;
	protected boolean isOpen = true;
	public abstract void addCustomerToQueue(Customer c);
	public abstract void close();
	protected abstract void notifyCustomer();
	protected boolean isRunning = true;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void stopThread(){
	}
}
