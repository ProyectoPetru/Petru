$(document).ready(function () {
    // Mapa para almacenar colores asociados a IDs de eventos
    const eventColorMap = {};

    $('#calendar').fullCalendar({
        header: {
            left: 'prev,today',
            center: 'title',
            right: 'next'
        },
        buttonText: {
            today: 'Hoy',
            month: 'Mes',
        },
        monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
        monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
        dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
        dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'],
        editable: true,
        eventClick: function (event) {
            // Lógica al hacer clic en un evento
            showEventDetails(event);
        },
        dayClick: function (date) {
            // Muestra un formulario en un cuadro de diálogo para ingresar detalles del evento
            showEventDetails({
                start: date,
                end: date
            });
        },
        eventRender: function (event, element) {
            // Verifica si el evento ya tiene un color asignado
            if (!eventColorMap[event._id]) {
                // Si no tiene un color asignado, genera uno y lo almacena en el mapa
                eventColorMap[event._id] = getRandomColor();
            }

            // Asigna el color al evento
            element.css('background-color', eventColorMap[event._id]);
        }
    });

    function showEventDetails(event) {
        // Determina si es un nuevo evento o uno existente
        const isNewEvent = !event.title;

        // Configuración del formulario en el cuadro de diálogo
        const formHtml =
            '<label for="event-title">Título:</label>' +
            '<input type="text" id="event-title" class="swal2-input" value="' + (event.title || '') + '">' +
            '<label for="event-description">Descripción:</label>' +
            '<textarea id="event-description" class="swal2-input">' + (event.description || '') + '</textarea>' +
            '<label for="event-start">Inicio:</label>' +
            '<input type="date" id="event-start" class="swal2-input" value="' + formatDate(event.start) + '">' +
            '<label for="event-end">Fin:</label>' +
            '<input type="date" id="event-end" class="swal2-input" value="' + formatDate(event.end) + '">';

        // Configuración de los botones en el cuadro de diálogo
        const buttons = {
            guardar: {
                text: 'Guardar',
                value: 'save',
            },
            cancelar: 'Cancelar',
        };

        // Add "Eliminar" (Delete) button for existing events
        if (!isNewEvent) {
            buttons.eliminar = {
                text: 'Eliminar',
                value: 'delete',
                className: 'btn-danger'
            };
        }

        // Muestra el cuadro de diálogo
        Swal.fire({
            title: isNewEvent ? 'Ingrese los detalles del evento' : 'Modificar evento',
            html: formHtml,
            focusConfirm: false,
            showCancelButton: true,
            customClass: {
                container: 'custom-swal-container'
            },
            confirmButtonText: 'Guardar',
            cancelButtonText: 'Cancelar',
            showLoaderOnConfirm: true,
            preConfirm: () => {
                // Captura los valores del formulario
                return {
                    title: $('#event-title').val(),
                    description: $('#event-description').val(),
                    start: $('#event-start').val(),
                    end: $('#event-end').val()
                };
            },
            allowOutsideClick: () => !Swal.isLoading(),
            buttons: buttons,  // Use the custom buttons configuration
        }).then((result) => {
            if (result.isConfirmed) {
                // Guarda o actualiza el evento en el calendario
                if (isNewEvent) {
                    $('#calendar').fullCalendar('renderEvent', result.value, true);
                } else {
                    event.title = result.value.title;
                    event.description = result.value.description;
                    event.start = result.value.start;
                    event.end = result.value.end;
                    $('#calendar').fullCalendar('updateEvent', event);
                }
            } else if (result.value === 'delete') {
                // Elimina el evento si se hace clic en "Eliminar" y el evento ya existe
                $('#calendar').fullCalendar('removeEvents', event._id);
            }
        });
    }

    function formatDate(date) {
        // Función para formatear la fecha en formato YYYY-MM-DD
        return date ? moment(date).format('YYYY-MM-DD') : '';
    }

    function getRandomColor() {
        // Genera un color hexadecimal aleatorio
        return '#' + Math.floor(Math.random()*16777215).toString(16);
    }
});
