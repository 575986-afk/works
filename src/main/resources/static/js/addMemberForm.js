// 1. 아이디 확인 버튼 클릭 Event
$(document).on("click", "#idCheckBtn", function() {
    const $form = $("#addMemberForm").length ? $("#addMemberForm") : $(this).closest("form");
    const userId = $form.find("#memberId, input[name='userId']").val().trim();
    const $msg = $form.find("#idCheckMsg");

    if (!userId) {
        alert("아이디를 입력해주세요.");
        return;
    }

    axios.get("/adminUser/member/checkId", { params: { userId: userId } })
        .then(function(response) {
            const user = response.data;

            if (!user || (!user.userNo && !user.user_no)) {
                $msg.text("가입되지 않은 회원입니다.").css("color", "#dc2626");
                $form.find("#userNo, input[name='userNo']").val("");
                return;
            }

            const companyNo = user.companyNo || user.company_no;
            if (companyNo) {
                $msg.text("이미 회사에 소속되어 있는 회원입니다.").css("color", "#dc2626");
                $form.find("#userNo, input[name='userNo']").val("");
                return;
            }

            const userNo = user.userNo || user.user_no;
            const userName = user.userName || user.user_name;

            $msg.text(`[${userName}] 님은 추가 가능한 회원입니다.`).css("color", "#2563eb");
            
            // userNo 값을 hidden 태그에 저장
            $form.find("#userNo, input[name='userNo']").val(userNo);
            $form.find("#memberId, input[name='userId']").prop("readonly", true);
            
            console.log("아이디 확인 성공 - 저장된 userNo:", $form.find("#userNo, input[name='userNo']").val());
        })
        .catch(function(error) {
            console.error("아이디 확인 실패:", error);
            alert("아이디 확인 중 오류가 발생했습니다.");
        });
});

// 2. 저장 (추가/수정) 버튼 클릭 Event
$(document).on("click", "#saveMemberBtn", function() {
    const $form = $("#addMemberForm").length ? $("#addMemberForm") : $(this).closest("form");
    const mode = $form.find("#formMode, input[name='mode']").val();

    if (mode === "add") {
        const userNo = $form.find("#userNo, input[name='userNo']").val();
        
        console.log("저장 시도 - 현재 mode:", mode, " / 읽어온 userNo:", userNo);

        if (!userNo) {
            alert("아이디 확인을 진행해 주세요.");
            return;
        }
    }

    const url = (mode === "modify") ? "/adminUser/member/modifyMember" : "/adminUser/member/addMember";
    const params = new URLSearchParams();

    $.each($form.serializeArray(), function(index, field) {
        params.append(field.name, field.value);
    });

    axios.post(url, params)
        .then(function(response) {
            if (response.data === "success") {
                alert(mode === "modify" ? "구성원 정보가 수정되었습니다." : "구성원이 추가되었습니다.");
                location.reload();
            }
        })
        .catch(function(error) {
            console.error(error);
            alert("처리 중 오류가 발생했습니다.");
        });
});

// 3. 모달 닫기
$(document).on("click", ".modalClose, .cancelBtn", function() {
    $("#addMemberFormArea").empty();
});