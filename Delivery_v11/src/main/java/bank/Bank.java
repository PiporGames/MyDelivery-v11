package bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bank {
	List <Account> accounts;
	int size;
	
	public Bank (int _size, double _amount) {
		size = _size;
		accounts = new ArrayList<> ();
		for (int i=0;i<size;i++) {
			accounts.add (new Account(_amount));
		}
	}
	
	public void addAccount (Account ac) {
		accounts.add(ac);
	}
	
	public int bankSize () {
		return size;
	}
	
	public Account getAccount (int i) {
		return accounts.get(i);
	}
	
	public List<Account> getListAccounts () {
		return accounts;
	}
	
	public double audit (int init, int end) {
		// it performs an audit between init and end accounts
		double total = 0;
		for (int i=init;i<end;i++) {
			total += accounts.get(i).getBalance();
		}
		return total;
	}
	
	public double partialAudit (int source) {
		// it returns the balance of a given account
		return accounts.get(source).getBalance();
	}
	
	public  void  transfer (int source, int target, int amount) {
		// We don't care whether the account has money or not
		accounts.get(source).withdraw(amount);
		try {
			// We intentionally sleep for a while between withdraw and deposit
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		accounts.get(target).deposit(amount);
	}
	
	public void randomTransfers (int times) {
		// we perform 'time' transfers randomly	
		Random r = new Random(times);
		int acc1, acc2 = 0;
		System.out.println("Doing "+times+" tranfers...");
		for (int i=0;i<times;i++) {
			acc1 = r.nextInt(100);
			do acc2 = r.nextInt(100); while (acc1==acc2);
			int amount = r.nextInt(10);
			this.transfer (acc1, acc2, amount);
		}
	}
}