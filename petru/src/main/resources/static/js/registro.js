/* Cambio el src de la imagen cuando se establece una via el input file */
const cambiarFoto = () => {
    let file = document.querySelector("#imagen").files[0]
    let reader = new FileReader()

    if (file == null) {
        document.querySelector("#registro-logo").src = "/img/user.png"
        document.querySelector("#registro-logo").parentNode.style.display = "block"
        return
    }

    if (file.size < 600000) {
        reader.onload = function(e) {
            const image = document.getElementById("registro-logo")
    
            image.src = e.target.result
        }
    
        reader.readAsDataURL(file)
    } else {
        document.querySelector("#imagen").value = ""
        document.querySelector("#registro-logo").src = "/img/user.png"
        document.querySelector("#errorAlert").getElementsByClassName("alert-info")[0].innerHTML = "La imagen no puede pesar mas de 600kb"
        document.querySelector("#errorAlert").hidden = false
        exitTimer()
    }
}

/* Llamo por primera vez para poner la foto por defecto */
cambiarFoto()

/* Borro la foto cuando se usa el boton "limpiar" */
const manejoReset = () => {
    document.getElementById("registro-logo").src = "/img/user.png"
}