const carousel = document.querySelector("#inicio").querySelector(".servicios").querySelector(".carousel")
const totalSlides = carousel.querySelectorAll(".carousel-item").length
let currentIndex = 0

const inicioHandleSlide = (index) => {
    if (index < 0) {
        currentIndex = totalSlides - 1
    } else if (index >= totalSlides) {
        currentIndex = 0
    } else {
        currentIndex = index
    }

    const translateValue = (-currentIndex * 100) + "%"
    carousel.querySelector(".carousel-wrapper").style.transform = "translateX(" + translateValue + ")"
}

const inicioNextSlide = () => {
    clearInterval(interval)
    inicioHandleSlide(currentIndex + 1)
    interval = setInterval(() => {
        inicioNextSlide()
    }, 10000)
}

const inicioPrevSlide = () => {
    clearInterval(interval)
    inicioHandleSlide(currentIndex - 1)
    interval = setInterval(() => {
        inicioNextSlide()
    }, 10000)
}

let interval = setInterval(() => {
    inicioNextSlide()
}, 10000)