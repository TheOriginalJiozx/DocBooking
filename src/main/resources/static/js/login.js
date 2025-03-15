console.log("Script loaded successfully");

document.addEventListener("DOMContentLoaded", function () {
    const userEmail = localStorage.getItem("userEmail");
    const empId = localStorage.getItem("empId");

    if (userEmail || empId) {
        console.log("User is already logged in. Redirecting to index.html...");
        window.location.href = "/index.html";
        return;
    }

    const loginButton = document.getElementById('loginButton');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');

    if (!loginButton) {
        console.error("Login button not found!");
        return;
    }

    loginButton.addEventListener('click', function () {
        console.log("Login button clicked");

        let email = emailInput.value.trim();
        let password = passwordInput.value.trim();

        console.log("Login attempt with:", { email, password });

        if (!email || !password) {
            console.warn("Missing email or password");
            document.getElementById('error-msg').innerHTML = `<span style="color: red;">Please enter email and password.</span>`;
            return;
        }

        fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        })
            .then(response => {
                console.log("Login response received:", response);
                return response.json().then(data => {
                    if (!response.ok) {
                        throw new Error(data.message || "Login failed.");
                    }
                    return data;
                });
            })
            .then(data => {
                console.log("Login successful:", data);

                if (data.userId) {
                    localStorage.setItem("isLoggedIn", "true");
                    localStorage.setItem("userEmail", data.email);

                    document.getElementById('response').innerHTML = `<span style="color: green;">Login successful! Welcome, ${data.email}.</span>`;

                    console.log("Redirecting to /index.html...");
                    setTimeout(() => {
                        window.location.href = '/index.html';
                    }, 2000);
                } else {
                    console.warn("Login response missing userId");
                }
            })
            .catch(error => {
                console.error("Login failed:", error.message);
                document.getElementById('error-msg').innerHTML = `<span style="color: red;">Error: ${error.message}</span>`;
            });
    });

    emailInput.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            loginButton.click();
        }
    });

    passwordInput.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            loginButton.click();
        }
    });
});