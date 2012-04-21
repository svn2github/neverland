var url = oprequest();
var TAG = "message_action";
var imgcache;
var hasHead1 = false;
var hasHead2 = false;

var reg = global_reg;

$(document).ready(function() {
			imgcache = preimg();
			opscroll('content1', {
						onPullUp : function() {
							loadMore($("#tab1").get(0))
						},
						bottom : '85px'
					}).disableMore();
			opscroll('content2', {
						onPullUp : function() {
							loadMore($("#tab2").get(0))
						},
						bottom : '0px'
					}).disableMore();
			opscroll('content3', {
						onPullUp : function() {
							loadMore($("#tab3").get(0))
						},
						bottom : '0px'
					}).disableMore();
			if (url.getParam("currentTab") == "1") {
				showTab($("#tab1"));
			} else if (url.getParam("currentTab") == "2") {
				showTab($("#tab2"));
			} else if (url.getParam("currentTab") == "3") {
				showTab($("#tab3"));
			} else {
				showTab($("#tab1"));
			}
		});

function deletePrivate(id, name) {
	opalert({
				title : nearmelang.lang_message_from + name,
				content : nearmelang.lang_message_deleteall,
				mode : opalert.BTN_BOTH,
				onok : function() {
					$.action({
								callback : function(data) {
									$("#content1").html("");
									obj = $("#tab1");
									var info = $.parseJSON(obj.attr('info'));
									obj.data('data', "");
									info.count = 0;
									info.total = 0;
									obj.attr('info', $.json2str(info));
									showTab(obj);
								},
								Action : {
									"currentHtml" : "message.html",
									"requestPath" : "delete_all_msg_by_chatter",
									"requestParams" : oprequest().setParam(
											"chatterId", id).getParamsStr(),
									"requestMethod" : "get",
									"needCallback" : "true",
									"callbackInterface" : "jQuery.actionCallback",
									"callbackParamsType" : "json",
									"needShowNativeLoading" : "false"
								}
							});
				}
			});
}

function deleteNotice(id) {
	opalert({
				content : nearmelang.lang_global_deleteornot,
				mode : opalert.BTN_BOTH,
				onok : function() {
					$.action({
								callback : function(data) {
									$("#content2_1").html("");
									$("#content2_2").html("");
									hasHead1 = false;
									hasHead2 = false;
									obj = $("#tab2");
									obj.data('data', "");
									var info = $.parseJSON(obj.attr('info'));
									info.count = 0;
									info.total = 0;
									obj.attr('info', $.json2str(info));
									showTab(obj);
								},
								Action : {
									"currentHtml" : "message.html",
									"requestPath" : "delete_the_msg",
									"requestParams" : oprequest().setParam(
											"messageId", id).setParam("messageType", "2").getParamsStr(),
									"requestMethod" : "get",
									"needCallback" : "true",
									"callbackInterface" : "jQuery.actionCallback",
									"callbackParamsType" : "json",
									"needShowNativeLoading" : "false"
								}
							});
				}
			});
}

function showTab(obj) {
	var info = $.parseJSON($(obj).attr('info'));
	$("#tab_list img").css("display", "none");
	if (info.board == 1) {
		android.onChangeTab("1");
		$("#tab1 img").css("display", "block");
		$("#tab1").attr("class", "li_click");
		$("#tab3").attr("class", "li_unclick");
		$("#tab2").attr("class", "li_unclick");
		$("#part1").css("display", "block");
		$("#part2").css("display", "none");
		$("#part3").css("display", "none");
		getScrollById('content1').show();
		getScrollById('content2').hide();
		getScrollById('content3').hide();
	} else if (info.board == 2) {
		android.onChangeTab("2");
		$("#tab2 img").css("display", "block");
		$("#tab1").attr("class", "li_unclick");
		$("#tab2").attr("class", "li_click");
		$("#tab3").attr("class", "li_unclick");
		$("#part1").css("display", "none");
		$("#part2").css("display", "block");
		$("#part3").css("display", "none");
		getScrollById('content1').hide();
		getScrollById('content2').show();
		getScrollById('content3').hide();
	} else if (info.board == 3) {
		android.onChangeTab("3");
		$("#tab3 img").css("display", "block");
		$("#tab1").attr("class", "li_unclick");
		$("#tab2").attr("class", "li_unclick");
		$("#tab3").attr("class", "li_click");
		$("#part1").css("display", "none");
		$("#part2").css("display", "none");
		$("#part3").css("display", "block");
		getScrollById('content1').hide();
		getScrollById('content2').hide();
		getScrollById('content3').show();
	}

	triggerMore(info);

	if (info.count <= 0) {
		loadMore(obj);
	}
}

function filltab(obj) {
	var data = obj.data('data');
	var info = $.parseJSON(obj.attr('info'));
	if (data == undefined || data == "" || data.length == 0) {

		if (info.board == 1) {
			$("#content1").html(nearmelang.ui_prompt_div.replace(/#/gi,
					nearmelang.lang_message_noletter));
			getScrollById('content1').disableMore().reDraw();
		} else if (info.board == 2) {

			$("#content2").html(nearmelang.ui_prompt_div.replace(/#/gi,
					nearmelang.lang_message_nonotice));
			getScrollById('content2').disableMore().reDraw();

		} else if (info.board == 3) {
			$("#content3").html(nearmelang.ui_prompt_div.replace(/#/gi,
					nearmelang.lang_message_nosysmessage));
			getScrollById('content3').disableMore().reDraw();
		}

		console.log("filltab in message_action tab" + info.board
				+ ", data undefined or 0");
		triggerMore(info);
		return;
	}
	if (info.board == 1) {
		for (var i in data) {
			data[i].MessageUser.imgid = 'imgcache_' + info.board + info.count
					+ (i + 1);
			imgcache.add(data[i].MessageUser.imgid,
					data[i].MessageUser.profilePictureUrl);
//			if (data[i].MessageUser.unReadMsgNum != 0) {
//				data[i].MessageUser.name = global_getShortName(data[i].MessageUser.name)
//						+ nearmelang.lang_message_unread1;
//			}
		}
		var tabContent = TrimPath.processDOMTemplate("tpl_private", {
					"data" : data
				});
		$("#content1").append(tabContent);
		if (info.count < info.total) {
			getScrollById('content1').enableMore().reDraw();
		} else {
			getScrollById('content1').disableMore().reDraw();
		}
		imgcache.start();
	} else if (info.board == 2) {
		var list = data;
		var tabContent1 = "";
		var tabContent2 = "";
		var countNum = 0;
		for (var index = 0; index < list.length; index++) {
			var node = list[index].Notice;
			var result = "";
			var temp = [];
			var nodes = [];
			var hasGame = false;
			var content = node.content
			var feedOwn;
			var imageUrl;
			temp = content.match(reg);
			if (temp == undefined) {
				console.log("temp undefined in " + TAG);
				continue;
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
					console.log("imageUrl undefined message_action ");
					continue;
				}
				if (hasGame) {
					result = TrimPath.processDOMTemplate("tpl_notice_game", {
								"node" : node,
								"content" : content,
								"updateTime" : getTimeString(Date
												.parse(new Date()),
										parseInt(node.createTime)),
								"icon" : imageUrl,
								"userId" : feedOwn
							});
				} else {
					result = TrimPath.processDOMTemplate("tpl_notice_nogame", {
								"node" : node,
								"content" : content,
								"updateTime" : getTimeString(Date
												.parse(new Date()),
										parseInt(node.createTime)),
								"icon" : imageUrl,
								"userId" : feedOwn
							});
				}
				if (node.isRead == false) {
					tabContent1 = tabContent1 + result;
					hasHead1 = true;
				} else if (node.isRead == true) {
					tabContent2 = tabContent2 + result;
					hasHead2 = true;
				}
			} catch (e) {
				console.log("exception from message_action.js : " + e);
				continue;
			}
		}
		$("#content2_1").html(tabContent1);
		$("#content2_2").html(tabContent2);
		if (info.count < info.total) {
			getScrollById('content2').enableMore().reDraw();
		} else {
			getScrollById('content2').disableMore().reDraw();
		}
		triggerHasHead();
		imgcache.start();
	} else if (info.board == 3) {
		var list = data;
		var tabContent = "";
		for (var index = 0; index < list.length; index++) {
			var result = TrimPath.processDOMTemplate("tpl_system_message", {
						"node" : list[index],
						"time" : getTimeString(Date.parse(new Date()),
								parseInt(list[index].SystemMessage.createTime))
					});
			tabContent = tabContent + result;
		}
		$("#content3").html(tabContent);
		if (info.count < info.total) {
			getScrollById('content3').enableMore().reDraw();
		} else {
			getScrollById('content3').disableMore().reDraw();
		}
	}
}
function triggerHasHead() {
	if (hasHead1) {
		$("#content2_1head").css("display", "block");
	} else {
		$("#content2_1head").css("display", "none");
	}
	if (hasHead2) {
		$("#content2_2head").css("display", "block");
	} else {
		$("#content2_2head").css("display", "none");
	}
}
function unlockedCall() {

}

/**
 * node : $("#id") data : ListResource
 * 
 * @param {}
 *            node
 * @param {}
 *            data
 */

function saveDataAndInfo(node, data) {
	obj = $(node);
	var info = $.parseJSON(obj.attr('info'));
	$(node).data('data', data.ListResource.resourceList.Datas)
	info.count = obj.data('data').length;
	info.total = parseInt(data.ListResource.number);
	obj.attr('info', $.json2str(info));
}

function loadMore(obj) {
	obj = $(obj);
	var info = $.parseJSON(obj.attr('info'));
	if (info.board == 1) {
		$.action({
			callback : function(data) {
				if (obj.data('data') == undefined || obj.data("data") == "") {
					if (!checkListResource(data)) {
						obj.data('data', data.ListResource.resourceList.Datas);
					}
				} else {
					pdata = obj.data('data');
					var plist = pdata;
					// var data = $.extend(true,pdata,data);
					if (checkListResource(data)) {
						obj.data('data', pdata);
					} else {
						if (!info.addtype || info.addtype != true) {
							data.ListResource.resourceList.Datas = plist
									.concat(data.ListResource.resourceList.Datas);
						}
						obj.data('data', data.ListResource.resourceList.Datas);
					}
				}
				if (obj.data('data')) {
					info.count = (!info.addtype || info.addtype != true)
							? 0
							: info.count + obj.data('data').length;
				} else {
					info.count = 0;
				}
				info.total = parseInt(data.ListResource.number);
				obj.attr('info', $.json2str(info));
				filltab(obj);
			},
			Action : {
				"currentHtml" : "message.html",
				"requestPath" : "get_chatter_list",
				"requestParams" : url.getParamsStr() + "&start=" + info.count
						+ "&end=" + (info.count + nearmelang.config_page_count),
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
				if (obj.data('data') == undefined || obj.data("data") == "") {
					if (!checkListResource(data)) {
						obj.data('data', data.ListResource.resourceList.Datas);
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
						obj.data('data', data.ListResource.resourceList.Datas);
					}
				}
				if (obj.data('data')) {
					info.count = obj.data('data').length;
				} else {
					info.count = 0;
				}
				info.total = parseInt(data.ListResource.number);
				obj.attr('info', $.json2str(info));
				filltab(obj);
			},
			Action : {
				"currentHtml" : "message.html",
				"requestPath" : "get_notice_list",
				"requestParams" : url.getParamsStr()
						+ "&start="
						+ parseInt(info.count)
						+ "&end="
						+ (parseInt(info.count) + parseInt(nearmelang.config_page_count)),
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
				if (obj.data('data') == undefined || obj.data("data") == "") {
					if (!checkListResource(data)) {
						obj.data('data', data.ListResource.resourceList.Datas);
					} else {
						return;
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
						obj.data('data', data.ListResource.resourceList.Datas);
					}
				}
				if (obj.data('data')) {
					info.count = obj.data('data').length;
				} else {
					info.count = 0;
				}
				info.total = parseInt(data.ListResource.number);
				obj.attr('info', $.json2str(info));
				filltab(obj);
			},
			Action : {
				"currentHtml" : "message.html",
				"requestPath" : "get_system_message_list",
				"requestParams" : url.getParamsStr()
						+ "&start="
						+ parseInt(info.count)
						+ "&end="
						+ (parseInt(info.count) + parseInt(nearmelang.config_page_count)),
				"requestMethod" : "get",
				"needCallback" : "true",
				"callbackInterface" : "jQuery.actionCallback",
				"callbackParamsType" : "json",
				"needShowNativeLoading" : "true"
			}
		});

	}
}

function triggerMore(info) {
	// console.log($.json2str(info));
	if (parseInt(info.count) < parseInt(info.total)) {
		getScrollById('content' + info.board).enableMore().reDraw();
	} else {
		getScrollById('content' + info.board).disableMore().reDraw();
	}
}

function goToNewLetter() {
	android.onLoadHtml("newletter.html?" + url.getParamsStr());
}

function goToChats(id, name) {
	android.onLoadHtml("chats.html?" + url.getParamsStr() + "&chatterId=" + id
			+ "&chatterName=" + name);
}

function goToOtherHome(id) {
	android
			.onLoadHtml("otherHome.html?" + "userId=" + url.getParam("userId")
					+ "&requestedUserId=" + id + "&gameVersionName="
					+ url.getParam("gameVersionName") + "&version="
					+ url.getParam("version") + "&from="
					+ add_friend_type.invitetohome);
}

function goToPage(id, attachContent, messageType, originId) {
	// alert(id+","+attachContent+","+messageType+","+originId);
	$.action({
				callback : function(data) {
					console.log("set notice read callback : " + data);
				},
				Action : {
					"currentHtml" : "message.html",
					"requestPath" : "set_notice_read",
					"requestParams" : "gameVersionName="
							+ url.getParam("gameVersionName") + "&version="
							+ url.getParam("version") + "&messageId=" + id,
					"requestMethod" : "get",
					"needCallback" : "true",
					"callbackInterface" : "jQuery.actionCallback",
					"callbackParamsType" : "json",
					"needShowNativeLoading" : "false"
				}
			});
	var tag = parseInt(messageType);
	if (tag == 1) {
		goToGame(originId, enter_game_type.noticeinvite);
	} else if (tag == 0) {
		goFriendHome();
	} else if (tag == 2) {
		// leave message
		goToFeedDetail(attachContent);
	} else if (tag == 3) {
		// comment
		goToFeedDetail(attachContent);
	} else if (tag == 4) {
		// reply
		goToFeedDetail(attachContent);
	} else if (tag == 5) {
		gotoMatch(originId);
		// contest
	}
}

function gotoMatch(id) {
	android.onLoadHtml("matchdetail.html?"
			+ url.setParam("contestId", id).getParamsStr());
}

function goToGame(id, from) {
	goToOtherGame(id, url, from);
}

function goToFeedDetail(id) {
	android.onLoadHtml("feedDetail.html?" + "gameVersionName="
			+ url.getParam("gameVersionName") + "&version="
			+ url.getParam("version") + "&userId=" + url.getParam("userId")
			+ "&feedId=" + id);
}

function goFriendHome() {
	android.onLoadHtml("friend.html?" + "needInfo=true"
			+ "&notRemoveTabInfo=true" + "&currentTab=2");
}