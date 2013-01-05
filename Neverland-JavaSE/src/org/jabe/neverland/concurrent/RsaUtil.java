package org.jabe.neverland.concurrent;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

public class RsaUtil {

	/**
	 * 这里为公钥,在pay_rsa_public_key.pem文件中
	 */
	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCFVNJgbaXqvQMWlrbifOUt75WQKr4LreG2gCUz7P+IEJNx2IbuTApfGbITiMUVn7WCtXTNHiT2wsuj600KwGi9o1J9V+jDN/C/WfQXCT4ijBssujwqPcS4Yu+s3ItgzadAGY/EMWV7jMVBy2F+tGjL2iU8KU0Yp4BcC2Wue+hcQIDAQAB";

	/**
	 * 验证签名的方法
	 * 
	 * content :
	 * notify_id=value&partner_code=value&partner_order=value&orders=value
	 * &pay_result=value 字段之间使用&连接
	 * 
	 * @param content
	 * @param sign
	 * @return
	 */
	public static boolean doCheck(String content, String sign) {
		String charset = "utf-8";
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decodeBase64(PUBLIC_KEY.getBytes());
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature
					.getInstance("SHA1WithRSA");
			signature.initVerify(pubKey);
			signature.update(content.getBytes(charset));
			boolean bverify = signature.verify(Base64.decodeBase64(sign
					.getBytes()));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		String sign = "GOLVlyBqkEhSwaD7oYPp1TTgnl3o0cpO9ZsNwetLvLcKPR2SXKa5b2WuYz36xmR/XXkDrYQznlWT5wLcL/I8JB8CSbN8FfCqyhT2z2XhdhCeRcoRDF/y6lXa1PbH9m2acpQU4/FhJ4Ba7qJlVQEXL0yMcsCE3OkHTjAK5nvipik=";
		String content = "notify_id=16812&partner_code=c5217trjnrmU6gO5jG8VvUFU0&partner_order=30365&orders=zsol1.0130365&pay_result=OK";
		System.out.println("public_key: " + PUBLIC_KEY);
		System.out.println("sign: " + sign);
		System.out.println("content: " + content);
		System.out.println("doCheckResult: " + doCheck(content, sign));
	}

	public static String sign(String content, String privateKey) {
		String charset = "utf-8";
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decodeBase64(privateKey.getBytes()));
			KeyFactory keyf = KeyFactory.getInstance("RSA", "BC");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature
					.getInstance("SHA1WithRSA");
			signature.initSign(priKey);
			signature.update(content.getBytes(charset));
			byte[] signed = signature.sign();
			return new String(Base64.encodeBase64(signed), charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
