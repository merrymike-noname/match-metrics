document.addEventListener('DOMContentLoaded', function () {
    const matchesList = document.getElementById('matchesList');
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

    fetch(`http://localhost:8080/matchmetrics/api/v0/users/${userEmail}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(checkForbidden)
        .then(response => response.json())
        .then(data => {
            if (data) {
                console.log(data.favouriteTeam.name);
                fetch(`http://localhost:8080/matchmetrics/api/v0/matches?homeTeam=${data.favouriteTeam.name}`, {
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
                            return fetch(`http://localhost:8080/matchmetrics/api/v0/matches?awayTeam=${data.favouriteTeam.name}`, {
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
        teamNamesDiv.textContent = `${match.homeTeam.name} vs ${match.awayTeam.name}`;

        const matchDateDiv = favoriteMatchDiv.querySelector('.match-date');
        matchDateDiv.textContent = new Date(match.date).toLocaleDateString('en-US', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }


    const datePicker = flatpickr('#datePicker', {
        defaultDate: new Date(),
        inline: true,
        onChange: function (selectedDates, dateStr, instance) {
            matchesList.innerHTML = '';
            const selectedDate = dateStr;

            fetch(`http://localhost:8080/matchmetrics/api/v0/matches?date=${selectedDate}&page=1&perPage=100`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
                .then(checkForbidden)
                .then(response => response.json())
                .then(matches => {
                    if (matches.length > 0) {
                        matches.forEach(match => {
                            const matchDiv = document.createElement('div');

                            const matchLink = document.createElement('a');
                            matchLink.href = `compare.html?homeTeam=${encodeURIComponent(match.homeTeam.name)}&awayTeam=${encodeURIComponent(match.awayTeam.name)}`;

                            const matchInfo = document.createElement('span');
                            matchInfo.textContent = `${match.homeTeam.name} vs ${match.awayTeam.name}`;

                            matchLink.appendChild(matchInfo);

                            matchDiv.appendChild(matchLink);

                            matchDiv.addEventListener('click', function() {
                                window.location.href = matchLink.href;
                            });


                            const leagueLogo = document.createElement('img');
                            leagueLogo.src = `/front/resources/countries/${match.league.toLowerCase()}.png`;
                            leagueLogo.style.width = '20px';
                            leagueLogo.style.height = '20px';
                            leagueLogo.style.marginRight = '20px';
                            leagueLogo.style.float = 'left';
                            leagueLogo.style.borderRadius = '50%';
                            leagueLogo.onerror = function () {
                                this.onerror = null;
                                this.src = '/front/resources/countries/default.png';
                            };

                            matchDiv.insertBefore(leagueLogo, matchDiv.firstChild);

                            matchesList.appendChild(matchDiv);
                        });

                    } else {
                        const noMatchesDiv = document.createElement('div');
                        noMatchesDiv.textContent = 'No matches found for this date.';
                        matchesList.appendChild(noMatchesDiv);
                    }
                })
                .catch(error => console.error('Error:', error));
        }
    });
});
