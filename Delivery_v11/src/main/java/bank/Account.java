package bank;

public class Account {
	private double balance;
	
	public Account (double _balance) {
		balance = _balance;
	}
	
	synchronized public void deposit (double amount) {
		balance +=amount;
	}
	
	synchronized public void withdraw (double amount) {
		balance -=amount;
	}
	
	public double getBalance () {
		return balance;
	}
	
	public void setBalance (double _balance) {
		balance = _balance;
	}
}