const horariosContainer = document.getElementById("horarios-container");
const reservaForm = document.getElementById("reserva-form");

const fechaInput = document.getElementById("fecha");
const horaInput = document.getElementById("hora");


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
            horaInput.value = horario.hora;
        });

        horariosContainer.appendChild(div);
    });
}

reservaForm.addEventListener("submit", async (e) => {

    e.preventDefault();

    const reserva = {
        nombreAlumno: document.getElementById("nombreAlumno").value,
        emailAlumno: document.getElementById("emailAlumno").value,
        fecha: fechaInput.value.trim(),
        hora: horaInput.value.trim()
    };


    const response = await fetch("http://localhost:8080/reservas", {
        method: "POST",
        headers: {
            "Accept" : "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(reserva)
    });

    if (response.ok) {
        alert("Reserva realizada");

        reservaForm.reset();

        await cargarHorarios();
    } else {
        const error = await response.text();
        alert(error);
    }
});

cargarHorarios().then(() => console.log("Horarios cargados"));