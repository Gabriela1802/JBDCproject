package Dao;

import Excepciones.Loggerfichero;
import Excepciones.LogicaException;
import Excepciones.PersistenciaException;
import Persistencia.StatementSingleton;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DaoInvitadoImpl implements IInvitadoDao {
    private final StatementSingleton statementSingleton;

    public DaoInvitadoImpl() throws PersistenciaException {
        this.statementSingleton = StatementSingleton.getInstance();
    }


    @Override
    public void addInvitado(String infoInvitado) throws PersistenciaException, IOException {
        String[] trozos = infoInvitado.split(",");

        String dni = trozos[0];
        String nombre = trozos[1];
        String apellidos = trozos[2];
        String email = trozos[3];
        String telefono = trozos[4];

        String sql = "INSERT INTO invitado (dni, nombre, apellidos, email, telefono) VALUES" +
                "(? ,? ,? ,? ,? );";

        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.setString(2, nombre);
            ps.setString(3, apellidos);
            ps.setString(4, email);
            ps.setString(5, telefono);
            ps.executeUpdate();
        } catch (PersistenciaException e) {
            Loggerfichero.getInstance().writeSmg("ERROR formato de Invitado al subir en BBDD: " + infoInvitado);
        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al insertar la lista de invitados en BBDD: " + e.getMessage());
        }


    }

    @Override
    public void borrarInvitado(String dni) throws PersistenciaException, LogicaException {
        if (buscarInvitado(dni) == null) {
            throw new LogicaException("ERROR invitado con '" + dni + "' no existe en la base de datos");
        }
        String sql = "DELETE FROM invitado WHERE dni = ?";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al borrar invitado en BBDD: " + e.getMessage());
        }
    }

    @Override
    public void modificarInvitado(String dni, String nombre, String apellidos, String email, String telefono)
            throws PersistenciaException, LogicaException {
        if (buscarInvitado(dni) == null) {
            throw new LogicaException("ERROR invitado con dni '" + dni + "' no existe en BBDD");
        }

        String sql = "UPDATE invitado SET nombre = ?, apellidos = ?, email = ?, telefono = ? WHERE dni = ?";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, email);
            ps.setString(4, telefono);
            ps.setString(5, dni);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al modificar invitado en BBDD: " + e.getMessage());
        }
    }


    @Override
    public String listarInvitados() throws PersistenciaException {
        String resul = "";
        String sql = "SELECT * FROM invitado";
        try (Statement st = statementSingleton.createStatement()) {
            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    String dni = rs.getString(1);
                    String nombre = rs.getString(2);
                    String apellidos = rs.getString(3);
                    String email = rs.getString(4);
                    String telefono = rs.getString(5);
                    resul += dni + "," + nombre + "," + apellidos + "," + email + "," + telefono + "\n";
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al listar invitados de BBDD: " + e.getMessage());
        } catch (PersistenciaException e) {
            throw new PersistenciaException(e.getMessage());
        }
        return resul;
    }

    @Override
    public String buscarInvitado(String dni) throws PersistenciaException, LogicaException {
        String sql = "SELECT * FROM invitado WHERE dni = ?";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String campoDni = rs.getString(1);
                    String nombre = rs.getString(2);
                    String apellidos = rs.getString(3);
                    String telefono = rs.getString(4);
                    String resul = campoDni + "," + nombre + "," + apellidos + "," + telefono;
                    return resul;
                } else {
                    return null;
                }
            }
        } catch (PersistenciaException e) {
            throw new PersistenciaException("ERROR sentencia al buscar invitado en BBDD: " + e.getMessage());
        } catch (SQLException e) {
            throw new LogicaException("ERROR al encontrar invitado en BBDD: " + e.getMessage());
        }
    }
}
