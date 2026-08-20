
const featureMap = {
    chat: { cards: 'cardsChat', visual: 'visualChat' },
    calendar: { cards: 'cardsCalendar', visual: 'visualCalendar' },
    task: { cards: 'cardsTask', visual: 'visualTask' },
    address: { cards: 'cardsAddress', visual: 'visualAddress' },
    admin: { cards: 'cardsAdmin', visual: 'visualAdmin' }
};

function selectFeature(key, btn) {
    // 탭 active 처리
    document.querySelectorAll('.feature-tab').forEach(t => t.classList.remove('active'));
    btn.classList.add('active');

    // 좌측 카드 전환
    Object.values(featureMap).forEach(v => {
        document.getElementById(v.cards).style.display = 'none';
        document.getElementById(v.visual).classList.remove('show');
    });
    document.getElementById(featureMap[key].cards).style.display = 'flex';
    document.getElementById(featureMap[key].visual).classList.add('show');
}

document.addEventListener("DOMContentLoaded", function() {
    
    const fileUploadInput = document.getElementById('file-upload');
    const fileNameDisplay = document.querySelector('.input-file-name');

    if (fileUploadInput && fileNameDisplay) { 
        
        fileUploadInput.addEventListener('change', function() {
            if (this.files && this.files.length > 0) {
                fileNameDisplay.value = this.files[0].name;
            } else {
                fileNameDisplay.value = '';
            }
        });
    }
    
});


