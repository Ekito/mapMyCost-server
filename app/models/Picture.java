package models;

public class Picture {

	public String id;

	public byte[] bytes;

	public String contentType;

	public Picture(String id, byte[] bytes, String contentType) {
		this.id = id;
		this.bytes = bytes;
		this.contentType = contentType;
	}

}
