const sendUsuList = (e) => {
    const email = e.target.closest("tr").querySelector("input[name=email]").value;
    const str = prompt("Ingrese el cuerpo del mensaje a " + email)
    if (str != null) {
        document.location = `/mensaje?cuerpo=${str}&destinatario=${email}`
    }
}

const joinPro = (e) => {
    document.location = `/proyecto/unirse/${e.target.parentNode.parentNode.querySelector('input[name=id]').value}`
}