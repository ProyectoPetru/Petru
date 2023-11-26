const handleClick = (e) => {
    const informacion = e.target.parentNode.querySelector(".informacion")

    if (informacion.hidden) {
        informacion.classList.remove("informacion-hide")
        informacion.hidden = false
    } else {
        informacion.classList.add("informacion-hide")
        setTimeout(() => {
            if (informacion.classList.contains("informacion-hide")) {
                informacion.classList.remove("informacion-hide")
                informacion.hidden = true
            }
        }, 400)
    }
}