async function bookAppointment() {
    const email = localStorage.getItem('userEmail');

    if (!email) {
        alert("Email not found in local storage.");
        return;
    }

    const doctorName = document.getElementById("doctorName").value;
    const bookingTime = document.getElementById("bookingTime").value;

    if (!doctorName || !bookingTime) {
        alert("Please provide both doctor name and booking time.");
        return;
    }

    const bookingData = {
        email: email,
        doctorName: doctorName,
        bookingTime: bookingTime
    };

    try {
        const response = await fetch('/api/patient/booking', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bookingData)
        });

        const result = await response.json();

        if (response.ok) {
            alert(result.message);
        } else {
            alert(`Error: ${result.message}`);
        }
    } catch (error) {
        console.error("Error booking appointment:", error);
        alert("An error occurred while booking the appointment.");
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const email = localStorage.getItem('userEmail');
    if (email) {
        document.getElementById('email').value = email;
    }
});