document.addEventListener('DOMContentLoaded', function () {
    const addMatchForm = document.getElementById('addMatchForm');
    const deleteMatchForm = document.getElementById('deleteMatchForm');
    const searchUserForm = document.getElementById('searchUserForm');
    const banUserForm = document.getElementById('banUserForm'); // Corrected ID
    const assignAdminForm = document.getElementById('assignAdminForm'); // Corrected ID
    const resultDiv = document.getElementById('result');

    addMatchForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this);
        console.log(formData);
        fetch('http://localhost:8080/matchmetrics/api/v0/matches/add', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
                resultDiv.textContent = JSON.stringify(data);
            })
            .catch(error => console.error('Error:', error));
    });

    deleteMatchForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const matchId = document.getElementById('matchId').value;
        fetch(`http://localhost:8080/matchmetrics/api/v0/matches/delete/${matchId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    resultDiv.textContent = 'Match deleted successfully.';
                } else {
                    resultDiv.textContent = 'Failed to delete match.';
                }
            })
            .catch(error => console.error('Error:', error));
    });

    searchUserForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const email = document.getElementById('email').value;
        fetch(`http://localhost:8080/matchmetrics/api/v0/users/search?email=${email}`)
            .then(response => response.json())
            .then(data => {
                resultDiv.textContent = JSON.stringify(data);
            })
            .catch(error => console.error('Error:', error));
    });

    banUserForm.addEventListener('submit', function (event) { // Corrected event listener
        event.preventDefault();
        const userId = document.getElementById('userBanId').value; // Corrected ID
        fetch(`http://localhost:8080/matchmetrics/api/v0/users/${userId}/ban`, {
            method: 'PUT'
        })
            .then(response => {
                if (response.ok) {
                    resultDiv.textContent = 'User banned successfully.';
                } else {
                    resultDiv.textContent = 'Failed to ban user.';
                }
            })
            .catch(error => console.error('Error:', error));
    });

    assignAdminForm.addEventListener('submit', function (event) { // Corrected event listener
        event.preventDefault();
        const userId = document.getElementById('userId').value;
        fetch(`http://localhost:8080/matchmetrics/api/v0/users/${userId}/grantAdmin`, {
            method: 'PUT'
        })
            .then(response => {
                if (response.ok) {
                    resultDiv.textContent = 'Admin assigned successfully.';
                } else {
                    resultDiv.textContent = 'Failed to assign admin.';
                }
            })
            .catch(error => console.error('Error:', error));
    });
});
