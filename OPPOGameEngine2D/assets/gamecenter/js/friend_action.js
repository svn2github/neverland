var url = oprequest();
var friendReqNum = 0;
var def = {
	'pagecount' : 20
};
var tag = 1;
var keyword = "";
var imgcache;
function showTab(obj) {
	$(obj).addClass('li_click').siblings('li').removeClass('li_click');
	var info = $.parseJSON($(obj).attr('info'));
	$("#tab_3 img").css("display" , "none");
	if (info.board == 1) {
		android.onChangeTab("1");
		$("#tab1 img").css("display", "block");
		$("#part3").hide();
		getScrollById('part1').show();
		getScrollById('part2').hide();
		getScrollById('part3content').hide();
	} else if (info.board == 2) {
		android.onChangeTab("2");
		$("#tab2 img").css("display", "block");
		$("#part3").hide();
		getScrollById('part1').hide();
		getScrollById('part2').show();
		getScrollById('part3content').hide();
	} else if (info.board == 3) {
		android.onChangeTab("3");
		$("#tab3 img").css("display", "block");
		$("#part3").show();
		getScrollById('part1').hide();
		getScrollById('part2').hide();
		getScrollById('part3content').show();
	}
	if (info.count > 0) {

	} else {
		if (info.board == 3) {
		} else {
			loadMore(obj);
		}
	}
}

function removeAllMore() {
	for (var index = 1; index < 4; index++) {
		$("#more" + index).html('');
	}
}

function filltab(obj) {
	var data = obj.data('data');
	var info = $.parseJSON(obj.attr('info'));
	if (info.board == 1) {
		if(parseInt(info.count) < parseInt(info.total)){
			getScrollById('part1').enableMore();
		} else {
			getScrollById('part1').disableMore();
		}
		if (checkListResourceEmpty(data)) {
			$("#friendsNum").html(0);
			$("#part1").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_friend_nofriend));
			return;
		}
		var list = data.ListResource.resourceList.Datas;
		var content = [];
		$("#friendsNum").html(data.ListResource.number);
		if (list == undefined || list.length == 0) {
			return;
		}
		for (var index = 0; index < list.length; index++) {
			var node = list[index].FriendUser;
			if (node.name == undefined) {
				continue;
			}
			node.imgid='imgcache_'+info.board+info.count+(index+1);
			imgcache.add(node.imgid,node.profilePictureUrl);
			content.push(node);
		}
		$("#part1").append(TrimPath.processDOMTemplate("tpl_friendslist", {
					data : content
				}));
		getScrollById('part1').reDraw();
		imgcache.start();

	} else if (info.board == 2) {
		if (checkListResourceEmpty(data)) {
			$("#friendRequestNum").html(0);
			$("#part2").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_friend_norequest))
			return;
		}
		var list = data.ListResource.resourceList.Datas;
		var content = [];
//		friendReqNum = list.length + friendReqNum;
		$("#friendRequestNum").html(info.total);
		if (list == undefined || list.length == 0) {
			return;
		}
		for (var index = 0; index < list.length; index++) {
			var node = list[index].FriendUser;
			if (node.name == undefined) {
				continue;
			} else {
				node.name = global_getShortName(node.name);
			}
			content.push(node);
		}
		$("#part2").append(TrimPath.processDOMTemplate("tpl_friendrequestlist", {
					data : content
				}));
		if(parseInt(info.count) < parseInt(info.total)){
			getScrollById('part2').enableMore();
		} else {
			getScrollById('part2').disableMore();
		}
		getScrollById('part2').reDraw();

	} else if (info.board == 3) {
		if (data.ListResource.resourceList == undefined) {
			$("#part3content").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_fansclub_searchnoresult));
			return;
		}
		var list = data.ListResource.resourceList.Datas;
		var content = [];
		if (list == undefined || list.length == 0) {
			$("#part3content").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_fansclub_searchnoresult));
			return;
		}
		for (var index = 0; index < list.length; index++) {
			var node = list[index].FriendUser;
			if (node.name == undefined) {
				continue;
			}
			node.imgid='imgcahce_'+info.board+info.count+(index+1);
			imgcache.add(node.imgid,node.profilePictureUrl);
			content.push(node);
		}
		$("#part3content").append(TrimPath.processDOMTemplate("tpl_searchlist", {
					data : content
				}));
		if(parseInt(info.count) < parseInt(info.total)){
			getScrollById('part3content').enableMore();
		} else {
			getScrollById('part3content').disableMore();
		}
		getScrollById('part3content').reDraw();
		imgcache.start();
	}
}

function searchFriend() {
	var word = $("#keyword").attr("value").trim();
	if (word == undefined || word.trim() == "" || word == nearmelang.lang_friend_inittext) {
		opalert(nearmelang.lang_global_message_notnull);
	} else if (word == keyword) {
		return false;
	} else {
		$("#more").html('');
		keyword = word;
		$("#part3content").html("");
		var tab3 = $("#tab3");
		tab3.data("data", "");
		var info = $.parseJSON($("#tab3").attr('info'));
		info.total = 0;
		info.count = 0;
		tab3.attr('info', $.json2str(info))
		loadMore($("#tab3"));
	}
}

function generateSearchUrl(keyword) {
	var data = {
		"currentHtml" : "friend.html",
		"requestPath" : "get_user_searched",
		"requestParams" : url.getParamsStr() + "&keyword=" + keyword,
		"requestMethod" : "get",
		"needCallback" : "true",
		"callbackInterface" : "searchCallback",
		"callbackParamsType" : "json",
		"needShowNativeLoading" : "true"
	};
	return $.json2str(data);
}

function addFriend(id) {
	var data = $($("#tab3")).data('data');
	var list = data.ListResource.resourceList.Datas;
	for (var index = 0; index < list.length; index++) {
		if (list[index].FriendUser.id == id) {
			list[index].FriendUser.isFriend = true;
		}
	}
	$.action({
			callback : function(data) {
				android.onStatistics(add_friend_type.search);
				changeState(id);
				clearTabInfo("1");
				android.makeToast(nearmelang.lang_friend_addsuccess);
			},
			Action : {
				"currentHtml" : "friend.html",
				"requestPath" : "post_add_friend",
				"requestParams" : url.getParamsStr() + "&beFriendId=" + id,
				"requestMethod" : "post",
				"needCallback" : "true",
				"callbackInterface" : "jQuery.actionCallback",
				"callbackParamsType" : "json",
				"needShowNativeLoading" : "true",
				"loadingHeader" : nearmelang.lang_friend_isadding,
				"failureMessage" : nearmelang.lang_friend_addfailure
			}
	});
			
}

function changeState(id) {
	$("#" + id).html('<div class="isfriend">'+ nearmelang.lang_friend_alreadyfriend +'</div>');
}

function loadMore(obj) {
	obj = $(obj);
	var info = $.parseJSON(obj.attr('info'));
	$.action({
		callback : function(data) {
			if (!obj.data('data') || obj.data("data") == "") {
				obj.data('data', data);
			} else {
				pdata = obj.data('data');
				if (pdata.ListResource.resourceList == undefined) {

				} else {
					var plist = pdata.ListResource.resourceList.Datas;
					// var data = $.extend(true,pdata,data);
					if(!info.addtype||info.addtype!=true){
						data.ListResource.resourceList.Datas = plist
							.concat(data.ListResource.resourceList.Datas);
					}
					obj.data('data', data);
				}
			}
			if (data.ListResource.resourceList
					&& data.ListResource.resourceList.Datas) {
				info.count = info.count+def.pagecount;
			} else {
				info.count = 0;
			}
			info.total = data.ListResource.number;
			obj.attr('info', $.json2str(info));
			filltab(obj);
		},
		Action : getAction(info)
	});
}

function getAction(info) {
	if (info.board == 1) {
		return {
			"currentHtml" : "friend.html",
			"requestPath" : "get_my_friend_list",
			"requestParams" : url.getParamsStr() + "&start=" + info.count
					+ "&end="
					+ (parseInt(info.count) + parseInt(def.pagecount))
					+ "&tag=" + tag,
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true"
		};
	} else if (info.board == 2) {
		return {
			"currentHtml" : "friend.html",
			"requestPath" : "get_friend_request_list",
			"requestParams" : url.getParamsStr() + "&start=" + info.count
					+ "&end="
					+ (parseInt(info.count) + parseInt(def.pagecount)),
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "false"
		};
	} else if (info.board == 3) {
		return {
			"currentHtml" : "friend.html",
			"requestPath" : "get_user_searched",
			"requestParams" : url.getParamsStr() + "&keyword=" + keyword
					+ "&start=" + info.count + "&end="
					+ (parseInt(info.count) + parseInt(def.pagecount)) + "&isSearchFriend=false",
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true"
		}
	}
}

function unlockedCall() {

}

function refuseFriend(id) {
	$.action({
		callback : function(data) {
			clearTabInfo("2");
			$("#part2").html("");
			showTab($("#tab2").get(0));
		},
		Action : {
			"currentHtml" : "friend.html",
			"requestPath" : "post_response_friend_request",
			"requestParams" : url.getParamsStr() + "&beFriendId=" + id
					+ "&isAccept=" + "false",
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true",
			"loadingHeader" : nearmelang.lang_friend_isrefuse,
			"failureMessage" : nearmelang.lang_friend_refusingfailure
		}
	});
}

function goToGame(id) {
//	android.onLoadHtml("othergame.html?" + url.setParam("gameId",id).getParamsStr());
	goToOtherGame(id, url, enter_game_type.friendgame);
}

function acceptFriend(id) {
	$.action({
		callback : function(data) {
			android.makeToast(nearmelang.lang_friend_acceptsuccess);
			clearTabInfo("2");
			clearTabInfo("1");
			$("#part2").html("");
			$("#part1").html("");
			showTab($("#tab2").get(0));
		},
		Action : {
			"currentHtml" : "friend.html",
			"requestPath" : "post_response_friend_request",
			"requestParams" : url.getParamsStr() + "&beFriendId=" + id
					+ "&isAccept=" + "true",
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true",
			"loadingHeader" : nearmelang.lang_friend_isaccepting,
			"failureMessage" : nearmelang.lang_friend_acceptfailure
		}
	});
}

function deleteFriend(id,name) {
	opalert({
		content:nearmelang.lang_friend_del_pre+ " " + name + " " + nearmelang.lang_friend_del_suf,
		mode:opalert.BTN_BOTH,
		onok:function(){
			$.action({
				callback : function(data) {
					android.makeToast(nearmelang.lang_friend_del_suc);
					clearTabInfo("1");
					$("#part1").html("");
					getScrollById('part1').disableMore();
					showTab($("#tab1").get(0));
				},
				Action : {
					"currentHtml" : "friend.html",
					"requestPath" : "post_delete_friend",
					"requestParams" : "gameVersionName=" + url.getParam("gameVersionName") + "&version=" + url.getParam("version") + "&userId="+ url.getParam("userId") + "&friendId=" + id,
					"requestMethod" : "get",
					"needCallback" : "true",
					"callbackInterface" : "jQuery.actionCallback",
					"callbackParamsType" : "json",
					"needShowNativeLoading" : "true",
					"loadingHeader" : nearmelang.lang_friend_isdeleting,
					"failureMessage" : nearmelang.lang_friend_deletefailure
				}
			});
		}
	});
}

function clearTabInfo(id) {
	var tab = $("#tab" + id);
	tab.data("data", "");
	var info = $.parseJSON(tab.attr('info'));
	info.total = 0;
	info.count = 0;
	$("#part" + id).html("");
	tab.attr('info', $.json2str(info));
}

function goToOtherHome(id) {
	android.onLoadHtml("otherHome.html?" + url.getParamsStr() + "&requestedUserId=" + id);
}

document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
$(document).ready(function() {
	imgcache=preimg();
	opscroll('part1',{onPullUp:function(tab){loadMore($("#tab1").get(0))}}).disableMore();
	opscroll('part2',{onPullUp:function(tab){loadMore($("#tab2").get(0))}}).disableMore();
	opscroll('part3content',{onPullUp:function(tab){loadMore($("#tab3").get(0))},top:'185px',bottom:'60px'}).disableMore();
	loadMore($("#tab2").get(0));
	
	if (url.getParam("currentTab") == "1") {
		showTab($("#tab1"));
	} else if (url.getParam("currentTab") == "2") {
		showTab($("#tab1"));
		
		obj = $("#tab2")
		$(obj).addClass('li_click').siblings('li').removeClass('li_click');
		$("#tab_3 img").css("display" , "none");
		android.onChangeTab("2");
		$("#tab2 img").css("display", "block");
		$("#part3").hide();
		getScrollById('part1').hide();
		getScrollById('part2').show();
		getScrollById('part3content').hide();
		
	} else if (url.getParam("currentTab") == "3") {
		showTab($("#tab1"));
		showTab($("#tab3"));
	} else {
		showTab($("#tab1"));
	}
})


function clearResult() {
	$("#keyword").attr("value", "");
	var tab = $("#tab" + 3);
	tab.data("data", "");
	var info = $.parseJSON(tab.attr('info'));
	info.total = 0;
	info.count = 0;
	tab.attr('info', $.json2str(info));
	keyword = "";
}

