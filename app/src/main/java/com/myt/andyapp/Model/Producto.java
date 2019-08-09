package com.myt.andyapp.Model;

import java.io.Serializable;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class Producto implements Serializable {
    private String id, nombre, descripcion, foto, capacidad, color, posicion, categoria, nota, nombreCapacidad, nombreColor, nombrePosicion;
    private int unidades;
    private float precio, costo, importe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public float getPrecio() {
        return precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
        obtenerNombreCapacidad(capacidad);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        obtenerNombreColor(color);
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
        obtenerNombrePosicion(posicion);
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getNomreCapacidad() {
        return nombreCapacidad;
    }

    public void setNombreCapacidad(String nomreCapacidad) {
        this.nombreCapacidad = nomreCapacidad;

    }

    public String getNombreColor() {
        return nombreColor;
    }

    public void setNombreColor(String nombreColor) {
        this.nombreColor = nombreColor;
    }

    public String getNombrePosicion() {
        return nombrePosicion;
    }

    public void setNombrePosicion(String nombrePosicion) {
        this.nombrePosicion = nombrePosicion;
    }

    public void obtenerNombreCapacidad(String capacidad){
        String nombreCapacidad="";
        switch (capacidad){
            case "1":
                nombreCapacidad="400 lts";
                break;
            case "2":
                nombreCapacidad="500 lts";
                break;
            case "3":
                nombreCapacidad="600 lts";
                break;
            case "4":
                nombreCapacidad="800 lts";
                break;
            case "5":
                nombreCapacidad="1,100 lts";
                break;
            case "6":
                nombreCapacidad="1,200 lts";
                break;
            case "7":
                nombreCapacidad="2,800 lts";
                break;
            case "8":
                nombreCapacidad="5,000 lts";
                break;
            case "9":
                nombreCapacidad="10,000 lts";
                break;
            default:
                nombreCapacidad="";
                break;
        }
        setNombreCapacidad(nombreCapacidad);

    }
    public void obtenerNombreColor(String color){
        String nombreColor="";
        switch (color){
            case "1":
                nombreColor="Azul";
                break;
            case "2":
                nombreColor="Beige";
                break;
            case "3":
                nombreColor="Blanco";
                break;
            case "4":
                nombreColor="Negro";
                break;
            default:
                nombreColor="";
                break;
        }
        setNombreColor(nombreColor);
    }
    public void obtenerNombrePosicion(String posicion){
        String nombrePosicion="";
        switch (posicion){
            case "1":
                nombrePosicion="Horizontal";
                break;
            case "2":
                nombrePosicion="Vertical";
                break;
            default:
                nombrePosicion="";
                break;
        }
        setNombrePosicion(nombrePosicion);
    }

}
