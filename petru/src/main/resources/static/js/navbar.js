const WIDTH_TOOLTIP = 800

const ventanaNavOpen = () => {
    const fondo = document.querySelector("#navbar").querySelector(".fondo")
    const ventana = document.querySelector("#navbar").querySelector(".fondo").querySelector(".ventana")
    fondo.hidden = false
    ventana.classList.remove("ventana-hide")
    fondo.classList.remove("fondo-hide")
}

const ventanaNavExit = () => {
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

/* Cambia el tema con el boton en el perfil de la navbar */
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

/* Cambio el src de la imagen cuando se establece una via el input file */
const cambiarNavFoto = () => {
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

/* Llamo por primera vez para poner la foto por defecto */
cambiarNavFoto()

/* Muestra tooltips en la navbar si la pantalla es mayor a WIDTH_TOOLTIP*/
if (document.documentElement.clientWidth > WIDTH_TOOLTIP) {
    Array.from(document.querySelector("#navbar").querySelectorAll(".tooltip")).map((e) => {
        e.parentNode.querySelector("a").addEventListener("mouseenter", () => {
            e.hidden = false
            e.classList.remove("tooltip-hide")
        })
        e.parentNode.querySelector("a").addEventListener("mouseleave", () => {
            e.classList.add("tooltip-hide")
            setTimeout(() => {
                if (e.classList.contains("tooltip-hide")) {
                    e.classList.remove("tooltip-hide")
                    e.hidden = true
                }
            }, 500)
        })
        return e
    })
}

/* Maneja que los dropdowns desaparezcan cuando se clickea afuera de ellos */
document.addEventListener("click", (element) => {
    if (Array.from(document.querySelector("#navbar").querySelectorAll(".dropdown")).filter((e) => !e.hidden).length < 1
        &&
        document.querySelector("#navbar").querySelector(".agregar-proyecto").querySelector("svg") != null
        &&
        document.querySelector("#navbar").querySelector(".fondo").hidden) {
        return
    }
    if (element.target.closest(".ventana") == null && element.target.closest(".dropdown") == null) {
        ventanaNavExit()
    }
    if (element.target.closest(".agregar-proyecto") == null) {
        document.querySelector("#navbar").querySelector(".agregar-proyecto").innerHTML = "<svg style='pointer-events: none' xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path fill='currentColor' d='M19 12.998h-6v6h-2v-6H5v-2h6v-6h2v6h6z' /></svg>"
    }

    if (element.target.closest(".item") == null || element.target.closest(".item").querySelector(".dropdown") == null) {
        Array.from(document.querySelector("#navbar").querySelectorAll(".dropdown")).map(e => {
            e.classList.add("dropdown-hide")
            e.style.zIndex = -1
            document.querySelector("main").style.zIndex = -999
            setTimeout(() => {
                if (e.classList.contains("dropdown-hide")) {
                    e.classList.remove("dropdown-hide")
                    document.querySelector("main").style.zIndex = 0
                    e.hidden = true
                }
            }, 200)
        })
    }
})

/* Maneja los dropdowns */
Array.from(document.querySelector("#navbar").querySelectorAll(".dropdown")).map((e) => {
    e.parentNode.querySelector(".icono").addEventListener("click", () => {
        if (e.hidden) {
            Array.from(document.querySelector("#navbar").querySelectorAll(".dropdown")).map(e => {
                e.classList.add("dropdown-hide")
                e.style.zIndex = -1
                document.querySelector("main").style.zIndex = -999
                setTimeout(() => {
                    if (e.classList.contains("dropdown-hide")) {
                        e.classList.remove("dropdown-hide")
                        document.querySelector("main").style.zIndex = 0
                        e.hidden = true
                    }
                }, 200)
            })
            e.hidden = false
            document.querySelector("main").style.zIndex = -999
            e.classList.remove("dropdown-hide")
            setTimeout(() => {
                if (!e.classList.contains("dropdown-hide")) {
                    e.style.zIndex = 1
                    document.querySelector("main").style.zIndex = 0
                }
            }, 200)
        } else {
            e.classList.add("dropdown-hide")
            e.style.zIndex = -1
            document.querySelector("main").style.zIndex = -999
            setTimeout(() => {
                if (e.classList.contains("dropdown-hide")) {
                    e.classList.remove("dropdown-hide")
                    document.querySelector("main").style.zIndex = 0
                    e.hidden = true
                }
            }, 200)
        }
    })
    return e
})

/* cuando se use el atributo redirect, se va a llamar a esta funcion */
Array.from(document.querySelector("#navbar").querySelectorAll("[redirect]")).map(e => {
    e.addEventListener("click", () => {
        document.location = e.getAttribute("redirect")
    })
})

if (document.querySelector("#navbar").querySelector(".agregar-proyecto") != null) {
    document.querySelector("#navbar").querySelector(".agregar-proyecto").addEventListener("click", (e) => {
        if (e.target.tagName == "INPUT" || e.target.tagName == "BUTTON") {
            return
        }

        e.target.innerHTML = ""
        const form = document.createElement("form")
        form.action = "/proyecto/registro"
        form.method = "POST"
        form.enctype = "multipart/form-data"
        const input = document.createElement("input")
        input.type = "text"
        input.name = "nombre"
        input.autocomplete = "off"
        const submit = document.createElement("button")
        submit.textContent = "✔"
        submit.type = "submit"
        form.appendChild(input)
        form.appendChild(submit)
        e.target.appendChild(form)
        input.focus()
    })
}
