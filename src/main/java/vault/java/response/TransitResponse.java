package vault.java.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransitResponse {
	/*
        {   "data": {     "cipher_mode": "aes-gcm",     "deletion_allowed": false,     "derived": false,     "keys": {       "1": 1442851412     },     "min_decryption_version": 0,     "name": "foo"   } }
	 */

	class Data{

		@JsonProperty("cipher_mode")
		private String cipher_mode;
		@JsonProperty
		private Boolean deletion_allowed;
		@JsonProperty
		private Boolean derived;
		@JsonProperty("name")
		private String name;
		@JsonProperty("latest_version")
		private Integer latest_version;
		@JsonProperty("min_decryption_version")
		private Integer min_decryption_version;
		@JsonProperty
		private Map<String, Object> keys;

		public String getCipherMode() {
			return cipher_mode;
		}

		public Boolean getDeletionAllowed() {
			return deletion_allowed;
		}
		
		public Integer getMinDecryptionVersion() {
			return min_decryption_version;
		}
		
		public Integer getLatestVersion() {
			return latest_version;
		}

		public String getName() {
			return name;
		}
		
		public Map<String, Object> getKeys() {
	        return keys;
	    }
		
		public Data(){
			
		}
		
		@Override
		public String toString() {
			return "Data [cipher_mode=" + cipher_mode + ", deletion_allowed=" + deletion_allowed + ", derived="
					+ derived + ", name=" + name + ", min_decryption_version=" + min_decryption_version + ", keys="
					+ keys + "]";
		}

	}

	@JsonProperty
	public Data data;

	public Data getData() {
		return data;
	}

	@Override
	public String toString() {
		return "TransitResponse [data=" + data + "]";
	}
	
	
}
