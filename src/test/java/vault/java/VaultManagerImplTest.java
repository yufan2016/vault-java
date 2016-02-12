package vault.java;



import java.net.Socket;
import java.util.Base64;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VaultManagerImplTest {

	
	private String host;
	private Integer port;
	private String full_host;
	private String token;
	private String keyName;
	private String data;

    public static boolean isPortOpen (String host,int port) {
		Socket s = null;
		try
		{
			s = new Socket(host, port);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			if(s != null)
				try {s.close();}
			catch(Exception e){}
		}
	}

	@Before
	public void setUp() {
		host = "vault";
		token = "482b95f9-6a29-cc20-f068-022989e4e758";
		keyName = "testApiKey";
		port = 8200;
		full_host = "http://"+host+":"+port;
		data = "听党指挥,能打胜仗,作风优良";
		Assume.assumeTrue("hdfs not accessible, skip test",isPortOpen(host, port));
	}
	
	@Test
	public void createKey(){
		VaultManager vm =new VaultManagerImpl(full_host, token);
		vm.createKey(keyName);
	}

	public String encrypt(){
		VaultManager vm =new VaultManagerImpl(full_host, token);
		String plaintext = Base64.getEncoder().encodeToString(data.getBytes());
		String cipertext = vm.encrypt(keyName, plaintext);
		return cipertext;
	}
	@Test
	public void decrypt(){
		VaultManager vm =new VaultManagerImpl(full_host, token);
		String cipertext = encrypt();
		String plaintext = vm.decrypt(keyName, cipertext);
		String data =  new String(Base64.getDecoder().decode(plaintext));
		Assert.assertEquals(data,this.data);
	}
	
	@Test
	public void deleteKey(){
		VaultManager vm =new VaultManagerImpl(full_host, token);
		vm.deleteKey(keyName);
	}
	

}
