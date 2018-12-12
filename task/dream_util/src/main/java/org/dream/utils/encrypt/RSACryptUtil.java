package org.dream.utils.encrypt;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.bouncycastle.util.encoders.UrlBase64Encoder;

public class RSACryptUtil {
	public static final java.security.Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
	static {
		java.security.Security.addProvider(provider);
	}
	public static KeyPair generateKeypair(int keyLength) throws Exception {
		try {
			KeyPairGenerator kpg;
			try {
				kpg = KeyPairGenerator.getInstance("RSA");
			} catch (Exception e) {
				kpg = KeyPairGenerator.getInstance("RSA", provider);
			}
			kpg.initialize(keyLength);
			KeyPair keyPair = kpg.generateKeyPair();
			System.out.println(new Base64().encodeAsString(keyPair.getPrivate().getEncoded()));
			System.out.println(new Base64().encodeAsString(keyPair.getPublic().getEncoded()));
			return keyPair;
		} catch (NoSuchAlgorithmException e1) {
			throw new RuntimeException("RSA algorithm not supported", e1);
		} catch (Exception e) {
			throw new Exception("other exceptions", e);
		}
	}

	

	public static void main(String[] args) throws Exception {
		  
		String privateKey="MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAkXbYNqM5evxy8ooSB8Ts4vZDh0ysCuyKq9kS5XYmis7D9S/"
				+ "Cou7yhnxl920Eg0MAU30HZm9nGZoymDwsZuo84QIDAQABAkBD8hNEUjHDNLAsgjmxz1YnHYilZjbmU17irl6ZN/sA0qTOnyVApaddDgE"
				+ "Nq6tx6mmx9RDmtJFJMrORuaR/x+cBAiEA5Ycmj7QeGRhUoLnA+HaI/Lovk0O8z7s3SL95RPM7yqkCIQCiPbMheSUDBUZ19wSD9UMQ16HL"
				+ "AnBKpqP4F9+E/m67eQIgMZh3c5u22TNRrf0VPlrWlM1iVE7RsI1Cj9yXxpdMNykCIA9zMrjQUY79FJ2tPVfXpmBXOIgdnlXtkpXQqC+BD0"
				+ "h5AiEAka2mrBwlRHkQU88yzuIpmhTlitX8HJ5EDh8AoV5ncRw=";
		String publicKey="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJF22DajOXr8cvKKEgfE7OL2Q4dMrArsiqvZEuV2JorOw/UvwqLu8oZ8ZfdtBINDAFN9B2ZvZxmaMpg8LGbqPOECAwEAAQ==";
		generateKeypair(1024);
		// 构造PKCS8EncodedKeySpec对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(new Base64().decode(privateKey));
			// KEY_ALGORITHM 指定的加密算法
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			// 取私钥匙对象
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
			
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(new Base64().decode(publicKey));
			// 取私钥匙对象
			PublicKey pubkey = keyFactory.generatePublic(x509KeySpec);
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, priKey);
			byte[] code=cipher.doFinal("你 你好你好你好你好".getBytes());
			System.out.println(new Base64().encodeAsString(code));
			System.out.println("ZJMZ1T4Q0ksPNLE3G42LYdDfTGKwGerWb+w2d9yap4iD88N1ThEE72+bS9rwBbi8pjdPKfjhvdti3nlZdQ9KVw==");
		
			
			
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, pubkey);
			System.out.println(new String(cipher.doFinal(code)));;
			
			
			
			String pp="Pl7Ir11t72QqcyZhwAu31Bfs39w5Be2/qbefe5SP7adPZxYlZbNaLHh/qDUxVmEiJNmDyXnfoMpYlSs8YnNo9g==";
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			System.out.println(new String(cipher.doFinal(new Base64().decode(pp))));;
			
			
//			
//			Signature signature = Signature.getInstance("MD5withRSA");
//			signature.initSign(priKey);
//			signature.update("123".getBytes());
//			byte[] sign=signature.sign();
//			
//			signature = Signature.getInstance("MD5withRSA");
//			signature.initVerify(pubkey);
//			signature.update("123".getBytes());
//			System.out.println(signature.verify(sign));
	}
}
