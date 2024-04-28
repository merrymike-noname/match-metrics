document.addEventListener('DOMContentLoaded', function() {
    const team1Input = document.getElementById('team1');
    const team2Input = document.getElementById('team2');
    const forecastButton = document.getElementById('forecastButton');
    const matchInfoDiv = document.getElementById('matchInfo');

    let teams = [];

    fetch('http://localhost:8080/matchmetrics/api/v0/teams/all')
        .then(response => response.json())
        .then(data => {
            teams = data.map(team => team.name);
            suggestTeams(team1Input, teams);
            suggestTeams(team2Input, teams);
        })
        .catch(error => console.error('Error:', error));

    forecastButton.addEventListener('click', function() {
        const team1 = team1Input.value.trim();
        const team2 = team2Input.value.trim();
        const team1Exists = teams.includes(team1);
        const team2Exists = teams.includes(team2);

        if (team1Exists && team2Exists) {
            fetch(`http://localhost:8080/matchmetrics/api/v0/teams/compare?homeTeam=${team1}&awayTeam=${team2}`)
                .then(response => response.json())
                .then(teams => {
                    matchInfoDiv.innerHTML = '';
                    const teamEloClasses = teams[0].elo > teams[1].elo ? ['high-elo', 'low-elo'] : ['low-elo', 'high-elo'];
                    teams.forEach((team, index) => {
                        const teamDiv = document.createElement('div');
                        teamDiv.className = 'team';

                        const teamLogo = document.createElement('img');
                        teamLogo.src = `/front/resources/logos/${team.name.toLowerCase()}.png`;
                        teamLogo.onerror = function() {
                            this.onerror = null;
                            this.src = '/front/resources/logos/defoult.png';
                        };
                        teamDiv.appendChild(teamLogo);

                        const teamName = document.createElement('h2');
                        teamName.textContent = team.name;
                        teamDiv.appendChild(teamName);

                        const teamCountry = document.createElement('p');
                        teamCountry.textContent = `Country: ${team.country}`;
                        teamDiv.appendChild(teamCountry);

                        const teamElo = document.createElement('p');
                        teamElo.textContent = `ELO: ${team.elo}`;
                        teamElo.className = teamEloClasses[index];
                        teamDiv.appendChild(teamElo);

                        matchInfoDiv.appendChild(teamDiv);
                    });
                })
                .catch(error => console.error('Error:', error));
        } else {
            alert("Enter two real teams.");
        }
    });

    function suggestTeams(input, suggestions) {
        input.addEventListener('input', function() {
            const inputValue = input.value.toLowerCase();
            const filteredSuggestions = suggestions.filter(team =>
                team.toLowerCase().startsWith(inputValue)
            );

            const datalist = document.getElementById(input.id + 'List');
            if (datalist) {
                datalist.parentNode.removeChild(datalist);
            }
            if (filteredSuggestions.length > 0) {
                const datalist = document.createElement('datalist');
                datalist.id = input.id + 'List';
                filteredSuggestions.forEach(team => {
                    const option = document.createElement('option');
                    option.value = team;
                    datalist.appendChild(option);
                });
                input.parentNode.appendChild(datalist);
                input.setAttribute('list', datalist.id);
            }
        });
    }
});