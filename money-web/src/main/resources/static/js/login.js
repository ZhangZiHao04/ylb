var referrer = "";//登录后返回页面
referrer = document.referrer;
if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}

//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});

$(function(){

	$('#imgCode').click(function(){
		$(this).attr("src", "../login-captcha?t="+new Date().getTime())
	})

	//登录
	$('#loginId').click(function () {
		$.ajax({
			url:'../api/login',
			type:'post',
			data:{
				phone:$('#phone').val(),
				loginPassword: $.md5($('#loginPassword').val()),
				captcha:$('#captcha').val()
			},
			success:function (result) {
				if(result.code==200){
					window.location.href='myCenter'
				}else{
					alert(result.message);
				}
			}
		});
	});
})
