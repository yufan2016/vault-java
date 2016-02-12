package vault.java.impl;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import vault.java.response.DecryptResponse;
import vault.java.response.EncryptResponse;
import vault.java.response.TokenResponse;
import vault.java.response.TransitResponse;
import vault.java.response.VaultResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java API for Hashicorp's Vault project. A tool for managing secrets.
 *
 * @see <a href="https://vaultproject.io/">https://vaultproject.io/</a>
 */
public class Vault {

	private final String vaultToken;
	private final Client client;
	private WebTarget baseTarget;

	/**
	 * Initialize Vault
	 *
	 * @param vaultServer http address to vault server, example: http://127.0.0.1:8200
	 * @param vaultToken  vault token used to authenticate requests
	 */
	public Vault(String vaultServer, String vaultToken) {
		this.vaultToken = vaultToken;
		this.client = ClientBuilder.newBuilder().register(JacksonJaxbJsonProvider.class).build();
		this.baseTarget = client.target(vaultServer);
	}

	/**
	 * Initialize Vault with custom Jersey HTTP client, for fine tuning and proxy configurations
	 *
	 * @param vaultServer http address to vault server, example: http://127.0.0.1:8200
	 * @param vaultToken  vault token used to authenticate requests
	 * @param client      custom Jersey client
	 */
	public Vault(String vaultServer, String vaultToken, Client client) {
		this.vaultToken = vaultToken;
		this.client = client;
		this.baseTarget = client.target(vaultServer);
	}

	/**
	 * Read value from vault
	 *
	 * @param path the vault path where secret is stored
	 * @return the response object containing your secret
	 * @throws VaultException if API returns anything except 200
	 */
	public VaultResponse read(String path) {
		WebTarget target = baseTarget.path(String.format("/v1/%s", path));
		Response response = null;
		try {
			response = target.request()
					.accept("application/json")
					.header("X-Vault-Token", this.vaultToken)
					.get(Response.class);
			if (response.getStatus() != 204 && response.getStatus() != 200) {
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new VaultException(response.getStatus(), error.getErrors());
			}
			return response.readEntity(VaultResponse.class);

		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * Read value from vault
	 *
	 * @param path the vault path where secret is stored
	 * @return the response object containing your secret
	 * @throws VaultException if API returns anything except 200
	 */
	public TransitResponse readTransitKey(String keyName) {
		WebTarget target = baseTarget.path(String.format("/v1/transit/keys/%s", keyName));
		Response response = null;
		try {
			response = target.request()
					.accept("application/json")
					.header("X-Vault-Token", this.vaultToken)
					.get(Response.class);
			if (response.getStatus() != 204 && response.getStatus() != 200) {
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new VaultException(response.getStatus(), error.getErrors());
			}
			return response.readEntity(TransitResponse.class);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * Write key/value secret to vault
	 *
	 * @param path   where to write secret
	 * @param secret A key, paired with an associated value, to be held at the given location.
	 *               Multiple key/value pairs can be specified, and all will be returned on a read operation.
	 *               The generic backend use 'value' as key
	 * @throws VaultException if operation fails
	 */
	public void write(String path, HashMap<String, Object> secret) {
		WebTarget target = baseTarget.path(String.format("/v1/%s", path));
		Response response = null;
		try {
			response = target.request()
					.accept("application/json")
					.header("X-Vault-Token", this.vaultToken)
					.post(Entity.entity(secret, MediaType.APPLICATION_JSON_TYPE));

			if (response.getStatus() != 204 && response.getStatus() != 200) {
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new VaultException(response.getStatus(), error.getErrors());
			}
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}


	public void writeTransitKey(String keyName, boolean keyDerivation) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("derived", keyDerivation);
		String path = String.format("transit/keys/%s", keyName);
		write(path, null);
	}

	/**
	 * Write key/value secret to vault
	 *
	 * @param path   where to write secret
	 * @param secret A key, paired with an associated value, to be held at the given location.
	 *               Multiple key/value pairs can be specified, and all will be returned on a read operation.
	 *               The generic backend use 'value' as key
	 * @throws VaultException if operation fails
	 */
	public EncryptResponse encrypt(String keyName, String plaintext, String context) {
		WebTarget target = baseTarget.path(String.format("/v1/transit/encrypt/%s", keyName));
		Response response = null;
		try {
			Map <String, String> data =new HashMap<String, String>();
			if (plaintext == null )
				throw new IllegalArgumentException("data content cannot be null");
			data.put("plaintext", plaintext);
			if (context != null)
				data.put("context", context);
			response = target.request()
					.accept("application/json")
					.header("X-Vault-Token", this.vaultToken)
					.post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));

			if (response.getStatus() != 204 && response.getStatus() != 200) {
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new VaultException(response.getStatus(), error.getErrors());
			}
			return response.readEntity(EncryptResponse.class);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}


	public DecryptResponse decrypt(String keyName, String ciphertext, String context) {
		WebTarget target = baseTarget.path(String.format("/v1/transit/decrypt/%s", keyName));
		Response response = null;
		try {
			Map <String, String> data =new HashMap<String, String>();
			if (ciphertext == null )
				throw new IllegalArgumentException("data content cannot be null");
			data.put("ciphertext", ciphertext);
			if (context != null)
				data.put("context", context);
			response = target.request()
					.accept("application/json")
					.header("X-Vault-Token", this.vaultToken)
					.post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
			if (response.getStatus() != 204 && response.getStatus() != 200) {
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new VaultException(response.getStatus(), error.getErrors());
			}
			return response.readEntity(DecryptResponse.class);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * Delete path from vault
	 *
	 * @param path to be deleted
	 * @throws VaultException containing error reason
	 */
	public void deleteTransitKey(String keyName) {


		HashMap<String, Object> config = new HashMap<>();
		config.put("deletion_allowed", true);
		write(String.format("transit/keys/%s/config", keyName), config);

		WebTarget target = baseTarget.path(String.format("/v1/transit/keys/%s", keyName));
		Response response = null;
		try {
			response = target.request()
					.accept("application/json")
					.header("X-Vault-Token", this.vaultToken)
					.delete();

			if (response.getStatus() != 204 && response.getStatus() != 200) {
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new VaultException(response.getStatus(), error.getErrors());
			}
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * Delete path from vault
	 *
	 * @param path to be deleted
	 * @throws VaultException containing error reason
	 */
	public void delete(String path) {
		WebTarget target = baseTarget.path(String.format("/v1/%s", path));
		Response response = null;
		try {
			response = target.request()
					.accept("application/json")
					.header("X-Vault-Token", this.vaultToken)
					.delete();

			if (response.getStatus() != 204 && response.getStatus() != 200) {
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new VaultException(response.getStatus(), error.getErrors());
			}
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * Get status from Vault
	 *
	 * @return VaultStatus containing shares, progress, shards and seal status
	 * @throws VaultException on non 200 status codes
	 */
	public VaultStatus getStatus() {

		WebTarget target = baseTarget.path("/v1/sys/seal-status");
		Response response = null;
		try {
			response = target.request()
					.accept("application/json")
					.header("X-Vault-Token", this.vaultToken)
					.get();
			if (response.getStatus() != 200) {
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new VaultException(response.getStatus(), error.getErrors());
			}
			return response.readEntity(VaultStatus.class);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * @param token to lookup
	 * @return TokenResponse where "data" contains token information
	 */
	public TokenResponse lookupToken(String token) {
		WebTarget target = baseTarget.path("/v1/auth/token/lookup/" + token);
		Response response = null;
		try {
			response = target.request()
					.accept("application/json")
					.header("X-Vault-Token", this.vaultToken)
					.get();
			if (response.getStatus() != 200) {
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new VaultException(response.getStatus(), error.getErrors());
			}

			return response.readEntity(TokenResponse.class);

		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * Create new Vault token used for requests
	 *
	 * @param tokenCreateRequest containing properties such as ttl, policies, ttl.
	 *                           It's easiest to use the TokenCreateRequestBuilder to generate request.
	 * @return TokenResponse containing new token properties
	 */
	public TokenResponse createToken(TokenCreateRequest tokenCreateRequest) {
		WebTarget target = baseTarget.path("/v1/auth/token/create");
		Response response = null;
		try {
			response = target.request()
					.accept("application/json")
					.header("X-Vault-Token", this.vaultToken)
					.post(Entity.entity(tokenCreateRequest, MediaType.APPLICATION_JSON_TYPE));
			if (response.getStatus() != 200) {
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new VaultException(response.getStatus(), error.getErrors());
			}

			return response.readEntity(TokenResponse.class);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	private static class ErrorResponse {
		@JsonProperty
		private List<String> errors;

		public List<String> getErrors() {
			return errors;
		}

		@Override
		public String toString() {
			return "ErrorResponse [errors=" + errors + "]";
		}
	}

}