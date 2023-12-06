document.querySelector("#alertas").querySelector(".error").querySelector(".salir").addEventListener("click", (e) => {
    const error = document.querySelector("#alertas").querySelector(".error")
    error.classList.add("error-hide")
    setTimeout(() => {
        error.classList.remove("error-hide")
        error.hidden = true
    }, 800)
})