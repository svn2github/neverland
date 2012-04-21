var url = oprequest();
var originName;
var tag = true;
var imageData = "";
var isAlert = false;
var changed = false;
var isDefaultIcon = false;
var globalSex = false;

function dumpImage(data) {
	changed = true;
	imageData = data;
	if (data == "0") {
		isDefaultIcon = true;
		if (globalSex) {
			$("#icon").attr("src", "images/hdpi/defimg_user_xy.png");
		} else {
			$("#icon").attr("src", "images/hdpi/defimg_user_xx.png");
		}
		
		return;
	}
	$("#icon").attr("src", "data:image/jpeg;base64," + data);
}

function getPicture() {
	android.getPicture(isDefaultIcon);
}

function retrySend() {
	opalert({
				content : nearmelang.lang_global_sendfailure_retry,
				mode : opalert.BTN_BOTH,
				onok : function() {
					onSubmit();
				},
				oktext : nearmelang.lang_global_retry
			});
}

function onSubmit() {
	var name = $("#name").attr("value");
	if (name !== originName) {
		changed = true;
	}
	if (!tag) {
		return;
	}

	if (!changed) {
		goBack();
	}

	// alert(checkNickname(name));
	// return;
	if (!checkNickname(name)) {
		tag = true;
		opalert(nearmelang.lang_setinfo_formaterror);
		return;
	} else {
		$.action({
			callback : function(data) {
				if (data.RequestResult.resultCode == 3012) {
					opalert(nearmelang.lang_setinfo_nicknameisexist);
					return;
				}
				android
						.makeToast(nearmelang.lang_global_message_submit_success);
				android.goBack();
			},
			Action : {
				"currentHtml" : "setinfo.html",
				"requestPath" : "post_user_info",
				"requestParams" : generateParams(),
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
		tag = true;
	}
}

function goBack() {
	var name = $("#name").attr("value");
	if (changed || name != originName) {
		if (isAlert) {
			return;
		}
		isAlert = true;
		opalert({
					content : nearmelang.lang_setinfo_cancelchange,
					mode : opalert.BTN_BOTH,
					onok : function() {
						android.goBack();
					},
					oncancel : function() {
						isAlert = false;
					}
				});
	} else {
		android.goBack();
	}
}

function generateSubmitUrl() {
	var data = {
		"currentHtml" : "setinfo.html",
		"requestPath" : "post_user_info",
		"requestParams" : generateParams(),
		"requestMethod" : "post",
		"needCallback" : "true",
		"callbackInterface" : "submitCallback",
		"callbackParamsType" : "json",
		"needShowNativeLoading" : "true",
		"paramsNeedDecode" : "true"
	};
	return json2str(data);
}

function generateParams() {
	var name = $("#name").attr("value");
	var sex;
	if ($("#boy").attr("info") == "true") {
		sex = "true";
	} else {
		sex = "false";
	}
	if (imageData == undefined || imageData == "") {
		return "sex=" + sex + "&" + "name=" + name + "&" + "userId="
				+ url.getParam("userId");
	}
	return "sex=" + sex + "&" + "name=" + name + "&" + "userId="
			+ url.getParam("userId") + "&" + "avatarContent=" + imageData;
}

function submitCallback() {

}
function unlockedCall() {

}
$(document).ready(function() {
	initLang();
	
	var content = $("#name");
	var omaxlength=parseInt(content.attr("omaxlength"));
	content.bind('keyup',function(e){
		var chlength = content.val().chlength();
		var maxlength = omaxlength*2-(2*chlength-content.val().length)+1;
		content.attr('maxlength',maxlength);
		if(content.val().chlength()>omaxlength){
			content.val(content.val().substring(0,maxlength));
		}
	});
	setInterval(function(){
		content.trigger('keyup'),50
	});
	
	$.action({
			callback : function(data) {
				var user = data.InternalUser;
				var sex = user.sex;
				$("#icon").attr("src", user.profilePictureUrl);
				var iconUrl = user.profilePictureUrl;
				if (sex == "true" || sex == true) {
					globalSex = true;
					$("#boy").attr("src", "images/hdpi/userhome_sex_boy_select.png");
					$("#boy").attr("info", "true");
					$("#girl").attr("src", "images/hdpi/setinfo_sex_girl.png");
					$("#girl").attr("info", "false");
					if (/male/gi.test(iconUrl)){
						isDefaultIcon = true;
					}
				} else {
					globalSex = false;
					$("#boy").attr("src", "images/hdpi/setinfo_sex_boy.png");
					$("#boy").attr("info", "false");
					$("#girl").attr("src", "images/hdpi/userhome_sex_girl_select.png");
					$("#girl").attr("info", "true");
					if (/female/gi.test(iconUrl)) {
						isDefaultIcon = true;
					}
				}
				$("#name").attr("value", user.name);
				originName = user.name;
				changed = false;
			},
			Action : {
				"currentHtml" : "setinfo.html",
				"requestPath" : "get_user_basic_info",
				"requestParams" : url.getParamsStr(),
				"requestMethod" : "get",
				"needCallback" : "true",
				"callbackInterface" : "jQuery.actionCallback",
				"callbackParamsType" : "json",
				"needShowNativeLoading" : "true"
			}
	});
});

function show(obj) {
	changed = true;
	obj = $(obj);
	if (obj.attr("info") == "true") {

	} else {
		if (obj.attr("id") == "boy") {
			globalSex = true;
			$("#boy").attr("src", "images/hdpi/userhome_sex_boy_select.png");
			$("#boy").attr("info", "true");
			$("#girl").attr("src", "images/hdpi/setinfo_sex_girl.png");
			$("#girl").attr("info", "false");
		} else {
			globalSex = false;
			$("#boy").attr("src", "images/hdpi/setinfo_sex_boy.png");
			$("#boy").attr("info", "false");
			$("#girl").attr("src", "images/hdpi/userhome_sex_girl_select.png");
			$("#girl").attr("info", "true");
		}
	}
}

function initLang() {
	$("#lang_setinfo_title").html(nearmelang.lang_setinfo_title);
	$("#lang_setinfo_changeavatar").html(nearmelang.lang_setinfo_changeavatar);
	$("#lang_setinfo_gender_colon").html(nearmelang.lang_setinfo_gender_colon);
	$("#lang_setinfo_nickname_colon")
			.html(nearmelang.lang_setinfo_nickname_colon);
	$("#lang_global_female").html(nearmelang.lang_global_female);
	$("#lang_global_male").html(nearmelang.lang_global_male);
	$("#lang_global_cancel").html(nearmelang.lang_global_cancel);
	$("#lang_global_save").html(nearmelang.lang_global_save);
}
