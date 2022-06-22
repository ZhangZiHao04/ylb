//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}


//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});

$(function(){
	$('#phone').blur(function(){
		var phone = $('#phone').val();
		if(phone==null){
			showError("phone","请填写手机号");
			return;
		}
		var reg = /^1[1-9]\d{9}$/;
		if(reg.test(phone)==false){
			showError("phone","手机号格式不正确")
			return;
		}
		showSuccess('phone');
	})
	$('#loginPassword').blur(function(){
		var password = $('#loginPassword').val();
		if(password.trim()==''){
			showError("loginPassword","密码不能为空");
			return;
		}
		var reg = /^[0-9a-zA-Z]{6,16}$/;
		if(reg.test(password)==false){
			showError("loginPassword","密码必须是6位以上数据和字母");
		}
		showSuccess("loginPassword");
	})
	$('#messageCode').blur(function(){
		var messageCode = $('#messageCode').val();
		if(messageCode.trim()==''){
			showError('messageCode', '请填写验证码');
			return;
		}
		showSuccess('messageCode');
	})

	$('#messageCodeBtn').click(function(){
		$('phone').blur();
		if($('#phoneErr').text()!=''){
			return;
		}
		//发送验证码
		if($('#messageCodeBtn').hasClass('on')){	//如果按钮已经变灰，滚蛋
			return;
		}

		$.ajax({
			url:'../api/reg-sms-code',
			data:{
				phone:$('#phone').val()
			},
			success:function(result){
				if(result.code==200){
					$('#messageCodeBtn').addClass('on');
					$.leftTime(60,function(d){
						if(d.status){
							$('#messageCodeBtn').text(d.s+'秒');
						}else {
							$('#messageCodeBtn').removeClass('on');
							$('#messageCodeBtn').text('获取验证码');
						}
					})
				}
			}
		})
	})

	$('#btnRegist').click(function(){
		$('#phone').blur();
		$('#loginPassword').blur();
		$('#messageCode').blur();
		if($('[id$=Err]').text()!=''){
			return;
		}

		$.ajax({
			url:"../api/register",
			data:{
				phone:$('#phone').val(),
				loginPassword:$.md5($('#loginPassword').val()),
				messageCode:$('#messageCode').val()
			},
			success:function(result){
				if(result.code==200){
					window.location.href='realName';
				}else {
					alert(result.message);
				}
			}
		})
	})
});
