package com.cts.sbauthclient;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SbauthclientApplication implements CommandLineRunner {

	private static final String KEYSTOREPATH = "../../../clientkeystore.jks"; // or .p12
	private static final String KEYSTOREPASS = "abcd1234";
	private static final String KEYPASS = "abcd";

	public static void main(String[] args) {
		SpringApplication.run(SbauthclientApplication.class, args);
	}

	KeyStore readStore() throws Exception {
		try (InputStream keyStoreStream = this.getClass().getResourceAsStream(KEYSTOREPATH)) {
			KeyStore keyStore = KeyStore.getInstance("JKS"); // or "PKCS12"
			keyStore.load(keyStoreStream, KEYSTOREPASS.toCharArray());
			return keyStore;
		}
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		log.info("Running..............");
		// Ignore self signed
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		builder.loadKeyMaterial(readStore(), KEYPASS.toCharArray());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		// -------------------------------------
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet request = new HttpGet("https://localhost:8443/hello");
		CloseableHttpResponse response = httpClient.execute(request);

		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// return it as a String
				String result = EntityUtils.toString(entity);
				log.info(result);
			}
		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
		} finally {
			response.close();
		}

	}

}
