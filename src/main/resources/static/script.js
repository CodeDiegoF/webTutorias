const horariosContainer = document.getElementById("horarios-container");
const reservaForm       = document.getElementById("reserva-form");
const fechaInput        = document.getElementById("fecha");
const horaInput         = document.getElementById("hora");

async function cargarHorarios() {
    const response = await fetch("http://localhost:8080/horarios");
    const horarios = await response.json();

    horariosContainer.innerHTML = "";

    horarios.forEach(horario => {
        const div = document.createElement("div");
        div.classList.add("horario");
        div.innerHTML = `
            <strong>${horario.fecha}</strong><br>
            ${horario.hora}
        `;
        div.addEventListener("click", () => {
            fechaInput.value = horario.fecha;
            horaInput.value  = horario.hora;
        });
        horariosContainer.appendChild(div);
    });
}

async function cargarMisReservas(emailAlumno) {
    if (!emailAlumno) return;

    const response = await fetch("http://localhost:8080/reservas");
    const reservas = await response.json();

    const misReservas = reservas.filter(r => r.emailAlumno === emailAlumno);
    const container   = document.getElementById("mis-reservas-container");
    container.innerHTML = "";

    if (!misReservas.length) {
        container.innerHTML = "<p>No tienes tutorías reservadas.</p>";
        return;
    }

    misReservas.forEach(reserva => {
        const div = document.createElement("div");
        div.classList.add("horario");
        div.innerHTML = `
            <strong>${reserva.nombreAlumno}</strong><br>
            ${reserva.fecha} — ${reserva.hora}
        `;
        container.appendChild(div);
    });
}


reservaForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("emailAlumno").value.trim();

    const reserva = {
        nombreAlumno: document.getElementById("nombreAlumno").value.trim(),
        emailAlumno:  email,
        fecha: fechaInput.value.trim(),
        hora:  horaInput.value.trim()
    };

    const response = await fetch("http://localhost:8080/reservas", {
        method: "POST",
        headers: {
            "Accept":       "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(reserva)
    });

    if (response.ok) {
        alert("Reserva realizada");
        reservaForm.reset();
        await cargarHorarios();
        await cargarMisReservas(email);
    } else {
        const error = await response.text();
        alert(error);
    }
});

cargarHorarios().then(() => console.log("Horarios cargados"));