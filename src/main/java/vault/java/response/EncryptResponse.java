package vault.java.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class EncryptResponse  {
	/*
    {   "data": {     "ciphertext": "vault:v1:abcdefgh"   } }
 */

public class Data{

	@Override
	public String toString() {
		return "Data [ciphertext=" + ciphertext + "]";
	}
  
	@JsonProperty("ciphertext")
	private String ciphertext;
	
	public String getCiphertext() {
		return ciphertext;
	}
	
	public Data(){
	}

}

@JsonProperty
private Data data;

@JsonProperty("lease_id")
private String lease_id;

public Data getData() {
	return data;
}

public String getLeaseId() {
    return lease_id;
}
@Override
public String toString() {
	return "TransitResponse [data=" + data + "]";
}


}
