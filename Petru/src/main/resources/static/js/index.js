const dataTheme = localStorage.getItem("data-theme")
const html = document.querySelector("html")

if (dataTheme != null) {
    html.setAttribute("data-theme", dataTheme)
} else {
    html.setAttribute("data-theme", "light")
}