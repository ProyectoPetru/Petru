const redirects = Array.from(document.querySelector("#navbar").querySelectorAll("[redirect]"))
const dropdowns = Array.from(document.querySelector("#navbar").querySelectorAll(".dropdown"))
const groups = Array.from(document.querySelector("#navbar").querySelectorAll(".group"))
const agregarProyecto = document.querySelector("#navbar").querySelector(".agregar-proyecto")

const ventanaNavOpen = () => {
    const fondo = document.querySelector("#navbar").querySelector(".fondo")
    const ventana = document.querySelector("#navbar").querySelector(".fondo").querySelector(".ventana")
    fondo.hidden = false
    ventana.classList.remove("ventana-hide")
    fondo.classList.remove("fondo-hide")
}

const ventanaNavExit = () => {
    if (document.querySelector("#navbar").querySelector(".fondo") != null) {
        const fondo = document.querySelector("#navbar").querySelector(".fondo")
        const ventana = fondo.querySelector(".ventana")
        ventana.classList.add("ventana-hide")
        fondo.classList.add("fondo-hide")
        setTimeout(() => {
            if (ventana.classList.contains("ventana-hide")) {
                ventana.classList.remove("ventana-hide")
                fondo.classList.remove("fondo-hide")
                fondo.hidden = true
            }
        }, 300)
    }
}

const cambiarNavFoto = () => {
    if (document.querySelector("#imagen") != null) {
        let file = document.querySelector("#imagen").files[0]
        let reader = new FileReader()

        if (file == null) {
            document.querySelector("#perfil-logo").src = document.querySelector("#navbar").querySelector(".ventana").querySelector(".perfil-foto").getAttribute("defecto")
            document.querySelector("#perfil-logo").parentNode.style.display = "block"
            return
        }

        if (file.size < 600000) {
            reader.onload = function (e) {
                const image = document.querySelector("#perfil-logo")

                image.src = e.target.result
            }

            reader.readAsDataURL(file)
        } else {
            document.querySelector("#imagen").value = ""
            document.querySelector("#perfil-logo").src = document.querySelector("#navbar").querySelector(".ventana").querySelector(".perfil-foto").getAttribute("defecto")
            document.querySelector("#alertas").querySelector(".error").querySelector(".informacion").innerHTML = "La imagen no puede pesar mas de 600kb"
            document.querySelector("#alertas").querySelector(".error").hidden = false
        }
    }
}

cambiarNavFoto()

document.addEventListener("click", (e) => {
    if (dropdowns.filter(dropdown => !dropdown.hidden).length == 0
        &&
        groups.filter(group => !group.hidden).length == 0
        &&
        agregarProyecto.querySelector("form").hidden
        &&
        document.querySelector("#navbar").querySelector(".fondo").hidden) {
        return
    }
    if (e.target.closest(".ventana") == null && e.target.closest(".dropdown") == null) {
        ventanaNavExit()
    }
    if (e.target.closest(".dropdown-handler") == null) {
        dropdowns.map(dropdown => dropdown.hidden = true)
    }
    if (e.target.closest(".group-handler") == null) {
        groups.map(group => group.hidden = true)
    }
    if (e.target.closest(".agregar-proyecto") == null) {
        agregarProyecto.querySelector("svg").classList.remove("hidden")
        agregarProyecto.querySelector("form").hidden = true
    }
})

redirects.map(redirect => {
    redirect.addEventListener("click", () => {
        document.location = redirect.getAttribute("redirect")
    })
})

dropdowns.map(dropdown => {
    dropdown.parentNode.querySelector(".icono").addEventListener("click", () => {
        dropdowns.map(dropdown => dropdown.hidden = true)
        dropdown.hidden = false
    })
})

groups.map(group => {
    group.parentNode.querySelector(".icono").addEventListener("click", () => {
        let hidden = group.hidden
        groups.map(group => group.hidden = true)
        group.hidden = !hidden
    })
})

const navProyectoHandler = (e) => {
    if (e.target.tagName == "INPUT" || e.target.tagName == "BUTTON") return
    e.target.querySelector("svg").classList.add("hidden")
    e.target.querySelector("form").hidden = false
}

const cambiarTema = () => {
    const html = document.querySelector("html")
    if (html.getAttribute("data-theme") == "dark") {
        html.setAttribute("data-theme", "light")
        localStorage.setItem("data-theme", "light")
    } else {
        html.setAttribute("data-theme", "dark")
        localStorage.setItem("data-theme", "dark")
    }
}