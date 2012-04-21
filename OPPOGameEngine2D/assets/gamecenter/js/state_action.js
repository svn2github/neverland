var url = oprequest();
var isAlert = false;
function retrySend() {
	opalert({
				content : nearmelang.lang_global_sendfailure_retry,
				mode : opalert.BTN_BOTH,
				onok : function() {
					callSend();
				},
				oktext : nearmelang.lang_global_retry
			});
}
function callSend() {
	var content = $("#content").attr("value");
	if (content.trim() == ""
			|| content == (nearmelang.lang_state_inittext + nearmelang.lang_global_word40)) {
		opalert(nearmelang.lang_global_message_notnull);
		return;
	}
	if (content == decodeURIComponent(url.getParam("signature"))) {
		android.goBack();
	}
	$.action({
				callback : function(data) {
					// opalert({
					// content:nearmelang.lang_global_message_submit_success,
					// mode:opalert.BTN_OK,
					// onok:function(){
					// android.goBack();
					// }
					// });
					android
							.makeToast(nearmelang.lang_global_message_submit_success);
					android.goBack();
				},
				Action : {
					"currentHtml" : "state.html",
					"requestPath" : "post_user_signature",
					"requestParams" : generateParams(),
					"requestMethod" : "post",
					"needCallback" : "true",
					"callbackInterface" : "jQuery.actionCallback",
					"callbackParamsType" : "json",
					"needShowNativeLoading" : "true",
					"paramsNeedDecode" : "true",
					"loadingHeader" : nearmelang.lang_global_issending,
					"failureMessage" : "null",
					"failureCallback" : "retrySend"
				}
			});
}

function goBack() {
	var content = $("#content").attr("value");
	if (content == ""
			|| content == decodeURIComponent(url.getParam("signature"))
			|| content == (nearmelang.lang_state_inittext + nearmelang.lang_global_word40)) {
		android.goBack();
		return;
	}
	if (isAlert) {
		return;
	}
	isAlert = true;
	opalert({
				content : nearmelang.lang_global_message_cancelsend,
				mode : opalert.BTN_BOTH,
				onok : function() {
					android.goBack();
				},
				oncancel : function() {
					isAlert = false;
				}
			});
}

function generateParams() {
	var content = $("#content").attr("value");
	return url.getParamsStr() + "&" + "signatureContent="
			+ encodeURIComponent(content);
}

function sendCallback() {
	android.onLoadHtml("userHome.html?needInfo=true");
}

function unlockedCall() {

}

$(document).ready(function() {
	$("#content").attr("value", decodeURIComponent(url.getParam("signature")));

	var content = $("#content");
	var word_number = $("#word_number");
	var omaxlength = parseInt(content.attr("omaxlength"));
	content.bind('keyup', function(e) {
				var chlength = content.val().chlength();
				var maxlength = omaxlength * 2
						- (2 * chlength - content.val().length) + 1;
				content.attr('maxlength', maxlength);
				if (content.val().chlength() > omaxlength) {
					content.val(content.val().substring(0, maxlength));
				}
				word_number.html(chlength + "/" + omaxlength);
			});
	setInterval(function() {
				content.trigger('keyup'), 50
			});
});
