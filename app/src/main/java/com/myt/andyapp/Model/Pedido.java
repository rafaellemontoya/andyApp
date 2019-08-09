package com.myt.andyapp.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class Pedido implements Serializable {

    private int  unidades, cliente, estado, estadoCalificado, calificacionRepartidor;
    private ArrayList<Producto> productos;
    private ArrayList<Carrito> carritos;
    private String fecha,id, nombreEstado, autorizacion, comprobantePago, cuatroDigitos, destino, formaPago, repartidor ;
    //    private Pedido fechaPago;
    private float total;
    private Date  fechaCreacion,fechaEntrega;

    public int getCalificacionRepartidor() {
        return calificacionRepartidor;
    }

    public void setCalificacionRepartidor(int calificacionRepartidor) {
        this.calificacionRepartidor = calificacionRepartidor;
    }

    public int getEstadoCalificado() {
        return estadoCalificado;
    }

    public void setEstadoCalificado(int estadoCalificado) {
        this.estadoCalificado = estadoCalificado;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    public String getFecha() {
        return fecha;
    }


    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }


    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public  void obtenerNombreEstado(int estado){
        String nombreEstado = "";
        switch (estado){
            case 0:
                nombreEstado = "Solicitado";
                break;
            case 1:
                nombreEstado = "En camino";
                break;
            case 2:
                nombreEstado = "Entregado";
                break;
            case 3:
                nombreEstado = "Calificado";
                break;
            case 4:
                nombreEstado = "Cancelado";
                break;


        }
        setNombreEstado(nombreEstado);



    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public String getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(String comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public String getCuatroDigitos() {
        return cuatroDigitos;
    }

    public void setCuatroDigitos(String cuatroDigitos) {
        this.cuatroDigitos = cuatroDigitos;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
    }

    public ArrayList<Carrito> getCarritos() {
        return carritos;
    }

    public void setCarritos(ArrayList<Carrito> carritos) {
        this.carritos = carritos;
    }
}
