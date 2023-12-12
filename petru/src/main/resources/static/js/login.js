const recuperarClave = () => {
    const str = prompt("Ingrese el email a recuperar")
    if (str != null) {
        document.location = `/cambiar-clave/${str}`
    }
}