document.addEventListener("DOMContentLoaded", function() {
    fetch("header.html")
        .then(response => response.text())
        .then(data => {
            document.querySelector("header").innerHTML = data;
            handleAuthLinks();
        });
});

function handleAuthLinks() {
    const isLoggedIn = localStorage.getItem("isLoggedIn");
    const empId = localStorage.getItem("empId");

    if (isLoggedIn === "true") {
        document.getElementById("register-link").style.display = "none";
        document.getElementById("login-link").style.display = "none";
        document.getElementById("logout-link").style.display = "inline";

        checkIfDoctor(empId);
    }
}

function checkIfDoctor(empId) {
    fetch(`/api/doctor/exists/${empId}`)
        .then(response => response.json())
        .then(exists => {
            if (exists) {
                document.getElementById("book-link").style.display = "none";
            } else {
                document.getElementById("book-link").style.display = "inline";
            }
        })
        .catch(error => console.error('Error checking doctor:', error));
}

function logout() {
    localStorage.removeItem("isLoggedIn");
    localStorage.removeItem("empId");
    localStorage.removeItem("userEmail");

    document.getElementById("register-link").style.display = "inline";
    document.getElementById("login-link").style.display = "inline";
    document.getElementById("book-link").style.display = "none";
    document.getElementById("logout-link").style.display = "none";

    window.location.href = "/";
}