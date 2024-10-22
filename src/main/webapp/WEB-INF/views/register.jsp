<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   

 <!-- 본문내용 --> 
                    <h1 class="my-4 text-center">회원가입</h1>
                    <p class="text-center text-secondary"><span class="text-danger">*</span>표시가 있는 곳은 필수 입니다.</p>
                    <form name="register" id="register" action="register" method="post" enctype="multipart/form-data">
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-md-3 text-right"><span class="text-danger">*</span>아이디</div>
                                    <div class="col-md-4">
                                        <input type="text" name="userid" id="userid" class="form-control">
                                        <input type="hidden" id="ok" />
                                    </div>
                                    <div class="col-md-3"><button type="button" id="checkid" class="btn btn-warning">아이디중복검사</button></div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-md-3 text-right"><span class="text-danger">*</span>비밀번호</div>
                                    <div class="col-md-4">
                                        <input type="password" name="userpass" id="userpass" class="form-control">
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-md-3 text-right"><span class="text-danger">*</span>비밀번호 확인</div>
                                    <div class="col-md-4">
                                        <input type="password" name="reuserpass" id="reuserpass" class="form-control">
                                        
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-md-3 text-right"><span class="text-danger">*</span>이름</div>
                                    <div class="col-md-4">
                                        <input type="text" name="username" id="username" class="form-control">
                                    </div>
                                </div>                               
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-md-3 text-right"><span class="text-danger">*</span>이메일</div>
                                    <div class="col-md-5">
                                        <input type="text" name="useremail" id="useremail" class="form-control">
                                    </div>
                                </div>                               
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-md-3 text-right"><span class="text-danger">*</span>전화번호</div>
                                    <div class="col-md-6">
                                        <input type="text" name="usertel" id="usertel" class="form-control">
                                    </div>
                                </div>                               
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-md-3 text-right">주소</div>
                                    <div class="col-md-9">
                                        <div class="row">
                                            <div class="col-12 d-flex">
                                                <input type="text" id="postcode" name="zipcode" class="form-control col-3" placeholder="우편번호">
                                                <input type="button" onclick="execDaumPostcode()" class="btn btn-primary ml-2" value="우편번호 찾기">
                                            </div>
                                            <div class="col-12">
                                                <input type="text" id="address" name="address" class="form-control mt-2" placeholder="주소">
                                                <input type="text" id="detailAddress" name="detail_address" class="form-control mt-2" placeholder="상세주소">
                                                <input type="text" id="extraAddress" name="extra_address" class="form-control mt-2" placeholder="참고항목">
                                            </div>
                                        </div>
                                    </div>
                                </div>                               
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-md-3 text-right">사진</div>
                                    <div class="col-md-6">
                                        <input type="file" name="userimg" id="userimg" class="form-control">
                                    </div>
                                </div>                               
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-md-3 text-right">자기소개</div>
                                    <div class="col-md-9">
                                        <textarea name="userprofile" id="userprofile" class="form-control" rows="3"></textarea>
                                    </div>
                                </div>                               
                            </li>
                        </ul>
                        <div class="text-center mt-3">
                            <button class="btn btn-danger mr-2 px-4">취소</button>
                            <button class="btn btn-primary ml-2 px-4">전송</button>
                        </div>
                    </form>
                    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
                    <script>
                        function execDaumPostcode() {
                            new daum.Postcode({
                                oncomplete: function(data) {
                                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
                    
                                    // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                                    var addr = ''; // 주소 변수
                                    var extraAddr = ''; // 참고항목 변수
                    
                                    //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                                    if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                                        addr = data.roadAddress;
                                    } else { // 사용자가 지번 주소를 선택했을 경우(J)
                                        addr = data.jibunAddress;
                                    }
                    
                                    // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                                    if(data.userSelectedType === 'R'){
                                        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                                        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                                        if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                                            extraAddr += data.bname;
                                        }
                                        // 건물명이 있고, 공동주택일 경우 추가한다.
                                        if(data.buildingName !== '' && data.apartment === 'Y'){
                                            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                                        }
                                        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                                        if(extraAddr !== ''){
                                            extraAddr = ' (' + extraAddr + ')';
                                        }
                                        // 조합된 참고항목을 해당 필드에 넣는다.
                                        document.getElementById("extraAddress").value = extraAddr;
                                    
                                    } else {
                                        document.getElementById("extraAddress").value = '';
                                    }
                    
                                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                                    document.getElementById('postcode').value = data.zonecode;
                                    document.getElementById("address").value = addr;
                                    // 커서를 상세주소 필드로 이동한다.
                                    document.getElementById("detailAddress").focus();
                                }
                            }).open();
                        }
                    </script>
                    <!--//본문 내용 끝-->