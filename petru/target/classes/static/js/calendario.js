$(document).ready(function () {
    // Configuración del calendario
    $('#calendar').fullCalendar({
        // Configuración del encabezado y botones del calendario
        header: {
            left: 'prev, today',
            center: 'title',
            right: 'next'
        },
        buttonText: {
            today: 'Hoy',
            month: 'Mes',
        },
        // Configuración adicional del calendario
        monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
        monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
        dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
        dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'],
        editable: true,
        eventClick: function (event) {
            // Lógica adicional al hacer clic en un evento
            console.log('Event Clicked:', event);
            // Puedes agregar aquí más acciones al hacer clic en un evento
        },
        dayClick: function (date) {
            // Muestra un formulario en un cuadro de diálogo para ingresar detalles del evento
            Swal.fire({
                title: 'Ingrese los detalles del evento',
                html:
                    '<label for="event-title">Título:</label>' +
                    '<input type="text" id="event-title" class="swal2-input">' +
                    '<label for="event-description">Descripción:</label>' +
                    '<textarea id="event-description" class="swal2-input"></textarea>' +
                    '<label for="event-start">Inicio:</label>' +
                    '<input type="date" id="event-start" class="swal2-input">' +
                    '<label for="event-end">Fin:</label>' +
                    '<input type="date" id="event-end" class="swal2-input">',
                focusConfirm: false,
                preConfirm: () => {
                    // Captura los valores del formulario
                    return {
                        title: $('#event-title').val(),
                        description: $('#event-description').val(),
                        start: $('#event-start').val(),
                        end: $('#event-end').val()
                    };
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    // Agrega el nuevo evento al calendario
                    $('#calendar').fullCalendar('renderEvent', result.value, true);
                }
            });
        }
    });
});
