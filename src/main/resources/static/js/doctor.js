function generateEmpId() {
    let firstName = document.getElementById("first_name").value.trim();
    let middleName = document.getElementById("middle_name").value.trim();
    let lastName = document.getElementById("last_name").value.trim();
    let phone = document.getElementById("phone").value.trim();

    if (firstName && lastName && phone.length >= 2) {
        let firstInitial = firstName.charAt(0).toUpperCase();
        let middleInitial = middleName ? middleName.charAt(0).toUpperCase() : '';
        let lastInitial = lastName.charAt(0).toUpperCase();
        let lastTwoDigits = phone.slice(-2);

        let empId = firstInitial + middleInitial + lastInitial + lastTwoDigits;
        document.getElementById("empId").value = empId;
    } else {
        document.getElementById("empId").value = "";
    }
}

document.getElementById("doctorForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    let empId = document.getElementById("empId").value.trim();
    let responseDiv = document.getElementById("response");
    let errorMsgDiv = document.getElementById("error-msg");

    responseDiv.textContent = "";
    errorMsgDiv.textContent = "";

    if (!empId) {
        errorMsgDiv.textContent = "Employee ID is required.";
        errorMsgDiv.style.color = "red";
        return;
    }

    let formData = {
        firstName: document.getElementById("first_name").value,
        middleName: document.getElementById("middle_name").value,
        lastName: document.getElementById("last_name").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value,
        empId: empId,
        password: document.getElementById("password").value
    };

    try {
        const response = await fetch('/api/doctor/docregister', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('empId')
            },
            body: JSON.stringify(formData)
        });

        const result = await response.json();

        if (response.ok) {
            responseDiv.textContent = "Doctor registered successfully!";
            responseDiv.style.color = "green";
        } else {
            errorMsgDiv.textContent = `Failed to register doctor: ${result.message || "Unknown error"}`;
            errorMsgDiv.style.color = "red";
        }
    } catch (error) {
        console.error("Error:", error);
        errorMsgDiv.textContent = "An error occurred while processing your request.";
        errorMsgDiv.style.color = "red";
    }
});