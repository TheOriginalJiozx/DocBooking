fetch('/api/patient/check', {
    method: 'GET',
    headers: {
        'Authorization': localStorage.getItem('userEmail')
    }
})
    .then(response => {
        if (!response.ok) {
            window.location.href = 'error.html';
        }
    });