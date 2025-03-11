console.log("Script loaded successfully");
console.log("Checking if loginButton exists:", document.getElementById('loginButton'));
console.log("Checking if registerButton exists:", document.getElementById('registerButton'));

document.getElementById('registerButton').addEventListener('click', function() {
    console.log("Register button clicked");

    let patientData = {
        firstName: document.getElementById('first_name').value,
        middleName: document.getElementById('middle_name').value,
        lastName: document.getElementById('last_name').value,
        phone: document.getElementById('phone').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    };

    console.log("Registering patient with data:", patientData);

    fetch('/api/patient/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(patientData)
    })
        .then(response => {
            console.log("Response received:", response);

            if (!response.ok) {
                return response.text().then(errorMsg => { throw new Error(errorMsg); });
            }
            return response.json();
        })
        .then(data => {
            console.log('Patient registered:', data);
            document.getElementById('response').innerHTML = `Patient registered: ${data.firstName} ${data.middleName} ${data.lastName}`;
        })
        .catch(error => {
            console.error('Registration failed:', error.message);
            document.getElementById('response').innerHTML = `Error: ${error.message}`;
        });
});