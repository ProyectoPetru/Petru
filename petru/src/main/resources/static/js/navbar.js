const WIDTH_TOOLTIP = 800;

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
    console.log(e.getAttribute("redirect"));
    e.addEventListener("click", () => {
        document.location = e.getAttribute("redirect")
    })
})