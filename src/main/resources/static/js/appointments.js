document.addEventListener("DOMContentLoaded", function () {
    const email = localStorage.getItem("userEmail");

    if (!email) {
        showError("You must be logged in to view your appointments.");
        return;
    }

    fetch(`/api/appointments/my-appointments?email=${email}`)
        .then(response => response.json())
        .then(data => {
            if (!data || data.length === 0) {
                document.getElementById('appointments').innerHTML = "You have no upcoming appointments.";
                return;
            }

            const upcomingAppointments = data.filter(appointment => appointment.appointmentStatus !== "Cancelled");

            if (upcomingAppointments.length === 0) {
                document.getElementById('appointments').innerHTML = "You have no upcoming appointments.";
                return;
            }

            let appointmentsHtml = '<ul>';
            upcomingAppointments.forEach(appointment => {
                appointmentsHtml += `
                    <li>
                        <strong>Doctor:</strong> ${appointment.doctorName}<br>
                        <strong>Patient:</strong> ${appointment.patientName}<br>
                        <strong>Booking Time:</strong> ${new Date(appointment.bookingTime).toLocaleString()}<br>
                        <strong>Service:</strong> ${appointment.service}<br>
                        <strong>Appointment Status:</strong> ${appointment.appointmentStatus}
                        <br>
                        <button onclick="cancelAppointment(event, ${appointment.id})">Cancel</button>
                        <button onclick="rescheduleAppointment(event, ${appointment.id}, '${appointment.doctorName}')">Reschedule</button>
                    </li>
                `;
            });
            appointmentsHtml += '</ul>';
            document.getElementById('appointments').innerHTML = appointmentsHtml;
        })
        .catch(error => {
            showError("Failed to fetch appointments: " + error.message);
        });
});

async function cancelAppointment(appointmentId) {
    try {
        const response = await fetch(`/api/appointments/cancel/${appointmentId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' }
        });

        const result = await response.json().catch(() => null);

        if (response.ok) {
            const successMessage = result ? result.message : "Your appointment has been cancelled!";
            showResponse(successMessage);
            localStorage.setItem("successMessage", successMessage);

            setTimeout(() => {
                window.location.reload();
            }, 3000);
        } else {
            showError(result ? result.message : "Failed to cancel appointment.");
        }
    } catch (error) {
        showError("Error cancelling appointment: " + error.message);
    }
}

async function rescheduleAppointment(event, appointmentId, doctorName) {
    event.preventDefault();

    try {
        const response = await fetch(`/api/available-hours?doctorName=${doctorName}`);
        if (!response.ok) {
            throw new Error("Failed to fetch available hours.");
        }

        const availableHours = await response.json();
        if (!availableHours.length) {
            showError("No available hours found.");
            return;
        }

        const newTime = await showAvailableHoursPopup(availableHours.map(hour => hour.availableTime));
        if (!newTime) {
            showError("You must select a new booking time.");
            return;
        }

        const rescheduleResponse = await fetch(`/api/appointments/reschedule/${appointmentId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ newBookingTime: newTime })
        });

        const result = await rescheduleResponse.json();
        if (rescheduleResponse.ok) {
            const successMessage = "Your appointment has been rescheduled!";
            showResponse(successMessage);
            localStorage.setItem("successMessage", successMessage);
            setTimeout(() => location.reload(), 2000);
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError("Error rescheduling appointment: " + error.message);
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const successMessage = localStorage.getItem("successMessage");
    if (successMessage) {
        showResponse(successMessage);
        localStorage.removeItem("successMessage");
    }
});

async function showAvailableHoursPopup(availableHours) {
    return new Promise((resolve) => {
        const existingModal = document.getElementById('rescheduleModal');
        if (existingModal) {
            existingModal.remove();
        }

        const modal = document.createElement('div');
        modal.id = "rescheduleModal";
        modal.style.position = "fixed";
        modal.style.top = "0";
        modal.style.left = "0";
        modal.style.width = "100%";
        modal.style.height = "100%";
        modal.style.background = "rgba(0,0,0,0.5)";
        modal.style.display = "flex";
        modal.style.alignItems = "center";
        modal.style.justifyContent = "center";
        modal.style.zIndex = "1000";

        modal.innerHTML = `
            <div style="background: white; padding: 20px; border-radius: 10px; text-align: center;">
                <h3>Select a new booking time</h3>
                <select id="availableHoursDropdown">
                    ${availableHours.map(hour => `<option value="${hour}">${hour}</option>`).join('')}
                </select>
                <br><br>
                <button id="confirmTime">Confirm</button>
                <button id="cancelTime">Cancel</button>
            </div>
        `;

        document.body.appendChild(modal);

        document.getElementById('confirmTime').onclick = function () {
            const selectedTime = document.getElementById('availableHoursDropdown').value;
            modal.remove();
            resolve(selectedTime);
        };

        document.getElementById('cancelTime').onclick = function () {
            modal.remove();
            resolve(null);
        };
    });
}

function showResponse(message) {
    const responseElement = document.getElementById('response');
    if (!responseElement) {
        console.error("Element #response not found in the DOM.");
        return;
    }
    responseElement.innerText = message;
    responseElement.style.color = "green";
    responseElement.style.display = "block";

    setTimeout(() => {
        responseElement.style.display = "none";
    }, 3000);
}

function showError(message) {
    const errorMsgElement = document.getElementById('error-msg');
    if (!errorMsgElement) {
        console.error("Element #error-msg not found in the DOM.");
        return;
    }
    errorMsgElement.innerText = message;
    errorMsgElement.style.color = "red";
    errorMsgElement.style.display = "block";

    setTimeout(() => {
        errorMsgElement.style.display = "none";
    }, 5000);
}