var url = oprequest();
var imgcache;
function unlockedCall() {
}
$(document).ready(function() {
	imgcache=preimg();
	opscroll('content1',{onPullUp:function(){loadMore($("#tab1").get(0))},top:'195px',bottom:'0px'}).disableMore();
	opscroll('content2',{onPullUp:function(){loadMore($("#tab2").get(0))},top:'195px',bottom:'0px'}).disableMore();
	var currentTab = url.getParam("currentTab");
	if (currentTab == "1") {
		showTab($("#tab1"));
	} else if (currentTab == "2") {
		showTab($("#tab2"));
	} else {
		showTab($("#tab1"));
	}
});
function clearContent() {
	$("#content").attr("value", "");
}
function showTab(obj) {
	var info = $.parseJSON($(obj).attr('info'));
	if (info.board == 1) {
		android.onChangeTab("1");
		$("#part1").css("display", "block");
		$("#part2").css("display", "none");	
		$("#tab1").attr("class","li_click")
		$("#tab2").removeClass("li_click");
		$(".img_ul").attr("src","images/hdpi/tab2_left_click.png");
		$(".img_li").css("left","52%");
		getScrollById('content1').show();
		getScrollById('content2').hide();
	} else if (info.board == 2) {
		android.onChangeTab("2");
		$("#part1").css("display", "none");
		$("#part2").css("display", "block");		
		$("#tab2").attr("class","li_click")
		$("#tab1").removeClass("li_click");
		$(".img_ul").attr("src","images/hdpi/tab2_right_click.png");
		$(".img_li").css("left","4%");
		getScrollById('content1').hide();
		getScrollById('content2').show();
	}
	if( info.count <= 0) {
		loadMore(obj);
	}
}


function filltab(obj) {
	var data = obj.data('data');
	var info = $.parseJSON(obj.attr('info'));
	if (data == undefined || data == "" || parseInt(data.ListResource.number) == 0 || checkListResource(data)) {
		console.log("filltab in newletter_action tab" + info.board
				+ " data undefined or 0");
		if (info.board == 1 ) { 
			$("#content1").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_newletter_nocontacts));
		} else if(info.board == 2 )	 {
			$("#content2").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_friend_nofriend));
		}
		if(info.count<info.total){
			getScrollById('content'+info.board).enableMore().reDraw();
		}else{
			getScrollById('content'+info.board).disableMore().reDraw();
		}
		return;
	}
	var list = data.ListResource.resourceList.Datas;
	if (info.board == 1) {
		for(var i in list){
			list[i].FriendUser.imgid='imgcache_'+info.board+info.count+(i+1);
			imgcache.add(list[i].FriendUser.imgid,list[i].FriendUser.profilePictureUrl);
		}
		var result = TrimPath.processDOMTemplate("tpl_user_new", {
							"data" : list
						});
		$("#content1").append(result);
	} else if (info.board == 2) {
		for(var i in list){
			list[i].FriendUser.imgid='imgcache_'+info.board+info.count+(i+1);
			imgcache.add(list[i].FriendUser.imgid,list[i].FriendUser.profilePictureUrl);
		}
		var result = TrimPath.processDOMTemplate("tpl_user_friend", {
							"data" : list
						});
		$("#content2").append(result);
	}
	if(info.count<info.total){
		getScrollById('content'+info.board).enableMore().reDraw();
	}else{
		getScrollById('content'+info.board).disableMore().reDraw();
	}
	imgcache.start();
}


function loadMore(obj) {
	obj = $(obj);
	var info = $.parseJSON(obj.attr('info'));
	if (info.board == 1) {
		$.action({
			callback : function(data) {
				if (obj.data('data') == undefined || obj.data("data") == "") {
					obj.data('data',data);
				} else {
					pdata = obj.data('data');
					var plist = pdata;
					if (checkListResource(data)) {
						obj.data('data', pdata);
					} else {
						if(!info.addtype||info.addtype!=true){
							data.ListResource.resourceList.Datas = plist
								.concat(data.ListResource.resourceList.Datas);
						}
						obj.data('data', data);
					}
				}
				if (!checkListResource(obj.data('data'))) {
					info.count = obj.data('data').ListResource.resourceList.Datas.length;
				} else {
					info.count = 0;
				}
				info.total = parseInt(data.ListResource.number);
				obj.attr('info', $.json2str(info));
				filltab(obj);
			},
			Action : {
				"currentHtml" : "newletter.html",
				"requestPath" : "get_contact_list",
				"requestParams" : url.getParamsStr() + "&start=" + info.count
						+ "&end=" + (info.count + nearmelang.config_page_count),
				"requestMethod" : "get",
				"needCallback" : "true",
				"callbackInterface" : "jQuery.actionCallback",
				"callbackParamsType" : "json",
				"needShowNativeLoading" : "true"
			}
		});
	} else if (info.board == 2) {
		$.action({
			callback : function(data) {
				if (obj.data('data') == undefined
						|| obj.data("data") == "") {
						obj.data('data',
								data);
				} else {
					pdata = obj.data('data');
					var plist = pdata;
					if (checkListResource(data)) {
						obj.data('data', pdata);
					} else {
						if(!info.addtype||info.addtype!=true){
							data.ListResource.resourceList.Datas = plist
								.concat(data.ListResource.resourceList.Datas);
						}
						obj.data('data',
								data);
					}
				}
				if (!checkListResource(obj.data('data'))) {
					info.count = (!info.addtype||info.addtype!=true)?0:info.count+obj.data('data').ListResource.resourceList.Datas.length;
				} else {
					info.count = 0;
				}
				info.total = parseInt(data.ListResource.number);
				obj.attr('info', $.json2str(info));
				filltab(obj);
			},
			Action : {
				"currentHtml" : "newletter.html",
				"requestPath" : "get_my_friend_list",
				"requestParams" : url.getParamsStr()
						+ "&start="
						+ parseInt(info.count)
						+ "&end="
						+ (parseInt(info.count) + parseInt(nearmelang.config_page_count)),
				"requestMethod" : "get",
				"needCallback" : "true",
				"callbackInterface" : "jQuery.actionCallback",
	 			"callbackParamsType" : "json",
				"needShowNativeLoading" : "true"
			}
		});
	}
	$(".refresh").unbind('touchend').bind('touchend',function(){
		info.count=0;
		info.total=0;
		obj.attr('info',$.json2str(info));
		obj.data('data','');
		loadMore(obj.get(0));
	});
}

function goToSend(name, id) {
	android.onLoadHtml("sendletter.html?" + url.getParamsStr() + "&receiverName=" + name + "&receiverId=" + id);
}

function goToSearch(){
	var content = $("#content").attr("value");
	if (content.trim() == "" || content == nearmelang.lang_newletter_inittext) {
		opalert(nearmelang.lang_global_message_notnull);	
		return;
	} else {
		android.onLoadHtml("newletter_search.html?" + url.getParamsStr() + "&keyword=" + content);
	}
}


