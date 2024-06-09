document.addEventListener('DOMContentLoaded', function () {
    const teamInput = document.getElementById('teamInput');
    const teamLogoDiv = document.getElementById('teamLogo');
    const teamInfoDiv = document.getElementById('teamInfo');
    const matchInfoDiv = document.getElementById('matchInfo');

    const userEmail = localStorage.getItem('userEmail');
    const token = localStorage.getItem('token');
    let favoriteTeam = 'Girona';

    if (!userEmail || !token) {
        window.location.href = 'login.html';
        return;
    }

    const checkForbidden = response => {
        if (response.status === 403) {
            window.location.href = 'login.html';
            throw new Error('403 Forbidden');
        }
        return response;
    };

    fetch(`http://localhost:8080/matchmetrics/api/v0/users/name/${userEmail}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(checkForbidden)
        .then(response => response.text())
        .then(name => {
            const usernameLink = document.getElementById('username');
            usernameLink.textContent = name;
        })
        .catch(error => console.error('Error:', error));

    let teams = [];

    fetch(`http://localhost:8080/matchmetrics/api/v0/users/${userEmail}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(checkForbidden)
        .then(response => response.json())
        .then(data => {
            if (data) {
                console.log(data);
                favoriteTeam = data.favouriteTeam;
                teamInput.value = favoriteTeam;

            }
        })
        .catch(error => console.error('Error:', error));

    teamInput.value = favoriteTeam;

    fetch('http://localhost:8080/matchmetrics/api/v0/teams/all?page=1&perPage=10000', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(checkForbidden)
        .then(response => response.json())
        .then(data => {
            teams = data.map(team => team.name);
            suggestTeams(teamInput, teams);
            teamInput.dispatchEvent(new Event('input'));
        })
        .catch(error => console.error('Error:', error));

    teamInput.addEventListener('input', function () {
        const teamName = teamInput.value.trim();
        if (teams.includes(teamName)) {
            fetch(`http://localhost:8080/matchmetrics/api/v0/teams?name=${teamName}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
                .then(checkForbidden)
                .then(response => response.json())
                .then(teams => {
                    teamInfoDiv.style.display = 'block';
                    matchInfoDiv.style.display = 'block';
                    const team = teams[0];
                    teamLogoDiv.innerHTML = '';
                    const teamLogo = document.createElement('img');
                    teamLogo.src = `/front/resources/logos/${team.name.toLowerCase()}.png`;
                    teamLogo.onerror = function () {
                        this.onerror = null;
                        this.src = '/front/resources/logos/default.png';
                    };
                    teamLogo.style.width = '80px';
                    teamLogo.style.height = '80px';
                    teamLogoDiv.appendChild(teamLogo);

                    const teamNameElement = document.createElement('span');
                    teamNameElement.textContent = team.name.toUpperCase();
                    teamNameElement.style.fontWeight = 'bold';
                    teamLogoDiv.appendChild(teamNameElement);

                    teamInfoDiv.style.backgroundColor = 'white';
                    teamInfoDiv.style.borderRadius = '10px';
                    teamInfoDiv.style.padding = '10px';
                    teamInfoDiv.style.boxShadow = '0px 0px 10px rgba(0,0,0,0.1)';
                    teamInfoDiv.style.margin = '10px 5px';
                    teamInfoDiv.style.width = '30%';
                    teamInfoDiv.innerHTML = '';

                    const teamElo = document.createElement('p');
                    teamElo.textContent = `ELO: ${Math.round(team.elo)}`;
                    teamInfoDiv.appendChild(teamElo);

                    fetch(`http://localhost:8080/matchmetrics/api/v0/matches?homeTeam=${teamName}`, {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    })
                        .then(checkForbidden)
                        .then(response => response.json())
                        .then(matches => {
                            if (matches.length > 0) {
                                displayMatch(matches[0]);
                            } else {
                                fetch(`http://localhost:8080/matchmetrics/api/v0/matches?awayTeam=${teamName}`, {
                                    headers: {
                                        'Authorization': `Bearer ${token}`
                                    }
                                })
                                    .then(checkForbidden)
                                    .then(response => response.json())
                                    .then(matches => {
                                        if (matches.length > 0) {
                                            displayMatch(matches[0]);
                                        } else {
                                            matchInfoDiv.textContent = 'No matches in the nearest time.';
                                        }
                                    })
                                    .catch(error => console.error('Error:', error));
                            }
                        })
                        .catch(error => console.error('Error:', error));
                })
                .catch(error => console.error('Error:', error));
        }
    });

    function displayMatch(match) {
        matchInfoDiv.innerHTML = '';

        const logosDiv = document.createElement('div');
        logosDiv.style.display = 'flex';
        logosDiv.style.justifyContent = 'center';
        logosDiv.style.alignItems = 'center';

        const homeTeamLogo = document.createElement('img');
        homeTeamLogo.src = `/front/resources/logos/${match.homeTeam.name.toLowerCase()}.png`;
        homeTeamLogo.onerror = function () {
            this.onerror = null;
            this.src = '/front/resources/logos/default.png';
        };
        homeTeamLogo.style.width = '45px';
        homeTeamLogo.style.height = '45px';
        homeTeamLogo.style.margin = '0 150px 0 1px';
        logosDiv.appendChild(homeTeamLogo);

        const awayTeamLogo = document.createElement('img');
        awayTeamLogo.src = `/front/resources/logos/${match.awayTeam.name.toLowerCase()}.png`;
        awayTeamLogo.onerror = function () {
            this.onerror = null;
            this.src = '/front/resources/logos/default.png';
        };
        awayTeamLogo.style.width = '45px';
        awayTeamLogo.style.height = '45px';
        logosDiv.appendChild(awayTeamLogo);

        matchInfoDiv.appendChild(logosDiv);

        const namesDiv = document.createElement('div');
        namesDiv.textContent = `${match.homeTeam.name} vs ${match.awayTeam.name}`;
        namesDiv.style.display = 'flex';
        namesDiv.style.justifyContent = 'center';
        namesDiv.style.alignItems = 'center';
        matchInfoDiv.appendChild(namesDiv);

        const dateDiv = document.createElement('div');
        dateDiv.textContent = new Date(match.date).toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
        dateDiv.style.display = 'flex';
        dateDiv.style.justifyContent = 'center';
        dateDiv.style.alignItems = 'center';
        matchInfoDiv.appendChild(dateDiv);
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
            }
        });
    }
});
