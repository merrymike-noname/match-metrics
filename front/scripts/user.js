document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('userSettingsForm');
    const favoriteTeamInput = document.getElementById('favoriteTeam');
    const successMessageDiv = document.getElementById('successMessage');
    const errorMessagesDiv = document.getElementById('errorMessages');

    const token = localStorage.getItem('token');
    const userEmail = localStorage.getItem('userEmail');

    let teams = [];
    let currentUserData = {};

    fetch('http://localhost:8080/matchmetrics/api/v0/teams/all?page=1&perPage=10000', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => response.json())
        .then(data => {
            teams = data.map(team => team.name);
            suggestTeams(favoriteTeamInput, teams);
        })
        .catch(error => console.error('Error:', error));

    fetch(`http://localhost:8080/matchmetrics/api/v0/users/${userEmail}`, {
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
            currentUserData = data;
            form.name.value = data.name;
            form.email.value = data.email;
            console.log(data);
            document.querySelector('.username').textContent = data.name;

            // Fetch the favorite team by ID
            fetch(`http://localhost:8080/matchmetrics/api/v0/teams/${data.favouriteTeam.id}`, {
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
                .then(teamData => {
                    form.favoriteTeam.value = teamData.name;
                })
                .catch(error => console.error('Error fetching team data:', error));
        })
        .catch(error => console.error('Error fetching user data:', error));

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        const name = form.name.value.trim() || currentUserData.name;
        const email = form.email.value.trim() || currentUserData.email;
        const favoriteTeam = form.favoriteTeam.value.trim() || currentUserData.favouriteTeam.name;
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
            name,
            email,
            favouriteTeam: favoriteTeam
        };

        if (password) {
            requestData.password = password;
        }

        fetch(`http://localhost:8080/matchmetrics/api/v0/users/update/${userEmail}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data) {
                    showSuccess('Settings updated successfully.');
                    currentUserData = requestData;
                } else {
                    showError('Failed to update settings.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showError('An error occurred while updating settings.');
            });
    });

    function showError(message) {
        const errorMessage = document.createElement('p');
        errorMessage.textContent = message;
        errorMessagesDiv.appendChild(errorMessage);
    }

    function showSuccess(message) {
        const successMessage = document.createElement('p');
        successMessage.textContent = message;
        successMessageDiv.appendChild(successMessage);
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
