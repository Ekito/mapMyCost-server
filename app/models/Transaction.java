package models;

/**
 * A transaction with the picture and the location.
 * 
 * @author ndeverge
 * 
 */
public class Transaction extends TransactionSummary {

	public float longitude;

	public float latitude;

	// TODO picture

	public Transaction() {

	}

	public Transaction(TransactionSummary summary, float latitude,
			float longitude) {
		super(summary.id, summary.date, summary.amount, summary.title,
				summary.mapped);
		this.longitude = longitude;
		this.latitude = latitude;
	}

}
