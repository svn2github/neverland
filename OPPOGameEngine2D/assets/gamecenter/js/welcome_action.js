var useOtherName = false;
var url = oprequest();
function checkbox_click(obj){
	obj = $(obj);
	if(obj.hasClass('checked')){
		obj.removeClass('checked').addClass('notchecked');
		useOtherName = false;
	}else{
		obj.removeClass('notchecked').addClass('checked');
		useOtherName = true;
	}
}

function beginPlay() {
	var name = $("#name").attr("value").trim();
	if (!checkNickname(name) || name == nearmelang.lang_welcome_inputnickname) {
		opalert(nearmelang.lang_setinfo_formaterror);
		return ;
	}
	if (useOtherName) {
		android.onShowHome();
	} else {
		$.action({
					callback : function(data) {
						if (data.RequestResult.resultCode == 3012) {
							opalert(nearmelang.lang_setinfo_nicknameisexist);
							return;
						}
						android.makeToast(nearmelang.lang_global_message_submit_success);
						android.onShowHome();
					},
					Action : {
						"currentHtml" : "welcome.html",
						"requestPath" : "post_user_info",
						"requestParams" : generateParams(name),
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
}

function retrySend() {
	opalert({
				content:nearmelang.lang_global_sendfailure_retry,
				mode:opalert.BTN_BOTH,
				onok:function(){
					beginPlay();
				},
				oktext:nearmelang.lang_global_retry
			});
}

function generateParams(name) {
	var sex;
	if (url.getParam("sex") == "true") {
		sex = "true";
	} else {
		sex = "false";
	}
	return "sex=" + sex + "&" + "name=" + name + "&" + "userId="
			+ url.getParam("userId");
}

$(document).ready(function() {
	$("#username").html(url.getParam("username"));
});