var url = oprequest();
var info = {};
info.count = 0;
info.board = 1;
info.total = 0;
var cache = [];
var imgcache;
$(document).ready(function() {
	imgcache=preimg();
	opscroll('content',{onPullUp:function(tab){loadMore()},bottom:"0px"}).disableMore();
	$("#categoryName").html(decodeURIComponent(url.getParam("categoryName")));
	loadMore();
});
function unlockedCall() {

}

function loadMore() {
	$.action({
		callback : function(data) {
			if (checkListResource(data)) {
				return;
			} else {
				var predata = cache;
				cache = predata.concat(data.ListResource.resourceList.Datas);
				info.count = info.count + 20;
				info.total = data.ListResource.number;
			}
			var list = [];
			for (var index = predata.length; index < cache.length; index++) {
				list[index]=cache[index];
				var price = list[index].Game.price;
				var bounds = list[index].Game.bounds;
				list[index].Game.imgid='imgcahce_'+index;
				imgcache.add(list[index].Game.imgid,getImgBySize(list[index].Game.gameIcon,66));
				var content ;
				if (price > 0) {
					content = price + nearmelang.lang_global_yuan;
				} else if (bounds > 0) {
					content = bounds + nearmelang.lang_global_bounds;
				} else {
					content = nearmelang.lang_gamehome_free;
				}
				list[index].content = content;
			}
			var tabContent = TrimPath.processDOMTemplate("tpl_game", {"data" : list});
			$("#content").append(tabContent);
			if(parseInt(info.count) < parseInt(info.total)){
				getScrollById('content').enableMore().reDraw();
			} else {
				getScrollById('content').disableMore().reDraw();
			}
			imgcache.start();
		},
		Action : {
			"currentHtml" : "categorylist.html",
			"requestPath" : "get_game_list",
			"requestParams" : "categoryId="+ url.getParam("categoryId") + "&start=" + info.count+ "&end="+ (parseInt(info.count) + parseInt(def.pagecount)),
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true"
		}
	});
}

function goToGame(id) {
	goToOtherGame(id, url, enter_game_type.category);
}