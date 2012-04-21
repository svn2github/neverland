package com.lion.engine.util;

import android.util.Log;

import com.nearme.pay.Rsa;

public class ResultChecker {
	
	public static final String NEARME_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdmErtzWi09mfD0C2j8Mb42tH3hgta+cgGlCRv"
+"XI7ToQbcEf8xGEYYDg7hLqK6hUeCs4ZEKsdwcvo0g1iKUCHiVRDzpOUWiGrGEgnYhj4YAMrSx7Ng"
+"7EB5J81QseCX3gWKmqIV+RO1Jpc6Qu4XcLi8t7pCaAZP3z7cU+Z+hr0OkQIDAQAB";

	public static boolean isPayOK(String response) {
		try {
			PayResult payResult = JsonParser.getPayResult(response);
			if (payResult.pay_result.equalsIgnoreCase("success")) {
				String signData = getSignData(payResult);
				boolean flag = Rsa.doCheck(signData, payResult.sign,
						NEARME_PUBLIC_KEY);
				Log.d("Test", "pay_is_ok=" + flag);
				Log.e("Test", "sign=" + payResult.sign);
				Log.d("Test", "signData=" + signData);
				return flag;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	private static String getSignData(PayResult payResult) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("partner_id=" + payResult.partner_id);
		stringBuilder.append("&partner_order=" + payResult.partner_order);
		stringBuilder.append("&product_name=" + payResult.product_name);
		stringBuilder.append("&product_describe=" + payResult.product_describe);
		stringBuilder.append("&product_totle_fee="
				+ payResult.product_totle_fee);
		stringBuilder.append("&product_count=" + payResult.product_count);
		stringBuilder.append("&packageName=" + payResult.packageName);
		stringBuilder.append("&notify_url=" + payResult.notify_url);
		stringBuilder.append("&pay_result=" + payResult.pay_result);

		return stringBuilder.toString();
	}
}
