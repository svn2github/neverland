var url = oprequest();
var userCache = {};
var username ;
var myIcon;
var otherIcon;
var marketCache = {};
var isFriend = true;
var imgcache;
var tab1Adding = false;
var reg = /<(user|game|contest){1}[^>]+\/>/gi;
function nodeToJons(node) {
	return node.replace(/<([^\s]*)\s/, "{\"$1\":{ ").replace(/\s\/>$/, " }}")
			.replace(/(\s([\w]+)=([^\s]+))/gi, "\"$2\":$3,")
			.replace(/, }/, "}");
}
var def = {
	'pagecount' : 20
};

var gameType = [3, 5, 9];

function checkFeedIsGame(feedType) {
	for (var index = 0; index < gameType.length; index++) {
		if (feedType == gameType[index]) {
			return true;
		}
	}
	return false;
}
function showTab(obj) {
	var info = $.parseJSON($(obj).attr('info'));
	if (info.board == 1) {
		android.onChangeTab('1');
		$("#part1").css("display", "block");
		$("#part2").css("display", "none");
		$("#part3").css("display", "none");
		
		$(".img_ul").attr("src","images/hdpi/tab3_left_click.png");
		$(".img_li").css("display","block");
		$("#img_li_1").css("display","none");
		$(".li_click").removeClass();
		$("#tab1").attr("class","li_click");
		getScrollById('content1').show();
		getScrollById('content2').hide();
		getScrollById('content3').hide();
		
		
	} else if (info.board == 2) {
		android.onChangeTab('2');
		$("#part1").css("display", "none");
		$("#part2").css("display", "block");
		$("#part3").css("display", "none");
		
		$(".img_ul").attr("src","images/hdpi/tab3_middle_click.png");
		$(".img_li").css("display","block");
		$("#img_li_2").css("display","none");
		$(".li_click").removeClass();
		$("#tab2").attr("class","li_click");
		getScrollById('content1').hide();
		getScrollById('content2').show();
		getScrollById('content3').hide();
		
	} else if( info.board  == 3) {
		android.onChangeTab('3');
		$("#part1").css("display", "none");
		$("#part2").css("display", "none");
		$("#part3").css("display", "block");
		
		$(".img_ul").attr("src","images/hdpi/tab3_right_click.png");
		$(".img_li").css("display","block");
		$("#img_li_3").css("display","none");
		$(".li_click").removeClass();
		$("#tab3").attr("class","li_click");
		getScrollById('content1').hide();
		getScrollById('content2').hide();
		getScrollById('content3').show();
	}
	
	if (info.board !== 1) {
		if(info.count > 0) {
			
		} else {
			loadMore(obj);
		}
	} 
}

function filltab(obj) {
	var data = obj.data('data');
	var info = $.parseJSON(obj.attr('info'));
	if (data == undefined || data == "" || data.length == 0) {
		if (info.board == 1) {
			$("#content1").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_otherhome_nofeed));
		} else if(info.board == 2) {
			$("#content2").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_otherhome_nogame));
		} else if (info.board == 3) {
			$("#content3").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_otherhome_nofriend));
		}
		
		console.log("filltab in userHome_action tab" + info.board
				+ " data undefined or 0");
		getScrollById('content'+info.board).disableMore().reDraw();
		return;
	}
	if (info.board == 1) {
		var list = data;
		var tabContent = "";
		var countNum = 0;
		var feedOwn = "";
		var tpl_game=TrimPath.parseDOMTemplate("tpl_game");
		var tpl_nogame=TrimPath.parseDOMTemplate("tpl_nogame");
		for (var index = 0; index < list.length; index++) {
			var node = list[index].InternalFeed;
			var result = "";
			var temp = [];
			var nodes = [];
			var hasGame = false;
			var content = node.content;
			var imageUrl;
			temp = content.match(reg);
			if (temp == undefined) {
				console.log("temp undefined in userHome_action ");
				continue;
			}
			try {
				if (node.originName != undefined && node.originName != ""
						&& (checkFeedIsGame(node.feedType))) {
					hasGame = true;
				}
				for (var c = 0; c < temp.length; c++) {
					var item = $.parseJSON(nodeToJons(temp[c]));
					nodes.push(item);
					if (item.user != undefined) {
						content = content.replace(temp[c],
							'<span class="friend_name">' + item.user.name
									+ '</span>');
					}
					if (item.contest != undefined) {
						content = content.replace(temp[c],
							'<span class="friend_name">' + item.contest.name
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
					continue;
				}
				var imgid = 'imgcache_'+info.board+info.count+(index+1);
				if (hasGame) {
					result = tpl_game.process({
						"node" : node,
						"content" : content,
						"updateTime" : getTimeString(Date
										.parse(new Date()),
								parseInt(node.updateTime)),
						"icon" : imageUrl,
						"userId" :feedOwn,
						"imgid" :imgid
					});
					imgcache.add(imgid,getImgBySize(imageUrl,66));//游戏有小图标
				} else {
					result = tpl_nogame.process({
						"node" : node,
						"content" : content,
						"updateTime" : getTimeString(Date
										.parse(new Date()),
								parseInt(node.updateTime)),
						"icon" : imageUrl,
						"userId" :feedOwn,
						"imgid" :imgid
					});
					imgcache.add(imgid,imageUrl);//头像么有小图标
				}
				tabContent = tabContent + result;
			} catch (e) {
				console.log("exception from userHome_action.js : " + e);
				continue;
			}
		}
		$("#content1").append(tabContent);
	} else if (info.board == 2) {
		var list = data;
		var tabContent = "";
		myIcon = data[0].UserGame.self.InternalUser.profilePictureUrl;
		marketCache = {};
		for (var index = 0; index < data.length; index++) {
			var node = data[index].UserGame;
			if (node.isMyGame == false) {
				var markets = node.marketList.Markets;
				marketCache[node.id] = {};
				marketCache[node.id].gamePackageName = node.gamePackageName;
				marketCache[node.id].markets = markets;
			}
			data[index].UserGame.imgid='imgcache_'+info.board+info.count+(index+1);
			imgcache.add(data[index].UserGame.imgid,getImgBySize(data[index].UserGame.gameIcon,66));
		}
		try {
			result = TrimPath.processDOMTemplate("tpl_usergame", {
				"nodes" : data,
				"username" : username
			});
			tabContent = tabContent + result;
		} catch (e) {
			console.log("exception from userHome_action.js : " + e
					+ " content : " + node.content);
		}
		$("#content2").html(tabContent);
		global_get_image("myIcon", myIcon);
		global_get_image("otherIcon", otherIcon);
		
	} else if(info.board == 3){
		var list = data;
		var content = [];
		for (var index = 0; index < data.length; index++) {
			var node = data[index].FriendUser;
			node.imgid='imgcache_'+info.board+info.count+(index+1);
			content.push(node);
			imgcache.add(node.imgid,node.profilePictureUrl);
		}
		$("#content3").html(TrimPath.processDOMTemplate("tpl_searchlist", {
					data : content
				}));
	}
	if(info.count<info.total){
		getScrollById('content'+info.board).enableMore().reDraw();
	} else {
		getScrollById('content'+info.board).disableMore().reDraw();
	}
	imgcache.start();
}

function goToOtherHome(id) {
	android.onLoadHtml("otherHome.html?" + "gameVersionName=" + url.getParam("gameVersionName") + "&version=" + url.getParam("version") + "&userId=" + url.getParam("userId") + "&requestedUserId=" + id);
}
function addFriend(id) {
	var list =  $($("#tab3")).data('data');
	for (var index = 0; index < list.length; index++) {
		if (list[index].FriendUser.id == id) {
			list[index].FriendUser.isFriend = true;
		}
	}
	$.action({
				callback : function(data) {
					if (url.getParam("from") == add_friend_type.lbtohome) {
						android.onStatistics(add_friend_type.lbtohome);
					} else if (url.getParam("from") == add_friend_type.myfeedtohome) {
						android.onStatistics(add_friend_type.myfeedtohome) ;
					} else if (url.getParam("from") == add_friend_type.otherfeedtohome) {
						android.onStatistics(add_friend_type.otherfeedtohome);
					} else if (url.getParam("from") == add_friend_type.invitetohome) {
						android.onStatistics(add_friend_type.invitetohome);
					}
					changeState(id);
					android.makeToast(nearmelang.lang_friend_addsuccess);
				},
				Action : {
					"currentHtml" : "otherHome.html",
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

function addFriendForHead() {
	if (isFriend == true) {
		return;
	}
	$.action({
				callback : function(data) {
						changeStateForHead();
				},
				Action : {
					"currentHtml" : "otherHome.html",
					"requestPath" : "post_add_friend",
					"requestParams" : url.getParamsStr() + "&beFriendId=" + url.getParam("requestedUserId"),
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
function changeState(id) {
	$("#" + id).removeClass().addClass('isfriend').html(nearmelang.lang_otherhome_commonfriend);
}
function changeStateForHead() {
	$("#friendTag").removeClass().html("");
}

function loadMore(obj) {
	obj = $(obj);
	var info = $.parseJSON(obj.attr('info'));
	if (info.board == 1) {
		$.action({
					callback : function(data) {
						if (!tab1Adding) {
							if (!checkListResource(data)) {
								obj.data('data',
										data.ListResource.resourceList.Datas);
								tab1Adding = true;
								info.count = 20;
							} else {
								info.count = 0;
							}
						} else {
							obj.data('data',
										data.ListResource.resourceList.Datas);
							info.count = info.count + 20;
						}
						info.total = data.ListResource.number;
						obj.attr('info', $.json2str(info));
						filltab(obj);
					},
					Action : {
						"currentHtml" : "otherHome.html",
						"requestPath" : "get_feed_list",
						"requestParams" : "userId=" + url.getParam("requestedUserId")
								+ "&start="
								+ info.count
								+ "&end="
								+ (parseInt(info.count) + parseInt(def.pagecount))
								+ "&isMyPage=" + "false",
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
//							obj.data('data', data);
							if(!checkListResource(data)) {
								obj.data("data", data.ListResource.resourceList.Datas);
							}
						} else {
							pdata = obj.data('data');
							var plist = pdata;
							// var data = $.extend(true,pdata,data);
							if (checkListResource(data)) {
								obj.data('data', pdata);
							} else {
								data.ListResource.resourceList.Datas = plist
										.concat(data.ListResource.resourceList.Datas);
								obj.data('data',
										data.ListResource.resourceList.Datas);
							}
						}
						if (!checkList(obj.data('data'))) {
							info.count = obj.data('data').length;
						} else {
							info.count = 0;
						}
						info.total = data.ListResource.number;
						obj.attr('info', $.json2str(info));
						filltab(obj);
					},
					Action : {
						"currentHtml" : "otherHome.html",
						"requestPath" : "get_user_game_list",
						"requestParams" : url.getParamsStr()
								+ "&start="
								+ info.count
								+ "&end="
								+ (parseInt(info.count) + parseInt(def.pagecount)),
						"requestMethod" : "get",
						"needCallback" : "true",
						"callbackInterface" : "jQuery.actionCallback",
						"callbackParamsType" : "json",
						"needShowNativeLoading" : "true"
					}
				});
	} else if (info.board == 3) {
		$.action({
					callback : function(data) {
						if (obj.data('data') == undefined
								|| obj.data("data") == "") {
//									如果data合法
							if(!checkListResource(data)) {
								obj.data("data", data.ListResource.resourceList.Datas);
							}
						} else {
							pdata = obj.data('data');
							var plist = pdata;
//							如果data不合法
							if (checkListResource(data)) {
								obj.data('data', pdata);
							} else {
								data.ListResource.resourceList.Datas = plist
										.concat(data.ListResource.resourceList.Datas);
								obj.data('data',
										data.ListResource.resourceList.Datas);
							}
						}
//						如果data合法
						if (!checkList(obj.data('data'))) {
							info.count = obj.data('data').length;
						} else {
							info.count = 0;
						}
						info.total = data.ListResource.number;
						obj.attr('info', $.json2str(info));
						filltab(obj);
					},
					Action : {
						"currentHtml" : "otherHome.html",
						"requestPath" : "get_user_friend_list",
						"requestParams" : url.getParamsStr()
								+ "&start="
								+ info.count
								+ "&end="
								+ (parseInt(info.count) + parseInt(def.pagecount)),
						"requestMethod" : "get",
						"needCallback" : "true",
						"callbackInterface" : "jQuery.actionCallback",
						"callbackParamsType" : "json",
						"needShowNativeLoading" : "true"
					}
				});
	}
}

function callSetInfo() {
	var p = generateParams();
	android.onLoadHtml("setinfo.html" + "?" + p);
}

function generateParams() {
	return "userId=" + userCache.id + "&" + "username="
			+ encodeURIComponent(userCache.name) + "&" + "sex=" + userCache.sex
			+ "&" + "icon=" + userCache.icon;
}

function callState() {
	android.onLoadHtml("state.html" + "?" + "signature="
			+ encodeURIComponent($("#signature").attr("value")) + "&"
			+ url.getParamsStr());
}

function loadMessageHome() {
	android.onLoadHtml("message.html" + "?" + params);
}

function unlockedCall() {

}

function fillUserCache(user) {
	userCache.icon = user.profilePictureUrl;
	userCache.name = user.name;
	userCache.sex = user.sex;
	userCache.id = user.id;
}

function checkListResource(data) {
	return data == undefined || data.ListResource == undefined
			|| data.ListResource.resourceList == undefined
			|| data.ListResource.resourceList.Datas == undefined;
}

function checkList(data) {
	return data == undefined;
}

function goToFeedDetail(id) {
	var list = $($("#tab1")).data("data");

	var data;
	for (var index = 0; index < list.length; index++) {
		if (list[index].InternalFeed.id == id) {
			// json2str 没有对引号和反斜杠处理
			// 先对反斜杠进行处理
//			list[index].InternalFeed.content = list[index].InternalFeed.content
//					.replace(/\\/gi, "\\\\");
//			list[index].InternalFeed.content = list[index].InternalFeed.content
//					.replace(/\"/gi, "\\\"");
			data = $.json2str(list[index].InternalFeed);
		}
	}
	android.onLoadHtml("feedDetail.html?" + "gameVersionName="
			+ url.getParam("gameVersionName") + "&version=" 
			+ url.getParam("version") + "&userId=" + url.getParam("userId") + "&feedId=" + id + "&data="
			+ encodeURIComponent(data) + "&from=2");
}

function goToFeedBack() {
	android.onLoadHtml("feedback.html?" + url.getParamsStr());
}

function getpercent(percent) {
	document.getElementById("part").style.width = percent;
}

$(document).ready(function() {
	imgcache=preimg();
	opscroll('content1',{onPullUp:function(){loadMore($("#tab1").get(0))},bottom:'0px'}).disableMore();
	opscroll('content2',{onPullUp:function(){loadMore($("#tab2").get(0))},bottom:'0px'}).disableMore();
	opscroll('content3',{onPullUp:function(){loadMore($("#tab3").get(0))},bottom:'0px'}).disableMore();

	
	$.action({
		callback : function(data) {
			var user = data.ProfileUser;
			fillUserCache(user);
//			$("#icon").attr("src", user.profilePictureUrl);
			global_get_image("myIcon", user.profilePictureUrl);
			otherIcon = user.profilePictureUrl;
			$("#name").html(user.name);
			username = user.name;
			$("#achievement").html(user.achievement);
			$("#level").html(user.level);
			
			var messageNum = parseInt(user.msgNum);
					if (messageNum <= 0) {
						messageNum = 0;
					}
			$("#messageNum").html(messageNum);
			
			$("#signature").html(user.signature.trim());
			$("#gameName").html(user.gameNum);
			$("#friendNum").html(user.friendsNum);
			var currentLevelExperience = parseInt(user.currentLevelExperience);
			var experience = parseInt(user.experience);
			var nextLevelExperience = parseInt(user.nextLevelExperience);
			var percent = (experience - currentLevelExperience) / (nextLevelExperience - currentLevelExperience);
			getpercent(percent*100 + "%");
			if (user.isFriend) {
				isFriend = true;
				$("#friendTag").html("");
			} else {
				isFriend = false;
				$("#friendTag").addClass("mypage_head_friend").html(nearmelang.lang_friend_addfriend);
			}
			var sexImage;
			if (user.sex) {
				sexImage = nearmelang.image.userhome_sex_boy;
			} else {
				sexImage = nearmelang.image.userhome_sex_girl;
			}
			$("#sex_icon").attr("src", sexImage);
			// 缓存tab1
			var info = $.parseJSON($($("#tab1")).attr("info"));
			if (user.feeds.InternalFeeds !== undefined) {
				info.count = 20;
				tab1Adding = true;
			} else {
				info.count = 0;
			}
			info.total = user.feedNum;
			$($("#tab1")).attr("info", $.json2str(info));
			
//					info = $.parseJSON($($("#tab1")).attr("info"));
//					alert(info.count + " " + info.total + " "  + info.board);
			
			var info = $.parseJSON($($("#tab1")).attr("info"));
			$($("#tab1")).data("data", user.feeds.InternalFeeds);
			filltab($($("#tab1")));
			var curtab=url.getParam("currentTab");
			try{
				getScrollById('content'+curtab).reDraw(true);
			}catch(e){
			}
				
			if (url.getParam("currentTab") == "1") {
				showTab($("#tab1"));
			} else if (url.getParam("currentTab") == "2") {
				showTab($("#tab2"));
			} else if (url.getParam("currentTab") == "3") {
				showTab($("#tab3"));
			} else {
				showTab($("#tab1"));
			}
		},
		Action : {
			"currentHtml" : "otherHome.html",
			"requestPath" : "get_user_info",
			"requestParams" : url.getParamsStr(),
			"requestMethod" : "get",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true"
		}
	});
});

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
	goToOtherGame(id,url,enter_game_type.friendgame);
}

function goToPage(id, type) {
	if(checkFeedCanGoToGame(type)) {
		goToGame(id);
	} else {
		gotoMatch(id);
	}
}

function gotoMatch(id){
	android.onLoadHtml("matchdetail.html?"+url.setParam("contestId",id).getParamsStr());
}

function goToDownload(id) {
	var game_result = {
		installedVersion : "null",
		gamePackageName : marketCache[id].gamePackageName,
		code : "1001"
	};
	game_result.Markets = marketCache[id].markets;
	android.onGameOpenOrBuy($.json2str(game_result));
}

function goToSend() {
	android.onLoadHtml("sendletter.html?" + "gameVersionName=" + url.getParam("gameVersionName") + "&version=" + url.getParam("version") + "&userId=" + url.getParam("userId") + "&receiverName=" + username + "&receiverId=" + url.getParam("requestedUserId"));
}