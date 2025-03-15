fetch('/api/admin/check', {
    method: 'GET',
    headers: {
        'Authorization': localStorage.getItem('empId')
    }
})
    .then(response => {
        if (!response.ok) {
            window.location.href = 'error.html';
        }
    });