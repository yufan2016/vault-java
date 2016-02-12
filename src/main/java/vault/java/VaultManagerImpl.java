package vault.java;

import vault.java.impl.Vault;
import vault.java.response.DecryptResponse;
import vault.java.response.EncryptResponse;
import vault.java.response.TransitResponse;

public class VaultManagerImpl implements VaultManager {
	
	private Vault vault;
	
	public VaultManagerImpl(String vaultServer, String vaultToken) {
		vault =new Vault(vaultServer, vaultToken);
	}
	
	
	/* (non-Javadoc)
	 * @see vault.java.VaultManager#createKey(java.lang.String)
	 */
	@Override
	public void createKey(String keyName){
		vault.writeTransitKey(keyName, false);
	}
	
	/* (non-Javadoc)
	 * @see vault.java.VaultManager#getKey(java.lang.String)
	 */
	@Override
	public String getKey(String keyName){
		TransitResponse response = vault.readTransitKey(keyName);
		return response.toString();
	}
	
	/* (non-Javadoc)
	 * @see vault.java.VaultManager#deleteKey(java.lang.String)
	 */
	@Override
	public void deleteKey(String keyName){
		vault.deleteTransitKey(keyName);
	}
	
	/* (non-Javadoc)
	 * @see vault.java.VaultManager#encrypt(java.lang.String, java.lang.String)
	 */
	@Override
	public String encrypt(String keyName, String base64Plaintext){
		EncryptResponse response = vault.encrypt(keyName, base64Plaintext, null);
		String cipertext = response.getData().getCiphertext();
		return cipertext;
	}
	
	/* (non-Javadoc)
	 * @see vault.java.VaultManager#decrypt(java.lang.String, java.lang.String)
	 */
	@Override
	public String decrypt(String keyName, String cipertext){
		DecryptResponse response = vault.decrypt(keyName, cipertext, null);
		String plaintext = response.getData().getPlaintext();
		return plaintext;
	}

}
