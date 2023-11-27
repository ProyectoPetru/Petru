Array.from(document.querySelector("#proyecto").querySelector(".contenedor").querySelectorAll(".tarea")).map((e) => {
    e.addEventListener("mouseenter", (e) => {
        const svg = e.target.querySelector("svg").classList.remove("hidden")
    })
})

Array.from(document.querySelector("#proyecto").querySelector(".contenedor").querySelectorAll(".tarea")).map((e) => {
    e.addEventListener("mouseleave", (e) => {
        const svg = e.target.querySelector("svg").classList.add("hidden")
    })
})

Array.from(document.querySelector("#proyecto").querySelector(".contenedor").querySelectorAll(".tarea")).map((e) => {
    e.querySelector("svg").addEventListener("click", () => {
        Array.from(document.querySelector("#proyecto").querySelector(".ventana").querySelectorAll(".ventana>div")).map((e) => e.hidden = true)
        document.querySelector("#proyecto").querySelector(".ventana").querySelector(`#${e.id}`).hidden = false
        document.querySelector("#proyecto").querySelector(".ventana").hidden = false
    })
})

document.addEventListener("click", (element) => {
    if (document.querySelector("#proyecto").querySelector(".ventana").hidden
        &&
        document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".agregar-input") == null
        &&
        document.querySelector("#proyecto").querySelector(".ventana").querySelector(".agregar-input") == null) {
        return
    }
    if (element.target.closest(".ventana") == null && element.target.closest(".tarea") == null) {
        document.querySelector("#proyecto").querySelector(".ventana").hidden = true
    }

    if (element.target.closest(".agregar") == null) {
        if (element.target.closest(".ventana") != null) {
            Array.from(document.querySelector("#proyecto").querySelector(".ventana").querySelectorAll(".agregar")).filter((e) => e.classList.contains("agregar-input")).map((e) => {
                e.removeChild(e.querySelector("form"))
                e.innerHTML = "<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'> <path fill='currentColor' d='M19 12.998h-6v6h-2v-6H5v-2h6v-6h2v6h6z'/></svg>"
                e.classList.remove("agregar-input")
            })
        }
        if (document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".agregar-input") != null) {
            document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".agregar").removeChild(document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".agregar").querySelector("form"))
            document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".agregar").innerHTML = "<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'> <path fill='currentColor' d='M19 12.998h-6v6h-2v-6H5v-2h6v-6h2v6h6z'/></svg>"
            document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".agregar").classList.remove("agregar-input")
        }
    }
})

document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".agregar").addEventListener("click", (e) => {
    if (e.target.tagName == "INPUT" || e.target.tagName == "BUTTON") {
        return
    }

    e.target.classList.add("agregar-input")
    e.target.removeChild(e.target.querySelector("svg"))
    const form = document.createElement("form")
    form.action = "/tarea/registro"
    const id = document.createElement("input")
    id.hidden = true
    id.type = "text"
    id.name = "id"
    id.value = e.target.getAttribute("tarea")
    const input = document.createElement("input")
    input.type = "text"
    input.name = "nombre"
    input.autocomplete = "off"
    const submit = document.createElement("button")
    submit.textContent = "✔"
    submit.type = "submit"
    submit.classList.add("agregar-submit")
    form.appendChild(id)
    form.appendChild(input)
    form.appendChild(submit)
    e.target.appendChild(form)
    input.focus()
})

Array.from(document.querySelector("#proyecto").querySelector(".ventana").querySelectorAll(".agregar")).map((e) => {
    e.addEventListener("click", (e) => {
        if (e.target.tagName == "INPUT" || e.target.tagName == "BUTTON" || e.target.tagName == "SELECT") {
            return
        }

        e.target.classList.add("agregar-input")
        e.target.textContent = ""
        const form = document.createElement("form")
        form.action = "/etiqueta/registro"
        const id = document.createElement("input")
        id.hidden = true
        id.type = "text"
        id.name = "id"
        id.value = e.target.getAttribute("tarea")
        const input = document.createElement("input")
        input.type = "text"
        input.name = "nombre"
        input.autocomplete = "off"
        const select = document.createElement("select")
        select.name = "color"
        const blue = document.createElement("option")
        blue.style.background = "blue"
        blue.value = "blue"
        blue.text = "🟦"
        const red = document.createElement("option")
        red.value = "red"
        red.text = "🟥"
        const yellow = document.createElement("option")
        yellow.value = "yellow"
        yellow.text = "🟨"
        const green = document.createElement("option")
        green.value = "green"
        green.text = "🟩"
        select.appendChild(blue)
        select.appendChild(red)
        select.appendChild(yellow)
        select.appendChild(green)
        const submit = document.createElement("button")
        submit.textContent = "✔"
        submit.type = "submit"
        submit.classList.add("agregar-submit")
        form.appendChild(id)
        form.appendChild(input)
        form.appendChild(select)
        form.appendChild(submit)
        e.target.appendChild(form)
        input.focus()
    })
})

Array.from(document.querySelector("#proyecto").querySelectorAll(".tarea")).map((e) => {
    Array.from(e.querySelectorAll(".etiqueta")).map((e) => {
        e.addEventListener("mouseenter", () => {
            e.querySelector(".tooltip").hidden = false
        })
        e.addEventListener("mouseleave", () => {
            e.querySelector(".tooltip").hidden = true
        })
    })
    return e
})

Array.from(document.querySelector("#proyecto").querySelector(".contenedor").querySelectorAll(".tarea")).map((e) => {
    e.addEventListener("dragstart", (e) => {

        document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".hechas")
    })
})

function dragStart(e) {
    let selected = e.target

    document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".pendientes").addEventListener("dragover", (e) => {
        e.preventDefault()
    })
    document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".pendientes").addEventListener("drop", (e) => {
        if (selected != null) {
            if (selected.closest(".pendientes") == null) {
                document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".pendientes").appendChild(selected)
                document.location = `/tarea/modificar/${selected.id}?tipoTarea=TODO`
            }
            selected = null
        }
    })

    document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".haciendo").addEventListener("dragover", (e) => {
        e.preventDefault()
    })
    document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".haciendo").addEventListener("drop", (e) => {
        if (selected != null) {
            if (selected.closest(".haciendo") == null) {
                document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".haciendo").appendChild(selected)
                document.location = `/tarea/modificar/${selected.id}?tipoTarea=DOING`
            }
            selected = null
        }
    })

    document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".hechas").addEventListener("dragover", (e) => {
        e.preventDefault()
    })
    document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".hechas").addEventListener("drop", (e) => {
        if (selected != null) {
            if (selected.closest(".hechas") == null) {
                document.querySelector("#proyecto").querySelector(".contenedor").querySelector(".hechas").appendChild(selected)
                document.location = `/tarea/modificar/${selected.id}?tipoTarea=DONE`
            }
            selected = null
        }
    })
}