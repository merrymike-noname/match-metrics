document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('token');
    const resultDiv = document.getElementById('result');
    const emailsDatalist = document.getElementById('emails');

    // Function to show a specific form and hide others
    function showForm(formId) {
        const forms = document.querySelectorAll('.form-container');
        forms.forEach(form => {
            form.style.display = 'none';
        });
        document.getElementById(formId).style.display = 'block';
    }

    // Event listeners for navigation buttons
    document.getElementById('addMatchButton').addEventListener('click', function () {
        showForm('addMatchForm');
    });
    document.getElementById('deleteMatchButton').addEventListener('click', function () {
        showForm('deleteMatchForm');
    });
    document.getElementById('banUserButton').addEventListener('click', function () {
        showForm('banUserForm');
    });
    document.getElementById('assignAdminButton').addEventListener('click', function () {
        showForm('assignAdminForm');
    });

    // Show the add match form by default
    showForm('addMatchForm');

    // Fetch all users and populate the email datalist
    function fetchAllUsers() {
        fetch('http://localhost:8080/matchmetrics/api/v0/users/all', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (!Array.isArray(data)) {
                    throw new Error('Data is not an array');
                }
                emailsDatalist.innerHTML = '';
                data.forEach(user => {
                    if (user.email) {
                        const option = document.createElement('option');
                        option.value = user.email;
                        emailsDatalist.appendChild(option);
                    }
                });
            })
            .catch(error => console.error('Error:', error));
    }

    fetchAllUsers();

    // Helper function to display messages
    function displayMessage(message, isSuccess = true) {
        resultDiv.textContent = message;
        resultDiv.style.color = isSuccess ? 'green' : 'red';
    }

    // Add Match
    const addMatchForm = document.getElementById('addMatchFormData');
    addMatchForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this);
        const matchData = {
            date: formData.get('date'),
            league: formData.get('league'),
            homeTeam: { name: formData.get('homeTeam') },
            awayTeam: { name: formData.get('awayTeam') },
            probability: {
                homeTeamWin: parseFloat(formData.get('homeTeamWin')),
                draw: parseFloat(formData.get('draw')),
                awayTeamWin: parseFloat(formData.get('awayTeamWin'))
            }
        };

        fetch('http://localhost:8080/matchmetrics/api/v0/matches/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(matchData)
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message); });
                }
                return response.json();
            })
            .then(data => {
                displayMessage('Match added successfully.');
                console.log('Success:', data);
            })
            .catch(error => {
                console.error('Error:', error);
                displayMessage('Failed to add match.', false);
            });
    });

    // Delete Match
    const deleteMatchForm = document.getElementById('deleteMatchFormData');
    deleteMatchForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const matchId = document.getElementById('matchId').value;

        fetch(`http://localhost:8080/matchmetrics/api/v0/matches/delete/${matchId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message); });
                }
                displayMessage('Match deleted successfully.');
            })
            .catch(error => {
                console.error('Error:', error);
                displayMessage('Failed to delete match.', false);
            });
    });

    // Ban User
    const banUserForm = document.getElementById('banUserFormData');
    banUserForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const email = document.getElementById('userBanEmail').value;

        fetch(`http://localhost:8080/matchmetrics/api/v0/users/delete/${email}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message); });
                }
                displayMessage('User banned (deleted) successfully.');
            })
            .catch(error => {
                console.error('Error:', error);
                displayMessage('Failed to ban (delete) user.', false);
            });
    });

    // Assign Admin
    const assignAdminForm = document.getElementById('assignAdminFormData');
    assignAdminForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const email = document.getElementById('userAdminEmail').value;

        fetch(`http://localhost:8080/matchmetrics/api/v0/users/admin/${email}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message); });
                }
                displayMessage('Admin assigned successfully.');
            })
            .catch(error => {
                console.error('Error:', error);
                displayMessage('Failed to assign admin.', false);
            });
    });
});
