package Dao;

import Excepciones.LogicaException;
import Excepciones.PersistenciaException;

import java.io.IOException;
import java.sql.Date;


public interface IEventoDao {
    public void addEvento(String infoEvento) throws PersistenciaException, IOException;
    public void borrarEvento(int codEvento) throws LogicaException, PersistenciaException;
    public void modificarEvento(int codEvento, Date fecha, String lugar) throws PersistenciaException, LogicaException;
    public String listarEventos() throws PersistenciaException;
    public String buscarEvento(int codEvento) throws LogicaException, PersistenciaException;


}
