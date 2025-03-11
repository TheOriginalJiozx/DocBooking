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

    if (isLoggedIn === "true") {
        document.getElementById("register-link").style.display = "none";
        document.getElementById("login-link").style.display = "none";
        document.getElementById("book-link").style.display = "inline";
        document.getElementById("logout-link").style.display = "inline";
    }
}

function logout() {
    localStorage.removeItem("isLoggedIn");
    localStorage.removeItem("userEmail");

    document.getElementById("register-link").style.display = "inline";
    document.getElementById("login-link").style.display = "inline";
    document.getElementById("book-link").style.display = "none";
    document.getElementById("logout-link").style.display = "none";

    window.location.href = "/";
}