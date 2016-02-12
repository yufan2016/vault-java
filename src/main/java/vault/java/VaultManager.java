package vault.java;

public interface VaultManager {

	void createKey(String keyName);

	String getKey(String keyName);

	void deleteKey(String keyName);

	String encrypt(String keyName, String base64Plaintext);

	String decrypt(String keyName, String cipertext);

}