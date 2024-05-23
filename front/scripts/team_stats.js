document.addEventListener('DOMContentLoaded', function () {
    const teamInput = document.getElementById('teamInput');
    const teamLogoDiv = document.getElementById('teamLogo');
    const teamNameDiv = document.getElementById('teamName');
    const teamInfoDiv = document.getElementById('teamInfo');
    const teamMatchDiv = document.getElementById('nextMatchBanner');
    const teamLogosDiv = document.querySelector('.team-logos');
    const teamNamesDiv = document.querySelector('.team-names');
    const matchDateDiv = document.querySelector('.match-date');

    let teams = [];

    fetch('http://localhost:8080/matchmetrics/api/v0/teams/all')
        .then(response => response.json())
        .then(data => {
            teams = data.map(team => team.name);
            suggestTeams(teamInput, teams);
        })
        .catch(error => console.error('Error:', error));

    teamInput.addEventListener('input', function () {
        const teamName = teamInput.value.trim();
        const teamExists = teams.includes(teamName);
        console.log(teamExists)
        if (teamExists) {
            fetch(`http://localhost:8080/matchmetrics/api/v0/teams?name=${teamName}`)
                .then(response => response.json())
                .then(teams => {
                    const team = teams[0];
                    console.log(team);
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

                    const teamInfoDiv = document.getElementById('teamInfo');
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


                    fetch(`http://localhost:8080/matchmetrics/api/v0/matches?homeTeam=${teamName}`)
                        .then(response => response.json())
                        .then(matches => {
                            if (matches.length > 0) {
                                displayMatch(matches[0]);
                            } else {
                                fetch(`http://localhost:8080/matchmetrics/api/v0/matches?awayTeam=${teamName}`)
                                    .then(response => response.json())
                                    .then(matches => {
                                        if (matches.length > 0) {
                                            displayMatch(matches[0]);
                                        } else {
                                            teamMatchDiv.textContent = 'No matches in the nearest time.';
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

        const logosDiv = document.createElement('div');

        const homeTeamLogo = document.createElement('img');
        homeTeamLogo.src = `/front/resources/logos/${match.homeTeam.name.toLowerCase()}.png`;
        homeTeamLogo.onerror = function () {
            this.onerror = null;
            this.src = '/front/resources/logos/default.png';
        };
        homeTeamLogo.style.width = '45px';
        homeTeamLogo.style.height = '45px';
        homeTeamLogo.style.marginLeft = '50px';
        homeTeamLogo.style.marginRight = '70px'; // Добавление отступа справа
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

        // Добавление div с логотипами в teamInfoDiv
        teamInfoDiv.appendChild(logosDiv);

        // Создание div для имен команд
        const namesDiv = document.createElement('div');
        namesDiv.textContent = `${match.homeTeam.name}  vs  ${match.awayTeam.name}`;
        namesDiv.style.display = 'flex';
        namesDiv.style.justifyContent = 'center';
        namesDiv.style.alignItems = 'center';

        // Добавление div с именами команд в teamInfoDiv
        teamInfoDiv.appendChild(namesDiv);

        // Создание div для даты матча
        const dateDiv = document.createElement('div');
        dateDiv.textContent = new Date(match.date).toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
        dateDiv.style.display = 'flex';
        dateDiv.style.justifyContent = 'center';
        dateDiv.style.alignItems = 'center';

        // Добавление div с датой матча в teamInfoDiv
        teamInfoDiv.appendChild(dateDiv);
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