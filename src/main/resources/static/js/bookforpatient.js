document.addEventListener('DOMContentLoaded', async () => {
    await populateDoctorNames();
    await populatePatientNames();
});

async function populateDoctorNames() {
    try {
        const response = await fetch('/api/doctor');
        const doctors = await response.json();

        if (response.ok) {
            const doctorSelect = document.getElementById('doctorName');
            doctors.forEach(doctor => {
                const fullName = `${doctor.firstName} ${doctor.middleName ? doctor.middleName + ' ' : ''}${doctor.lastName}`;
                const option = document.createElement('option');
                option.value = fullName;
                option.textContent = fullName;
                doctorSelect.appendChild(option);
            });
        } else {
            showError('Failed to load doctor names.');
        }
    } catch (error) {
        console.error('Error fetching doctors:', error);
        showError('An error occurred while loading doctor names.');
    }
}

async function populatePatientNames() {
    try {
        const response = await fetch('/api/patient/all');
        const patients = await response.json();

        if (response.ok) {
            const patientSelect = document.getElementById('patientName');
            patients.forEach(patient => {
                const fullName = `${patient.firstName} ${patient.middleName ? patient.middleName + ' ' : ''}${patient.lastName}`;
                const option = document.createElement('option');
                option.value = patient.id;
                option.textContent = fullName;
                patientSelect.appendChild(option);
            });
        } else {
            showError('Failed to load patient names.');
        }
    } catch (error) {
        console.error('Error fetching patients:', error);
        showError('An error occurred while loading patient names.');
    }
}

async function updateAvailableTimes() {
    const doctorName = document.getElementById("doctorName").value;
    if (!doctorName) {
        return;
    }

    try {
        const response = await fetch(`/api/available-hours?doctorName=${doctorName}`);
        const availableHours = await response.json();

        if (response.ok) {
            const bookingTimeSelect = document.getElementById("bookingTime");
            bookingTimeSelect.innerHTML = '<option value="" disabled selected>Select a time</option>';

            availableHours.forEach(slot => {
                const option = document.createElement('option');
                option.value = slot.availableTime;
                option.textContent = formatDateTime(slot.availableTime);
                bookingTimeSelect.appendChild(option);
            });
        } else {
            showError('Failed to load available times.');
        }
    } catch (error) {
        console.error('Error fetching available times:', error);
        showError('An error occurred while loading available times.');
    }
}

function formatDateTime(dateTimeStr) {
    const date = new Date(dateTimeStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

async function bookAppointment() {
    const patientId = document.getElementById("patientName").value;
    const doctorName = document.getElementById("doctorName").value;
    const service = document.getElementById("service").value;
    const bookingTime = document.getElementById("bookingTime").value;

    if (!patientId || !doctorName || !bookingTime) {
        showError("Please provide patient, doctor, and booking time.");
        return;
    }

    const bookingData = {
        patientId: patientId,
        service: service,
        doctorName: doctorName,
        bookingTime: bookingTime
    };

    try {
        const response = await fetch('/api/admin/booking', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bookingData)
        });

        const result = await response.json();

        if (response.ok) {
            showSuccess(result.message);
        } else {
            showError(`Error: ${result.message}`);
        }
    } catch (error) {
        console.error("Error booking appointment:", error);
        showError("An error occurred while booking the appointment.");
    }
}

function showSuccess(message) {
    const responseDiv = document.getElementById('response');
    responseDiv.textContent = message;
    responseDiv.style.display = 'block';
    document.getElementById('error-msg').style.display = 'none';
}

function showError(message) {
    const errorDiv = document.getElementById('error-msg');
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
    document.getElementById('response').style.display = 'none';
}