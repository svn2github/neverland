var url = oprequest();
var info = {count:0,total:0,board:1,addtype:true};
var keyword = "";
var cache;
var imgcache;


function clearContent() {
	$("#content").attr("value", "");
}


function doSearch() {
	var content = $("#content").attr("value");
	if (content.trim() == "" || content.trim() == nearmelang.lang_newletter_inittext) {
		opalert(nearmelang.lang_global_message_notnull);
		return;
	} else {
		if (keyword == content) {
			return;
		}
		$("#content1").html("");
		keyword = content;
		info.count = 0;
		info.total = 0;
		loadMore();
	}
}

$(document).ready(function() {
	imgcache=preimg();
	keyword = url.getParam("keyword");
	opscroll('content1',{onPullUp:function(){loadMore()},top:'143px',bottom:'0px'}).disableMore();
	loadMore();
	$("#content").attr("value", keyword);
})

function fillData(data) {
	var list = data;
	if (data.length == 0) { 
		$("#content1").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_fansclub_searchnoresult));
		getScrollById('content1').disableMore().reDraw();
		return ;
	}
	for(var i in list){
		list[i].FriendUser.imgid='imgcache_'+info.board+info.count+(i+1);
		imgcache.add(list[i].FriendUser.imgid,list[i].FriendUser.profilePictureUrl);
	}
	var result = TrimPath.processDOMTemplate("tpl_user_friend", {
				"data" : data
			});
	$("#content1").append(result);
	if(info.count<info.total){
		getScrollById('content1').enableMore().reDraw();
	} else {
		getScrollById('content1').disableMore().reDraw();
	}
	imgcache.start();
}

function loadMore() {
	$.action({
		callback : function(data) {
			if (checkListResource(data) || data.ListResource.number == 0) {
				$("#content1").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_fansclub_searchnoresult));
				return;
			} else {
				if(!info.addtype||info.addtype!=true){
					cache = cache.concat(data.ListResource.resourceList.Datas);
				}else{
					cache = data.ListResource.resourceList.Datas;
				}
				info.count = (!info.addtype||info.addtype!=true)?0:info.count+cache.length;
				info.total = data.ListResource.number;
				fillData(cache);
			}
		},
		Action : {
			"currentHtml" : "newletter_search.html",
			"requestPath" : "get_user_searched",
			"requestParams" : url.setParam("keyword", keyword).getParamsStr()
					+ "&start="
					+ parseInt(info.count)
					+ "&end="
					+ (parseInt(info.count) + parseInt(nearmelang.config_page_count))
					+ "&isSearchFriend=" + "true",
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true"
		}
	});
}

function goToSend(name, id) {
	android.onLoadHtml("sendletter.html?" + url.getParamsStr() + "&receiverName=" + name + "&receiverId=" + id);
}

function unlockedCall() {
	
}