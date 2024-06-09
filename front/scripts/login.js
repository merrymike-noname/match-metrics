document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('loginForm');
    const errorMessagesDiv = document.getElementById('loginErrorMessages');

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        const email = form.email.value.trim();
        const password = form.password.value.trim();

        errorMessagesDiv.innerHTML = '';

        if (!email || !password) {
            showError('Please enter both email and password.');
            return;
        }

        const requestData = {
            email,
            password
        };

        fetch('http://localhost:8080/matchmetrics/api/v0/auth/authenticate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.token) {
                    localStorage.setItem('userEmail', email);
                    localStorage.setItem('token', data.token);
                    window.location.href = 'compare.html';
                } else {
                    showError('Invalid email or password.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showError('An error occurred while logging in.');
            });
    });

    function showError(message) {
        const errorMessage = document.createElement('p');
        errorMessage.textContent = message;
        errorMessagesDiv.appendChild(errorMessage);
    }
});
