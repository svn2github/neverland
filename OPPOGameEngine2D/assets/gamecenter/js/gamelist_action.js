var url = oprequest();
var cache1;
var isVisitor = false;
var imgcache;
function checkListResource(data) {
	return data == undefined || data.ListResource == undefined
			|| data.ListResource.resourceList == undefined
			|| data.ListResource.resourceList.Datas == undefined;
}
$(document).ready(function() {
			if (url.getParam("visitor") == "true") {
				isVisitor = true;
			}
			imgcache = preimg();
			opscroll('part1', {
						top : '216px',
						bottom : '60px'
					});
			opscroll('content2', {
						bottom : '60px'
					});
			opscroll('content3', {
						onPullUp : function() {
							loadMore($("#tab3"))
						},
						bottom : '60px'
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

			$.action({
						callback : function(data) {
							if (data == undefined) {
								console
										.log("data undefined in gamelist action ");
								return;
							}
							$("#tab1").data("data", data);
							filltab($("#tab1"));
						},
						Action : {
							"currentHtml" : "gamelist.html",
							"requestPath" : "get_recommended_game_list",
							"requestParams" : "",
							"requestMethod" : "get",
							"needCallback" : "true",
							"callbackInterface" : "jQuery.actionCallback",
							"callbackParamsType" : "json",
							"needShowNativeLoading" : "true"
						}
					});
		});

function showTab(obj) {
	var info = $.parseJSON($(obj).attr('info'));
	$("#tab_3 img").css("display", "none");
	if (info.board == 1) {
		android.onChangeTab("1");
		$("#tab1 img").css("display", "block");
		$("#tab1").attr("class", "li_click");
		$("#tab3").attr("class", "li_unclick");
		$("#tab2").attr("class", "li_unclick")
		$("#part1").css("display", "block");
		$("#part2").css("display", "none");
		$("#part3").css("display", "none");
		getScrollById('part1').show();
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
		getScrollById('part1').hide();
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
		getScrollById('part1').hide();
		getScrollById('content2').hide();
		getScrollById('content3').show();
	}
	if (info.board == 1) {

	} else {
		if (info.count <= 0) {
			loadMore($(obj));
		}
	}
}

function filltab(obj) {
	var data = obj.data('data');
	var info = $.parseJSON(obj.attr('info'));
	if (info.board == 1) {
		var data1;
		var data2;
		if (parseInt(data.FirstPage.commonPrompts.ListResource.number) != 0) {
			data1 = data.FirstPage.commonPrompts.ListResource.resourceList.Datas;
		}
		if (parseInt(data.FirstPage.specialPrompts.ListResource.number) != 0) {
			data2 = data.FirstPage.specialPrompts.ListResource.resourceList.Datas;
		}
		var content1;
		if (data1 != undefined) {
			cache1 = data1;
			for (var i in data1) {
				data1[i].PromptObject.imgid = 'imgcache_' + info.board
						+ info.count + (i + 1);
				imgcache.add(data1[i].PromptObject.imgid, data1[i].PromptObject.promptAdImage);
			}
			var tabContent = TrimPath.processDOMTemplate("tpl_prompt", {
						"data" : data1
					});
			$("#content1").html(tabContent);
			for (var index = 0; index < data1.length; index++) {
				var node = data1[index].PromptObject;
				if (node.promptType == prompt_type.game
						|| node.promptType == prompt_type.contest) {

					if (node.marketList != undefined
							&& node.marketList.Markets != undefined) {
						android.onGetGameVersion(node.promptObjectPackage,
								node.promptObjectVersion, "onGetGameCallback",
								(node.id + node.promptType + ""));
					}
				}
			}
		}
		if (data2 != undefined) {
			for (var j = 0; j < data2.length; j++) {
				var node = data2[j].PromptObject;
				imgid = "special" + (j + 1);
				var obj = $("#" + imgid);
				$.action({
							callback : function(data) {
								$("#" + data.id).attr("src", data.path);
							},
							Action : {
								"currentHtml" : "gamelist.html",
								"requestPath" : "get_image",
								"requestParams" : $.json2str({
											id : imgid,
											url : node.promptAdImage
										}),
								"requestMethod" : "get",
								"needCallback" : "true",
								"callbackInterface" : "jQuery.actionCallback",
								"callbackParamsType" : "path",
								"needShowNativeLoading" : "false"
							}
						});
				var info = node.promptInfo;
				var id = node.id;
				switch (parseInt(node.promptType)) {
					case prompt_type.game :
						var id1 = id;
						obj.bind("click", function() {
									goToGame(id1, enter_game_type.prompt);
								});
						break;
					case prompt_type.contest :
						var id2 = id;
						obj.bind("click", function() {
									gotoMatch(id2);
								});
						break;
					case prompt_type.theme :
						var internalInfo1 = info;
						obj.bind("click", function() {
									gotoTheme(internalInfo1);
								});
						break;
					case prompt_type.evaluation :
						var internalInfo2 = info;
						obj.bind("click", function() {
									gotoEvaluation(internalInfo2);
								});
						break;
				}
			}
		}
		getScrollById('part1').reDraw();

	} else if (info.board == 2) {
		if (data == undefined || data == "" || data.length == 0) {
			console.log("filltab in gamelist_action tab" + info.board
					+ " data undefined or 0");
			return;
		}
		var tabContent = TrimPath.processDOMTemplate("tpl_category", {
					"data" : data
				})
		$("#content2").html(tabContent);
		getScrollById('content2').reDraw();

	} else if (info.board == 3) {
		if (data == undefined || data == "" || data.length == 0) {
			console.log("filltab in gamelist_action tab" + info.board
					+ " data undefined or 0");
			return;
		}
		for (var i in data) {
			data[i].FriendGame.imgid = 'imgcache_' + info.board + info.count
					+ (i + 1);
			imgcache.add(data[i].FriendGame.imgid, getImgBySize(
							data[i].FriendGame.gameIcon, 66));
		}
		var tabContent = TrimPath.processDOMTemplate("tpl_friendgame", {
					"data" : data
				})
		$("#content3").append(tabContent);
		if (info.count < info.total) {
			getScrollById('content3').enableMore().reDraw();
		} else {
			getScrollById('content3').disableMore().reDraw();
		}

	}
	imgcache.start();
}

function loadMore(obj) {
	obj = $(obj);
	var info = $.parseJSON(obj.attr('info'));
	if (info.board == 1) {
		console.log("gamelist tab1 load more!!");
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
						if (!info.addtype || info.addtype != true) {
							data.ListResource.resourceList.Datas = plist
									.concat(data.ListResource.resourceList.Datas);
						}
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
				"currentHtml" : "gamelist.html",
				"requestPath" : "get_category_list",
				"requestParams" : "",
				"requestMethod" : "get",
				"needCallback" : "true",
				"callbackInterface" : "jQuery.actionCallback",
				"callbackParamsType" : "json",
				"needShowNativeLoading" : "true"
			}
		});
	} else if (info.board == 3) {
		if (isVisitor) {
			var data = '<div class="visitor" onclick="goToLogin()"><img src="images/hdpi/gamelist_login.png" /><p class="oppolang">'
					+ nearmelang.lang_global_clickmemorefun + '</p></div>';
			$("#content3").html(data);
			return;
		}
		$.action({
			callback : function(data) {
				if (obj.data('data') == undefined || obj.data("data") == "") {
					if (!checkListResource(data)) {
						obj.data('data', data.ListResource.resourceList.Datas);
					} else {
						$("#content3").html(nearmelang.ui_prompt_div.replace(
								/#/gi, nearmelang.lang_gamelist_nofriendgame));
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
					info.count = ((!info.addtype || info.addtype != true)
							? 0
							: info.count)
							+ obj.data('data').length;
				} else {
					info.count = 0;
				}
				info.total = parseInt(data.ListResource.number);
				obj.attr('info', $.json2str(info));
				filltab(obj);
			},
			Action : {
				"currentHtml" : "gamelist.html",
				"requestPath" : "get_friends_game_list",
				"requestParams" : "userId=" + url.getParam("userId")
						+ "&start=" + info.count + "&end="
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

function unlockedCall() {

}

function goToPage(id, type) {
	var pType = parseInt(type);
	var fuckId = id;
	switch (pType) {
		case prompt_type.game :
			android.onStatistics(firstpage_click.game);
			goToGame(fuckId, enter_game_type.prompt);
			break;
		case prompt_type.contest :
			android.onStatistics(firstpage_click.contest);
			gotoMatch(fuckId);
			break;
		case prompt_type.theme :
			android.onStatistics(firstpage_click.theme);
			gotoTheme(fuckId);
			break;
		case prompt_type.evaluation :
			android.onStatistics(firstpage_click.evaluation);
			gotoEvaluation(fuckId);
			break;
	}
}

function goToCategoryList(id, categoryName) {
	android.onLoadHtml("categorylist.html?" + url.getParamsStr()
			+ "&categoryId=" + id + "&categoryName="
			+ encodeURIComponent(categoryName));
}

function gotoMatch(id) {
	android.onLoadHtml("matchdetail.html?"
			+ url.setParam("contestId", id).getParamsStr());
}

function goToGame(id, from) {
	goToOtherGame(id, url, from);
}

function gotoTheme(info) {
	if (info == undefined) {
		android.makeToast("no Theme");
		return;
	}
	android.onLoadUrl(info);
}

function gotoEvaluation(info) {
	if (info == undefined) {
		android.makeToast("no Evaluation");
		return;
	}
	android.onLoadUrl(info);
}

function onGetGameCallback(data) {
	var game_result = $.parseJSON(data);
	var game_code = parseInt(game_result.code);
	for (var index = 0; index < cache1.length; index++) {
		var node = cache1[index].PromptObject;
		if ((node.id + node.promptType + "") == game_result.id) {
			if (node.marketList.Markets != undefined) {
				game_result.Markets = node.marketList.Markets;
				$($("#" + game_result.id)).bind("click", function() {
							android.onGameOpenOrBuy($.json2str(game_result));
						});
				switch (game_code) {
					case game_result_code.notinstall :
						$("#" + game_result.id).html("<p>"
								+ nearmelang.lang_gamelist_downloadgame
								+ "</p>");
						break;
					case game_result_code.open :
						$("#" + game_result.id).html("<p>"
								+ nearmelang.lang_gamelist_opengame + "</p>");
						break;
					case game_result_code.update :
						$("#" + game_result.id).html("<p>"
								+ nearmelang.lang_gamelist_updategame + "</p>");
						break;
				}
			}
		}
	}
}

function goToLogin() {
	android.onGoToLogin();
}
