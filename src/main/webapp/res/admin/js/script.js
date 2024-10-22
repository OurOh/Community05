$(function(){
   
    $("#addGrade").click(function(){  //input 추가
        const gradeLength = $(".grade").length;
        const addGrade = `
            <div class="row mt-3 pb-3 border-bottom grade">
                 <label for="" class="col-2 text-center">${gradeLength}</label>
                 <input type="text" class="form-control col-9" name="grade-${gradeLength}" value="">
            </div> 
        `;
        $("#gradeForm").append(addGrade);
    });

    $("#deleteGrade").click(function(){  //input 삭제
        const gradeLength = $(".grade").length;
        if(gradeLength > 1) {
            $(".modal-body form .grade").last().remove();
        }
    });
});