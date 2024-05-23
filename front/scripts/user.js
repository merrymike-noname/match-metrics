document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('userSettingsForm');
    const favoriteTeamInput = document.getElementById('favoriteTeam');
    const errorMessagesDiv = document.getElementById('errorMessages');

    let teams = [];

    fetch('http://localhost:8080/matchmetrics/api/v0/teams/all')
        .then(response => response.json())
        .then(data => {
            teams = data.map(team => team.name);
            suggestTeams(favoriteTeamInput, teams);
        })
        .catch(error => console.error('Error:', error));

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        const name = form.name.value.trim();
        const email = form.email.value.trim();
        const favoriteTeam = form.favoriteTeam.value.trim();
        const password = form.password.value.trim();
        const confirmPassword = form.confirmPassword.value.trim();

        errorMessagesDiv.innerHTML = '';

        if (password !== confirmPassword) {
            showError('Passwords do not match.');
            return;
        }

        if (password.length < 6) {
            showError('Password must be at least 6 characters long.');
            return;
        }

        const requestData = {
            name,
            email,
            favoriteTeam,
            password
        };

        fetch('http://localhost:8080/matchmetrics/api/v0/user/settings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Settings updated successfully.');
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
