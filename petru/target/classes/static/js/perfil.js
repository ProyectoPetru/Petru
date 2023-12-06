const cambiarPeFoto = () => {
    if (document.querySelector("#perfil-imagen") != null) {
        let file = document.querySelector("#perfil-imagen").files[0]
        let reader = new FileReader()

        if (file == null) {
            document.querySelector("#perfil-photo").src = document.querySelector("main").querySelector(".container-perfil").querySelector(".perfil-foto").getAttribute("defecto")
            document.querySelector("#perfil-photo").parentNode.style.display = "block"
            return
        }

        if (file.size < 600000) {
            reader.onload = function (e) {
                const image = document.querySelector("#perfil-photo")

                image.src = e.target.result
            }

            reader.readAsDataURL(file)
        } else {
            document.querySelector("#perfil-imagen").value = ""
            document.querySelector("#perfil-photo").src = document.querySelector("#navbar").querySelector(".ventana").querySelector(".perfil-foto").getAttribute("defecto")
            document.querySelector("#alertas").querySelector(".error").querySelector(".informacion").innerHTML = "La imagen no puede pesar mas de 600kb"
            document.querySelector("#alertas").querySelector(".error").hidden = false
        }
    }
}

cambiarPeFoto()