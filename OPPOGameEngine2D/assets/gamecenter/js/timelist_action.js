var def = {icon:'images/c20_014.png','pagecount':20}
var url = oprequest();
var params;
var isVisitor = false;
var imgcache;
function showTab(obj){
	var info = $.parseJSON($(obj).attr('info'));
	android.onChangeTab('' + info.board);
	$("#tab_3 img").css("display" , "none");
	$("#tag1").css("display", "block");
	$("#tag2").css("display", "block");
	$("#tag3").css("display", "block");
	$("#contentAll").css("display","block");
	$("#line").html("");
	var boards=[0,1,3];
	$("#contentAll .common_part").hide();
	for(var i=1;i<=3;i++){
		if(info.board==boards[i-1]){
			$("#tab"+i+" img").css("display", "block");
			$("#tab"+i).attr("class", "li_click");
			$("#part1"+boards[i-1]).show();
			$("#part2"+boards[i-1]).show();
			$("#part3"+boards[i-1]).show();
		}else{
			$("#tab"+i+" img").css("display", "none");
			$("#tab"+i).attr("class", "li_unclick");
		}
	}
	checkTagShow(obj);
	getScrollById('contentAll').setPullUp(function(){loadMore(obj)}).disableMore().reDraw();
	if( info.count>0 ){
		//filltab($(obj));
	} else {
		loadMore( obj );
	}
}

function filltab( obj ) {
	var data = obj.data('data');
	var info = $.parseJSON(obj.attr('info'));
	$("#name").html(info.tag);
	if( info.total==0 ){
		$("#contentAll").css("display", "none");
		$("#line").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_timelist_noscore));
		return;
	} else {
		var list = data.ListResource.resourceList.Datas;

		var data1=[],data2=[],data3=[];
		for (var index = 0; index < list.length; index++) {
			var node = list[index].InternalScore;
			if(node.user && node.user.InternalUser.name !== undefined){
				var imgid="imgcache_"+info.board+info.count+(index+1);
				node.user.imgid=imgid;
				imgcache.add(imgid,node.user.InternalUser.profilePictureUrl);
				if (node.user.InternalUser.id == url.getParam('userId') &&
					data1.length==0 && $("#part1"+info.board).find("dd[isMy]").size()==0 ) {
					node.isMy=true;
					data1.push(node);
				} else if (node.isFriend == true) {
					node.isMy=false;
					data2.push(node);
				} else {
					node.isMy=false;
					data3.push(node);
				}
			}else{
				continue;
			}
		}
	}
	var tpl = TrimPath.parseDOMTemplate("tpl");
	$("#part1"+info.board).append(tpl.process({data:data1}));
	$("#part2"+info.board).append(tpl.process({data:data2}));
	$("#part3"+info.board).append(tpl.process({data:data3}));
	checkTagShow(obj);
	if(parseInt(info.count) < parseInt(info.total)){
		getScrollById('contentAll').enableMore().reDraw();
	} else {
		getScrollById('contentAll').disableMore().reDraw();
	}
	imgcache.start();
}

function checkTagShow(obj){
	var info = $.parseJSON($(obj).attr('info'));
	for(var i=1;i<=3;i++){
		if($('#part'+i+info.board).children().size()>0){
			$("#tag"+i).css("display", "block");
		} else {
			$("#tag"+i).css("display", "none");
		}
	}
}

function loadMore( obj ) {
	obj = $(obj);
	var info = $.parseJSON(obj.attr('info'));
	params.leaderboardType=info.board;
	params.start=info.count;
	params.end=parseInt(info.count)+parseInt(def.pagecount);
	$.action({
		callback:function(data){
			if(!obj.data('data') && checkListResource(obj.data('data'))){
				if (!checkListResource(data)) {
					obj.data('data',data);
					info.count = (!info.addtype||info.addtype!=true)?0:info.count+data.ListResource.resourceList.Datas.length;
					info.total=data.ListResource.number;
				} else {
					$("#contentAll").css("display", "none");
					$("#line").html(nearmelang.ui_prompt_div.replace(/#/gi,nearmelang.lang_timelist_noscore));
				}
			} else {
				pdata = obj.data('data');
				if (!checkListResource(pdata)) {
					var plist = pdata.ListResource.resourceList.Datas;
					if(!info.addtype||info.addtype!=true){
						data.ListResource.resourceList.Datas = plist
							.concat(data.ListResource.resourceList.Datas);
					}
					obj.data('data',data);
					info.count = (!info.addtype||info.addtype!=true)?0:info.count+data.ListResource.resourceList.Datas.length;
					info.total=data.ListResource.number;
				}
			}
			info.tag=data.ListResource.tag;
			obj.attr('info',$.json2str(info));
			filltab(obj);
		},
		Action:{
			"currentHtml" : "timelist.html",
			"requestPath" : "get_leaderboard_detail",
			"requestParams" : oprequest().emptyParam().setParams(params).getParamsStr(),
			"requestMethod" : "get",
			"needCallback" : true,
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : true
		}
	});
}
function unlockedCall(){
	
}

function goToOtherHome(id) {
	if (isVisitor) {
		opalert({
			content:nearmelang.lang_global_youneedloginforfun,
			mode:opalert.BTN_BOTH,
			onok:function(){
				android.onGoToLogin();
			},
			oktext:nearmelang.lang_global_login
		});
		
	} else {
		android.onLoadHtml("otherHome.html?" + url.getParamsStr() + "&requestedUserId=" + id + "&from=" + add_friend_type.lbtohome);
	}
}

$(document).ready(function(){
	if (url.getParam("visitor") == "true") {
		isVisitor = true;
	}
	if (isVisitor) {
		params = {
			"gameId":url.getParam("gameId"),
			"gameVersionName" : url.getParam("gameVersionName"),
			"leaderboardId":url.getParam("leaderboardId"),
			"leaderboardType":0,
			"start":0,
			"end":def.pagecount
		}
	} else {
		params = {
			"gameId":url.getParam("gameId"),
			"gameVersionName" : url.getParam("gameVersionName"),
			"leaderboardId":url.getParam("leaderboardId"),
			"leaderboardType":0,
			"userId":url.getParam("userId"),
			"start":0,
			"end":def.pagecount
		}
	}
	imgcache=preimg();
	opscroll('contentAll',{onPullUp:function(tab){loadMore(obj.get(0))},top:"140px",bottom:"0px"}).disableMore();
	if (url.getParam("currentTab") == "1") {
		showTab($("#tab1"));
	} else if (url.getParam("currentTab") == "2") {
		showTab($("#tab2"));
	} else if (url.getParam("currentTab") == "3") {
		showTab($("#tab3"));
	} else {
		showTab($("#tab1"));
	}
})