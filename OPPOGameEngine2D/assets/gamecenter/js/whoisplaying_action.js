var url = oprequest();
var isVisitor = false;
var hasContent = false;
var def = {
	'pagecount' : 20
};
var tag = 1;
var keyword = "";
var imgcache;
var appendCount = false;

function showTab(obj) {
//	$(obj).addClass('li_click').siblings('li').removeClass('li_click');
	$("#contentAll").css("display", "block");
	$("#line").html("");
	var info = $.parseJSON($(obj).attr('info'));
	loadMore(obj);
}

function removeAllMore() {
	for (var index = 1; index < 4; index++) {
		$("#more" + index).html('');
	}
}

function filltab(obj) {
	var data = obj.data('data');
	var info = $.parseJSON(obj.attr('info'));
	if(parseInt(info.count) < parseInt(info.total)){
		getScrollById('tab1').enableMore();
	} else {
		getScrollById('tab1').disableMore();
	}
	if (info.board == 1) {
		var list = data.ListResource.resourceList.Datas;
		var content1 = [],content2=[],content3=[];
		if (list == undefined || list.length == 0) {
			if (!hasContent) {
				$("#contentAll").css("display", "none");
				$("#line").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_whoisplaying_nooneplay));
				getScrollById('tab1').reDraw();
			}
			return;
		}
		for (var index = 0; index < list.length; index++) {
			var node = list[index].GameUser;
			if (node.name == undefined) {
				continue;
			}
			node.imgid="imgcache_"+info.board+info.count+(index+1);
			if(node.id==url.getParam('userId')){
				node.isMy=true;
				if(content1.length==0&&$("#part1>div").size()==0) {
					content1.push(node);
				}
			} else if( node.isFriend ){
				node.isMy=false;
				content2.push(node);
			} else {
				node.isMy=false;
				content3.push(node);
			}
			imgcache.add(node.imgid,node.profilePictureUrl);
		}
		$("#part1").append(TrimPath.processDOMTemplate("tpl", {data : content1}));
		$("#part2").append(TrimPath.processDOMTemplate("tpl", {data : content2}));
		$("#part3").append(TrimPath.processDOMTemplate("tpl", {data : content3}));
		hasContent = true;
		checkTagShow();
		getScrollById('tab1').reDraw();
		imgcache.start();
	}
	
}

function addFriend(id) {
	if (isVisitor) {
		opalert({
			content:nearmelang.lang_global_youneedloginforfun,
			mode:opalert.BTN_BOTH,
			onok:function(){
				android.onGoToLogin();
			},
			oktext:nearmelang.lang_global_login
		});
		return;
	} 
	var data = $($("#tab1")).data('data');
	var list = data.ListResource.resourceList.Datas;
	for (var index = 0; index < list.length; index++) {
		if (list[index].GameUser.id == id) {
			list[index].GameUser.isFriend = true;
		}
	}
	$.action({
		callback : function(data) {
			if (data.RequestResult.resultCode == 1001) {
				android.onStatistics("1000037");
				changeState(id);
				android.makeToast(nearmelang.lang_friend_addsuccess);
				var obj = $("#tab1").get(0)
				clearTabInfo(obj);
				showTab(obj);
			}
		},
		Action : {
			"currentHtml" : "whoisplaying.html",
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

function checkTagShow(){
	for(var i=1;i<=3;i++){
		if($('#part'+i).children().size()>0){
			$("#tag"+i).css("display", "block");
		} else {
			$("#tag"+i).css("display", "none");
		}
	}
}

function changeState(id) {
	//getById(id).innerHTML = '<div class="isfriend">'+nearmelang.lang_friend_alreadyfriend+'</div>';
	$("#"+id).removeClass().addClass("isfriend").html(nearmelang.lang_friend_alreadyfriend);
}

function loadMore(obj) {
	obj = $(obj);
	var info = $.parseJSON(obj.attr('info'));
	$.action({
		callback : function(data) {
//			console.log($.json2str(data));
			if (!obj.data('data') || obj.data("data") == "") {
				obj.data('data', data);
			} else {
				pdata = obj.data('data');
				if (pdata.ListResource.resourceList == undefined) {
					
				} else {
					var plist = pdata.ListResource.resourceList.Datas;
					// var data = $.extend(true,pdata,data);
					if(info.addtype&&info.addtype==true){
//						data.ListResource.resourceList.Datas = plist;
					} else {
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
			"currentHtml" : "whoisplaying.html",
			"requestPath" : "get_game_user_list",
			"requestParams" : url.getParamsStr() + "&start=" + (appendCount ? 0 : info.count)
					+ "&end="
					+ (parseInt(info.count) + parseInt(def.pagecount))
					+ "&tag=" + tag,
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true"
		};
	}
}

function unlockedCall() {}

function goToGame(id) {
	android.onLoadHtml("othergame.html?" + url.setParam("gameId",id).getParamsStr());
}

function goToOtherHome(id) {
	if (isVisitor) {
		opalert({
				content:nearmelang.lang_global_youneedloginforfun,
				mode:opalert.BTN_BOTH,
				onok:function(){
					android.onGoToLogin();
				},
				oktext:nearmelang.lang_global_login
			});
		return;
	}
	android.onLoadHtml("otherHome.html?" + url.getParamsStr() + "&requestedUserId=" + id);
}

$(document).ready(function() {
	imgcache=preimg();
	if (url.getParam("visitor") == "true") {
		isVisitor = true;
	}
	opscroll('tab1',{onPullUp:function(tab){loadMore($("#tab1").get(0))},bottom:'0px'}).disableMore();
	showTab($("#tab1").get(0));
});

function clearTabInfo(obj) {
	$("#part1").html("");
	$("#part2").html("");
	$("#part3").html("");
	$("#line").html("");
	var info = $.parseJSON($(obj).attr('info'));
	info.count = 0;
	info.total = 0;
	$("#tab1").attr('info', $.json2str(info));
}