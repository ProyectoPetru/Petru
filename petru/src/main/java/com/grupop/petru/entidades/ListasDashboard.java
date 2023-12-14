
package com.grupop.petru.entidades;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

public class ListasDashboard {
    private String nombre;
    private Long dato;

    public ListasDashboard(String nombre, Long dato) {
        this.nombre = nombre;
        this.dato = dato;
    }

    public ListasDashboard() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getDato() {
        return dato;
    }

    public void setDato(Long dato) {
        this.dato = dato;
    }
    
}
