var params;
function callNative() {
	initPage();
}
function initPage() {
	params = oprequest();
	opscroll('part1',{bottom:'0px'}).disableMore();
	var opt = {
		callback : function(data) {
			if (data == undefined ) {
				console.log("return error json data to html");
				return;
			}
			var json = data;
			var list = json.ListResource.resourceList.Datas;
			$("#leaderboardNum").html(list.length);
			var result = TrimPath.processDOMTemplate("tpl", json);
			$("#part1").html(result);
			getScrollById('part1').reDraw();
		},
		Action : {
			"currentHtml" : "leaderboardlist.html",
			"requestPath" : "get_leaderboard_list",
			"requestParams" : params.getParamsStr(),
			"requestMethod" : "get",
			"needCallback" : true,
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : true
		}
	};
	jQuery.action(opt);
}
function generate() {
	var data =  {
		"currentHtml" : "leaderboardlist.html",
		"requestPath" : "get_leaderboard_list",
		"requestParams" : params,
		"requestMethod" : "get",
		"needCallback" : "true",
		"callbackInterface" : "initData",
		"callbackParamsType" :"json",
		"needShowNativeLoading" : "true"
	};
	return json2str(data);
}
function unlockedCall() {
}

function callTimelist(id) {
	var p = params.getParamsStr() + "&" + "leaderboardId=" + id;
	android.onLoadHtml("timelist.html?" + p);
}
