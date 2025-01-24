package Modelo;

import Excepciones.PersistenciaException;

import java.sql.Date;

public class Evento implements Serialize {
    private int codEvento;
    private String nombre;
    private Date fecha;
    private String lugar;
    private int aforo;

    /**
     * Constructor con todos los parametros
     *
     * @param codEvento : entero
     * @param nombre    : String
     * @param fecha     : Date
     * @param lugar     : String
     * @param aforo     : entero
     */
    public Evento(int codEvento, String nombre, Date fecha, String lugar, int aforo) {
        this.codEvento = codEvento;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.aforo = aforo;
    }

    /**
     * Constructor vacio
     */
    public Evento() {

    }

    /**
     * Metodo get del arributo codEvento
     *
     * @return : entero
     */
    public int getCodEvento() {
        return codEvento;
    }

    /**
     * Metodo set del arributo codEvento
     *
     * @param codEvento : entero
     */
    public void setCodEvento(int codEvento) {
        this.codEvento = codEvento;
    }

    /**
     * Metodo get del arributo nombre
     *
     * @return : String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Metodo set del arributo nombre
     *
     * @param nombre : String
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo get del arributo fecha
     *
     * @return : String
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Metodo set del arributo nombre
     *
     * @param : String
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Metodo get del arributo lugar
     *
     * @return : String
     */
    public String getLugar() {
        return lugar;
    }

    /**
     * Metodo set del arributo lugar
     *
     * @param : String
     */
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    /**
     * Metodo get del arributo aforo
     *
     * @return : entero
     */
    public int getAforo() {
        return aforo;
    }

    /**
     * Metodo set del arributo aforo
     *
     * @param aforo : entero
     */
    public void setAforo(int aforo) {
        this.aforo = aforo;
    }

    @Override
    public String toString() {
        return "Evento{" + "codEvento=" + codEvento + ", nombre=" + nombre + ", fecha=" + fecha + ", lugar=" + lugar + ", aforo=" + aforo + '}';
    }


    @Override
    public String serialize(Object object) {
        Evento evento = (Evento) object;
        String linea = evento.getCodEvento() + "," + evento.getNombre() + "," + evento.getFecha() + "," +
                evento.getLugar() + "," + evento.getAforo();
        return linea;
    }

    @Override
    public Object serialize(String linea) throws PersistenciaException {
        String[] trozos = linea.split(",");
        try {
            int codigo = Integer.parseInt(trozos[0]);
            String nombre = trozos[1];
            Date fecha = Date.valueOf(trozos[2]);
            String lugar = trozos[3];
            int aforo = Integer.parseInt(trozos[4]);
            return new Evento(codigo, nombre, fecha, lugar, aforo);
        } catch (Exception e) {
            throw new PersistenciaException("ERROR formato para serializar el objeto Evento");
        }

    }
}
