var url = oprequest();

function callSend() {
	var content = $("#content").attr("value");
	if (content.trim() == "" || content == (nearmelang.lang_fansclub_feedback_inittext + nearmelang.lang_global_word140)) {
		opalert(nearmelang.lang_global_message_notnull);	
		return;
	}
	$.action({
		callback : function(data) {
			android.makeToast(nearmelang.lang_global_message_submit_success);
			android.goBack();
		},
		Action : {
			"currentHtml" : "fansreply.html",
			"requestPath" : "post_game_suggest",
			"requestParams" : url.setParam('proponentId',url.getParam('userId')).getParamsStr() + "&content=" + content,
			"requestMethod" : "post",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true",
			"paramsNeedDecode" : "true",
			"loadingHeader" : nearmelang.lang_global_issending,
			"failureMessage" : nearmelang.lang_global_sendfailure
		}
	});
}


function goBack() {
	var content = $("#content").attr("value");
	if (content == "" || content == (nearmelang.lang_fansclub_feedback_inittext + nearmelang.lang_global_word140)) {
		android.goBack();
		return;
	}
	opalert({
	content:nearmelang.lang_global_message_cancelsend,
	mode:opalert.BTN_BOTH,
	onok:function(){
		android.goBack();
	}
	});
}
function unlockedCall(){}

$(document).ready(function(){
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
})

