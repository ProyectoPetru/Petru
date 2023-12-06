
package com.grupop.petru.entidades;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */


import com.grupop.petru.enumeraciones.Visibilidad;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
public class Proyecto {
   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   private String id;
   private String nombre;
   @OneToOne
   @Basic(optional = true)
   private Archivo archivo;
   @ManyToMany
   private List<Usuario> usuarios;
   @Enumerated(EnumType.STRING)
   private Visibilidad visibilidad;
   @Column(columnDefinition = "LONGTEXT")
   @Basic(optional = true)
   private String notas;
   private Boolean baja;
   @Temporal(TemporalType.DATE)
   private Date fecha;
   @Temporal(TemporalType.DATE)
   private Date fechaLimite;
   
   public void addUsuario(Usuario usuario) {
      this.usuarios.add(usuario);
   }
}
