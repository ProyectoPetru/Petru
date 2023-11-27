package com.grupop.petru.servicios;
/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Proyecto;
import com.grupop.petru.entidades.Tarea;
import com.grupop.petru.enumeraciones.TipoTarea;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.repositorios.EtiquetaRepositorio;
import com.grupop.petru.repositorios.ProyectoRepositorio;
import com.grupop.petru.repositorios.TareaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TareaServicio {
    
    @Autowired
    private TareaRepositorio tareaRepositorio;
    
    @Autowired 
    private ProyectoRepositorio proyectoRepositorio;
    
//    @Autowired 
//    private EtiquetaRepositorio etiquetaRepositorio;
   
   //-------------------------------------CREAR-----------------------------------------------------// 
   @Transactional
   public void crearTarea(String id, String nombre, String idProyecto)throws MiException{
    
       Proyecto proyecto = (Proyecto) proyectoRepositorio.findById(idProyecto).get();
       
       
       Tarea tarea = new Tarea();
       
       tarea.setId(id);
       tarea.setNombre(nombre);
       tarea.setProyecto(proyecto);
       //tarea.setEtiquetas(etiqueta);
       tarea.setTipoTarea(TipoTarea.TODO);
       
       tareaRepositorio.save(tarea);
   }
   
 //---------------------------------------LISTAR -----------------------------------------------------//
    public List<Tarea> listarTareas(){
    
        List<Tarea> tarea = new ArrayList();
        
        tarea = tareaRepositorio.findAll();
        
        return tarea;
    }
   
 //-------------------------------------MODIFICAR-----------------------------------------------------//
    public void modificarTarea(String id, String nombre, String idProyecto, String idEtiqueta) throws MiException{
    
       validar(id, nombre, idProyecto, idEtiqueta);
        
       Optional<Tarea> respuestaTarea = tareaRepositorio.findById(id);
       Optional<Proyecto> respuestaProyecto = proyectoRepositorio.findById(idProyecto);
       
       Proyecto proyecto = new Proyecto();
       
       
       if(respuestaProyecto.isPresent()){
           proyecto = respuestaProyecto.get();
       }
       
       if(respuestaTarea.isPresent()){
           
           Tarea tarea = respuestaTarea.get();
           
           tarea.setId(id);
           tarea.setNombre(nombre);
           tarea.setProyecto(proyecto);
           //tarea.setEtiquetas();
           tarea.setTipoTarea(TipoTarea.TODO);
       
          tareaRepositorio.save(tarea);
        } 
    }
     
    //-------------------------------------ELIMINAR -----------------------------------------------------//
     public void EliminarTarea(String id){
     
         Optional<Tarea> respuestaTarea = tareaRepositorio.findById(id);
         
         if(respuestaTarea.isPresent()){
             
            tareaRepositorio.deleteById(id);
         }
     }
 
    //-------------------------------------VALIDAR -----------------------------------------------------//
    private void validar (String id, String nombre, String idProyecto, String idEtiqueta) throws MiException{
    
        if(id == null){
            throw new MiException("El ID no puede ser NULO");
        }
        
        if(nombre == null || nombre.isEmpty()){
            throw new MiException("El NOMBRE no puede ser NULO o estar VACIO");
        }
        
        if(idProyecto == null || idProyecto.isEmpty()){
            throw new MiException("El PROYECTO no puede ser NULO o estar VACIO");
        }
        
//        if(idEtiqueta == null || idEtiqueta.isEmpty()){
//            throw new MiException("La ETIQUETA no puede ser NULA o estar VACIA");
//        }
    }
}
