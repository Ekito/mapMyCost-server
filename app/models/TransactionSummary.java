package models;

import java.util.Date;

/**
 * A transaction summary.
 * 
 * @author ndeverge
 * 
 */
public class TransactionSummary {

	/**
	 * The id of the transaction.
	 */
	public String id;

	/**
	 * Date of the transaction (not when the transaction occured on the account)
	 */
	public Date date;

	public String amount;

	public String title;

	/**
	 * The transaction has been mapped with a picture and a location ?
	 */
	public boolean mapped;

	public TransactionSummary() {

	}

	public TransactionSummary(String id, Date date, String amount,
			String title, boolean mapped) {
		super();
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.title = title;
		this.mapped = mapped;
	}

}
