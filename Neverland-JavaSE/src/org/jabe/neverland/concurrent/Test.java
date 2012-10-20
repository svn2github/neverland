package org.jabe.neverland.concurrent;

import com.nearme.oauth.model.AccessToken;
import com.nearme.oauth.open.AccountAgent;
import com.nearme.oauth.provider.URLProvider;
import com.nearme.oauth.util.Constants;

public class Test {
	public static void main(String[] args) {
//		System.out.println(URLDecoder.decode("POST&http%3A%2F%2Fucenterdev1.wanyol.com%3A8087%2Faccount%2FCreateOrder&amount%3D100%26consumer_callback%3Ds%26consumer_requestId%3Ds%26oauth_consumer_key%3D838435498%26oauth_nonce%3D-106299568%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1340966631%26oauth_token%3D5a3017f7409571ee77e3fde97f0d6051%26oauth_version%3D1.0%26productdesc%3Da%26productName%3Db"));
//
//		String a = "productName";
//		String b = "productdesc";
//		String c = "productDesc";
//
//		ArrayList<String> list = new ArrayList<String>();
//		list.add(a);
//		list.add(b);
//		list.add(c);
//		Collections.sort(list);
//		System.out.println(list);
		URLProvider.init();
		Constants.APP_KEY = "c5217trjnrmU6gO5jG8VvUFU0";
		Constants.APP_SECRET = "e2eCa732422245E8891F6555e999878B";
		try {
			String gcUserInfo = AccountAgent.getInstance().getGCUserInfo(new AccessToken("b1d1712199b57d0977a86711354a490b", "b95c462e7a5d02849b1f1976441198ad"));
			System.out.println(gcUserInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
