var url = oprequest();
var userCache = {};
var reg = global_reg;
var gameType = global_feeddetail_gametype;
var imgcache;
var tab1Adding = false;
function checkFeedIsGame(feedType) {
	for (var index = 0; index < gameType.length; index++) {
		if (feedType == gameType[index]) {
			return true;
		}
	}
	return false;
}
var def = {
	'pagecount' : 20
};
function showTab(obj) {
	var info = $.parseJSON($(obj).attr('info'));
	if (info.board == 1) {
		android.onChangeTab("1");
		$("#part1").css("display", "block");
		$("#part2").css("display", "none");	
		$("#tab1").attr("class","li_click")
		$("#tab2").removeClass("li_click");
		$(".img_ul").attr("src","images/hdpi/tab2_left_click.png");
		$(".img_li").css("left","52%");
		getScrollById('content1').show();
		getScrollById('content2').hide();
	} else if (info.board == 2) {
		android.onChangeTab("2");
		$("#part1").css("display", "none");
		$("#part2").css("display", "block");		
		$("#tab2").attr("class","li_click")
		$("#tab1").removeClass("li_click");
		$(".img_ul").attr("src","images/hdpi/tab2_right_click.png");
		$(".img_li").css("left","4%");
		getScrollById('content1').hide();
		getScrollById('content2').show();
	}
}

function goToFeedDetail(id) {
	android.onLoadHtml("feedDetail.html?" + "gameVersionName="
			+ url.getParam("gameVersionName") + "&version="
			+ url.getParam("version") + "&userId=" + url.getParam("userId")
			+ "&feedId=" + id + "&from=1");
}

function filltab(obj) {
	var data = obj.data('data');
	var info = $.parseJSON(obj.attr('info'));
	if (data == undefined || data == "" || data.length == 0) {
		
		if (info.board == 1) {
			$("#content1").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_userhome_nofeed));
		} else if(info.board == 2) {
			$("#content2").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_userhome_nogame));
		}
		
		console.log("filltab in userHome_action tab" + info.board
				+ " data undefined or 0");
		return;
	}
	if (info.board == 1) {
		var list = data;
		var tabContent = "";
		var countNum = 0;
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
					var item = $.parseJSON(nodeToJson(temp[c]));
					nodes.push(item);
					if (item.user != undefined) {
						content = content.replace(temp[c],
								'<span class="friend_name">' + item.user.name
										+ '</span>');
					}

					if (item.contest != undefined) {
						content = content.replace(temp[c],
								'<span class="friend_name">'
										+ item.contest.name + '</span>');
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
						"userId" : feedOwn,
						"imgid":imgid
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
						"userId" : feedOwn,
						"imgid":imgid
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
		var tpl_game_played=TrimPath.parseDOMTemplate("tpl_game_played");
		for (var index = 0; index < list.length; index++) {
			var node = list[index].MyGame
			node.imgid="imgcache_"+info.board+info.count+(index+1);
			imgcache.add(node.imgid,node.gameIcon);
			var result = "";
			try {
				result = tpl_game_played.process({
					"game" : node,
					"time" : getTimeString(Date.parse(new Date()),
							parseInt(node.onSaleTime))
				});
				tabContent = tabContent + result;
			} catch (e) {
				console.log("exception from userHome_action.js : " + e
						+ " content : " + node.content);
				continue;
			}
		}
		$("#content2").html(tabContent);
	}
	if(info.count<info.total){
		getScrollById('content'+info.board).enableMore().reDraw();
	} else {
		getScrollById('content'+info.board).disableMore().reDraw();
	}
	imgcache.start();
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
						
//						if (obj.data('data') == undefined
//								|| obj.data("data") == "") {
//							// obj.data('data', data);
//							if (!checkListResource(data)) {
//								obj.data('data',
//										data.ListResource.resourceList.Datas);
//							}
//						} else {
//							pdata = obj.data('data');
//							var plist = pdata;
//							// var data = $.extend(true,pdata,data);
//							if (data == undefined
//									|| data.ListResource == undefined
//									|| data.ListResource.resourceList == undefined
//									|| !data.ListResource.resourceList.Datas) {
//								obj.data('data', pdata);
//							} else {
//
//								data.ListResource.resourceList.Datas = plist
//										.concat(data.ListResource.resourceList.Datas);
//								obj.data('data',
//										data.ListResource.resourceList.Datas);
//							}
//						}
//						if (obj.data('data')) {
//							info.count = obj.data('data').length;
//						} else {
//							info.count = 0;
//						}
//						
//						obj.attr('info', $.json2str(info));
//						filltab(obj);
					},
					Action : {
						"currentHtml" : "userHome.html",
						"requestPath" : "get_feed_list",
						"requestParams" : url.getParamsStr()
								+ "&start="
								+ info.count
								+ "&end="
								+ (parseInt(info.count) + parseInt(def.pagecount))
								+ "&isMyPage=" + "true",
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
							obj.data('data', data);
						} else {
							pdata = obj.data('data');
							var plist = pdata;
							// var data = $.extend(true,pdata,data);
							if (data == undefined
									|| data.ListResource == undefined
									|| data.ListResource.resourceList == undefined
									|| !data.ListResource.resourceList.Datas) {
								obj.data('data', pdata);
							} else {
								data.ListResource.resourceList.Datas = plist
										.concat(data.ListResource.resourceList.Datas);
								obj.data('data',
										data.ListResource.resourceList.Datas);
							}
						}
						if (obj.data('data')) {
							info.count = obj.data('data').length;
						} else {
							info.count = 0;
						}
						info.total = data.ListResource.number;
						obj.attr('info', $.json2str(info));
						filltab(obj);
					},
					Action : {
						"currentHtml" : "userHome.html",
						"requestPath" : "get_mygame_list",
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
	android.onLoadHtml("setinfo.html" + "?" + url.getParamsStr());
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
	android.onLoadHtml("message.html" + "?" + url.getParamsStr());
}

function unlockedCall() {

}

function fillUserCache(user) {
	userCache.icon = user.profilePictureUrl;
	userCache.name = user.name;
	userCache.sex = user.sex;
	userCache.id = user.id;
}

function getpercent(percent) {
	document.getElementById("part").style.width = percent;
}

$(document).ready(function() {
	imgcache=preimg();
	opscroll('content1',{onPullUp:function(){loadMore($("#tab1").get(0))},top:'247px'}).disableMore();
	opscroll('content2',{onPullUp:function(){loadMore($("#tab2").get(0))},top:'247px'}).disableMore();
	
	if (url.getParam("currentTab") == "1") {
		showTab($("#tab1"));
	} else if (url.getParam("currentTab") == "2") {
		showTab($("#tab2"));
	} 
	
	$.action({
				callback : function(data) {
					var user = data.ProfileUser;
					fillUserCache(user);
//					$("#icon").attr("src", user.profilePictureUrl);
					global_get_image("icon", user.profilePictureUrl);
					$("#name").html(user.name);
					$("#achievement").html(user.achievement);
					$("#level").html(user.level);
					var messageNum = parseInt(user.msgNum);
					if (messageNum <= 0) {
						messageNum = 0;
					}
					$("#messageNum").html(messageNum);
					$("#signature").attr("value", user.signature);
					var currentLevelExperience = parseInt(user.currentLevelExperience);
					var experience = parseInt(user.experience);
					var nextLevelExperience = parseInt(user.nextLevelExperience);
					var percent = (experience - currentLevelExperience) / (nextLevelExperience - currentLevelExperience);
					getpercent(percent*100 + "%");
					if (user.sex) {
						sexImage = nearmelang.image.userhome_sex_boy;
					} else {
						sexImage = nearmelang.image.userhome_sex_girl;
					}
					$("#sex_icon").attr("src", sexImage);
					// 缓存tab1
					var info = $.parseJSON($($("#tab1")).attr("info"));
					if (user.feeds.InternalFeeds !== undefined) {
						tab1Adding = true;
						info.count = 20;
					} else {
						info.count = 0;
					}
					info.total = user.feedNum;
					$($("#tab1")).attr("info", $.json2str(info));
					$($("#tab1")).data("data", user.feeds.InternalFeeds);
					filltab($($("#tab1")));
					var curtab=url.getParam("currentTab");
					try{
						getScrollById('content'+curtab).reDraw(true);
					}catch(e){
					}
				},
				Action : {
					"currentHtml" : "userHome.html",
					"requestPath" : "get_my_info",
					"requestParams" : url.getParamsStr(),
					"requestMethod" : "get",
					"needCallback" : "true",
					"callbackInterface" : "jQuery.actionCallback",
					"callbackParamsType" : "json",
					"needShowNativeLoading" : "true"
				}
			});
	$.action({
				callback : function(data) {
					if (checkListResource(data)) {
						$("#gameNum").html("0");
						return;
					} else {
						$("#gameNum").html(data.ListResource.number);
						// 缓存tab2
						var info = $.parseJSON($($("#tab2")).attr("info"));
						info.count = data.ListResource.resourceList.Datas.length;
						info.total = data.ListResource.number;
						$($("#tab2")).attr("info", $.json2str(info));
						$($("#tab2")).data("data",
								data.ListResource.resourceList.Datas);
						filltab($($("#tab2")));
					}
				},
				Action : {
					"currentHtml" : "userHome.html",
					"requestPath" : "get_mygame_list",
					"requestParams" : url.getParamsStr() + "&start=0&end=20",
					"requestMethod" : "get",
					"needCallback" : "true",
					"callbackInterface" : "jQuery.actionCallback",
					"callbackParamsType" : "json",
					"needShowNativeLoading" : "false"
				}
			});
});

function checkListResource(data) {
	return data == undefined || data.ListResource == undefined
			|| data.ListResource.resourceList == undefined
			|| data.ListResource.resourceList.Datas == undefined;
}

function goToGame(id) {
	goToOtherGame(id, url, enter_game_type.myself);
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

function goToMessageBox() {
	android.onLoadHtml("message.html?" + url.getParamsStr());
}

function goToOtherHome(id) {
	android.onLoadHtml("otherHome.html?" + "userId=" + url.getParam("userId")
			+ "&requestedUserId=" + id + "&gameVersionName="
			+ url.getParam("gameVersionName") + "&version="
			+ url.getParam("version"));
}

function goComment(receiverId, feedId) {
	if (receiverId == undefined || receiverId == "") {
		console.log("receiverId = null: in userhome_action");
		return;
	}
	android.onLoadHtml("comment.html?" + "senderId=" + url.getParam("userId")
			+ "&feedId=" + feedId + "&replyId=" + "0" + "&receiverId="
			+ receiverId + "&gameVersionName="
			+ url.getParam("gameVersionName") + "&version="
			+ url.getParam("version"));
}