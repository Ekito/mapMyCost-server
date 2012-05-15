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

	/**
	 * The picture represented by an url on the server.
	 */
	public String picture;

	public Transaction() {
		 
	}

	public Transaction(TransactionSummary summary, float latitude,
			float longitude, String picture) {
		super(summary.id, summary.date, summary.amount, summary.title,
				summary.mapped);
		this.longitude = longitude;
		this.latitude = latitude;
		this.picture = picture;
	}

	public Transaction(TransactionSummary summary) {
		super(summary.id, summary.date, summary.amount, summary.title,
				summary.mapped);
	}

}
