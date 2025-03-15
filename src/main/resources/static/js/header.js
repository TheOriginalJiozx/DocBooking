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
        document.getElementById("patient-appointments-link").style.display = "none";
        document.getElementById("book-for-patient-link").style.display = "none";
        document.getElementById("appointments-link").style.display = "inline";
        document.getElementById("book-link").style.display = "inline";
        document.getElementById("logout-link").style.display = "inline";

        checkUserRole(empId);
    }
}

function checkUserRole(empId) {
    if (!empId) return;

    Promise.all([
        fetch(`/api/doctor/exists/${empId}`).then(res => res.ok ? res.json() : false),
        fetch(`/api/admin/exists/${empId}`).then(res => res.ok ? res.json() : false)
    ])
        .then(([isDoctor, isAdmin]) => {
            console.log("Doctor status:", isDoctor);
            console.log("Admin status:", isAdmin);

            if (isDoctor) {
                document.getElementById("book-link").style.display = "none";
                document.getElementById("appointments-link").style.display = "none";
                document.getElementById("patient-appointments-link").style.display = "inline";
                document.getElementById("book-for-patient-link").style.display = "none";
            }

            if (isAdmin) {
                document.getElementById("book-link").style.display = "none";
                document.getElementById("appointments-link").style.display = "none";
                document.getElementById("patient-appointments-link").style.display = "none";
                document.getElementById("book-for-patient-link").style.display = "inline";
            }
        })
        .catch(error => console.error("Error checking user role:", error));
}

function logout() {
    localStorage.removeItem("isLoggedIn");
    localStorage.removeItem("empId");
    localStorage.removeItem("userEmail");

    document.getElementById("register-link").style.display = "inline";
    document.getElementById("login-link").style.display = "inline";
    document.getElementById("book-for-patient-link").style.display = "none";
    document.getElementById("patient-appointments-link").style.display = "none";
    document.getElementById("book-link").style.display = "none";
    document.getElementById("appointments-link").style.display = "none";
    document.getElementById("logout-link").style.display = "none";

    window.location.href = "/";
}