var params;
var isFavourite;
var gameinfo ;
var canGoToFans = true;
var canClick = false;
var visitor = false;
$(document).ready(function(){
	opscroll('gameHomeBody',{});
	$("#leaderboard").html(nearmelang.lang_global_leaderboard);
	$("#achievement").html(nearmelang.lang_global_achievement);
	$("#lang_gamehome_lovedplayers").html(nearmelang.lang_gamehome_lovedplayers);
	$("#lang_gamehome_nowplayers").html(nearmelang.lang_gamehome_nowplayers);
	$("#lang_gamehome_downloadnum").html(nearmelang.lang_gamehome_downloadnum);
	$("#lang_gamehome_whoisplay").html(nearmelang.lang_gamehome_whoisplay);
	$("#lang_gamehome_fansclub").html(nearmelang.lang_gamehome_fansclub);
	callNative();
});

function callNative() {
	initPage();
}

function initPage() {
	params = oprequest();
	if (params.getParam("visitor") == "true") {
		visitor = true;
	}
	var opt = {
		callback : function(data) {
			if (data == undefined) {
				console.log("return error json data to html");
				return;
			}
			var json = data;
			gameinfo = json.ProfileGame;
			$("#favouriteNum").html(json.ProfileGame.favouriteNum);
			$("#playerNum").html(json.ProfileGame.playerNum);
			$("#friendsNum").html(json.ProfileGame.friendNum);
			$("#achievementNum").html(json.ProfileGame.achievementGetNum);
			$("#totalAchNum").html(json.ProfileGame.achievementNumber);
			$("#gameName").html(json.ProfileGame.gameName);
//			$("#gameIcon").attr("src", json.ProfileGame.gameIcon);
			global_get_image("gameIcon", json.ProfileGame.gameIcon);
//			$.action({
//				callback : function(data) {
//					$("#" + data.id).attr("src", data.path);
//				},
//				Action : {
//					"currentHtml" : oprequest().getFileName(),
//					"requestPath" : "get_image",
//					"requestParams" : $.json2str({id:"gameIcon",url:json.ProfileGame.gameIcon}),
//					"requestMethod" : "get",
//					"needCallback" : "true",
//					"callbackInterface" : "jQuery.actionCallback",
//					"callbackParamsType" : "path",
//					"needShowNativeLoading" : "false"
//				}
//			});
			var player;
			if (json.ProfileGame.currentFriendUserName !== undefined) {
				player = global_getShortName(json.ProfileGame.currentFriendUserName);
			}
			
			$("#currentPlayer").html(player != undefined ? ("("+player + nearmelang.lang_global_more+")") : "");
			
			android.onGetGameVersion(json.ProfileGame.gamePackageName, json.ProfileGame.gameVersionName, "onGetGameCallback","hehe");
			isFavourite = json.ProfileGame.isFavourite;
			if (!gameinfo.hasAchievement) {
				$("#button_achievement").css("display", "none");
			}
			if (!gameinfo.hasLeaderboard) {
				$("#button_leaderboard").css("display", "none");				
			}
			if (isFavourite == "true" || isFavourite == true) {
				$("#isloved").removeClass().addClass("gameHome_head_right_love");
			} else {
				$("#isloved").removeClass().addClass("gameHome_head_right");
			}
			params.setParams({
				'isFavourite':isFavourite,
				'gameAuthorName':json.ProfileGame.gameAuthorName,
				'gameAuthorId':json.ProfileGame.gameAuthorId
			});
		},
		Action : {
			"currentHtml" : /gameHome.html/gi.test(location.href) ? "gameHome.html" : "othergame.html",
			"requestPath" : "get_game_page",
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



var game_code;
var game_result;
function onGetGameCallback(data) {
	canClick = true;
 	game_result = $.parseJSON(data);
	game_code = parseInt(game_result.code);
	switch(game_code) {
		case game_result_code.notinstall : 
			canGoToFans = false;
			var price = parseFloat(gameinfo.price);
			var bounds = parseFloat(gameinfo.bounds);
			var content ;
			if (price > 0) {
				content = nearmelang.lang_gamehome_download + "(" + price + nearmelang.lang_global_yuan + ")";
			} else if (bounds > 0) {
				content = nearmelang.lang_gamehome_download + "(" + bounds + nearmelang.lang_global_bounds + ")";
			} else {
				content = nearmelang.lang_gamehome_download + "(" + nearmelang.lang_gamehome_free + ")";
			}
			$("#buyType").html(content);
		 	break;
		case game_result_code.open:
			$("#buyType").html(nearmelang.lang_gamehome_open);
			break;
		case game_result_code.update: 
			$("#buyType").html(nearmelang.lang_gamehome_hasnewversion);
			break;
	}
}
function gameOpen() {
	game_result.Markets = gameinfo.marketList.Markets;
	android.onGameOpenOrBuy($.json2str(game_result));
}



function unlockedCall() {
	
}

function insertData(id, data) {
	document.getElementById(id).innerHTML = data;
}

function callLeaderboardList() {
	android.onLoadHtml("leaderboardlist.html?" + params.getParamsStr());
}

function callAchievementList() {
	android.onLoadHtml("achievement.html?" + params.getParamsStr());
}

function goFansClub() {
	if (!canClick) {
		return;
	}
	if (canGoToFans) {
		if (visitor) {
			opalert({
				content:nearmelang.lang_global_youneedloginforfun,
				mode:opalert.BTN_BOTH,
				onok:function(){
					android.onGoToLogin();
				},
				oktext:nearmelang.lang_global_login
			});
		} else {
			android.onLoadHtml("fansclub.html?"  +  params.getParamsStr());
		}
	} else {
		android.makeToast(nearmelang.lang_gamehome_gamenotinstall);
	}
}

function goWhoisplay() {
	android.onLoadHtml("whoisplaying.html?"  +  params.getParamsStr());
}
