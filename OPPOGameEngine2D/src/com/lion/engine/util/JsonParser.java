package com.lion.engine.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
	public static PayResult getPayResult(String response) {
		if (response == null) {
			return null;
		}
		PayResult payResult = null;
		try {
			JSONObject jsonObject = new JSONObject(response);
			payResult = new PayResult();
			payResult.partner_id = jsonObject.getString("partner_id");
			payResult.partner_order = jsonObject.getString("partner_order");
			payResult.product_name = jsonObject.getString("product_name");
			payResult.product_describe = jsonObject
					.getString("product_describe");
			payResult.product_totle_fee = jsonObject
					.getString("product_totle_fee");
			payResult.product_count = jsonObject.getString("product_count");
			payResult.packageName = jsonObject.getString("packageName");
			payResult.notify_url = jsonObject.getString("notify_url");

			payResult.pay_result = jsonObject.getString("pay_result");
			payResult.pay_status = jsonObject.getString("pay_status");
			payResult.sign = jsonObject.getString("sign");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return payResult;
	}
}
