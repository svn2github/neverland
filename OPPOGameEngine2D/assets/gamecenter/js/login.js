
function showinfo(txtId,info,color){
	  $("#"+txtId).html(info);
	  if(color==1){$("#"+txtId).css("color","green");}
	  else if(color==2){$("#"+txtId).css("color","red");}
	  else {$("#"+txtId).css("color","#666666");}	
	}
function Ischecked(){
	if($("#agreement").hasClass("checked")){$("#agreement").removeClass().addClass("notchecked");}
	else {$("#agreement").removeClass().addClass("checked");}
	
}

var loginFlg = 1;
var registerFlg = 1;

function userLogin() {
	var userName = $("#userName").val();
	var userPwd = $("#userPwd").val();
	if(loginFlg == 1) {
		$.ajax({
	    	url: "./user_login",
	        data: "userName=" + userName + "&userPwd=" + userPwd,
	        type: "post",
	        async : false,
	        success: function (data) {
	        	if(data.indexOf("ERR_") != 0) {
	        		$("#errArea").html("");
	        		showMainPage(data);
	        	} else {
	        		$("#errArea").html(data.replace("ERR_", ""));
	        	}
	        	loginFlg = 1;
	        }
	    });
		loginFlg = 2;
	} else {
		return false;
	}
}

function checkLogin() {
	var userName = $("#userName").val();
	var userPwd = $("#userPwd").val();
	if(userName.length==0 || ($.trim(userName)).length == 0 || userName == nearmelang.lang_login_accountandemail) {
		showinfo("errArea",nearmelang.lang_login_inputusername,2);
		return false;
	} else if(userPwd.length==0 || ($.trim(userPwd)).length == 0) {
		showinfo("errArea",nearmelang.lang_login_inputpassword,2);
		return false;
	}
	android.onUserLogin(userName, userPwd);
}

function loginCallback(username, password) {
	$.action({
		callback : function(data) {
			var resultInfo = data.UserBasicInfo;
			var code = parseInt(resultInfo.result);
			if (code == 1001	&& resultInfo.uid !== undefined && resultInfo.uid !== "0") {
				android.onLoginFinish(resultInfo.uid);
				return;
			}
			if (code == 3008) {
				showinfo("errArea",nearmelang.lang_global_secreterror,2);
				return;
			}
			if (code == 3005) {
				showinfo("errArea",nearmelang.lang_global_accountnotexist,2);
				return;
			}
			
			if (resultInfo.errorMsg !== undefined) {
				showinfo("errArea",resultInfo.errorMsg,2);
			}
		},
		Action : {
			"currentHtml" : "login.html",
			"requestPath" : "sdk_user_login",
			"requestParams" : "userName=" + username + "&password=" + password,
			"requestMethod" : "post",
			"needCallback" : "true",
			"callbackInterface" : "jQuery.actionCallback",
			"callbackParamsType" : "json",
			"needShowNativeLoading" : "true",
			"paramsNeedDecode" : "true",
			"loadingHeader" : nearmelang.lang_login_islogining,
			"failureMessage" : nearmelang.lang_global_sendfailure
		}
	});
}

$(document).ready(function(){
	$("#userName").attr("value", nearmelang.lang_login_accountandemail);
	$("#register").attr("value", nearmelang.lang_login_registernew);
	$("#button").attr("value", nearmelang.lang_global_login);
	
	$("#userName").bind({
		  focus: function() {
			  	if($(this).val()==nearmelang.lang_login_accountandemail) {
			    $(this).val("");   }
			  },
			  blur: function() {
				if($(this).val()==""){
			    $(this).val(nearmelang.lang_login_accountandemail);}
			  }
			});

	
})

function checkName() {
	//验证用户名
	var userName=$("#userName").val();
	var userName_validate=userName.match(/[\s<>\/\|\!\#\@\$\^\&\*\(\)\{\}\:\'\"\.\?\-\+\=,;\\\\]+/);
	var userName_len=userName.replace(/[^\x00-\xff]/g, "aa").length;
	if(userName.length==0 || ($.trim(userName)).length == 0){showinfo("userName_errArea","用户名不能为空",2);return false;}
	else if(!isNaN($.trim(userName))){showinfo("userName_errArea","用户名不能为纯数字",2);return false;}
	else if(userName_validate){showinfo("userName_errArea","用户名有非法字符",2);return false;}	
	else if(userName_len>12 || userName_len<2){showinfo("userName_errArea","用户名长度为2-12个英文数字或1-6个中文字符",2);return false;}
	else {showinfo("userName_errArea","",1);return true;}	
}
function checkEmail(){
	//验证邮箱
	var email=$("#email").val();
	var isEmailRegex = /^([\w-_~`!#$%^&*{}.+]{3,20})+(\.[\w-]+)*@[\w-_]+((\.){1}([\w-_]+)+)+$/;
	if(email.length==0 || ($.trim(email)).length == 0){showinfo("email_errArea","请输入邮箱",2);return false;}
	else if(!email.match(isEmailRegex)){showinfo("email_errArea","邮箱格式错误",2);return false;}
	else {showinfo("email_errArea","",1);return true;}	
}
function checkPwd(){
	//验证密码及密码重复
	var userPwd1=$("#userPwd1").val();
	var userPwd2=$("#userPwd2").val();
	var isPasswordRegex = /^[0-9a-zA-Z_]{6,16}$/;
	if(userPwd1.length==0 || ($.trim(userPwd1)).length == 0){showinfo("userPwd1_errArea","请输入密码",2);return false;}
	else if(!userPwd1.match(isPasswordRegex)){showinfo("userPwd1_errArea","密码格式错误或长度错误",2);return false;}
	else if(userPwd1!=userPwd2){showinfo("userPwd2_errArea","两次密码输入不一致",2);return false;}
	else {showinfo("userPwd2_errArea","",1);showinfo("userPwd1_errArea","",1);return true;}
}
function checkCode(){
	//验证验证码是否为空
	var code=$("#validateCode").val();
	if(code.length==0 || ($.trim(code)).length == 0){showinfo("validateCode_errArea","请输入验证码",2);return false;}
	else {showinfo("validateCode_errArea","",1);return true;}
}
function checkAgreement(){
	if($("#agreement").hasClass("checked")){showinfo("errArea","",1);return true;}
	else {showinfo("errArea","请阅读用户使用协议",2);return false;}
	
}
function RegCheck (){	
	checkName();
	checkEmail();
	checkPwd();
	checkCode();
	checkAgreement();
	if(checkName()&&checkEmail()&&checkPwd()&&checkCode()&&checkAgreement()==true){return true;}
	else {return false;}
}

function toRegister() {
	$("#form1").submit();
}

function toLogin() {
	window.location.href = "./user_login";
}

function userRegister() {
	if(registerFlg == 1) {
		registerFlg = 2;
		$("#form1").submit();
	} else {
		return;
	}
}

function showMainPage(id) {
	android.onLoginFinish(id);
}


function callActionLogin() {
	if(checkLogin() == true) {
		android.onCheckNetwork("userLogin");
	} else {
		return false;
	}
}

function callActionToRegister() {
//	android.onStartIntent("web", "http://i.gc.nearme.com.cn/user_register");
//	android.onLoadUrl("http://i.gc.nearme.com.cn/user_register");
	android.onLoadPath("user_register");
}

function callActionRegister() {
	if(RegCheck() == true) {
		android.onCheckNetwork("userRegister");
	} else {
		return false;
	}
}

function callActionToLogin() {
	android.onCheckNetwork("toLogin");
}

function callActionVisitorAwaked() {
	android.onVisitorAwaked();
}

function changeCode() {
	$("#validatePic").attr("src", "ValidateCode?rand=" + Math.random());
	return false;
}