
package com.grupop.petru.entidades;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.enumeraciones.TipoTarea;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
public class Tarea {
   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   private String id;
   private String nombre;
   @ManyToOne
   private Proyecto proyecto;
   @OneToMany
   @Basic(optional = true)
   private List<Comentario> comentarios;
   @Enumerated(EnumType.STRING)
   private TipoTarea tipoTarea;
   @OneToMany
   @Basic(optional = true)
   private List<Etiqueta> etiquetas;
   private Boolean baja;

   public void addEtiqueta(Etiqueta etiqueta) {
      etiquetas.add(etiqueta);
   }

   public void addComentario(Comentario comentario) {
      comentarios.add(comentario);
   }
}
