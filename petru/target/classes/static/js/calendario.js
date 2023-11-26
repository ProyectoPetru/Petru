$(document).ready(function () {
    // Variable para almacenar el evento actualmente seleccionado
    var currentEvent;

    // Inicialización del calendario con FullCalendar
    $('#calendar').fullCalendar({
        // Configuración del encabezado y botones del calendario
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
        },
        buttonText: {
            today: 'Hoy',
            month: 'Mes',
            week: 'Semana',
            day: 'Día'
        },
        // Configuración de nombres de meses y días
        monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
        monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
        dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
        dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'],
        // Habilita la edición de eventos en el calendario
        editable: true,
        // Eventos iniciales en el calendario
        events: [
            {
                title: 'Evento 1',
                start: '2023-11-25T10:00:00',
                end: '2023-11-25T12:00:00'
            },
            {
                title: 'Evento 2',
                start: '2023-11-27T14:00:00',
                end: '2023-11-27T16:30:00'
            },
            // Agrega más eventos según sea necesario
        ],
        // Manejadores de eventos al hacer clic en un evento o día en blanco
        eventClick: function (event) {
            currentEvent = event;
            $('#event-title').val(event.title);
            $('#event-start').val(moment(event.start).format('YYYY-MM-DDTHH:mm'));
            $('#event-end').val(moment(event.end).format('YYYY-MM-DDTHH:mm'));
            $('#event-details').show();
        },
        dayClick: function (date) {
            // Pregunta si se desea asignar un nuevo evento al día clickeado
            Swal.fire({
                title: '¿Quieres asignar un evento?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Sí',
                cancelButtonText: 'No'
            }).then((result) => {
                if (result.isConfirmed) {
                    // Inicializa un nuevo evento para el día clickeado
                    currentEvent = {
                        start: date,
                        end: moment(date).add(1, 'hour'),
                        title: ''
                    };
                    $('#event-title').val('');
                    $('#event-start').val(moment(currentEvent.start).format('YYYY-MM-DDTHH:mm'));
                    $('#event-end').val(moment(currentEvent.end).format('YYYY-MM-DDTHH:mm'));
                    $('#event-details').show();
                }
            });
        }
    });

    // Función para actualizar un evento
    window.updateEvent = function () {
        if (currentEvent) {
            // Obtiene los datos del formulario
            var eventData = {
                title: $('#event-title').val(),
                start: $('#event-start').val(),
                end: $('#event-end').val()
            };

            // Actualiza los detalles del evento actual
            currentEvent.title = eventData.title;
            currentEvent.start = eventData.start;
            currentEvent.end = eventData.end;

            // Renderiza o actualiza el evento en el calendario
            if (!currentEvent.id) {
                $('#calendar').fullCalendar('renderEvent', currentEvent, true);
            } else {
                $('#calendar').fullCalendar('updateEvent', currentEvent);
            }

            // Oculta el panel de detalles
            $('#event-details').hide();
        }
    };
});
