document.addEventListener('DOMContentLoaded', function () {
    const team1Input = document.getElementById('team1');
    const team2Input = document.getElementById('team2');
    const forecastButton = document.getElementById('forecastButton');
    const matchInfoDiv = document.getElementById('matchInfo2');
    const predictionButtonContainer = document.getElementById('predictionButtonContainer');
    const predictionButton = document.createElement('button');
    predictionButton.id = 'predictionButton';
    predictionButton.textContent = 'Prediction for the next match';
    predictionButton.style.display = 'none';
    predictionButtonContainer.appendChild(predictionButton);

    const urlParams = new URLSearchParams(window.location.search);
    const homeTeam = urlParams.get('homeTeam');
    const awayTeam = urlParams.get('awayTeam');

    if (homeTeam && awayTeam) {
        document.getElementById('team1').value = homeTeam;
        document.getElementById('team2').value = awayTeam;
    }

    let teams = [];

    const userEmail = localStorage.getItem('userEmail');
    const token = localStorage.getItem('token');

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

    fetch('http://localhost:8080/matchmetrics/api/v0/teams/all?page=1&perPage=10000', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(checkForbidden)
        .then(response => response.json())
        .then(data => {
            teams = data.map(team => team.name);
            suggestTeams(team1Input, teams);
            suggestTeams(team2Input, teams);
        })
        .catch(error => console.error('Error:', error));

    console.log(userEmail)

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
                fetch(`http://localhost:8080/matchmetrics/api/v0/matches?homeTeam=${data.favouriteTeam}`, {
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
                            return fetch(`http://localhost:8080/matchmetrics/api/v0/matches?awayTeam=${data.favouriteTeam}`, {
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
                                        const favoriteMatchDiv = document.getElementById('favoriteTeamNextMatch');
                                        favoriteMatchDiv.textContent = 'No matches in the nearest time.';
                                    }
                                });
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        })
        .catch(error => console.error('Error fetching user data:', error));

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

    function displayMatch(match) {
        const favoriteMatchDiv = document.getElementById('favoriteTeamNextMatch');
        favoriteMatchDiv.style.padding = '40px';

        const teamLogosDiv = favoriteMatchDiv.querySelector('.team-logos');
        teamLogosDiv.innerHTML = '';

        const homeTeamLogo = document.createElement('img');
        homeTeamLogo.src = `/front/resources/logos/${match.homeTeam.name.toLowerCase()}.png`;
        const awayTeamLogo = document.createElement('img');
        awayTeamLogo.src = `/front/resources/logos/${match.awayTeam.name.toLowerCase()}.png`;

        homeTeamLogo.onerror = function () {
            this.onerror = null;
            this.src = '/front/resources/logos/default.png';
        };
        awayTeamLogo.onerror = function () {
            this.onerror = null;
            this.src = '/front/resources/logos/default.png';
        };

        homeTeamLogo.style.width = '60px';
        homeTeamLogo.style.height = '60px';
        awayTeamLogo.style.width = '60px';
        awayTeamLogo.style.height = '60px';

        homeTeamLogo.style.marginRight = '80px';
        awayTeamLogo.style.marginLeft = '80px';

        teamLogosDiv.style.justifyContent = 'space-between';
        teamLogosDiv.appendChild(homeTeamLogo);
        teamLogosDiv.appendChild(awayTeamLogo);

        const teamNamesDiv = favoriteMatchDiv.querySelector('.team-names');
        teamNamesDiv.textContent = `${match.homeTeam.name}  vs  ${match.awayTeam.name}`;
        teamNamesDiv.style.display = 'flex';
        teamNamesDiv.style.alignItems = 'center';
        teamNamesDiv.style.justifyContent = 'center';

        const matchDateDiv = favoriteMatchDiv.querySelector('.match-date');
        matchDateDiv.textContent = new Date(match.date).toLocaleDateString('en-US', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }


    forecastButton.addEventListener('click', function () {
        const team1 = team1Input.value.trim();
        const team2 = team2Input.value.trim();
        const team1Exists = teams.includes(team1);
        const team2Exists = teams.includes(team2);

        if (team1Exists && team2Exists) {
            const team1InfoDiv = document.getElementById('team1Info');
            const team2InfoDiv = document.getElementById('team2Info');
            matchInfoDiv.innerHTML = '';
            fetch(`http://localhost:8080/matchmetrics/api/v0/teams?name=${team1}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
                .then(checkForbidden)
                .then(response => response.json())
                .then(teams => {
                    team1InfoDiv.innerHTML = '';
                    appendTeamInfo(teams[0], team1InfoDiv);
                })
                .catch(error => console.error('Error:', error));

            fetch(`http://localhost:8080/matchmetrics/api/v0/teams?name=${team2}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
                .then(checkForbidden)
                .then(response => response.json())
                .then(teams => {
                    team2InfoDiv.innerHTML = '';
                    appendTeamInfo(teams[0], team2InfoDiv);
                })
                .catch(error => console.error('Error:', error));

            predictionButton.style.display = 'block';
        } else {
            alert("Enter two real teams.");
        }
    });

    predictionButton.addEventListener('click', function () {
        const team1 = document.getElementById('team1').value.trim();
        const team2 = document.getElementById('team2').value.trim();
        fetch(`http://localhost:8080/matchmetrics/api/v0/matches?homeTeam=${team1}&awayTeam=${team2}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(checkForbidden)
            .then(response => response.json())
            .then(matches => {
                console.log(matches);
                const matchInfoDiv = document.getElementById('matchInfo2');
                matchInfoDiv.innerHTML = '';
                matchInfoDiv.style.display = 'flex';
                matchInfoDiv.style.flexDirection = 'column';
                matchInfoDiv.style.visibility = 'visible';

                if (matches.length > 0) {
                    const match = matches[0];
                    const matchDate = document.createElement('p');
                    matchDate.textContent = `${new Date(match.date).toLocaleDateString('en-US', {
                        weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'
                    })}`;
                    matchDate.style.textAlign = 'center';
                    matchInfoDiv.appendChild(matchDate);

                    const leagueDiv = document.createElement('div');
                    leagueDiv.style.display = 'flex';
                    leagueDiv.style.alignItems = 'center';

                    const leagueLogo = document.createElement('img');
                    leagueLogo.src = `/front/resources/countries/${match.league.toLowerCase()}.png`;
                    leagueLogo.style.width = '40px';
                    leagueLogo.style.height = '40px';
                    leagueLogo.style.borderRadius = '50%';
                    leagueLogo.onerror = function () {
                        this.onerror = null;
                        this.src = '/front/resources/countries/default.png';
                    };
                    leagueDiv.appendChild(leagueLogo);

                    leagueDiv.style.display = 'flex';
                    leagueDiv.style.alignItems = 'center';
                    leagueDiv.style.justifyContent = 'center';
                    matchInfoDiv.appendChild(leagueDiv);

                    const matchLeague = document.createElement('p');
                    matchLeague.textContent = `${match.league}`;
                    leagueDiv.appendChild(matchLeague);
                    matchInfoDiv.appendChild(leagueDiv);

                    const teamsDiv = document.createElement('div');
                    teamsDiv.className = 'teams';

                    const matchHomeTeam = document.createElement('div');
                    matchHomeTeam.className = 'team home';
                    const homeTeamLogo = document.createElement('img');
                    homeTeamLogo.src = `/front/resources/logos/${match.homeTeam.name.toLowerCase()}.png`;
                    homeTeamLogo.onerror = function () {
                        this.onerror = null;
                        this.src = '/front/resources/logos/default.png';
                    };
                    matchHomeTeam.appendChild(homeTeamLogo);

                    const homeTeamName = document.createElement('p');
                    homeTeamName.textContent = `${match.homeTeam.name}`;
                    matchHomeTeam.appendChild(homeTeamName);
                    teamsDiv.appendChild(matchHomeTeam);

                    const matchAwayTeam = document.createElement('div');
                    matchAwayTeam.className = 'team away';
                    const awayTeamLogo = document.createElement('img');
                    awayTeamLogo.src = `/front/resources/logos/${match.awayTeam.name.toLowerCase()}.png`;
                    awayTeamLogo.onerror = function () {
                        this.onerror = null;
                        this.src = '/front/resources/logos/default.png';
                    };
                    matchAwayTeam.appendChild(awayTeamLogo);

                    const awayTeamName = document.createElement('p');
                    awayTeamName.textContent = `${match.awayTeam.name}`;
                    matchAwayTeam.appendChild(awayTeamName);
                    teamsDiv.appendChild(matchAwayTeam);

                    matchInfoDiv.appendChild(teamsDiv);

                    const probabilityDiv = document.createElement('div');
                    probabilityDiv.style.display = 'flex';
                    probabilityDiv.style.height = '20px';
                    probabilityDiv.style.width = '100%';
                    probabilityDiv.style.border = '1px solid black';
                    probabilityDiv.style.fontSize= '16px';

                    const homeWinProbability = document.createElement('div');
                    homeWinProbability.style.backgroundColor = 'green';
                    homeWinProbability.style.width = `${match.probability.homeTeamWin * 100}%`;
                    homeWinProbability.style.height = '100%';
                    homeWinProbability.style.position = 'relative';

                    const homeWinLabel = document.createElement('p');
                    homeWinLabel.textContent = `${(match.probability.homeTeamWin * 100).toFixed(1)}%`;
                    homeWinLabel.style.position = 'absolute';
                    homeWinLabel.style.top = '-15px';
                    homeWinLabel.style.width = '100%';
                    homeWinLabel.style.textAlign = 'center';
                    homeWinLabel.style.color = 'white';
                    homeWinProbability.appendChild(homeWinLabel);
                    probabilityDiv.appendChild(homeWinProbability);

                    const drawProbability = document.createElement('div');
                    drawProbability.style.backgroundColor = 'blue';
                    drawProbability.style.width = `${match.probability.draw * 100}%`;
                    drawProbability.style.height = '100%';
                    drawProbability.style.position = 'relative';

                    const drawLabel = document.createElement('p');
                    drawLabel.textContent = `${(match.probability.draw * 100).toFixed(1)}%`;
                    drawLabel.style.position = 'absolute';
                    drawLabel.style.top = '-15px';
                    drawLabel.style.width = '100%';
                    drawLabel.style.textAlign = 'center';
                    drawLabel.style.color = 'white';
                    drawProbability.appendChild(drawLabel);
                    probabilityDiv.appendChild(drawProbability);

                    const awayWinProbability = document.createElement('div');
                    awayWinProbability.style.backgroundColor = 'red';
                    awayWinProbability.style.width = `${match.probability.awayTeamWin * 100}%`;
                    awayWinProbability.style.height = '100%';
                    awayWinProbability.style.position = 'relative';

                    const awayWinLabel = document.createElement('p');
                    awayWinLabel.textContent = `${(match.probability.awayTeamWin * 100).toFixed(1)}%`;
                    awayWinLabel.style.position = 'absolute';
                    awayWinLabel.style.top = '-15px';
                    awayWinLabel.style.width = '100%';
                    awayWinLabel.style.textAlign = 'center';
                    awayWinLabel.style.color = 'white';
                    awayWinProbability.appendChild(awayWinLabel);
                    probabilityDiv.appendChild(awayWinProbability);

                    matchInfoDiv.appendChild(probabilityDiv);
                    matchInfoDiv.style.backgroundColor = 'white';
                    matchInfoDiv.style.borderRadius = '10px';
                    matchInfoDiv.style.padding = '20px';
                } else {
                    const noMatchFoundMessage = document.createElement('p');
                    noMatchFoundMessage.textContent = 'No match found for the given teams.';
                    matchInfoDiv.appendChild(noMatchFoundMessage);
                }
            })
            .catch(error => console.error('Error:', error));
    });


    function appendTeamInfo(team, teamInfoDiv) {
        teamInfoDiv.innerHTML = '';

        const teamLogo = document.createElement('img');
        teamLogo.src = `/front/resources/logos/${team.name.toLowerCase()}.png`;
        teamLogo.onerror = function () {
            this.onerror = null;
            this.src = '/front/resources/logos/default.png';
        };
        teamInfoDiv.appendChild(teamLogo);

        const teamName = document.createElement('h2');
        teamName.textContent = team.name;
        teamInfoDiv.appendChild(teamName);

        const teamCountry = document.createElement('p');
        teamCountry.textContent = `Country: ${team.country}`;
        teamInfoDiv.appendChild(teamCountry);

        const teamElo = document.createElement('p');
        teamElo.textContent = `ELO: ${Math.round(team.elo)}`;
        teamInfoDiv.appendChild(teamElo);
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

$('button').click(function () {
    $('#matchInfo, #matchInfo2').css('visibility', 'visible').hide().fadeIn();
});

$(document).ready(function () {
    var delay = 0;
    var animationSpeed = 10000;

    $('.teamInfo').each(function () {
        $(this).css({opacity: 0, left: "-=50px"});
        $(this).delay(delay).animate({
            opacity: 1,
            left: "+=50px"
        }, animationSpeed);
        delay += 200;
    });

    $('.matchInfoDiv').each(function () {
        $(this).css({opacity: 0, left: "-=50px"});
        $(this).delay(delay).animate({
            opacity: 1, left: "+=50px"
        }, animationSpeed);
        delay += 200;
    });

});
