var url = oprequest();
var count = 0;
var total = 0;
var cache;
var feedOwn;

var reg = global_reg

var def = {
	'pagecount' : 20
};
function checkFeed(data) {
	return data == "" || data == undefined || data.Feed == undefined;
}

var gameType = global_feeddetail_gametype;

function checkFeedIsGame(feedType) {
	for (var index = 0; index < gameType.length; index++) {
		if (feedType == gameType[index]) {
			return true;
		}
	}
	return false;
}

$(document).ready(function() {
	initLang();
	opscroll('content2',{top:'160px',bottom:'75px'});
	$.action({
		callback : function(data) {
			if (checkFeed(data)) {
				console.log("feedDetail data undefined");
				return;
			} else {
				var node = data.Feed;
				var tabContent = "";
				var result = "";
				var temp = [];
				var hasGame = false;
				var nodes = [];
				var content = node.content;
				var imageUrl;
				temp = content.match(reg);
				if (temp == undefined) {
					console.log("temp undefined in feeDetail_action ");
				}
				try {
					if (node.originName != undefined && node.originName != "") {
						hasGame = true;
					}
					for (var c = 0; c < temp.length; c++) {
						var item = $.parseJSON(nodeToJson(temp[c]));
						nodes.push(item);
						if (item.user != undefined) {
							content = content.replace(temp[c],
									'<span class="friend_name" onclick="goToOtherHome(' + item.user.id + ')">'
											+ item.user.name + '</span>');
						}
						
						if (item.contest != undefined) {
						content = content.replace(temp[c],
							'<span class="friend_name" onclick="gotoMatch('+ item.contest.id +')">' + item.contest.name
									+ '</span>');
						}
					}

					for (var i = 0; i < nodes.length; i++) {
						if (checkHasUrl(nodes[i])) {
							imageUrl = nodes[i].user.profilePictureUrl;
							feedOwn = nodes[i].user.id;
							break;
						}
					}

					if (imageUrl == undefined) {
						console.log("imageUrl undefined in userHome_action ");
					}
					if (hasGame) {
						result = TrimPath.processDOMTemplate("tpl_game", {
									"node" : node,
									"content" : content,
									"updateTime" : getTimeString(Date
													.parse(new Date()),
											parseInt(node.updateTime)),
									"icon" : imageUrl,
									"userId" : feedOwn
								});
					} else {
						result = TrimPath.processDOMTemplate("tpl_nogame", {
									"node" : node,
									"content" : content,
									"updateTime" : getTimeString(Date
													.parse(new Date()),
											parseInt(node.updateTime)),
									"icon" : imageUrl,
									"userId" : feedOwn
								});
					}
					$("#content1").html(result);
				} catch (e) {
					console.log("exception from feeDetail_action : " + e);
				}
				var repContent = "";
			
				total = data.Feed.replyNum;	
				repContent = repContent + getReplyContent(data.Feed.replyList.Feeds);
				$("#content2").html(repContent);
				getScrollById('content2').reDraw();
			}
		},
		
		
		Action : {
			"currentHtml" : "feedDetail.html",
			"requestPath" : "get_feed_detail",
			"requestParams" : "gameVersionName="
					+ url.getParam("gameVersionName") + "&version="
					+ url.getParam("version") + "&feedId="
					+ url.getParam("feedId") + "&start=" + count + "&end="
					+ (count + def.pagecount),
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true"
		}
	});

});

function getReplyContent(list) {
	cache = list;
 	count = list.length;

	var result = "";
	for (var index = 0; index < list.length; index++) {
		var node = list[index].Reply;
		var nodes = [];
		var imageIcon = "";
		imageIcon = "";
		try {
			var content = node.replyContent;
			var temp = content.match(reg);
			if (temp == undefined) {
				console.log("temp undefined in feeDetail_action ");
				continue;
			}
			// 替换内容
			for (var c = 0; c < temp.length; c++) {
				// temp数组可能有“”元素
				if (temp[c] != "") {
					var item = $.parseJSON(nodeToJson(temp[c]));
					nodes.push(item);
					content = content.replace(temp[c],
							'<span class="reply_name" onclick="goToOtherHome(' + item.user.id + ')">'
								+ item.user.name + '</span>');
				}
			}
			for (var i = 0; i < nodes.length; i++) {
				if (checkHasUrl(nodes[i])) {
					imageIcon = nodes[i].user.profilePictureUrl;
					break;
				}
			}
			
			if (nodes[0].user.id == undefined) {
				console
						.log("userid undefined in feeDetail_action ");
				continue;
			} else {
				userId = nodes[0].user.id;
			}

			if (imageIcon == undefined) {
				console
						.log("imageUrl undefined in feeDetail_action ");
				continue;
			}
			result = result + TrimPath.processDOMTemplate("tpl_replay", {
						"node" : node,
						"content" : content,
						"updateTime" : getTimeString(Date
										.parse(new Date()),
								parseInt(node.replyTime)),
						"icon" : imageIcon,
						"userId" : userId
					});
		} catch (e) {
			console.log("exception from feeDetail_action.js : " + e
					+ " content : " + node.replyContent);
			continue;
		}
	}
	
	return result;
}

function goReply(replyId, receiverId) {
	if (receiverId == "null") {
		console.log("receiverId = null: in feedDetail_action");
		return;
	}
	android.onLoadHtml("reply.html?" + "senderId=" + url.getParam("userId")
			+ "&feedId=" + url.getParam("feedId") + "&replyId=" + replyId
			+ "&receiverId=" + receiverId + "&gameVersionName="
			+ url.getParam("gameVersionName") + "&version="
			+ url.getParam("version"));
}


function gotoMatch(id){
	android.onLoadHtml("matchdetail.html?"+url.setParam("contestId",id).getParamsStr());
}

function goToComment() {
	if (feedOwn == undefined) {
		console.log("receiverId = null: in feedDetail_action");
		return;
	}
	android.onLoadHtml("comment.html?" + "senderId=" + url.getParam("userId")
			+ "&feedId=" + url.getParam("feedId") + "&replyId=" + "0"
			+ "&receiverId=" + feedOwn + "&gameVersionName="
			+ url.getParam("gameVersionName") + "&version="
			+ url.getParam("version"));
}

function unlockedCall() {

}

function goToPage(id, type) {
	if(checkFeedCanGoToGame(type)) {
		goToGame(id);
	} else {
		gotoMatch(id);
	}
}

function goToOtherHome(id) {
	var from;
	if (url.getParam("from") == "1") {
		from = add_friend_type.myfeedtohome;
	} else {
		from = add_friend_type.otherfeedtohome;
	}
	android.onLoadHtml("otherHome.html?" + "userId=" + url.getParam("userId")
			+ "&requestedUserId=" + id + "&gameVersionName="
			+ url.getParam("gameVersionName") + "&version="
			+ url.getParam("version") + "&from=" + from);
}

function initLang() {
	$("#lang_feeddetail_title").html(nearmelang.lang_feeddetail_title);
	$("#lang_feeddetail_addcomment")
			.html(nearmelang.lang_feeddetail_addcomment);
}

function goComment(receiverId, feedId) {
	if (receiverId == undefined || receiverId == "") {
		console.log("receiverId = null: in userhome_action");
		return;
	}
	android.onLoadHtml("comment.html?" + "senderId=" + url.getParam("userId")
			+ "&feedId=" + feedId + "&replyId=" + "0"
			+ "&receiverId=" + receiverId + "&gameVersionName="
			+ url.getParam("gameVersionName") + "&version="
			+ url.getParam("version"));
}

function goToGame(id) {
	if (url.getParam("from") == "1") {
// android.onStatistics("1000006");
		goToOtherGame(id, url, enter_game_type.myfeed);
	} else if(url.getParam("from") == "2") {
		goToOtherGame(id, url, enter_game_type.otherfeed);
	}
// android.onLoadHtml("othergame.html?" + "userId=" + url.getParam("userId") +
// "&gameId=" + id + "&gameVersionName=" + "1.0.0");
}
