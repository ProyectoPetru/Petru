const redirects = Array.from(document.querySelector("#navbar").querySelectorAll("[redirect]"))
const dropdowns = Array.from(document.querySelector("#navbar").querySelectorAll(".dropdown"))

document.addEventListener("click", (e) => {
    if (dropdowns.filter(dropdown => !dropdown.hidden) == 0) {
        return
    }

    if (e.target.closest(".item") == null) {
        dropdowns.map(dropdown => dropdown.hidden = true)
    }
})

redirects.map(redirect => {
    redirect.addEventListener("click", () => {
        document.location = redirect.getAttribute("redirect")
    })
})

dropdowns.map(dropdown => {
    dropdown.parentNode.querySelector(".icono").addEventListener("click", (e) => {
        dropdown.hidden = !dropdown.hidden
    })
})

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