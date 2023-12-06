const redirects = Array.from(document.querySelector("#navbar").querySelectorAll("[redirect]"))
const dropdowns = Array.from(document.querySelector("#navbar").querySelectorAll(".dropdown"))
const groups = Array.from(document.querySelector("#navbar").querySelectorAll(".group"))
const agregarProyecto = document.querySelector("#navbar").querySelector(".agregar-proyecto")

document.addEventListener("click", (e) => {
    if (dropdowns.filter(dropdown => !dropdown.hidden).length == 0
        &&
        groups.filter(group => !group.hidden).length == 0
        &&
        agregarProyecto.querySelector("form").hidden) {
        return
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