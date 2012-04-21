url = oprequest();
var game;
$(document).ready(function (){
	opscroll('part2',{top:'255px',bottom:'0px'}).hide().disableMore();
	var part1=$("#part1"),part2=$("#part2");
	var li=$(".tab2_new>li");
	var li1=li.eq(0),li2=li.eq(1);
	li1.click(function(){
		android.onChangeTab("1");
		$("#part1").css("display", "block");
		$("#part2").css("display", "none");	
		$("#tab1").attr("class","li_click")
		$("#tab2").removeClass("li_click");
		$(".img_ul").attr("src","images/hdpi/tab2_left_click.png");
		$(".img_li").css("left","52%");
		getScrollById("part2").hide();
		loadMatch(li1.get(0));
	})
	li2.click(function(){
		android.onChangeTab("2");
		$("#part1").css("display", "none");
		$("#part2").css("display", "block");		
		$("#tab2").attr("class","li_click")
		$("#tab1").removeClass("li_click");
		$(".img_ul").attr("src","images/hdpi/tab2_right_click.png");
		$(".img_li").css("left","4%");
		getScrollById("part2").show();
		loadRanking(li2.get(0));
	})
	part2.hide();
	if (url.getParam("currentTab") == "1") {
		li1.trigger('click');
	} else if (url.getParam("currentTab") == "2") {
		li1.trigger('click');
		li2.trigger('click');
	} else {
		li1.trigger('click');
	}
});

function loadMatch(obj){
	var opt = {
		callback : function(data) {
			if (data == undefined) {
				console.log("return error json data to html");
				return;
			}
			data=data.Contest;
			$("#matchName").html(data.name);
			game=data.game.Game;
			$("#game_name").html(game.gameName);
			$("#game_icon").attr('src',game.gameIcon).unbind('click'
			).bind('click',function(){
				goToGame(game.id);
			});
			
//			check price for game
			var price = game.price;
			var bounds = game.bounds;
			var priceContent ="";
			if (price > 0) {
				priceContent = price + nearmelang.lang_global_yuan;
			} else if (bounds > 0) {
				priceContent = bounds + nearmelang.lang_global_bounds;
			} else {
				priceContent = nearmelang.lang_gamehome_free;
			}
			$("#game_price").html(priceContent);
			
			if(getGameInstallpath(game.gamePackageName)){
				$("#match_download").html(nearmelang.lang_match_downloadgame).unbind('click').bind('click',function(){downloadGame(game.gamePackageName)});
			}else{
				$("#match_download").html(nearmelang.lang_match_opengame).unbind('click').bind('click',function(){openGame(game.gamePackageName)});
			}
//			call android for check open or update
			android.onGetGameVersion(game.gamePackageName, game.gameVersionName, "onGetGameCallback", "hehe");
			
			var conteststate = 0;
			if(data.currentTime<data.startTime){
				conteststate=0;
				data.leftTime = getTimeLeft(Math.floor((data.startTime-data.currentTime)/1000,'m'));
				$("#match_state").html(nearmelang.lang_match_stat_notstart);
				$("#lang_match_fighting").html(nearmelang.lang_match_fighting1);
			}else if( data.currentTime>=data.startTime && data.currentTime<=data.endTime ){
				conteststate=1;
				data.leftTime = getTimeLeft(Math.floor((data.endTime-data.currentTime)/1000),'m');
				$("#match_state").html(nearmelang.lang_match_stat_inprogress);
				$("#lang_match_fighting").html(nearmelang.lang_match_fighting1);
			}else{
				conteststate=-1;
				data.leftTime = '';
				$("#match_state").html(nearmelang.lang_match_stat_ended);
				$("#lang_match_fighting").html(nearmelang.lang_match_fighting2);
			}
			$(".disclaimer>a").attr('href',data.declarationUrl);
			data.timerand=getTimeRand(data.startTime,data.endTime);
			data.contestInfoarr=nodeToJson(data.contestInfo);
			data.conteststate=conteststate;
			var tpl = TrimPath.parseDOMTemplate("tpl_personspart");
			$("#personspart").html(tpl.process({data:data}));
		},
		Action : {
			"currentHtml" : "matchdetail.html",
			"requestPath" : "get_contest_detail",
			"requestParams" : url.setParam("t",(new Date()).getTime()).getParamsStr(),
			"requestMethod" : "get",
			"needCallback" : true,
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : true
		}
	};
	jQuery.action(opt);
	$(".refresh").unbind('touchend').bind('touchend',function(){
		loadMatch(obj);
	});
}


var game_code;
var game_result;
function onGetGameCallback(data) {
 	game_result = $.parseJSON(data);
	game_code = parseInt(game_result.code);
	switch(game_code) {
		case game_result_code.notinstall : 
			var price = parseFloat(game.price);
			var bounds = parseFloat(game.bounds);
			var content ;
//			if (price > 0) {
//				content = nearmelang.lang_gamehome_download + "(" + price + nearmelang.lang_global_yuan + ")";
//			} else if (bounds > 0) {
//				content = nearmelang.lang_gamehome_download + "(" + bounds + nearmelang.lang_global_bounds + ")";
//			} else {
//				content = nearmelang.lang_gamehome_free;
//			}
			
			$("#buyType").html(nearmelang.lang_gamehome_download);
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
	game_result.Markets = game.marketList.Markets;
	android.onGameOpenOrBuy($.json2str(game_result));
}

function loadRanking(obj){
	var opt = {
		callback : function(data) {
			if (data == undefined) {
				console.log("return error json data to html");
				return;
			}
			if(data.ListResource.number==0){
				data=[];
			}else{
				data=data.ListResource.resourceList.Datas;
			}
			var tpl = TrimPath.parseDOMTemplate("tpl_rankpart");
			$("#part2").html(tpl.process({data:data}));
			getScrollById('part2').show();
		},
		Action : {
			"currentHtml" : "matchdetail.html",
			"requestPath" : "get_contest_leaderboard",
			"requestParams" : url.setParam("t",(new Date()).getTime()).getParamsStr(),
			"requestMethod" : "get",
			"needCallback" : true,
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : true
		}
	};
	jQuery.action(opt);
	$(".refresh").unbind('touchend').bind('touchend',function(){
		loadRanking(obj);
	});
}

function goToOtherHome(id) {
	if(url.getParam("visitor")=="true"){
		opalert({
			content:nearmelang.lang_global_youneedloginforfun,
			mode:opalert.BTN_BOTH,
			onok:function(){
				android.onGoToLogin();
			},
			oktext:nearmelang.lang_global_login
		});
		return;
	}
	android.onLoadHtml("otherHome.html?" + url.getParamsStr() + "&requestedUserId=" + id);
}

function subscribeContent(obj,gameid){
	if(url.getParam("visitor")=="true"){
		opalert({
			content:nearmelang.lang_global_youneedloginforfun,
			mode:opalert.BTN_BOTH,
			onok:function(){
				android.onGoToLogin();
			},
			oktext:nearmelang.lang_global_login
		});
		return;
	}
	var that = $(obj);
	var opt = {
		callback : function(data) {
			if (data == undefined) {
				console.log("return error json data to html");
				return;
			}
			if(data.RequestResult&&data.RequestResult.resultCode&&data.RequestResult.resultCode==1001){
				that.parent(".match_subscibe").html(nearmelang.lang_match_subscribe).removeClass('match_subscibe').addClass('match_message');
			}else{
				opalert(nearmelang.lang_match_subscribeerr);
			}
		},
		Action : {
			"currentHtml" : "matchdetail.html",
			"requestPath" : "post_contest_subscribe",
			"requestParams" : oprequest(url.getUrl()).setParam("gameId",gameid).getParamsStr(),
			"requestMethod" : "get",
			"needCallback" : true,
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : true
		}
	};
	jQuery.action(opt);
}

function signContest(obj,couldSign){
	if(couldSign==false){
		opalert({
			content:nearmelang.lang_match_signup_full,
			mode:opalert.BTN_OK
		});
		return;
	}
	if(url.getParam("visitor")=="true"){
		opalert({
			content:nearmelang.lang_global_youneedloginforfun,
			mode:opalert.BTN_BOTH,
			onok:function(){
				android.onGoToLogin();
			},
			oktext:nearmelang.lang_global_login
		});
		return;
	}
	var that = $(obj);
	var opt = {
		callback : function(data) {
			if (data == undefined) {
				console.log("return error json data to html");
				return;
			}
			if(data.RequestResult&&data.RequestResult.resultCode&&data.RequestResult.resultCode==1001){
				$("#match_competitorNum").html(parseInt($("#match_competitorNum").text())+1);
				that.replaceWith($('<div class="match_message">'+nearmelang.lang_match_signup_suc+'</div>'));
			}else if(data.RequestResult&&data.RequestResult.resultCode&&data.RequestResult.resultCode==5012){
				opalert(nearmelang.lang_match_signup_limit);
			} else {
				opalert(nearmelang.lang_match_signup_err);
			}
		}, 
		Action : {
			"currentHtml" : "matchdetail.html",
			"requestPath" : "post_sign_up",
			"requestParams" : url.getParamsStr(),
			"requestMethod" : "get",
			"needCallback" : true,
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : true
		}
	};
	jQuery.action(opt);
}

function unlockedCall () {
	
}

function nodeToJson(str) {
	var arr = str.replace(/>\s+</gi,'').replace(/\/><describe/gi,"/>\n<describe").split("\n");
	var nodes=[];
	for(var i=0;i<arr.length;i++){
		var node = $.parseJSON(arr[i].replace(/<([^\s]*)\s/, "{\"$1\":{ ").replace(/\s\/>$/, " }}")
			.replace(/(\s([\w]+)=(".*?"))/gi, "\"$2\":$3,")
			.replace(/, }/, "}"));
		nodes.push( node );
	}
	return nodes;
}

function goToGame(id) {
//	android.onStatistics("1000007");
//	android.onLoadHtml("othergame.html?" + "userId=" + url.getParam("userId") + "&gameId=" + id + "&gameVersionName=" + "1.0.0");
	goToOtherGame(id, url, enter_game_type.contest);
}

