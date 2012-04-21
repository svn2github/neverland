var url = oprequest();
var isAlert = false;

$(document).ready(function (){
	
	$("#receiverName").html(global_getShortName(decodeURIComponent(url.getParam("receiverName"))));
	
	var content = $("#content");
	var word_number = $("#word_number");
	var omaxlength=parseInt(content.attr("omaxlength"));
	content.bind('keyup',function(e){
		var chlength = content.val().chlength();
		var maxlength = omaxlength*2-(2*chlength-content.val().length)+1;
		content.attr('maxlength',maxlength);
		if(content.val().chlength()>omaxlength){
			content.val(content.val().substring(0,maxlength));
		}
		word_number.html(chlength+"/"+omaxlength);
	});
	setInterval(function(){
		content.trigger('keyup'),50
	});
});


function callSend(){
	var content = $("#content").attr("value");
	if (content.trim() == "" || content == (nearmelang.lang_sendletter_inittext + nearmelang.lang_global_word140)) {
		opalert(nearmelang.lang_global_message_notnull);	
		return;
	}
	$.action({
				callback : function(data) {
//					opalert({
//						content:nearmelang.lang_comment_message_success,
//						mode:opalert.BTN_OK,
//						onok:function(){
//							android.goBack();
//						}
//					});
					android.makeToast(nearmelang.lang_message_sendsuccess);
					android.goBack();
				},
				Action : {
					"currentHtml" : "sendletter.html",
					"requestPath" : "post_message",
					"requestParams" : "gameVersionName=" + url.getParam("gameVersionName") + "&version=" + url.getParam("version") 
												 + "&content=" + content + "&senderId=" + url.getParam("userId") + "&receiverId=" + url.getParam("receiverId")
												 + "&messageType=" + "0",
					"requestMethod" : "post",
					"needCallback" : "true",
					"callbackInterface" : "jQuery.actionCallback",
					"callbackParamsType" : "json",
					"needShowNativeLoading" : "true",
					"loadingHeader" : nearmelang.lang_global_issending,
					"failureMessage" : "null",
					"failureCallback" : "retrySend"
				}
			});
}
function retrySend() {
	opalert({
				content:nearmelang.lang_global_sendfailure_retry,
				mode:opalert.BTN_BOTH,
				onok:function(){
					callSend();
				},
				oktext:nearmelang.lang_global_retry
			});
}
function goBack() {
	var content = $("#content").attr("value");
	if (content.trim() == "" || content == (nearmelang.lang_sendletter_inittext + nearmelang.lang_global_word140)) {
		android.goBack();
	} else {
		if (isAlert) {
			return;
		}
		isAlert = true;
		opalert({
			content:nearmelang.lang_global_message_cancelsend,
			mode:opalert.BTN_BOTH,
			onok:function(){
				android.goBack();
			},
			oncancel : function() {
				isAlert = false;
			}
		});
	}
}