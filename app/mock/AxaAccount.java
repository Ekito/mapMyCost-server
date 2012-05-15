package mock;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class AxaAccount {
	public BigInteger id; 
	public long customer;
	public String type;
	public String currency;
	public String bic;
	public String label;
	
	
	/*public Map toHashMap() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("accountid", id.toString());
		data.put("customer", customer+"");
		data.put("currency",  "\""+currency +"\"");
		data.put("bic",  "\""+bic+ "\"");
		data.put("label", "\""+label +"\"");
		return data;

	}*/
}