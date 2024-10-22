const regex = {
    // 값이 비어있는지 확인
    value: function(selector, msg) {
        var target = $(selector); // 선택한 변수를 target에 저장
        if (!target.val()) {
            alert(msg);
            target.focus();
            return false;
        }
        return true;
    },

    // 길이 제한
    max_length: function(selector, len, msg) {
        var target = $(selector);
        if (target.val().length < len) {
            alert(msg);
            target.val("");
            target.focus();
            return false;
        }
        return true;
    },

    // 정규 표현식을 이용한 유효성 검사
    field: function(selector, msg, regex_expr) {
        var target = $(selector);
        if (!target.val() || !regex_expr.test(target.val())) {
            alert(msg);
            target.val("");
            target.focus();
            return false;
        }
        return true;
    },

    // 두 요소의 입력값이 같은지 확인
    equalField: function(origin, compare, msg) {
        var src = $(origin);
        var dsc = $(compare);
        if (src.val() !== dsc.val()) {
            alert(msg);
            src.val("");
            dsc.val("");
            src.focus();
            return false;
        }
        return true;
    },

    // 전화번호 정규식
    tel: function(selector, msg) {
        return this.field(selector, msg, /^(01[0-9]{1})([ -]?\d{3,4})([ -]?\d{4})$/);
    },

    // 아이디 조건 - 첫 자는 영문만, 두 번째부터는 영문, 숫자, 3자 이상
    uid: function(selector, msg) {
        return this.field(selector, msg, /^[a-zA-Z][a-zA-Z0-9]{2,}$/);
    },

    // 이메일 정규식
    email: function(selector, msg) {
        return this.field(selector, msg, /^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/);
    }
};
