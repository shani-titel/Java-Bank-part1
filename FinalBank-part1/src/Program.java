
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

import logger.BankLogger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dal.XMLReader;

import bl.actions.GetInfoPage;
import bl.actions.GiveAuthorization;
import bl.actions.WithdrawOrDeposit;
import bl.bank.ATM;
import bl.bank.Account;
import bl.bank.Bank;
import bl.bank.Banker;
import bl.bank.Branch;
import bl.bank.Customer;


public class Program {
	
	private static XMLReader xmlReader = new XMLReader();
	public static final int amountToWithdraw = 10;
	public static final String organization = "IBM";
	
	public static void main(String[] args) throws SecurityException, IOException {
		testThreads();		
	}
	public static void testThreads() throws SecurityException, IOException{
		BankLogger myBankLogger = new BankLogger();
		Bank theBank = new Bank(12, "Hapoalim");
		Branch theBranch = new Branch(1, "Hakishon 1, Ramat-Gan");
		
		//read data from XML
		List<ATM> allAtms = xmlReader.createXMLATMs(myBankLogger);
		List<Customer> allCustomers = xmlReader.createXMLCustomers(myBankLogger);
		List<Banker> allBankers = xmlReader.createXMLBankers(myBankLogger);
		
		//must build with constructor for LOG functions
		ATM theAtm = new ATM(allAtms.get(0).getId(), allAtms.get(0).getAddress(), myBankLogger); 
		//start atm thread
		Runnable ra = theAtm;
		Thread atmThread = new Thread(ra);
		atmThread.start();
		
		//must build with constructor for LOG functions
		Banker theBanker = new Banker(allBankers.get(0).getId(), myBankLogger); 
		theBank.addBranch(theBranch);
		theBranch.addBanker(theBanker);
		//start banker thread 
		Runnable rb = theBanker;
		Thread bankerThread = new Thread(rb);
		bankerThread.setName("Banker");
		bankerThread.start();
		
			
		//register 2 customers to the banker with withdraw action of 10 shekels
		System.out.println("Customers to Banker - WITHDRAW "+amountToWithdraw);
		for(int i=0; i<2;i++){
			Customer c = allCustomers.get(i);
			int accountID = c.getCustId();
			c.setAccountForAction(accountID);
			boolean isBanker = true;
			int amount = amountToWithdraw;
			WithdrawOrDeposit withdraw = new WithdrawOrDeposit(-amount, isBanker);
			c.applyAction(withdraw);
			theBanker.addCustomerToQueue(c);
		}
		
		//register 2 customers to the ATM for give authorization action to IBM
		System.out.println("Customers to ATMs - Give Authorization Action "+organization);
		for(int i=2; i<4;i++){
			Customer c = allCustomers.get(i);
			int accountID = c.getCustId();
			c.setAccountForAction(accountID);
			boolean isBanker = false;
			GiveAuthorization au = new GiveAuthorization(organization, isBanker);
			c.applyAction(au);
			theAtm.addCustomerToQueue(c);
		}
		
		//register 3 customers to the banker for printing info page
		System.out.println("Customers to Banker - Print Info Page");
		for(int i=4; i<allCustomers.size();i++){
			Customer c = allCustomers.get(i);
			int accountID = c.getCustId();
			c.setAccountForAction(accountID);
			GetInfoPage page = new GetInfoPage(c);
			c.applyAction(page);
			theBanker.addCustomerToQueue(c);
		}
		
		//join all customer threads
		for (int i=0 ; i < allCustomers.size() ; i++) {
			try {
				if(allCustomers.get(i).isAlive())
					allCustomers.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(Customer c : allCustomers){
			theBranch.addAccount(c.getCurrAccount());
			//just for testing 
			if(c.getCustId() % 2 == 0){
				theBanker.chargeCustomer(20, c);
			}
			else{
				theBanker.creditCustomer(100, c);
			}
		}
		
		//close banker and atm
		theBanker.close();
		theAtm.close();
		try {
			atmThread.join();
			bankerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		theBranch.closeSystem();
		System.out.println("GoodBye!");
	}
}
	
	