package com.example.pasitosappv2;

public class POGOPasos {
    private String fecha;
    private Integer id, bateria;
    private Double latitud, longitud;

    public POGOPasos(Integer id, String hora, Integer bateria, double latitud, double longitud) {
        this.id = null;
        this.fecha = null;
        this.bateria = 0;
        this.latitud = 0.;
        this.longitud = 0.;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBateria() {
        return bateria;
    }

    public void setBateria(Integer bateria) {
        this.bateria = bateria;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }



}
