document.addEventListener("DOMContentLoaded", function () {
    const empIdStored = localStorage.getItem("empId");

    if (empIdStored) {
        console.log("Admin already logged in. Redirecting to index.html...");
        window.location.href = "index.html";
        return;
    }

    document.getElementById('loginForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const empId = document.getElementById('empId').value.trim();
        const password = document.getElementById('password').value.trim();
        const responseDiv = document.getElementById("response");
        const errorMsgDiv = document.getElementById("error-msg");

        responseDiv.textContent = "";
        errorMsgDiv.textContent = "";

        if (!empId || !password) {
            errorMsgDiv.textContent = "Please enter both Employee ID and Password.";
            errorMsgDiv.style.color = "red";
            return;
        }

        try {
            const response = await fetch('/api/auth/adminlogin', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ empId, password })
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || "Login failed.");
            }

            // Store login status
            localStorage.setItem("isLoggedIn", "true");
            localStorage.setItem("empId", empId);

            responseDiv.textContent = "Login successful! Redirecting...";
            responseDiv.style.color = "green";

            setTimeout(() => {
                window.location.href = "index.html";
            }, 2000);

        } catch (error) {
            console.error('Error during login:', error);
            errorMsgDiv.textContent = error.message;
            errorMsgDiv.style.color = "red";
        }
    });
});