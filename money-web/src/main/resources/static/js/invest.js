$(function(){
   $('#bidMoney').blur(function(){
       var bidMoney = $('#bidMoney').val();
       if(bidMoney.trim()==''){
           alert("投资金额不能为空")
           return;
       }
       if(bidMoney<=0 || bidMoney%10!=0){
           alert("投资金额应该为大于0并且为100的倍数的整数");
           return;
       }
   })

   $('#investNow').click(function(){
       $('#bidMoney').blur();
       $.ajax({
           url:'../api/invest',
           type:'post',
           data:{
               bidMoney:$('#bidMoney').val(),
               infoId:$('#loanId').val()
           },
           success:function(result){
               if(result.code==200){
                   $('#shouyi').text(result.data)
                   window.location.href=window.location.href;
               }else {
                   alert(result.message);
               }
           }
       })
   })
});