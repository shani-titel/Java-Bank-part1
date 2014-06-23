package bl.bank;
import java.util.ArrayList;
import java.util.List;


public class Bank {
	private int id;
	private String name;
	private List<Branch> branches;
	
	public Bank(int id, String n) {
		this.id = id;
		this.name = n;
		this.branches = new ArrayList<>();
		System.out.println("WELCOME TO " + this);
	}
	

	
	public void addBranch(Branch b){
		this.branches.add(b);
	}

	@Override
	public String toString() {
		return "BANK id=" + id + ", name=" + name + "";
	}


	
	
	
}
