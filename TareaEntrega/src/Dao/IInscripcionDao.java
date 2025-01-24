package Dao;

import Excepciones.LogicaException;
import Excepciones.PersistenciaException;

import java.io.IOException;

public interface IInscripcionDao {
    public void addInscripcion(String infoInscripcion) throws PersistenciaException, IOException;

    public void borrarInscripcion(int codInscripcion) throws PersistenciaException, LogicaException;

    public void modificarInscripcion(int codInscripcion, int codEvento, int codNuevoEvento) throws PersistenciaException, LogicaException;

    public String listarInscripciones() throws PersistenciaException;

    public String buscarInscripcion(int codInscripcion) throws PersistenciaException, LogicaException;
}
