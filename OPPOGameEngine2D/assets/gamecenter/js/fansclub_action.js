var url = oprequest();
var isLove = url.getParam('isFavourite');
function unlockedCall() {
	
}

$(document).ready(function() {
	if (isLove == "true") {
		$("#love").attr("class", "fans_love");
	} else {
		$("#love").attr("class", "fans_love_not");
	}
});

function changeLove() {
	$.action({
		callback:function(data){
			if (data == undefined) {
				console.log("return error json data to html");
				return;
			}
			if( data.RequestResult.resultCode==4011 ) {
				opalert(nearmelang.lang_fansclub_notplayed);
				return;
			}
			if( data.RequestResult.resultCode==1001 ) {
				if (isLove=="true") {
					$("#love").attr("class", "fans_love_not");
					$("#marklove").html(nearmelang.lang_fansclub_marklove);
					isLove = "false";
					android.onChangeParam("isFavourite", "false");
					android.makeToast(nearmelang.lang_fansclub_marklovefailure);
				} else {
					$("#love").attr("class", "fans_love");
					$("#marklove").html(nearmelang.lang_fansclub_dislove);
					isLove = "true";
					android.onChangeParam("isFavourite", "true");
					android.makeToast(nearmelang.lang_fansclub_marklovesuccess);
				}
			}
		},
		Action:{
			"currentHtml" : "fansclub.html",
			"requestPath" : "set_game_favourite_tag",
			"requestParams" : url.setParam('isFavourite',isLove=="false"?true:false).getParamsStr(),
			"requestMethod" : "get",
			"needCallback" : true,
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : true
		}
	});
	
}
function gotoInvite(){
	android.onLoadHtml("invitefriend.html?" + url.getParamsStr());
}

function gotoDevgames(){
	android.onLoadHtml("devgames.html?" + url.getParamsStr());
}

function gotoMessage(){
	android.onLoadHtml("fansreply.html?"+url.getParamsStr());
}