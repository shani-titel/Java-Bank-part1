package dal;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bl.bank.ATM;
import bl.bank.Account;
import bl.bank.Banker;
import bl.bank.Customer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import logger.BankLogger;

public class XMLReader {
	
	private File xmlFile;
	
	public XMLReader(){
		URL url = getClass().getResource("bankConfig.xml");
		xmlFile = new File(url.getPath());
	}
	public NodeList readNodeFromFile(String node){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			
			return doc.getElementsByTagName(node);
		 
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<ATM> createXMLATMs(BankLogger theLogger) throws SecurityException, IOException{
		List<ATM> atms = new ArrayList<>();
		NodeList atmsFromXML = readNodeFromFile("atm");
		for(int i=0; i<atmsFromXML.getLength(); i++){
			Node currAtm = atmsFromXML.item(i);
			if (currAtm.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) currAtm;
				int id = Integer.parseInt(eElement.getAttribute("id"));
				String address = eElement.getAttribute("location");	
				ATM newAtm = new ATM(id, address, theLogger);
				atms.add(newAtm);
				//System.out.println(newAtm);
			}
		}
		return atms;
	}
	
	public List<Banker> createXMLBankers(BankLogger theLogger) throws SecurityException, IOException{
		List<Banker> bankers = new ArrayList<>();
		NodeList bankersFromXML = readNodeFromFile("banker");
		for(int i=0; i<bankersFromXML.getLength(); i++){
			Node currBanker = bankersFromXML.item(i);
			if (currBanker.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) currBanker;
				int id = Integer.parseInt(eElement.getAttribute("id"));
				Banker newBanker = new Banker(id, theLogger);
				bankers.add(newBanker);
				//System.out.println(newAtm);
			}
		}
		return bankers;
	}
	
	public List<Account> createAllXMLAccounts(){
		List<Account> accounts = new ArrayList<>();
		NodeList accountsFromXML = readNodeFromFile("account");
		
		for(int i=0; i<accountsFromXML.getLength(); i++){
			Node currAccount = accountsFromXML.item(i);
			if (currAccount.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) currAccount;
				double balance = Double.parseDouble(eElement.getAttribute("balance"));
				String type = eElement.getAttribute("type");
				Account.AccountType aType = Account.AccountType.valueOf(type);
				Account newAccount = new Account(aType, balance);
				accounts.add(newAccount);
				//System.out.println(newAccount);
			}
		}
		return accounts;
	}
	public List<Customer> createXMLCustomers(BankLogger theLogger){
		List<Customer> customers = new ArrayList<>();
		NodeList customersFromXML = readNodeFromFile("customer");
		for(int i=0; i < customersFromXML.getLength(); i++){
			Node currCust = customersFromXML.item(i);
			if (currCust.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) currCust;
				//read customer details from xml
				String name = eElement.getAttribute("name");	
				//read accounts
				List<Account> custAccounts = readXMLAccountsByCustomer(eElement);
				Customer c;
				try {
					c = new Customer(name, custAccounts, theLogger);
					customers.add(c);
				} catch (SecurityException | IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		return customers;
	}
	public List<Account> readXMLAccountsByCustomer(Element currCustomer){
		List<Account> accounts = new ArrayList<>();
		NodeList accountsFromXML = currCustomer.getElementsByTagName("account");
		for(int i=0; i<accountsFromXML.getLength(); i++){
			Node currAccount = accountsFromXML.item(i);
			if (currAccount.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) currAccount;
				double balance = Double.parseDouble(eElement.getAttribute("balance"));
				String type = eElement.getAttribute("type");
				Account.AccountType aType = Account.AccountType.valueOf(type);
				Account newAccount = new Account(aType, balance);
				accounts.add(newAccount);
				//System.out.println(newAccount);
			}
		}
		return accounts;
	}
}

	 
	  
	 
	