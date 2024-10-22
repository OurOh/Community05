$(function(){
   $("#register").submit(function(e){
      e.preventDefault();
      
      /* 아이디 검증 */
      if(!regex.value("#userid", "아이디를 입력하세요")){ return false; }      
      if(!regex.uid("#userid", "아이디는 영문과 숫자만 가능합니다. 첫 글자에 숫자는 올 수 없어요.")){ return false;}
      if(!regex.max_length("#userid", 10, "최대 10자까지만 허용해요.")){return false;}
      
      /* 비밀번호 검증 */
      if(!regex.value("#userpass", "비밀번호를 입력하세요.")){ return false; }
      if(!regex.min_length("#userpass", 4, "비밀번호는 최소4자 까지 입니다.")){ return false; }
      if(!regex.value("#reuserpass", "비밀번호를 다시 확인해 주세요.")){ return false; }
      if(!regex.equalField("#userpass", "#reuserpass", "비밀번호가 일치하지 않습니다.")){ return false; }
      
      /* 이름 검증 */
      if(!regex.value("#username", "이름을 입력하세요.")){ return false; }
      
      /* 이메일 검증 */
      if(!regex.value("#useremail", "이메일을 입력하세요.")){ return false; }
      if(!regex.email("#useremail", "이메일 형식이 아닙니다.")){ return false; }
      
      /* 전화번호 검증 */
      if(!regex.value("#usertel", "전화번호를 입력하세요.")){ return false; }
      if(!regex.tel("#usertel", "전화번호 형식이 아닙니다.")){ return false; }
   });
   
   
   $(document).on("keyup", "#usertel", function() { 
   $(this).val( $(this).val().replace(/[^0-9]/g, "").replace(/(^02|^0505|^1[0-9]{3}|^0[0-9]{2})([0-9]+)?([0-9]{4})$/,"$1-$2-$3").replace("--", "-") ); 
});
   
});