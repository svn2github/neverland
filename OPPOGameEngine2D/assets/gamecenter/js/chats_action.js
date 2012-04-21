var url = oprequest();
/*
 *50一页
 */
var pageNumber = 50;
var info = {};
var myId = url.getParam("userId");
var cache;
info.count = 0;
info.total = 0;

function goToOtherHome(id) {
	android.onLoadHtml("otherHome.html?" + url.getParamsStr() + "&requestedUserId=" + id);
}

$(document).ready(function() {
	var name = url.getParam("chatterName");
	if (name.length > 8) {
		name = name.substring(0,5) + "..."
	}
	$("#chatterName").html(name);
	callOnce("true");
	setMessageRead();
});

function setMessageRead() {
	$.action({
		callback : function(data) {
		},
		Action : {
			"currentHtml" : "chats.html",
			"requestPath" : "set_message_read",
			"requestParams" : url.getParamsStr(),
			"requestMethod" : "get",
			"needCallback" : "false",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "false"
		}
	});
}


function unlockedCall() {
	
}


function callSend(content) {
	cache = content;
	if (cache.trim() == "") {
		opalert(nearmelang.lang_global_message_notnull);	
		return;
	}
	$.action({
		callback : function(data) {
			android.makeToast(nearmelang.lang_global_message_submit_success);
			clear();
			callOnce("false");
		},
		Action : {
			"currentHtml" : "chats.html",
			"requestPath" : "post_message",
			"requestParams" : "gameVersionName=" + url.getParam("gameVersionName") + "&version=" + url.getParam("version") 
												 + "&content=" + cache + "&senderId=" + url.getParam("userId") + "&receiverId=" + url.getParam("chatterId")
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
					callSend(cache);
				},
				oktext:nearmelang.lang_global_retry
			});
}


function callOnce(b) {
	$.action({
		callback : function(data) {
			if (checkListResource(data)) {
				inableMore();
				return ;
			} else {
				var list = data.ListResource.resourceList.Datas;
				info.count = info.count + list.length;
				info.total = data.ListResource.number;
				if (parseInt(info.count) < parseInt(info.total)) {
					ableMore();
				} else {
					inableMore();
				}
				var tabContent = "";
				var tpl = TrimPath.parseDOMTemplate("tpl_chat");
				for (var index = 0; index < list.length; index++) {
					var result = "";
					var node = list[index].Message
					result = tpl.process({
								"node" : node,
								"time" : getTimeString(Date
												.parse(new Date()),
										parseInt(node.createTime)),
								"myId" : myId
							});
					tabContent = result + tabContent;
				}
				$("#content1").prepend(tabContent);
			}
		},
		Action : {
			"currentHtml" : "chats.html",
			"requestPath" : "get_message_list",
			"requestParams" : url.deleteParam("chatterName").getParamsStr()
					+ "&start="
					+ parseInt(info.count)
					+ "&end="
					+ (parseInt(info.count) + parseInt(pageNumber)),
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : b
		}
	});
}
function ableMore() {
	$("#loadMore").css("display", "block");
	
}
function inableMore() {
	$("#loadMore").css("display", "none");
}
function refresh(){
	clear();
	callOnce("true");
	setMessageRead();
}
function clear() {
	info.count = 0;
	info.total = 0;
	$("#content1").html("");
	$("#content").attr("value", "");
}

function loadMore() {
	callOnce("true");
}
