package models;

/**
 * A transaction with the picture and the location.
 * 
 * @author ndeverge
 * 
 */
public class Transaction {

	public TransactionSummary summary;

	public float longitude;

	public float latitude;

	// TODO picture

	public Transaction() {

	}

	public Transaction(TransactionSummary summary, float latitude,
			float longitude) {
		super();
		this.summary = summary;
		this.longitude = longitude;
		this.latitude = latitude;
	}

}
