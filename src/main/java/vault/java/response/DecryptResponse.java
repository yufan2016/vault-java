package vault.java.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DecryptResponse {
	/*
    {   "data": {     "plaintext": "dGhlIHF1aWNrIGJyb3duIGZveAo="   } }
 */

public class Data{

	@Override
	public String toString() {
		return "Data [plaintext=" + plaintext + "]";
	}



	@JsonProperty("plaintext")
	private String plaintext;
	
	public String getPlaintext() {
		return plaintext;
	}
	

	
	public Data(){
		
	}
	


}

@JsonProperty
private Data data;

@JsonProperty("lease_id")
private String leaseId;

public Data getData() {
	return data;
}

public String getLeaseId() {
    return leaseId;
}


@Override
public String toString() {
	return "TransitResponse [data=" + data + "]";
}


}
