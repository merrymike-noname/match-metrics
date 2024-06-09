document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('userSettingsForm');
    const favoriteTeamInput = document.getElementById('favoriteTeam');
    const successMessageDiv = document.getElementById('successMessage');
    const errorMessagesDiv = document.getElementById('errorMessages');
    const logoutButton = document.getElementById('logoutButton');
    const usernameSpan = document.getElementById('username');


    const token = localStorage.getItem('token');
    const userEmail = localStorage.getItem('userEmail');

    let teams = [];
    let currentUserData = {};

    if (!userEmail || !token) {
        window.location.href = 'login.html';
        return;
    }

    const checkForbidden = response => {
        if (response.status === 403) {
            console.error('403 Forbidden:', response);
            throw new Error('403 Forbidden');
        }
        return response;
    };

    fetch('http://localhost:8080/matchmetrics/api/v0/teams/all?page=1&perPage=10000', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(checkForbidden)
        .then(response => response.json())
        .then(data => {
            teams = data.map(team => team.name);
            suggestTeams(favoriteTeamInput, teams);
        })
        .catch(error => console.error('Error fetching teams:', error));

    fetch(`http://localhost:8080/matchmetrics/api/v0/users/${userEmail}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(checkForbidden)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            currentUserData = data;
            form.name.value = data.name;
            form.email.value = data.email;
            usernameSpan.textContent = data.name;
            console.log('Current user data:', data.favouriteTeam);
            form.favoriteTeam.value = data.favouriteTeam;
            console.log('Current user data:', data);

            if (currentUserData.role === 'ROLE_ADMIN') {
                const adminPanelButtonContainer = document.createElement('div');
                adminPanelButtonContainer.id = 'adminPanelButtonContainer';
                adminPanelButtonContainer.style.position = 'fixed';
                adminPanelButtonContainer.style.top = '70%';
                adminPanelButtonContainer.style.left = '50%';
                adminPanelButtonContainer.style.transform = 'translate(-50%, -50%)';
                adminPanelButtonContainer.style.display = 'flex';
                adminPanelButtonContainer.style.alignItems = 'center';
                adminPanelButtonContainer.style.justifyContent = 'center';

                const adminPanelButton = document.createElement('button');
                adminPanelButton.textContent = 'Go to admin panel';
                adminPanelButton.addEventListener('click', function () {
                    window.location.href = 'admin.html';
                });

                adminPanelButtonContainer.appendChild(adminPanelButton);

                document.body.appendChild(adminPanelButtonContainer);
            }
        })


    form.addEventListener('submit', function (event) {
        event.preventDefault();
        const name = form.name.value.trim() || currentUserData.name;
        const email = form.email.value.trim() || currentUserData.email;
        const favoriteTeam = form.favoriteTeam.value.trim() || currentUserData.favouriteTeam;
        const password = form.password.value.trim();
        const confirmPassword = form.confirmPassword.value.trim();

        errorMessagesDiv.innerHTML = '';
        successMessageDiv.style.display = 'none';
        successMessageDiv.innerHTML = '';

        if (password && password !== confirmPassword) {
            showError('Passwords do not match.');
            return;
        }

        if (password && password.length < 6) {
            showError('Password must be at least 6 characters long.');
            return;
        }

        const requestData = {
            name: name,
            email: email,
            favouriteTeam: favoriteTeam
        };

        if (password) {
            requestData.password = password;
        }

        console.log('Request data:', JSON.stringify(requestData));

        fetch(`http://localhost:8080/matchmetrics/api/v0/users/update/${userEmail}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
            .then(checkForbidden)
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message); });
                }
                return response.json();
            })
            .then(data => {
                console.log('Response data:', data);
                if (data.name !== undefined && data.email !== undefined && data.favouriteTeam !== undefined) {
                    currentUserData.name = data.name;
                    currentUserData.email = data.email;
                    currentUserData.favouriteTeam = data.favouriteTeam;

                    form.name.value = data.name;
                    form.email.value = data.email;
                    form.favoriteTeam.value = data.favouriteTeam;
                    usernameSpan.textContent = data.name;
                    showSuccess('Settings updated successfully.');
                } else {
                    showError('Failed to update settings: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showError('An error occurred while updating settings.');
            });
    });

        logoutButton.addEventListener('click', function () {
        localStorage.removeItem('token');
        localStorage.removeItem('userEmail');
        window.location.href = 'login.html';
    });

    function showError(message) {
        const errorMessage = document.createElement('p');
        errorMessage.textContent = message;
        errorMessagesDiv.appendChild(errorMessage);
    }

    function showSuccess(message) {
        successMessageDiv.textContent = message;
        successMessageDiv.style.display = 'block';
    }

    function suggestTeams(input, suggestions) {
        input.addEventListener('input', function () {
            const inputValue = input.value.toLowerCase();
            const filteredSuggestions = suggestions.filter(team =>
                team.toLowerCase().startsWith(inputValue)
            );
            const datalist = document.getElementById(input.id + 'List');
            if (datalist) {
                datalist.parentNode.removeChild(datalist);
            }
            if (filteredSuggestions.length > 0) {
                const newDatalist = document.createElement('datalist');
                newDatalist.id = input.id + 'List';
                filteredSuggestions.forEach(team => {
                    const option = document.createElement('option');
                    option.value = team;
                    newDatalist.appendChild(option);
                });
                input.parentNode.appendChild(newDatalist);
                input.setAttribute('list', newDatalist.id);
            } else {
                input.removeAttribute('list');
            }
        });
    }
});
