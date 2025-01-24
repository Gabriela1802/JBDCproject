package Dao;

import Excepciones.LogicaException;
import Excepciones.PersistenciaException;

import java.io.IOException;

public interface IInvitadoDao {
    public void addInvitado(String infoInvitado) throws PersistenciaException, IOException;
    public void borrarInvitado(String dni) throws PersistenciaException, LogicaException;
    public void modificarInvitado(String dni, String nombre, String apellidos, String email, String telefono) throws PersistenciaException, LogicaException;
    public String listarInvitados() throws PersistenciaException;
    public String buscarInvitado(String dni) throws Exception;
}
