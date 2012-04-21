var url = oprequest();
var def = {
	'pagecount' : 20
};
var tag = 1;
var keyword = "";
var imgcache;

function showTab(obj) {
	$(obj).addClass('li_click').siblings('li').removeClass('li_click');
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
	if (info.board == 1) {
		var list = data.ListResource.resourceList.Datas;
		var content = [];
		if (list == undefined || list.length == 0) {
			$("#part1content").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_fansclub_nofriend));
			return;
		}
		for (var index = 0; index < list.length; index++) {
			var node = list[index].FriendUser;
			node.imgid='imgcahce_'+info.board+info.count+(index+1);
			imgcache.add(node.imgid,node.profilePictureUrl);
			content.push(node);
		}
		$("#part1content").append(TrimPath.processDOMTemplate("tpl_friendslist", {
					data : content
				}));

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
			node.imgid='imgcahce_'+info.board+info.count+(index+1);
			imgcache.add(node.imgid,node.profilePictureUrl);
			content.push(node);
		}
		$("#part3content").append(TrimPath.processDOMTemplate("tpl_friendslist", {
					data : content
				}));
	}
	if(info.count<info.total){
		getScrollById('part'+info.board+'content').enableMore().reDraw();
	}else{
		getScrollById('part'+info.board+'content').disableMore().reDraw();
	}
	imgcache.start();
}

function searchFriend() {
	var word = $("#keyword").attr("value").trim();
	if (word == undefined || word == "" || word == nearmelang.lang_fansclub_invitefriend_inittext) {
		opalert(nearmelang.lang_global_message_notnull);
	} else if (word == keyword) {
		removeAllMore();
		keyword = word;
		$("#part1").hide();
		$("#part3").show();
		getScrollById('part3content').reDraw();
		return false;
	} else {
		keyword = word;
		$("#part1").hide();
		$("#part3").show();
		$("#part3content").html("");
		var tab3 = $("#part3");
		tab3.data("data", "");
		var info = $.parseJSON($("#part3").attr('info'));
		info.total = 0;
		info.count = 0;
		tab3.attr('info', $.json2str(info))
		loadMore($("#part3"));
	}
}

function clearSearch() {
	removeAllMore();
	$("#part3").hide();
	var part1=$("#part1");
	$("#keyword").val("").trigger("blur");
	$("#part1").show();
	getScrollById('part1content').reDraw();
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
				info.count = (info.addtype&&info.addtype==true)?info.count+data.ListResource.resourceList.Datas.length:data.ListResource.resourceList.Datas.length;
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
			"currentHtml" : "invitefriend.html",
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
	} else if (info.board == 3) {
		return {
			"currentHtml" : "invitefriend.html",
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

function goToOtherHome(id) {
	android.onLoadHtml("otherHome.html?" + url.getParamsStr() + "&requestedUserId=" + id);
}

$(document).ready(function() {
	imgcache=preimg();
	opscroll('part1content',{onPullUp:function(){loadMore($("#part1").get(0))},top:'190px',bottom:'98px'}).disableMore();
	opscroll('part3content',{onPullUp:function(){loadMore($("#part1").get(0))},top:'190px',bottom:'98px'}).disableMore();
	showTab($("#part1").get(0));
});

function checkbox_click(obj,allelementid){
	obj = $("#"+obj);
	allelementid = $('#'+allelementid);
	if(obj.hasClass('checked')){
		obj.removeClass('checked').addClass('notchecked');
		if(allelementid.size()>0){
			allelementid.removeClass('checked').addClass('notchecked');
		}
	}else{
		obj.removeClass('notchecked').addClass('checked');
		if($('.notchecked').size()==1){
			allelementid.removeClass('notchecked').addClass('checked');
		}
	}
}

function selectAll(obj){
	obj=$(obj);
	if(obj.hasClass('checked')){
		$('.checked').removeClass('checked').addClass('notchecked');
	} else {
		$('.notchecked').removeClass('notchecked').addClass('checked');
	}
}

function sendInvite(){
	var checkedFriend = $('.checked[fid]');
	if(checkedFriend.size()>0){
		var fid = '';
		checkedFriend.each(function(){
			if(fid==''){
				fid=$(this).attr('fid');
			}else{
				fid=fid+';'+$(this).attr('fid');
			}
		})
		$.action({
			callback:function(data){
				if (data == undefined) {
					console.log("return error json data to html");
					return;
				}
				if(data.RequestResult.resultCode==1001 ) {
//					opalert(nearmelang.lang_fansclub_invitesuccess);
					android.makeToast(nearmelang.lang_fansclub_invitesuccess);
					android.goBack();
				} else {
//					opalert('a error happend,please try it aging!');
				}
			},
			Action:{
				"currentHtml" : "invitefriend.html",
				"requestPath" : "post_invite_game",
				"requestParams" : url.setParam('invitedFriendIds',fid).getParamsStr(),
				"requestMethod" : "post",
				"needCallback" : "true",
				"callbackInterface" : "jQuery.actionCallback",
				"callbackParamsType" : "json",
				"needShowNativeLoading" : "true",
				"loadingHeader" : nearmelang.lang_fansclub_isinviting,
				"failureMessage" : nearmelang.lang_fansclub_invitefailure
			}
		})
	} else {
//		opalert('select a friend at least');
	}
}
