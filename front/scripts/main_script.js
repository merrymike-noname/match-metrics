document.addEventListener('DOMContentLoaded', function() {
    const team1Input = document.getElementById('team1');
    const team2Input = document.getElementById('team2');
    const forecastButton = document.getElementById('forecastButton');

    forecastButton.addEventListener('click', function() {
        console.log("lox");
        const team1 = team1Input.value.trim();
        const team2 = team2Input.value.trim();
        const team1Exists = teams.includes(team1);
        const team2Exists = teams.includes(team2);

        if (team1Exists && team2Exists) {
            forecastButton.style.transform = 'translateY(200px)';
        } else {
            alert("Enter two real teams.");
        }
    });

    const teams = [
        'Manchester City',
        'Manchester United',
        'Liverpool',
        'Chelsea',
        'Arsenal',
        'Tottenham Hotspur',
        'Leicester City',
        'West Ham United',
        'Aston Villa',
        'Everton',
        'Leeds United',
        'Wolverhampton Wanderers',
        'Newcastle United',
        'Crystal Palace',
        'Southampton',
        'Brighton & Hove Albion',
        'Burnley',
        'Fulham',
        'West Bromwich Albion',
        'Sheffield United'
    ];

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

    suggestTeams(team1Input, teams);
    suggestTeams(team2Input, teams);
});
