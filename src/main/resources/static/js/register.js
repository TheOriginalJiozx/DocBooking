document.addEventListener("DOMContentLoaded", function () {
    const userEmail = localStorage.getItem("userEmail");
    const empId = localStorage.getItem("empId");

    if (userEmail || empId) {
        window.location.href = "index.html";
        return;
    }

    // Registreringslogik
    document.getElementById('registerButton').addEventListener('click', function() {
        console.log("Register button clicked");

        let patientData = {
            firstName: document.getElementById('first_name').value,
            middleName: document.getElementById('middle_name').value,
            lastName: document.getElementById('last_name').value,
            phone: document.getElementById('tel').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        };

        console.log("Registering patient with data:", patientData);

        fetch('/api/patient/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(patientData)
        })
            .then(response => {
                console.log("Response received:", response);
                return response.json().then(data => {
                    if (!response.ok) {
                        throw new Error(data.message || "Registration failed.");
                    }
                    return data;
                });
            })
            .then(data => {
                console.log('Patient registered:', data);
                document.getElementById('response').innerHTML = `<span style="color: green;">Patient registered successfully: ${data.firstName} ${data.middleName} ${data.lastName}</span>`;
            })
            .catch(error => {
                console.error('Registration failed:', error.message);
                document.getElementById('error-msg').innerHTML = `<span style="color: red;">Error: ${error.message}</span>`;
            });
    });
});