document.addEventListener('DOMContentLoaded', () => {

    // ==========================================
    // [추가됨] 공통 헤더 드롭다운 메뉴 토글 로직
    // ==========================================
    const searchInput = document.getElementById('searchInput');
    const searchDropdown = document.getElementById('searchDropdown');
    const appLauncherBtn = document.getElementById('appLauncherBtn');
    const appLauncherDropdown = document.getElementById('appLauncherDropdown');
    const notiBtn = document.getElementById('notiBtn');
    const notiDropdown = document.getElementById('notiDropdown');
    const profileBtn = document.getElementById('profileBtn');
    const profileDropdown = document.getElementById('profileDropdown');

    function closeAllDropdowns() {
        if (searchDropdown) searchDropdown.classList.add('hidden');
        if (appLauncherDropdown) appLauncherDropdown.classList.add('hidden');
        if (notiDropdown) notiDropdown.classList.add('hidden');
        if (profileDropdown) profileDropdown.classList.add('hidden');
    }

	if (searchInput) {
	        searchInput.addEventListener('click', (e) => { 
	            e.stopPropagation(); 
	            closeAllDropdowns(); 
	            searchDropdown.classList.remove('hidden'); 
	        });

	        // ==========================================
	        // [새로 추가] 검색창 엔터키 입력 시 검색 로직
	        // ==========================================
	        searchInput.addEventListener('keypress', function(e) {
	            if (e.key === "Enter") {
	                e.preventDefault(); // 기본 폼 제출 방지
	                const keyword = this.value.trim();
	                
	                // 현재 URL의 기존 파라미터(userNo, representativeUserNo 등) 유지
	                const urlParams = new URLSearchParams(window.location.search);
	                
	                if (keyword) {
	                    urlParams.set("keyword", keyword);
	                } else {
	                    urlParams.delete("keyword");
	                }
	                
	                // 검색어가 반영된 URL로 리다이렉트
	                window.location.href = "/todo?" + urlParams.toString();
	            }
	        });
	    }
    if (appLauncherBtn) appLauncherBtn.addEventListener('click', (e) => { e.stopPropagation(); const isHidden = appLauncherDropdown.classList.contains('hidden'); closeAllDropdowns(); if (isHidden) appLauncherDropdown.classList.remove('hidden'); });
    if (notiBtn) notiBtn.addEventListener('click', (e) => { e.stopPropagation(); const isHidden = notiDropdown.classList.contains('hidden'); closeAllDropdowns(); if (isHidden) notiDropdown.classList.remove('hidden'); });
    if (profileBtn) profileBtn.addEventListener('click', (e) => { e.stopPropagation(); const isHidden = profileDropdown.classList.contains('hidden'); closeAllDropdowns(); if (isHidden) profileDropdown.classList.remove('hidden'); });

    document.addEventListener('click', closeAllDropdowns);

    [searchDropdown, appLauncherDropdown, notiDropdown, profileDropdown].forEach(dropdown => {
        if (dropdown) dropdown.addEventListener('click', (e) => e.stopPropagation());
    });


    // ==========================================
    // 1. 전체선택 체크박스 로직
    // ==========================================
    const selectAllCheckbox = document.getElementById('selectAll');
    const itemCheckboxes = document.querySelectorAll('.item-checkbox');

    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', (e) => {
            const isChecked = e.target.checked;
            itemCheckboxes.forEach(checkbox => {
                checkbox.checked = isChecked;
            });
        });
    }

    itemCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const allChecked = Array.from(itemCheckboxes).every(cb => cb.checked);
            const someChecked = Array.from(itemCheckboxes).some(cb => cb.checked);

            if (selectAllCheckbox) {
                selectAllCheckbox.checked = allChecked;
                selectAllCheckbox.indeterminate = someChecked && !allChecked;
            }
        });
    });


    // ==========================================
    // [추가됨] 선택 삭제 로직
    // ==========================================
    const deleteTodoBtn = document.getElementById('deleteTodoBtn');

    if (deleteTodoBtn) {
        deleteTodoBtn.addEventListener('click', () => {
            const checkedItems = document.querySelectorAll('.item-checkbox:checked');

            if (checkedItems.length === 0) {
                alert('삭제할 할 일을 선택해주세요.');
                return;
            }

            if (!confirm(`선택한 ${checkedItems.length}개의 할 일을 삭제하시겠습니까?`)) {
                return;
            }

            const todoNos = [];
            checkedItems.forEach(checkbox => {
                const todoItem = checkbox.closest('.todo-item');
                if (todoItem && todoItem.dataset.no) {
                    todoNos.push(todoItem.dataset.no);
                }
            });

            // 서버로 삭제 요청 전송 (Fetch API)
            fetch('/deleteTodos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(todoNos)
            })
                .then(response => {
                    if (response.ok) {
                        alert('삭제되었습니다.');
                        location.reload(); // 화면 새로고침
                    } else {
                        alert('삭제 처리에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('삭제 중 오류가 발생했습니다.');
                });
        });
    }

    let currentSelectedTodo = null;


    // ==========================================
    // 2. 상세 정보 패널 토글 로직
    // ==========================================
    const todoItems = document.querySelectorAll('.todo-item');
    const emptyPanel = document.getElementById('detailEmptyPanel');
    const detailPanel = document.getElementById('detailPanel');

    todoItems.forEach(item => {
        item.addEventListener('click', (e) => {
            if (e.target.closest('input[type="checkbox"]') || e.target.closest('button')) {
                return;
            }

            currentSelectedTodo = item;

            // 클릭된 항목 하이라이트 표시
            todoItems.forEach(el => el.classList.remove('bg-gray-50'));
            item.classList.add('bg-gray-50');

            // dataset 속성 가져오기
            const title = item.dataset.title || '제목 없음';
            const content = item.dataset.content || '내용이 없습니다.';
            const endDate = item.dataset.endDate || '기한 없음';
            const representatives = item.dataset.representatives || '미정';
            const requester = item.dataset.requester || '알 수 없음';
            const status = item.dataset.status;

            // DOM 요소에 값 대입
            document.getElementById('detailTitle').textContent = title;
            document.getElementById('detailContent').textContent = content;
            document.getElementById('detailEndDate').textContent = endDate;
            document.getElementById('detailRepresentatives').textContent = representatives;
            document.getElementById('detailRequester').textContent = requester;

            // 상태값에 따른 텍스트 및 버튼 스타일 변경
            const detailStatusText = document.getElementById('detailStatusText');
            const detailActionBtn = document.getElementById('detailActionBtn');
            const detailTodoNoInput = document.getElementById('detailTodoNoInput');
            const detailStatusInput = document.getElementById('detailStatusInput');

            if (detailTodoNoInput) {
                detailTodoNoInput.value = item.dataset.no;
            }

            if (status === '1') {
                if (detailStatusText) detailStatusText.textContent = '완료된 할 일';
                if (detailStatusInput) detailStatusInput.value = '0';
                if (detailActionBtn) {
                    detailActionBtn.textContent = '미완료로 변경';
                    detailActionBtn.className = 'w-full bg-gray-400 hover:bg-gray-500 text-white font-bold py-3.5 rounded-lg transition shadow-sm';
                }
            } else {
                if (detailStatusText) detailStatusText.textContent = '미완료된 할 일';
                if (detailStatusInput) detailStatusInput.value = '1';
                if (detailActionBtn) {
                    detailActionBtn.textContent = '완료하기';
                    detailActionBtn.className = 'w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3.5 rounded-lg transition shadow-sm';
                }
            }

            // 빈 화면 숨기고 상세 패널 표시
            if (emptyPanel) {
                emptyPanel.classList.remove('flex');
                emptyPanel.classList.add('hidden');
            }

            if (detailPanel) {
                detailPanel.classList.remove('hidden');
                detailPanel.classList.add('flex');
            }
        });
    });


    // ==========================================
    // 4. 모달창 열기/닫기 로직
    // ==========================================
    const todoModal = document.getElementById('todoModal');
    const openTodoModalBtn = document.getElementById('openTodoModalBtn');
    const editTodoBtn = document.getElementById('editTodoBtn');
    const closeTodoModalBtn = document.getElementById('closeTodoModalBtn');
    const cancelTodoModalBtn = document.getElementById('cancelTodoModalBtn');
    const modalTitleInput = document.getElementById('modalTitleInput');
    const modalContentInput = document.getElementById('modalContentInput');

    // 모달 열기
    if (openTodoModalBtn) {
        openTodoModalBtn.addEventListener('click', () => {
            if (modalTitleInput) modalTitleInput.value = '';
            if (modalContentInput) modalContentInput.value = '';
            if (todoModal) todoModal.classList.remove('hidden');
        });
    }

    if (editTodoBtn) {
        editTodoBtn.addEventListener('click', () => {
            if (!currentSelectedTodo) return;

            const title = currentSelectedTodo.dataset.title || '';
            const content = currentSelectedTodo.dataset.content || '';

            if (modalTitleInput) modalTitleInput.value = title;
            if (modalContentInput) modalContentInput.value = content;
            if (todoModal) todoModal.classList.remove('hidden');
        });
    }

    // 모달 닫기 함수
    const closeModal = () => {
        if (todoModal) todoModal.classList.add('hidden');
    };

    if (closeTodoModalBtn) closeTodoModalBtn.addEventListener('click', closeModal);
    if (cancelTodoModalBtn) cancelTodoModalBtn.addEventListener('click', closeModal);

    if (todoModal) {
        todoModal.addEventListener('click', (e) => {
            if (e.target === todoModal) closeModal();
        });
    }


    // ==========================================
    // 5. 기한(날짜) 선택 로직
    // ==========================================
    const todoEndDateInput = document.getElementById('todoEndDate');
    const customDateInput = document.getElementById('customDateInput');
    const dateBtns = document.querySelectorAll('.date-btn');

    const formatDate = (date) => {
        if (!date) return '';
        const y = date.getFullYear();
        const m = String(date.getMonth() + 1).padStart(2, '0');
        const d = String(date.getDate()).padStart(2, '0');
        return `${y}-${m}-${d}`;
    };

    const setActiveDateButton = (activeBtn) => {
        dateBtns.forEach(btn => {
            btn.classList.remove('bg-blue-600', 'text-white', 'font-medium');
            btn.classList.add('bg-white', 'text-gray-600');
        });

        if (activeBtn) {
            activeBtn.classList.remove('bg-white', 'text-gray-600');
            activeBtn.classList.add('bg-blue-600', 'text-white', 'font-medium');
        }
    };

    dateBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            setActiveDateButton(btn);

            const dateType = btn.dataset.dateType;
            let targetDate = new Date();

            if (dateType === 'none') {
                if (todoEndDateInput) todoEndDateInput.value = '';
                if (customDateInput) customDateInput.value = '';
                return;
            } else if (dateType === 'tomorrow') {
                targetDate.setDate(targetDate.getDate() + 1);
            } else if (dateType === 'nextWeek') {
                targetDate.setDate(targetDate.getDate() + 7);
            }

            const formattedDate = formatDate(targetDate);
            if (todoEndDateInput) todoEndDateInput.value = formattedDate;
            if (customDateInput) customDateInput.value = formattedDate;
        });
    });

    if (customDateInput) {
        customDateInput.addEventListener('change', (e) => {
            if (todoEndDateInput) todoEndDateInput.value = e.target.value;
            setActiveDateButton(null);
        });
    }

    // ==========================================
    // [추가됨] 동그란 체크박스(상태 변경) 클릭 이벤트
    // ==========================================
    const circleCheckboxes = document.querySelectorAll('.circle-checkbox');

    circleCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function(e) {
            // 이벤트 전파 방지 (상세 패널이 열리는 것을 막음)
            e.stopPropagation();

            const todoItem = this.closest('.todo-item');
            if (!todoItem) return;

            const todoNo = todoItem.dataset.no;
            // 체크박스가 체크되었으면 '1'(완료), 해제되었으면 '0'(미완료)
            const newStatus = this.checked ? '1' : '0';

            // 컨트롤러로 전송할 가짜(Hidden) 폼 동적 생성
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '/modifyTodoStatus';

            const noInput = document.createElement('input');
            noInput.type = 'hidden';
            noInput.name = 'todoNo';
            noInput.value = todoNo;

            const statusInput = document.createElement('input');
            statusInput.type = 'hidden';
            statusInput.name = 'status';
            statusInput.value = newStatus;

            form.appendChild(noInput);
            form.appendChild(statusInput);
            document.body.appendChild(form);

            // 폼 전송 (컨트롤러 실행 후 자동으로 화면 새로고침 됨)
            form.submit();
        });
    });

}); // DOMContentLoaded 종료


// ==========================================
// [전역 함수] 팝업창 및 담당자 추가 로직
// ==========================================
function openAddressPopup(url) {
    const title = "AddressBookPopup";
    const specs = "width=850,height=600,left=200,top=100,scrollbars=yes,resizable=yes";
    window.open(url, title, specs);
}

window.addRepresentative = function(empName, empId) {
    const container = document.getElementById('repListContainer');
    if (!container) return;

    const existing = container.querySelector(`input[value="${empId}"]`);
    if (existing) {
        alert("이미 추가된 담당자입니다.");
        return;
    }

    const span = document.createElement('span');
    span.className = "inline-flex items-center gap-1.5 px-3 py-1 bg-gray-100 text-gray-600 rounded-md text-[13px] font-medium";

    span.innerHTML = `
        ${empName} 
        <button type="button" class="hover:text-gray-800" onclick="this.closest('span').remove();">
            <i class="fa-solid fa-xmark text-[10px]"></i>
        </button>
        <input type="hidden" name="representUserNo" value="${empId}">
    `;

    container.appendChild(span);
};