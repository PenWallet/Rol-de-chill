package com.penwallet.roldechill.Entities;

public class Creature {
    private static int idGenerator = 0;
    private String nombre;
    private int vida, vidaMaxima, iniciativa, id;
    private boolean esJugador;
    private Status estado;

    public Creature()
    {
        this.id = idGenerator++;
        this.nombre = "";
        this.vida = 1;
        this.vidaMaxima = 1;
        this.iniciativa = 1;
        this.esJugador = false;
        this.estado = Status.NORMAL;
    }

    public Creature(String nombre, int vida, int vidaMaxima, int iniciativa, boolean esJugador, Status estado)
    {
        this.id = idGenerator++;
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vidaMaxima;
        this.iniciativa = iniciativa;
        this.esJugador = esJugador;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    public int getIniciativa() {
        return iniciativa;
    }

    public void setIniciativa(int iniciativa) {
        this.iniciativa = iniciativa;
    }

    public boolean isEsJugador() {
        return esJugador;
    }

    public void setEsJugador(boolean esJugador) {
        this.esJugador = esJugador;
    }

    public Status getEstado() {
        return estado;
    }

    public void setEstado(Status estado) {
        this.estado = estado;
    }

    public void cambiarVida(int vidaASumarORestar)
    {
        this.vida += vidaASumarORestar;
    }
}
