var url = oprequest();
var def = {
	'pagecount' : 20
};
var tag = 1;
var keyword = "";
var marketCache = [];
 

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
			return;
		}
		marketCache = {};
		for (var index = 0; index < list.length; index++) {
			var node = list[index].DeveloperGame;
			if (node.isMyGame == false && node.marketList != undefined && node.marketList.Markets != undefined) {
				var markets = node.marketList.Markets;
				marketCache[node.id] = {};
				marketCache[node.id].lists = markets;
				marketCache[node.id].packageName = node.gamePackageName;
			}
			var price = node.price;
			var bounds = node.bounds;
			var priceContent ="";
			if (price > 0) {
				priceContent = price + nearmelang.lang_global_yuan;
			} else if (bounds > 0) {
				priceContent = bounds + nearmelang.lang_global_bounds;
			} else {
				priceContent = nearmelang.lang_gamehome_free;
			}
			node.priceContent = priceContent;
			content.push(node);
		}
		$("#part1").append(TrimPath.processDOMTemplate("tpl_list", {
					data : content
				}));
		if(parseInt(info.count) < parseInt(info.total)){
			getScrollById('part1').enableMore();
		} else {
			getScrollById('part1').disableMore();
		}
		getScrollById('part1').reDraw();
	}
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
			"currentHtml" : "devgames.html",
			"requestPath" : "get_developer_game_list",
			"requestParams" : url.setParam('developerId',url.getParam('gameAuthorId')).getParamsStr() + "&start=" + info.count
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

function unlockedCall() {

}

function goToGame(id,e) {
	e.preventDefault();
	goToOtherGame(id, url, enter_game_type.devgame);
}

function goToOtherHome(id) {
	android.onLoadHtml("otherHome.html?" + url.getParamsStr() + "&requestedUserId=" + id);
}

$(document).ready(function() {
	opscroll('part1',{onPullUp:function(tab){loadMore($("#part1").get(0))},bottom:'0px'}).disableMore();
	showTab($("#part1").get(0));
});

function goToDownload(id) {
	var game_result = {
		installedVersion : "null",
		gamePackageName : marketCache[id].packageName,
		code : "1001"
	};
	game_result.Markets = marketCache[id].lists;
	android.onGameOpenOrBuy($.json2str(game_result));
}