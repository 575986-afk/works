// 별 아이콘(즐겨찾기) 토글[cite: 3]
function toggleStar(element, targetNo) {
    const icon = element.querySelector('i');
    const isCurrentlyStarred = icon.classList.contains('fa-solid'); // 현재 즐겨찾기 상태인지 확인

    // fa-solid(채워진 별)가 있으면 삭제(remove) 요청, 없으면 추가(add) 요청
    const action = isCurrentlyStarred ? 'remove' : 'add';

    $.ajax({
        url: '/toggleFavorite',
        type: 'POST',
        data: {
            targetNo: targetNo,
            action: action
        },
        success: function(response) {
            if (response === 'success') {
                // DB 처리 성공 시에만 화면의 별 아이콘 변경
                if (isCurrentlyStarred) {
                    icon.classList.remove('fa-solid', 'text-yellow-400');
                    icon.classList.add('fa-regular', 'text-gray-300');
                } else {
                    icon.classList.remove('fa-regular', 'text-gray-300');
                    icon.classList.add('fa-solid', 'text-yellow-400');
                }
            } else {
                alert('즐겨찾기 처리에 실패했습니다.');
            }
        },
        error: function() {
            alert('서버 통신 중 오류가 발생했습니다.');
        }
    });
}

// 로우 선택 및 우측 상세 패널 열기[cite: 3]
function selectRow(row, event) {
    document.querySelectorAll('.contact-row').forEach(r => {
        r.classList.remove('table-row-selected');
    });
    row.classList.add('table-row-selected');

    const initial = row.querySelector('.data-initial').value;
    let color = row.querySelector('.data-color').value;
    const name = row.querySelector('.data-name').value;
    let title = row.querySelector('.data-title').value;
    const org = row.querySelector('.data-org').value;
    const dept = row.querySelector('.data-dept').value;
    const email = row.querySelector('.data-email').value;
    var targetUserNo = $(row).find('.data-userno').val();
    const userNo = row.querySelector('.data-userno').value;
    const isBookmarked = row.querySelector('.data-bookmark').value;

    if (!title || title.trim() === '') title = '직급 없음';

    const avatarElem = document.getElementById('panelAvatar');
    avatarElem.className = `w-20 h-20 rounded-full text-white flex items-center justify-center font-medium text-3xl shrink-0 ${color}`;
    avatarElem.innerText = initial;

    document.getElementById('panelName').innerText = name;
    document.getElementById('panelTitle').innerText = title;
    document.getElementById('panelOrg').innerText = org;
    document.getElementById('panelDept').innerText = dept;


    const deptTitleWrapper = document.getElementById('panelDeptTitle');
    if (dept && dept.trim() !== '') {
        deptTitleWrapper.style.display = 'block';
    } else {
        deptTitleWrapper.style.display = 'none';
    }

    document.getElementById('panelEmail').innerText = email;
    document.getElementById('panelEmail').href = `mailto:${email}`;

    const panelStarBtn = document.getElementById('panelStarBtn');
    panelStarBtn.setAttribute('data-userno', userNo); // 클릭 시 toggleStar로 넘겨줄 사번 세팅

    const panelStarIcon = panelStarBtn.querySelector('i');
    if (isBookmarked === '1') {
        // 북마크 된 상태: 노란색 꽉 찬 별
        panelStarIcon.className = 'fa-solid fa-star text-yellow-400';
    } else {
        // 북마크 안 된 상태: 회색 빈 별
        panelStarIcon.className = 'fa-regular fa-star text-gray-300 hover:text-yellow-400';
    }

    const panel = document.getElementById('detailPanel');
    panel.classList.remove('translate-x-full');

    $.ajax({
        url: '/api/log/addressDetail', // 로그를 처리할 백엔드 URL
        type: 'POST',
        data: { userNo: targetUserNo },
        success: function(response) {
            console.log("상세조회 로그 저장 성공"); // 필요시 콘솔 확인용
        },
        error: function(xhr, status, error) {
            console.error("로그 저장 실패", error);
        }
    });

}

// 우측 상세 패널 닫기[cite: 3]
function closePanel() {
    const panel = document.getElementById('detailPanel');
    panel.classList.add('translate-x-full');

    document.querySelectorAll('.contact-row').forEach(r => {
        r.classList.remove('table-row-selected');
    });
}

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

        if (checkAllBtn) {
            checkAllBtn.checked = (total > 0 && total === checkedCount);
        }
        window.updatePreview();
    });

	
    // 4. 저장 버튼 클릭 시 배열 추출 및 Ajax 전송[cite: 3]
    $('#saveUsersBtn').off('click').on('click', function() {
        const checkedBoxes = $('.user-checkbox:checked');

        const selectedUserNos = checkedBoxes.map(function() {
            return $(this).val();
        }).get();

        if (selectedUserNos.length === 0) {
            alert('선택된 사원이 없습니다.');
            return;
        }

        $.ajax({
            url: '/api/users/save',
            type: 'POST',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({ userNos: selectedUserNos }),
            success: function(response) {
                window.close();
            },
            error: function(xhr, status, error) {
                console.error('저장 실패:', error);
                alert('저장 중 오류가 발생했습니다.');
            }
        });
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