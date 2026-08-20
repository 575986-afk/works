
// 조직도 토글[cite: 3]
function toggleMenu(element) {
    const childMenu = element.nextElementSibling;
    const icon = element.querySelector('.fa-caret-down');

    if (childMenu) {
        childMenu.classList.toggle('hidden');
    }
    if (icon) {
        icon.classList.toggle('-rotate-90');
    }
}

// ------------------------------------------------------------------
// 하단 미리보기 업데이트 함수 (전역)
// ------------------------------------------------------------------
window.updatePreview = function() {
    const previewContainer = document.getElementById('selected-preview');
    const checkedBoxes = document.querySelectorAll('.user-checkbox:checked');
    previewContainer.innerHTML = ''; // 기존 내용 초기화

    const maxDisplay = 5;
    const totalChecked = checkedBoxes.length;

    for (let i = 0; i < totalChecked; i++) {
        if (i >= maxDisplay) {
            const moreSpan = document.createElement('span');
            moreSpan.className = 'text-xs text-gray-500 font-medium ml-1';
            moreSpan.textContent = `외 ${totalChecked - maxDisplay}명`;
            previewContainer.appendChild(moreSpan);
            break;
        }
        const name = checkedBoxes[i].getAttribute('data-name');
        const badge = document.createElement('span');
        badge.className = 'bg-blue-50 text-blue-700 border border-blue-200 text-xs px-2 py-1 rounded-md whitespace-nowrap';
        badge.textContent = name;
        previewContainer.appendChild(badge);
    }
};

// ------------------------------------------------------------------
// 검색 결과 클릭 시 실행되는 함수
// ------------------------------------------------------------------
window.addFromSearch = function(userNo, userName) {
    // 1. 이미 화면 리스트에 해당 사원의 체크박스가 있는지 확인
    let $checkbox = $('.user-checkbox[value="' + userNo + '"]');
    
    if ($checkbox.length > 0) {
        // 화면에 있으면 강제로 체크
        $checkbox.prop('checked', true);
    } else {
        // 화면에 없으면 (다른 부서/그룹 사원인 경우) 보이지 않는 hidden 체크박스를 만들어 리스트 컨테이너에 추가
        const hiddenCb = `<input type="checkbox" class="user-checkbox hidden" value="${userNo}" data-name="${userName}" checked>`;
        $('.space-y-4').append(hiddenCb);
    }
    
    // 2. 하단 미리보기 배지 및 전체선택 동기화 업데이트
    window.updatePreview();
    const total = $('.user-checkbox:not(.hidden)').length;
    const checkedCount = $('.user-checkbox:not(.hidden):checked').length;
    const checkAllBtn = document.getElementById('checkAllBtn');
    if(checkAllBtn) {
        checkAllBtn.checked = (total > 0 && total === checkedCount);
    }
    
    // 3. 검색창 초기화 및 결과창 숨기기
    $('#searchInput').val('');
    $('#searchResults').addClass('hidden');
};

// ------------------------------------------------------------------
// 페이지 로드 후 실행되는 DOM 이벤트들
// ------------------------------------------------------------------
document.addEventListener('DOMContentLoaded', function() {
    const checkAllBtn = document.getElementById('checkAllBtn');

    // 1. 전체 선택/해제 이벤트 (hidden 체크박스는 제외)
    if (checkAllBtn) {
        checkAllBtn.addEventListener('change', function() {
            const isChecked = this.checked;
            $('.user-checkbox:not(.hidden)').prop('checked', isChecked);
            window.updatePreview();
        });
    }

    // 2. 개별 체크박스 변경 시 이벤트 (jQuery 이벤트 위임 방식 사용 - 동적 생성 요소 지원)
    $(document).on('change', '.user-checkbox', function() {
        const total = $('.user-checkbox:not(.hidden)').length;
        const checkedCount = $('.user-checkbox:not(.hidden):checked').length;
        
        if(checkAllBtn) {
            checkAllBtn.checked = (total > 0 && total === checkedCount);
        }
        window.updatePreview();
    });

    // 3. 취소 버튼 동작[cite: 3]
    $('#cancelBtn').on('click', function() {
        window.close();
    });

    // 4. 저장 버튼 클릭 시 배열 추출 및 Ajax 전송[cite: 3]
	$('#saveUsersBtn').off('click').on('click', function() {
	        const checkedBoxes = $('.user-checkbox:checked');

	        if (checkedBoxes.length === 0) {
	            alert('선택된 사원이 없습니다.');
	            return;
	        }

	        // 부모 창이 존재하고 닫히지 않았는지 확인
	        if (window.opener && !window.opener.closed) {
	            // 체크된 사원들을 순회하며 부모 창의 함수 호출
	            checkedBoxes.each(function() {
	                const userNo = $(this).val(); // value에 있는 사번 추출
	                const userName = $(this).attr('data-name'); // data-name 속성에 있는 이름 추출
	                
	                // 부모 창에 만들어둔 addRepresentative 함수 실행
	                if(typeof window.opener.addRepresentative === 'function') {
	                    window.opener.addRepresentative(userName, userNo);
	                }
	            });
	        } else {
	            console.warn('부모 창을 찾을 수 없습니다.');
	        }

	        // 정보 전달 후 팝업 닫기
	        window.close(); 
	    });
});

// ------------------------------------------------------------------
// 검색창 관련 AJAX 로직[cite: 3]
// ------------------------------------------------------------------
$(document).ready(function() {
    let searchTimeout;

    // 검색창 입력 이벤트[cite: 3]
    $('#searchInput').on('input', function() {
        clearTimeout(searchTimeout); 
        const keyword = $(this).val().trim();
        const $results = $('#searchResults');

        if (keyword.length === 0) {
            $results.addClass('hidden').empty();
            return;
        }

        // 300ms(0.3초) 동안 입력이 없으면 서버로 AJAX 요청[cite: 3]
        searchTimeout = setTimeout(function() {
            $.ajax({
                url: '/address/search', 
                type: 'GET',
                data: { keyword: keyword },
                success: function(response) {
                    $results.empty();
                    
                    if (response && response.length > 0) {
                        let html = '<ul class="py-2">';
                        
                        response.forEach(function(item) {
                            // onclick 부분을 addFromSearch 호출로 수정
                            html += `
                                <li class="px-4 py-2 hover:bg-gray-50 cursor-pointer flex flex-col border-b border-gray-100 last:border-0"
                                    onclick="addFromSearch('${item.userNo}', '${item.userName}')">
                                    <div class="flex justify-between items-center">
                                        <span class="font-bold text-sm text-gray-800">${item.userName}</span>
                                        <span class="text-xs px-2 py-0.5 bg-blue-50 text-blue-600 rounded">${item.organizationName || item.groupName || '-'}</span>
                                    </div>
                                    <div class="text-xs text-gray-500 mt-1">${item.phone || item.email || ''}</div>
                                </li>
                            `;
                        });
                        
                        html += '</ul>';
                        $results.html(html).removeClass('hidden');
                    } else {
                        $results.html('<div class="p-4 text-sm text-center text-gray-500">일치하는 결과가 없습니다.</div>').removeClass('hidden');
                    }
                },
                error: function() {
                    $results.html('<div class="p-4 text-sm text-center text-red-500">검색 중 오류가 발생했습니다.</div>').removeClass('hidden');
                }
            });
        }, 300); 
    });

    // 외부 영역 클릭 시 검색 결과창 닫기[cite: 3]
    $(document).on('click', function(e) {
        if (!$(e.target).closest('#searchContainer').length) {
            $('#searchResults').addClass('hidden');
        }
    });

    // 검색창을 다시 클릭했을 때 입력값이 있으면 결과창 다시 열기[cite: 3]
    $('#searchInput').on('click', function() {
        if ($(this).val().trim().length > 0 && $('#searchResults').children().length > 0) {
            $('#searchResults').removeClass('hidden');
        }
    });
});