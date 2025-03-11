console.log("Script loaded successfully");

document.addEventListener("DOMContentLoaded", function () {
    const loginButton = document.getElementById('loginButton');

    if (!loginButton) {
        console.error("Login button not found!");
        return;
    }

    loginButton.addEventListener('click', function () {
        console.log("Login button clicked");

        let email = document.getElementById('email').value.trim();
        let password = document.getElementById('password').value.trim();

        console.log("Login attempt with:", { email, password });

        if (!email || !password) {
            console.warn("Missing email or password");
            document.getElementById('error-msg').innerText = "Please enter email and password.";
            return;
        }

        fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        })
            .then(response => {
                console.log("Login response received:", response);

                if (!response.ok) {
                    return response.text().then(errorMsg => { throw new Error(errorMsg); });
                }
                return response.json();
            })
            .then(data => {
                console.log("Login successful:", data);

                if (data.userId) {
                    localStorage.setItem("isLoggedIn", "true");
                    localStorage.setItem("userEmail", data.email);

                    document.getElementById('response').innerHTML = `Login successful! Welcome, ${data.email}.`;

                    console.log("Redirecting to /index.html");
                    window.location.href = '/index.html';
                } else {
                    console.warn("Login response missing userId");
                }
            })
            .catch(error => {
                console.error("Login failed:", error.message);
                document.getElementById('error-msg').innerText = `Error: ${error.message}`;
            });
    });
});