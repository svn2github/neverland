var gameInfo;
var paramsUtil;
var gameVersionName;

function callNative() {
	initPage();
}

function initPage() {
	paramsUtil = new ParamsUtil();
	paramsUtil.parserUrl(decodeURI(location.href));
	gameInfo = new GameInfo(paramsUtil.entry["userId"], paramsUtil.entry["gameId"]);
	callAction(generateUrl());
}

function callAction(data) {
	if (gameInfo.canCallNative) {
		android.onAction(data);
		gameInfo.canCallNative = false;
	} else {
		console.log("不能 重复CallNative");
		android.makeToast("你点太快了！");
	}
}

function generateUrl() {
	var data = {
		"currentHtml" : "gamedetails.html",
		"requestPath" : "get_game_profile",
		"requestParams" : "gameId=" + paramsUtil.entry["gameId"] + "&" + "userId=" + paramsUtil.entry["userId"],
		"requestMethod" : "get",
		"needCallback" : "true",
		"callbackInterface" : "initData",
		"callbackParamsType" : "json",
		"needShowNativeLoading" : "true"
	};
	return json2str(data);
}

function initData(data) {
	var json = change2json(data).ProfileGame;
	gameVersionName = json.gameVersionName;
	insertData("favouriteNum", json.favouriteNum);
	insertData("friendNum", json.friendNum);
	insertData("gameName", json.gameName);
	insertData("price", json.price);
	insertData("gameAuthor", json.gameAuthorName);
	insertData("gameProfile", json.gameProfile);
	insertData("playerNum", json.playerNum);
	getById("gameIcon").src = json.gameIcon;
}

function unlockedCall() {
	gameInfo.canCallNative = true;
}

function goToLeaderboard() {
	android.onLoadHtml("leaderboardlist.html?" + "gameId=" + paramsUtil.entry["gameId"] + "&" + "userId=" + paramsUtil.entry["userId"] + "&" + "gameVersionName=" + gameVersionName);
}
function goToAchievement() {
	android.onLoadHtml("achievement.html?" + "gameId=" + paramsUtil.entry["gameId"] + "&" + "userId=" + paramsUtil.entry["userId"] + "&" + "gameVersionName=" + gameVersionName);
}