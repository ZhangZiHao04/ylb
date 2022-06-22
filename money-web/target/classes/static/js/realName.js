//同意实名认证协议
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

$(function(){
	$('#realName').blur(function(){
		var realName = $('#realName').val()
		if(realName.trim() == ''){
			showError('realName',"请填写真实姓名");
			return;
		}
		var reg = /^[\u4e00-\u9fa5]{2,}$/;
		if(reg.test(realName)==false){
			showError('realName',"真实姓名应为2位以上的汉字");
			return;
		}
		showSuccess('realName');
	})

	$('#idCard').blur(function(){
		var idCard = $('#idCard').val();
		var reg = /(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
		if(reg.test(idCard)==false){
			showError('idCard',"身份证号无效");
			return;
		}
		showSuccess('idCard');
	})

	$('#imgCode').click(function(){
		$(this).attr("src", "../login-captcha?t="+new Date().getTime())
	})

	$('#btnRegist').click(function(){
		//输入验证
		$('#realName').blur();
		$('#idCard').blur();
		//检查验证结果
		if($('[id$=Err]').text()!=''){
			return;
		}
		$.ajax({
			url:"../api/realName",
			data:{
				realName:$('#realName').val(),
				cardNo:$('#idCard').val(),
				captcha:$('#captcha').val()
			},
			type:'post',
			success:function(result){
				if(result.code==200){
					alert('实名成功');
					window.location.href='myCenter';
				}else {
					alert(result.message);
				}
			}
		})
	})
});