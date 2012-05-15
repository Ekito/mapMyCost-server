package models;

public class Heatpoint {

	public float lon;

	public float lat;

	/**
	 * The amount of the transaction, in '%'
	 */
	public int count;

	public Heatpoint(float latitude, float longitude, int amount) {
		this.lat = latitude;
		this.lon = longitude;
		this.count = amount;
	}

}
