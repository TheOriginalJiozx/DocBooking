document.addEventListener("DOMContentLoaded", function () {
    const courseForm = document.getElementById('courseForm');
    const responseMessage = document.getElementById('response');
    const errorMessage = document.getElementById('error-msg');
    const patientNameSelect = document.getElementById('patientName');
    const patientEmailInput = document.getElementById('patientEmail');

    fetchPatientNames();

    courseForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const patientEmail = patientEmailInput.value.trim();
        const courseStatus = document.getElementById('courseStatus').value;

        if (!patientEmail) {
            showErrorMessage('Please select a valid patient.');
            return;
        }

        updateCourseStatus(patientEmail, courseStatus);
    });

    async function fetchPatientNames() {
        try {
            const response = await fetch('/api/admin/patient-names');
            const data = await response.json();
            if (response.ok) {
                populatePatientNameDropdown(data);
            } else {
                showErrorMessage(data.message || 'Failed to fetch patient names.');
            }
        } catch (error) {
            showErrorMessage('Error fetching patient names: ' + error.message);
        }
    }

    function populatePatientNameDropdown(patients) {
        patients.forEach(patient => {
            const option = document.createElement('option');
            option.value = patient.email;
            option.textContent = patient.name;
            patientNameSelect.appendChild(option);
        });
    }

    patientNameSelect.addEventListener('change', function () {
        const selectedName = patientNameSelect.options[patientNameSelect.selectedIndex];
        const selectedEmail = selectedName.value;

        if (selectedEmail) {
            patientEmailInput.value = selectedEmail;
        } else {
            patientEmailInput.value = '';
        }
    });

    async function updateCourseStatus(patientEmail, courseStatus) {
        try {
            const response = await fetch(`/api/admin/patient-course/${patientEmail}?courseStatus=${courseStatus}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            const result = await response.json();
            if (response.ok) {
                showResponseMessage(result.message || 'Course status updated successfully.');

                setTimeout(() => {
                    location.reload();
                }, 2000);
            } else {
                showErrorMessage(result.message || 'Failed to update course status.');
            }
        } catch (error) {
            showErrorMessage('Error updating course status: ' + error.message);
        }
    }

    function showResponseMessage(message) {
        responseMessage.textContent = message;
        responseMessage.style.color = 'green';
        responseMessage.style.display = 'block';

        setTimeout(() => {
            responseMessage.style.display = 'none';
        }, 5000);
    }

    function showErrorMessage(message) {
        errorMessage.textContent = message;
        errorMessage.style.color = 'red';
        errorMessage.style.display = 'block';

        setTimeout(() => {
            errorMessage.style.display = 'none';
        }, 5000);
    }
});