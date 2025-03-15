document.addEventListener("DOMContentLoaded", function() {
    const empId = localStorage.getItem("empId");

    if (!empId) {
        document.getElementById('error-msg').textContent = "You must be logged in as a doctor to view appointments.";
        return;
    }

    fetch(`/api/doctor/appointments/my-patients?empId=${empId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch appointments.");
            }
            return response.json();
        })
        .then(data => {
            if (!data || data.length === 0) {
                document.getElementById('appointments').innerHTML = "No upcoming appointments.";
                return;
            }

            let appointmentsHtml = '<ul>';
            data.forEach(appointment => {
                appointmentsHtml += `
                    <li>
                        <strong>Patient:</strong> ${appointment.patientName}<br>
                        <strong>Booking Time:</strong> ${new Date(appointment.bookingTime).toLocaleString()}<br>
                        <strong>Service:</strong> ${appointment.service}<br>
                        <strong>Status:</strong> ${appointment.appointmentStatus}<br>

                        <button onclick="heldAppointment(event, ${appointment.id})">Mark as held</button>
                    </li>
                `;
            });
            appointmentsHtml += '</ul>';
            document.getElementById('appointments').innerHTML = appointmentsHtml;
        })
        .catch(error => {
            document.getElementById('error-msg').textContent = "Failed to fetch appointments: " + error.message;
        });
});

async function heldAppointment(event, appointmentId) {
    event.preventDefault();
    try {
        const response = await fetch(`/api/doctor/appointments/held/${appointmentId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' }
        });

        const result = await response.json().catch(() => null);

        if (response.ok) {
            const successMessage = result ? result.message : "The appointment has been marked as held!";
            showResponse(successMessage);
            localStorage.setItem("successMessage", successMessage);

            setTimeout(() => {
                window.location.reload();
            }, 3000);
        } else {
            showError(result ? result.message : "Failed to mark appointment as held.");
        }
    } catch (error) {
        showError("Error marking appointment as held: " + error.message);
    }
}

function showResponse(message) {
    const responseElement = document.getElementById('response');
    console.log("Response Element Found:", responseElement); // Debugging log

    if (!responseElement) {
        console.error("Element #response not found in the DOM.");
        return;
    }

    responseElement.innerText = message;
    responseElement.style.color = "green";
    responseElement.style.display = "block";

    console.log("Response Message Set:", message); // Debugging log

    setTimeout(() => {
        responseElement.style.display = "none";
        console.log("Response Message Hidden");
    }, 3000);
}

function showError(message) {
    const errorMsgElement = document.getElementById('error-msg');
    console.log("Error Message Element Found:", errorMsgElement); // Debugging log

    if (!errorMsgElement) {
        console.error("Element #error-msg not found in the DOM.");
        return;
    }

    errorMsgElement.innerText = message;
    errorMsgElement.style.color = "red";
    errorMsgElement.style.display = "block";

    console.log("Error Message Set:", message); // Debugging log

    setTimeout(() => {
        errorMsgElement.style.display = "none";
        console.log("Error Message Hidden");
    }, 5000);
}