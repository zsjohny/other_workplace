package com.jiuyuan.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class RSAUtil {

	public static final int ENCRYPT_CHUNK_SIZE = 117;

	public static final int DECRYPT_CHUNK_SIZE = 128;

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	public static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuvgbAZTUiPuToZhzLkkolyEnTosRagyKfnQXQth08m19i/c2bG681o7IMTZ5CLteSNGQ93uf62eeJhZwFRLfubygUQaUl88pSk36AT0o6C2w+7e9XiUKTMsadYAoYkcsVn3evPjfbpDsM+/yOE1Zoae3thgxanHUe9MEESAKm7wIDAQAB";
	public static final String DEFAULT_CHARSET = "utf-8";

	private static final KeyFactory factory = createKeyFactory();

	private static KeyFactory createKeyFactory() {
		try {
			return KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Can't get rsa key factory.", e);
		}
	}

	public static KeyPair generateRSAKeyPair() {
		try {
			// generate pair
			KeyPairGenerator generator = KeyPairGenerator.getInstance(factory.getAlgorithm());
			generator.initialize(1024); // (512-2048)
			return generator.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Can't generate rsa key pair.", e);
		}
	}


	/**
	 * RSA签名
	 * 
	 * @param content
	 *            待签名数据
	 * @param privateKey
	 *            商户私钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey, String input_charset) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(input_charset));

			byte[] signed = signature.sign();

			return Base64.encodeBase64String(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * RSA验签名检查
	 * 
	 * @param content
	 *            待签名数据
	 * @param sign
	 *            签名值
	 * @param ali_public_key
	 *            支付宝公钥
	 * @param input_charset
	 *            编码格式
	 * @return 布尔值
	 */
	public static boolean verify(String content, String sign, String public_key, String input_charset) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decodeBase64(public_key);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(input_charset));

			boolean bverify = signature.verify(Base64.decodeBase64(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static RSAPublicKey decodeRSAPublicKey(String encodedPublicKey) {
		try {
			return (RSAPublicKey) factory
					.generatePublic(new X509EncodedKeySpec(Hex.decodeHex(encodedPublicKey.toCharArray())));
		} catch (Exception e) {
			throw new IllegalStateException("Can't decode rsa public key.", e);
		}
	}

	public static RSAPrivateKey decodeRSAPrivateKey(String encodedPrivateKey) {
		try {
			return (RSAPrivateKey) factory
					.generatePrivate(new PKCS8EncodedKeySpec(Hex.decodeHex(encodedPrivateKey.toCharArray())));
		} catch (Exception e) {
			throw new IllegalStateException("Can't decode rsa private key.", e);
		}
	}

	private static byte[] encOrDec(Key key, byte[] bytes, int cipherMode, int chunkSize)
			throws GeneralSecurityException, IOException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(cipherMode, key);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int length = bytes.length;
		int offset = 0;
		while (length > chunkSize) {
			byte[] chunk = cipher.doFinal(bytes, offset, chunkSize);
			os.write(chunk);
			length -= chunkSize;
			offset += chunkSize;
		}
		byte[] chunk = cipher.doFinal(bytes, offset, length);
		os.write(chunk);

		return os.toByteArray();
	}

	public static String encryptAsBase64(String source) {
		return encryptAsBase64(DEFAULT_PUBLIC_KEY, source, DEFAULT_CHARSET);
	}

	public static String encryptAsBase64(String base64PublicKey, String source, String charset) {
		try {
			byte[] bytes = source.getBytes(charset);
			return Base64.encodeBase64String(
					encOrDec(loadPublicKey(base64PublicKey), bytes, Cipher.ENCRYPT_MODE, ENCRYPT_CHUNK_SIZE));
		} catch (Exception e) {
			throw new IllegalStateException("Failed to encrypt. public key: , source: " + source, e);
		}
	}

	public static String decryptAsBase64(String base64PrivateKey, String base64String, String charset) {
		try {
			byte[] bytes = Base64.decodeBase64(base64String);
			return new String(
					encOrDec(loadPrivateKey(base64PrivateKey), bytes, Cipher.DECRYPT_MODE, DECRYPT_CHUNK_SIZE),
					charset);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to decrypt. private key: , base64String: " + base64String, e);
		}
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decodeBase64(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (Exception e) {
			throw new Exception("公钥数据内容读取错误");
		}
	}

	public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decodeBase64(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new Exception("私钥非法");
		} catch (Exception e) {
			throw new Exception("私钥数据内容读取错误");
		}
	}

	public static String getKeyFromFile(String filePath) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

		String line = null;
		List<String> list = new ArrayList<String>();
		while ((line = bufferedReader.readLine()) != null) {
			list.add(line);
		}
		bufferedReader.close();

		// remove the firt line and last line
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 1; i < list.size() - 1; i++) {
			stringBuilder.append(list.get(i)).append("\r");
		}

		String key = stringBuilder.toString();
		return key;
	}

	public static void main(String[] args) throws Exception {
		String encoding = "utf-8";

		// 文件读取 openssl生成
		String publicKeyPath = "f:/rsa_public_key.pem";
		String privateKeyPath = "f:/pkcs8_private_key.pem";
		String public_key = getKeyFromFile(publicKeyPath);
		String private_key = getKeyFromFile(privateKeyPath);
		RSAPublicKey publicKey = loadPublicKey(public_key);
		RSAPrivateKey privateKey = loadPrivateKey(private_key);
		String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
		String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());

		// 直接生成(ios 签名无法使用)
		// KeyPair keyPair = RSAUtil.generateRSAKeyPair();
		// String publicKeyStr =
		// Base64.encodeBase64String(keyPair.getPublic().getEncoded());
		// System.out.println("publicKey:" + publicKeyStr);
		// System.out.println("privateKey:" +
		// Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));

		// 公钥加密跟验签用 服务器端使用
		System.out.println("publicKey:" + publicKeyStr);

		// 公钥解密跟签名用 app端使用
		System.out.println("privateKey:" + privateKeyStr);

		// String source =
		// "123456789javajjavajjavajjavajjavajjavajjavajjavajjavajjavajjavajavajavajavajavajavajavajavajavajavajavajavajavajavajajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajavajava123456789";
		String source = "123456789";
		String encryptedSource = RSAUtil.encryptAsBase64(publicKeyStr, source, encoding);
		System.out.println("encryptedSource:" + encryptedSource);

		String decryptedSource = RSAUtil.decryptAsBase64(privateKeyStr, encryptedSource, encoding);
		System.out.println("decryptedSource:" + decryptedSource);

		String sign = RSAUtil.sign(source, privateKeyStr, encoding);
		System.out.println("sign:" + sign);
		System.out.println("todo verify:" + source);
		System.out.println("verify:" + RSAUtil.verify(source, sign, publicKeyStr, encoding));

	}
}
