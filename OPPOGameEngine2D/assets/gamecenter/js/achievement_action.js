var params;
function callNative() {
	opscroll('part1',{bottom:'0px'});
	params = oprequest();
	var opt = {
		callback : function(data) {
			if (data == undefined) {
				console.log("return error json data to html");
				return;
			}
			var json = data;
			var list = json.ListResource.resourceList.Datas;
			$("#achievementNum").html(list.length);
			var result = TrimPath.processDOMTemplate("tpl", json);
			$("#part1").html(result);
			getScrollById('part1').reDraw();
		},
		Action : {
			"currentHtml" : "achievement.html",
			"requestPath" : "get_achievement_list",
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
function unlockedCall() {
	
}