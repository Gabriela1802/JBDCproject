package Modelo;

import Excepciones.PersistenciaException;

import java.sql.Date;

public class Inscripcion implements Serialize {
    private int codInscripcion;
    private int codEvento;
    private String dni;
    private Date fecha;

    /**
     * Constructor con todos los parametros
     * @param codInscripcion : entero
     * @param codEvento : entero
     * @param dni : String
     * @param fecha : Date
     */
    public Inscripcion(int codInscripcion, int codEvento, String  dni, Date fecha) {
        this.codInscripcion = codInscripcion;
        this.codEvento = codEvento;
        this.dni = dni;
        this.fecha = fecha;
    }

    /**
     * Constructor vacio
     */
    public Inscripcion() {

    }

    /**
     * Metodo get del arributo codInscripcion
     * @return : entero
     */
    public int getCodInscripcion() {
        return codInscripcion;
    }

    /**
     * Metodo set del arributo codInscripcion
     * @param codInscripcion : entero
     */
    public void setCodInscripcion(int codInscripcion) {
        this.codInscripcion = codInscripcion;
    }

    /**
     * Metodo get del arributo codEvento
     * @return : entero
     */
    public int getCodEvento() {
        return codEvento;
    }

    /**
     * Metodo set del arributo codEvento
     * @param codEvento : entero
     */
    public void setCodEvento(int codEvento) {
        this.codEvento = codEvento;
    }

    /**
     * Metodo get del arributo dni
     * @return : String
     */
    public String getDni() {
        return dni;
    }

    /**
     * Metodo set del arributo codInscripcion
     * @param dni : String
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Metodo get del arributo fecha
     * @return : String
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Metodo set del arributo fecha
     * @param fecha : String
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Inscripcion{" + "codInscripcion=" + codInscripcion + ", codEvento=" + codEvento + ", dni=" + dni + ", fecha=" + fecha + '}';
    }

    @Override
    public String serialize(Object object) {
        Inscripcion in = (Inscripcion) object;
        String linea = in.getCodInscripcion()+","+in.getCodEvento()+","+in.getDni()+","+
                        in.getFecha();
        return linea;
    }

    @Override
    public Object serialize(String linea) throws PersistenciaException {
        String []trozos = linea.split(",");
        try{
            int codInscripcion = Integer.parseInt(trozos[0]);
            int codEvento = Integer.parseInt(trozos[1]);
            String dni = trozos[2];
            Date fecha = Date.valueOf(trozos[3]);
            return new Inscripcion(codInscripcion, codEvento, dni, fecha);
        } catch (Exception e){
            throw new PersistenciaException("ERROR formato para serializar el objeto Inscripcion");
        }
    }
}
