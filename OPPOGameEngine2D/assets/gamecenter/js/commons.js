/**
 * 加载语言文件
 */
(function() {
	var script, scripts, _i, _len;
	var language = oprequest().getParam("language");
	if (language == false) {
		scripts = ['commons_lang_sc'];
	} else if (language == "en") {
		scripts = ['commons_lang_en'];
	} else if (language == "sc") {
		scripts = ['commons_lang_sc'];
	} else if (language == "tc") {
		scripts = ['commons_lang_tc'];
	}
	for (_i = 0, _len = scripts.length; _i < _len; _i++) {
		script = scripts[_i];
		document.write(("<script type='text/javascript' src='js/"
				+ script + ".js'></scr")
				+ "ipt>");
	}
	return true;
})();

var GC_globaldata_image = {
	"leaderBoardImageUrl" : "images/hdpi/icon_leaderboard.png",
	"icon_female" : "images/hdpi/icon_female.png",
	"icon_male" : "images/hdpi/icon_male.png"
}

var GC_globaldata_url = {
	"callbackParamsType" : "json"
}

var GC_globaldata_settings = {
	"defaultPageCount" : 20
}

/*******************************************************************************
 * Options
 ******************************************************************************/
function generateOptionUrl(currentHtml, requestPath, params, requestMethod,
		needCallback, callbackInterface, needShowNativeLoading) {
	var data = {
		"currentHtml" : currentHtml,
		"requestPath" : requestPath,
		"requestParams" : params,
		"requestMethod" : requestMethod,
		"needCallback" : needCallback,
		"callbackInterface" : callbackInterface,
		"callbackParamsType" : GC_globaldata_url.callbackParamsType,
		"needShowNativeLoading" : needShowNativeLoading
	};
	return json2str(data);
}

function json2str(o) {
	var arr = [];
	var fmt = function(s) {
		if (typeof s == 'object' && s != null)
			return json2str(s);
		return /^(string|number)$/.test(typeof s) ? '"' + s + '"' : s;
	}
	for (var i in o)
		arr.push('"' + i + '":' + fmt(o[i]));
	return '{' + arr.join(',') + '}';
}
		
function getGameInstallpath(packagename){
	return true;
}

var global_reg = /<(user|game|contest){1}[^>]+\/>/gi;
var global_feeddetail_gametype  = [3, 5, 9, 10, 11, 12];
var global_gametype_gotogame = [3, 5, 9];
var global_matchtype_gotomatch = [10, 11, 12];

function global_getShortName(name) {
	if (name.length > 8) {
		return name.substring(0,5) + "...";
	} else {
		return name;
	}
}


function global_callGoBack() {
	if(typeof opalert.lastObj !='undefined'){
		try{
			opalert.lastObj.hide();
		}catch(e){
			android.goBack();
		}
	} else {
		android.goBack();
	}
}

function global_get_image(id, url) {
	$.action({
			callback : function(data) {
				$("#" + data.id).attr("src", data.path);
			},
			Action : {
				"currentHtml" : oprequest().getFileName(),
				"requestPath" : "get_image",
				"requestParams" : $.json2str({id:id,url:url}),
				"requestMethod" : "get",
				"needCallback" : "true",
				"callbackInterface" : "jQuery.actionCallback",
				"callbackParamsType" : "path",
				"needShowNativeLoading" : "false"
			}
	});
}

function checkFeedCanGoToGame(feedType) {
	for (var index = 0; index < global_gametype_gotogame.length; index++) {
		if (feedType == global_gametype_gotogame[index]) {
			return true;
		}
	}
	return false;
}

function nodeToJson(node) {
	return node.replace(/<([^\s]*)\s/, "{\"$1\":{ ").replace(/\s\/>$/, " }}")
			.replace(/(\s([\w]+)=("[^"]*"))/gi, "\"$2\":$3,")
			.replace(/, }/, "}");
}

/**
 * import js,动态加载js代码 使用方法 _import("js/lib/tools.js")
 * 
 */
function _import(jsfiles) {
	var allScriptTags = "";
	var host = "";
	if (/MSIE/.test(navigator.userAgent) || /Safari/.test(navigator.userAgent)) {
		var currentScriptTag = "<scr" + "ipt src='" + host + jsfiles
				+ "'></scr" + "ipt>";
		allScriptTags += currentScriptTag
	} else {
		var s = document.createElement("script");
		s.src = host + jsfiles;
		var h = document.getElementsByTagName("head").length ? document
				.getElementsByTagName("head")[0] : document.body;
		h.appendChild(s)
	};
	if (allScriptTags)
		document.write(allScriptTags)
}

function getTimeString(now, last) {
	var count = now - last;
	var dateNow = new Date(now);
	var dateLast = new Date(last);
	var dateCount = new Date(count);
	var year = dateNow.getYear() - dateLast.getYear();
	
	if(count <= 3600000){
		if (count <= 60 * 1000) {
			return nearmelang.lang_global_rightnow;
		} else {
			return parseInt(count/(60 * 1000) + "") + nearmelang.lang_global_minago;
		}
	} else {
		var h;
		var m;
		if (dateLast.getHours() < 10) {
			h= "0" + dateLast.getHours();
		} else {
			h= dateLast.getHours() + "";
		}
		if (dateLast.getMinutes() < 10) {
			m = "0" + dateLast.getMinutes();
		} else {
			m = dateLast.getMinutes() +"";
		}
		if (count >= 3600000 * 24 * 31 * 12 || year > 0) {
			return (dateLast.getYear()+1900) +"-"+getTimeZero((dateLast.getMonth()+1))+"-" + getTimeZero(dateLast.getDate())+" "+h+":"+m;
		}
		return (getTimeZero(dateLast.getMonth()+1)) + "-" + getTimeZero(dateLast.getDate()) + " " + h + ":" + m;
	}
}

function getTimeZero(num) {
	if (num < 10) {
		return "0" + num;
	} else {
		return num;
	}
}

/**时间区间表示
 * 
 * @param {开始时间戳} timestart
 * @param {结束时间戳} timeend
 * @return {}
 */
function getTimeRand(timestart,timeend){
	if(timestart>timeend){
		var timetmp = timestart;
		timestart=timeend;
		timeend = timetmp;
	}
	var date1 = new Date(),date2=new Date();
	date1.setTime(timestart);
	date2.setTime(timeend);
	var strpre = "",strsuf;
	if(nearmelang.lang_match_timeformart=="ch"){
		var year=nearmelang.lang_global_date_year,
			month=nearmelang.lang_global_date_month,
			day=nearmelang.lang_global_date_day;
	}else{
		var year="-",month="-",day="";
	}
	var d1={
		y:date1.getFullYear(),
		m:(date1.getMonth()+1+"").str_pad(2),
		d:(date1.getDate()+"").str_pad(2),
		h:(date1.getHours()+"").str_pad(2),
		i:(date1.getMinutes()+"").str_pad(2)
	}
	var d2={
		y:date2.getFullYear(),
		m:(date2.getMonth()+1+"").str_pad(2),
		d:(date2.getDate()+"").str_pad(2),
		h:(date2.getHours()+"").str_pad(2),
		i:(date2.getMinutes()+"").str_pad(2)
	}
	strpre=d1.y+year+d1.m+month+d1.d+day+""+d1.h+":"+d1.i;
	if(d1.y!=d2.y){
		strsuf="-"+d2.y+year+d2.m+month+d2.d+day+" "+d2.h+":"+d2.i;
	} else if( date1.getMonth() !=date2.getMonth() ) {
		strsuf="-"+d2.m+month+d2.d+day+" "+d2.h+":"+d2.i;
	} else if( date1.getDate() !=date2.getDate() ) {
		strsuf="-"+d2.d+day+" "+d2.h+":"+d2.i;
	} else {
		strsuf="-"+d2.h+":"+d2.i;
	}
	return strpre+strsuf;
}

/**获取时间间隔
 * 
 * @param {秒数} seconds
 * @param {最小时间[s|m|h|d]} de
 * @return {string}
 */
function getTimeLeft(seconds,de){
	var str='',unit=60;
	var tt=['s','m','h','d'];
	var ss={s:nearmelang.lang_global_date_second,
			m:nearmelang.lang_global_date_minute,
			h:nearmelang.lang_global_date_hour,
			d:nearmelang.lang_global_date_day};
	if(de!='s'&&de!='m'&&de!='h'&&de!='d'){
		de='m';
	}
	while(de!=tt[0]){
		tt.shift();
		seconds=Math.floor(seconds/60);
	}
	while(tt.length>0){
		unit=tt[0]=='h'?24:60;
		if(seconds>0){
			if(tt[0]!='d'){
				str=seconds%unit+ss[tt[0]]+str;
				tt.shift();
				seconds=Math.floor(seconds/unit);
			}else{
				str=seconds+ss[tt[0]]+str;
				tt.shift();
				seconds=0;
			}
		}else{
			break;
		}
	}
	return str!=''?str:0+ss[de];
}
var def = {
	'pagecount' : 20
};
var game_result_code = {
	notinstall : 1001,
	open : 1002,
	update : 1003
}
var prompt_type = {
	game : 1,
	contest : 2,
	theme : 3,
	evaluation : 4
}

var enter_game_type = {
	prompt :    	 "1000001",
	myself :    	 "1000002",
	category :  	 "1000003",
	friendgame : 	 "1000004",
	otherfeed : 	 "1000005",
	myfeed : 		 "1000006",
	contest :    	 "1000007",
	sysmessage:  	 "1000009",
	noticeinvite:	 "1000010",
	noticecontest:	 "1000011",
	devgame : 		 "1000012",
	friendlist : 	 "1000013",
	contestfeed : 	 "1000014",
	theme : 		 "1000015",
	evaluation : 	 "1000016"
}

var add_friend_type = {
	search : 		 "1000030",
	friendsfriend :	 "1000031",
	lbtohome : 		 "1000032",
	myfeedtohome :   "1000034",
	otherfeedtohome: "1000035",
	invitetohome :   "1000036",
	whoisplay :		 "1000037"
}

var firstpage_click = {
	game :  		 "1000020",
	contest : 		 "1000021",
	evaluation : 	 "1000022",
	theme : 		 "1000023"
};

function getImgBySize(path,size){
	var sizes = nearmelang.image.config_gameimg_size;
	var realsize=size;
	if( (size+"").match(/^\d+$/) ){
		realsize=size<=sizes.min?sizes.min:(size<=sizes.middle?sizes.middle:sizes.max);
	}else if(size.match(/^max|middle|min$/)){
		realsize=eval("sizes."+size);
	}else{
		return path;
	}
	return path.replace(/_\d+(\.[^\.]+)$/,"_"+realsize+realsize+"$1");
}


function checkHasUrl(node) {
	return node != undefined && node.user != undefined && node.user.profilePictureUrl != undefined
							&& node.user.profilePictureUrl != "null" && node.user.profilePictureUrl != ""
}

function checkListResource(data) {
	return data == "" || data == undefined || data.ListResource == undefined
			|| data.ListResource.resourceList == undefined
			|| data.ListResource.resourceList.Datas == undefined;
}

function checkListResourceEmpty(data) {
	return data == "" || data == undefined || data.ListResource == undefined
			|| data.ListResource.resourceList == undefined
			|| data.ListResource.resourceList.Datas == undefined || data.ListResource.resourceList.Datas.length == 0;
}


function checkNickname(value) {
	var account_pattern = /^[a-zA-Z0-9\u4e00-\u9fa5]{1,12}$/gi;
	if (value == null || value.trim() == "") {
		return false;
	} 
	var length = value.charlength();
	if (length < 2 || length > 12) {
		return false;
	}
	return account_pattern.test(value);
}

function goToOtherGame(gameId, url, from) {
	android.onStatistics(from);
	android.onLoadHtml("othergame.html?" + url.setParam("gameId",gameId).getParamsStr());
}

/**
 * 初始化部分公用效果。
 */
$(document).ready(function(){
	/**
	 * 固定表头
	 */
	/*var fixed = $('.popfixed');
	if( fixed.size()>0 ) {
		if(window.onscroll){
			var oldscroll=window.onscroll;
		}
		fixed.each(function(e){
			o=$(this);
			if(this.style.top){
				var itop = this.style.top;
			} else {
				var itop=o.css('top');
			}
			var iheight = o.height();
			if(iheight==0){
				var ps = [];
				o.parents().each(function(){
					_this=$(this);
					if(_this.css('display')=='none'){
						ps.push(_this);
						_this.css({'display':'block'});
					}
				});
				if(o.css('display')=='none'){
					o.css({'display':'block','visibility':'hidden','position':'absolute'});
					iheight = o.height();
					o.css({'display':'none','visibility':'visible','position':'static'});
				}else{
					iheight = o.height();
				}
				if(ps.length>0){
					for( var i=0;i<ps.length;i++){
						ps[i].css({'display':'none'});
					}
				}
			}
			o.data('initop',parseInt(itop.replace(/[^\d]/gi,'')));
			var div = o.clone();
			div.html('').css({height:iheight}).appendTo(o.parent());
			o.after(div);
			o.css({'position':'absolute','z-index':'1001'});
		})
		
		window.ontouchstart=function(){
			fixed.animate({opacity:'0'},200);
		}
		
		window.onscroll=function(){
			var scrolltop = $(window).scrollTop();
			fixed.each(function(o){
				o=$(this);
				o.css({
					top:scrolltop+o.data('initop')
				})
			})
			
			if(oldscroll){
				oldscroll.call(window);
			}
		}
		window.ontouchend=function(){
			window.lastscrolltop=$(window).scrollTop();
			window.fixtimer=setInterval(function(){
				var scrolltop = $(window).scrollTop();
				if(scrolltop==window.lastscrolltop){
					fixed.each(function(o){
						o=$(this);
						o.css({
							top:scrolltop+o.data('initop')
						}).animate({opacity:'1'},200);
					});
					clearInterval(window.fixtimer);
				}
				window.lastscrolltop=scrolltop;
			},100)
		}
	}*/
	/**
	 * 固定表头 结束
	 */
	
	
	/**
	 * 自动国际化支持
	 */
	
	var oppolang = $('.oppolang');
	if(oppolang.size()>0){
		oppolang.each(function(){
			if(this.innerHTML && this.innerHTML!=''){
				this.innerHTML = TrimPath.processTemplate(this.innerHTML, {url:oprequest(),window:window});
			}
			if(this.value&&this.value!=''){
				this.value = TrimPath.processTemplate(this.value, {url:oprequest(),window:window});
			}
		})
	}
	
	
	/**
	 * 初始化文本框,加载自定颜色
	 */
	
	var areas = $("textarea[inittext]");
	var inputs = $("input[type='text'][inittext]");
	if( areas.size()>0 ){
		initTextBorder(areas);
	}
	if( inputs.size()>0 ){
		initTextBorder(inputs);
	}
	function initTextBorder(obj){
		obj.each(function(index,dom){
			var that=$(this);
			var inittext = TrimPath.processTemplate(that.attr('inittext'), {url:oprequest()});
			if(!that.hasClass('initcolor')){
				that.addClass('initcolor');
			}
			that.val(inittext);
			
			that.focus(function(e){
				var that=$(this);
				if(that.trim().val()==inittext){
					that.val('');
					that.removeClass('initcolor');
				}
			}).blur(function(e){
				var that=$(this);
				if(that.trim().val()==''){
					that.val(inittext);
					that.addClass('initcolor');
				}
			});
		});
	}
})
