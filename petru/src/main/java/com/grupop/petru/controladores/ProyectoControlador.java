
package com.grupop.petru.controladores;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Comentario;
import com.grupop.petru.entidades.Etiqueta;
import com.grupop.petru.entidades.Proyecto;
import com.grupop.petru.entidades.Tarea;
import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.enumeraciones.TipoTarea;
import com.grupop.petru.enumeraciones.Visibilidad;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.servicios.ProyectoServicio;
import com.grupop.petru.servicios.TareaServicio;
import com.grupop.petru.servicios.UsuarioServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/proyecto") // localhost:8080/proyecto
public class ProyectoControlador {

    @Autowired
    private ProyectoServicio proyectoServicio;
    @Autowired
    private TareaServicio tareaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    private Usuario cargarModelo(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        String error = (String) session.getAttribute("error");
        String exito = (String) session.getAttribute("exito");
        session.removeAttribute("error");
        session.removeAttribute("exito");

        if (usuario != null) {
            modelo.addAttribute("proyectos", proyectoServicio.listarPorUsuario(usuario.getId()));
        }
        modelo.addAttribute("usuariosession", usuario);
        modelo.put("error", error);
        modelo.put("exito", exito);

        return usuario;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping("")
    public String proyectos(@RequestParam(required = false) String error, HttpSession session,
            ModelMap modelo) {
        cargarModelo(modelo, session);

        List<Proyecto> proyectos = proyectoServicio.listarTodos();

        modelo.addAttribute("proyectos", proyectos);

        return "proyecto/listar.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping("/unirse/{id}")
    public String unirse(@PathVariable String id, @RequestParam(required = false) String error, HttpSession session,
            ModelMap modelo) {
        Usuario usuario = cargarModelo(modelo, session);

        try {        
            proyectoServicio.invitar(id, usuario.getEmail());

            session.setAttribute("exito", "Se ha unido con exito!");

            return "redirect:/proyecto/" + id;
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());

            return "redirect:/proyecto/" + id;
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PostMapping("/buscar")
    public String buscarUsuario(@RequestParam String busca, @RequestParam String filtro, ModelMap modelo,
            HttpSession session) {
        cargarModelo(modelo, session);

        List<Proyecto> proyectos = new ArrayList<Proyecto>();
        if (filtro.equals("nombre")) {
            proyectos = proyectoServicio.listarPorNombre(busca);
        } else if (filtro.equals("notas")) {
            proyectos = (proyectoServicio.listarPorNotas(busca));
        }
        modelo.addAttribute("proyectos", proyectos);

        return "proyecto/listar.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PostMapping("/modificar")
    public String modificar(@RequestParam String id, @RequestParam String nombre, @RequestParam String notas, ModelMap modelo,
            HttpSession session) {
        cargarModelo(modelo, session);
        
        try {
            proyectoServicio.modificar(id, nombre, notas);

            session.setAttribute("exito", "Proyecto modificado con exito!");
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());
        }

        return "redirect:/proyecto";
    }

    @GetMapping("/{id}")
    public String proyecto(@PathVariable String id, @RequestParam(required = false) String error, HttpSession session,
            ModelMap modelo) {
        cargarModelo(modelo, session);

        Proyecto proyecto = proyectoServicio.getOne(id);

        List<Tarea> tareas = tareaServicio.listarTareasProyecto(id);

        modelo.addAttribute("proyecto", proyecto);
        modelo.addAttribute("tareas", tareas);

        tareas.forEach(tarea -> tarea.ordernarComentarios());

        return "proyecto.html";
    }

    @PostMapping("/{id}/invitar")
    public String invitar(@PathVariable String id, @RequestParam String email, HttpSession session, ModelMap modelo,
            HttpServletRequest request) {
        try {
            proyectoServicio.invitar(id, email);

            session.setAttribute("exito", "Usuario invitado con exito");

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
    }

    @GetMapping("/registrar")
    public String carga_proyecto(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        cargarModelo(modelo, session);
        Usuario logueado = cargarModelo(modelo, session);

        List<Usuario> clientes = usuarioServicio.listarClientes();
        List<Usuario> agentes = usuarioServicio.listarColaboradores();
        List<String> visibilidad = new ArrayList();
        for (Visibilidad v : Visibilidad.values()) {
            visibilidad.add(v.toString());
        }
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("agentes", agentes);
        modelo.addAttribute("visibilidad", visibilidad);
        if (logueado.getRol().toString().equals("ADMIN"))
            modelo.addAttribute("proyectos", proyectoServicio.listarTodos());
        else
            modelo.addAttribute("proyectos", proyectoServicio.listarPorUsuario(logueado.getId()));

        return "lista_proyectosNuevo.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PostMapping("/busqueda")
    public String busca_proyecto(@RequestParam String busca, HttpSession session, ModelMap modelo) {
        
        cargarModelo(modelo, session);
        Usuario logueado = cargarModelo(modelo, session);

        List<Usuario> clientes = usuarioServicio.listarClientes();
        List<Usuario> agentes = usuarioServicio.listarColaboradores();
        List<String> visibilidad = new ArrayList();
        for (Visibilidad v : Visibilidad.values()) {
            visibilidad.add(v.toString());
        }
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("agentes", agentes);
        modelo.addAttribute("visibilidad", visibilidad);
        modelo.addAttribute("proyectos", proyectoServicio.listarPorBusqueda(busca));

        return "lista_proyectosNuevo.html";
    }








    @PostMapping("/registro")
    public String proyectoRegistro(@RequestParam String nombre, @RequestParam(required = false) MultipartFile archivo,
            @RequestParam(required = false) String idCliente, @RequestParam(required = false) String idAgente,
            String fechaLimite, Visibilidad visibilidad, ModelMap modelo,
            HttpSession session, HttpServletRequest request) throws ParseException {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);
        
        try {
            if (idCliente == null && idAgente == null) {
                proyectoServicio.guardar(archivo, nombre, Visibilidad.GRUPO, "s",
                        Arrays.asList(new Usuario[] { logueado }));
            } else {
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
                Date fecha = formato.parse(fechaLimite);
                proyectoServicio.guardar(archivo, nombre, visibilidad, "s",
                        idCliente, idAgente, fecha);
      //          modelo.put("exito", "El Proyecto se cargó correctamente!");
            }
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
        session.setAttribute("exito", "Proyecto registrado con exito!");

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    /* Propuesta de Mati para el calendario
     
    postmapping que va en proyectoControlador

 @PostMapping("/guardarEvento")
    public String guardarEvento(@RequestParam String title, @RequestParam String description,
            @RequestParam String start, @RequestParam String end, @RequestParam String projectId,ModelMap modelo, HttpSession session,HttpServletRequest request) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date fechainicio;
        Date fechafin ;

         Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);
        try {
            fechainicio = formato.parse(start);
            fechafin = formato.parse(end);
            eventoServicio.crearEvento(title, fechainicio, fechafin, description, projectId);
            
        } catch (MiException e) {
             session.setAttribute("error", e.getMessage());

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


    calendario.js modificado

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
        
        const projectId = obtenerIdProyectoDeUrl();

        const projectIdInput = '<input type="hidden" name="projectId" value="' + projectId + '">';
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
            html: projectIdInput + formHtml,
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
            if (result.isConfirmed) {
                const form = document.createElement('form');
                form.action = '/proyecto/guardarEvento'; // Ruta del controlador en el servidor para guardar eventos
                form.method = 'post';
            
                const eventData = {
                    title: $('#event-title').val(),
                    description: $('#event-description').val(),
                    start: $('#event-start').val(),
                    end: $('#event-end').val(),
                    projectId: projectId
                };
            
                // Crear campos ocultos para enviar los datos al servidor
                Object.entries(eventData).forEach(([key, value]) => {
                    const input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = key;
                    input.value = value;
                    form.appendChild(input);
                });

                window.location.href = '/proyecto/guardarEvento?' + $.param(eventData);
            
                document.body.appendChild(form);
                form.submit();
            }
        });
    }
    function obtenerIdProyectoDeUrl() {
        const url = window.location.href;
        const match = url.match(/proyecto\/([a-f\d-]+)$/); // Patron para extraer el ID del proyecto
        return match ? match[1] : null;
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
     
     */
}
