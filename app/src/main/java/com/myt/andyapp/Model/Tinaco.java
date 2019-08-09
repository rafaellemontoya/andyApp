package com.myt.andyapp.Model;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class Tinaco {
    private int calificacion;
    private String folio, idProducto, idTinaco, capacidad, color, posicion, nombreColor, nombreCapacidad, nombrePosicion;
    private Boolean isSelected;
    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getIdTinaco() {
        return idTinaco;
    }

    public void setIdTinaco(String idTinaco) {
        this.idTinaco = idTinaco;
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

    public String getNombreColor() {
        return nombreColor;
    }

    public void setNombreColor(String nombreColor) {
        this.nombreColor = nombreColor;
    }

    public String getNombreCapacidad() {
        return nombreCapacidad;
    }

    public void setNombreCapacidad(String nombreCapacidad) {
        this.nombreCapacidad = nombreCapacidad;
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
                nombreCapacidad="Error lts";
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
                nombreColor="Error";
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
                nombrePosicion="Error ";
                break;
        }
        setNombrePosicion(nombrePosicion);
    }
    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
