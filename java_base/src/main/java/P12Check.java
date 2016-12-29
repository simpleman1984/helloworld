import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class P12Check {

	public static void main(String args[]) throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, UnrecoverableKeyException {
		KeyStore p12 = KeyStore.getInstance("pkcs12");
		p12.load(P12Check.class.getResourceAsStream("client_2.p12"), "bzt@2017".toCharArray());
		Enumeration e = p12.aliases();
		String alias = null;
		while (e.hasMoreElements()) {
			alias = (String) e.nextElement();
			Certificate c1 = (Certificate) p12.getCertificate(alias);
			X509Certificate c = (X509Certificate) c1;
			Collection<List<?>> o = c.getSubjectAlternativeNames();
			Iterator it = o.iterator();
			while(it.hasNext()){
				System.out.println(it.next());
			}
			Date notBefore = c.getNotBefore();
			Date notAfter = c.getNotAfter();
			System.out.println(notBefore+"____"+notAfter);
			String subjectArray[] = c.getSubjectDN().toString().split(",");
			for (String s : subjectArray) {
				String[] str = s.trim().split("=");
				String key = str[0];
				String value = str[1];
				System.out.println(key + " - " + value);
			}
		}
		System.out.println("is key entry=" + p12.isKeyEntry(alias));
		PrivateKey prikey = (PrivateKey) p12.getKey(alias, "bzt@2017".toCharArray());

		Certificate cert = p12.getCertificate(alias);
		PublicKey pubkey = cert.getPublicKey();

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(p12);
		TrustManager[] tm = tmf.getTrustManagers();
		X509TrustManager managerImpl = (X509TrustManager)tm[0];
		
		System.out.println(tm[0]);

		System.out.println("cert class = " + cert.getClass().getName());
		// System.out.println("cert = " + cert);
		System.out.println("public key  = " + bytesToHexString(pubkey.getEncoded()));
		System.out.println("private key = " + bytesToHexString(prikey.getEncoded()));
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		//读取ca的pem
		//openssl pkcs12 -export -in client-cert.pem -inkey client-key.pem -certfile ca-cert.pem -out client.p12
		PemReader reader = new PemReader(new InputStreamReader(P12Check.class.getResourceAsStream("/client-cert.pem"), Charset.defaultCharset()));
		PemObject pemObject = reader.readPemObject();
		System.out.println(pemObject.getType());
		System.out.println("client-cert   = "+bytesToHexString(pemObject.getContent()));
		
		reader = new PemReader(new InputStreamReader(P12Check.class.getResourceAsStream("/client-key.pem"), Charset.defaultCharset()));
		pemObject = reader.readPemObject();
		System.out.println(pemObject.getType());
		System.out.println("client-key   = "+bytesToHexString(pemObject.getContent()));
		
		reader = new PemReader(new InputStreamReader(P12Check.class.getResourceAsStream("/ca-cert.pem"), Charset.defaultCharset()));
		pemObject = reader.readPemObject();
		System.out.println(pemObject.getType());
		System.out.println("ca-cert   = "+bytesToHexString(pemObject.getContent()));
	}
	
	

	public static List<String> getSubjectAlternativeNames(X509Certificate certificate) {
        List<String> identities = new ArrayList<String>();
        try {
            Collection<List<?>> altNames = certificate.getSubjectAlternativeNames();
            
        }
        catch (CertificateParsingException e) {
            e.printStackTrace();
        }
        return identities;
    }
	
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

}
