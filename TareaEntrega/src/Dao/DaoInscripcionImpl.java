package Dao;

import Excepciones.Loggerfichero;
import Excepciones.LogicaException;
import Excepciones.PersistenciaException;
import Modelo.Inscripcion;
import Persistencia.StatementSingleton;

import java.io.IOException;
import java.sql.*;

public class DaoInscripcionImpl implements IInscripcionDao {
    private final StatementSingleton statementSingleton;

    public DaoInscripcionImpl() throws PersistenciaException {
        this.statementSingleton = StatementSingleton.getInstance();
    }

    @Override
    public void addInscripcion(String infoInscripcion) throws PersistenciaException, IOException {
        String[] trozos = infoInscripcion.split(",");
        String sql = "INSERT INTO inscripcion (id, codEvento, dni, fecha) VALUES (? ,? ,? ,?);";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            try {
                int codInscripcion = Integer.parseInt(trozos[0]);
                int codEvento = Integer.parseInt(trozos[1]);
                String dni = trozos[2];
                Date fecha = Date.valueOf(trozos[3]);

                ps.setInt(1, codInscripcion);
                ps.setInt(2, codEvento);
                ps.setString(3, dni);
                ps.setDate(4, fecha);
                ps.executeUpdate();
            } catch (Exception e) {
                Loggerfichero.getInstance().writeSmg
                        ("ERROR formato de Inscripcion al subir en BBDD: " + infoInscripcion);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al insertar la lista de inscripciones en BBDD: " + e.getMessage());
        }
    }

    @Override
    public void borrarInscripcion(int codInscripcion) throws PersistenciaException, LogicaException {
        if (buscarInscripcion(codInscripcion) == null) {
            throw new LogicaException("ERROR inscripcion con codigo '" + codInscripcion + "' no existe en BBDD");
        }
        String sql = "DELETE FROM inscripcion WHERE id = ?";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            ps.setInt(1, codInscripcion);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al borrar inscripcion en BBDD: " + e.getMessage());
        }
    }

    @Override
    public void modificarInscripcion(int codInscripcion, int codEvento, int codNuevoEvento)
            throws PersistenciaException, LogicaException {
        if (buscarInscripcion(codInscripcion) == null) {
            throw new LogicaException("ERROR inscripcion con id '" + codInscripcion + "' no existe en BBDD");
        }

        String sql = "UPDATE inscripcion SET codEvento = ? WHERE id = ?";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            ps.setInt(1, codNuevoEvento);
            ps.setInt(2, codInscripcion);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al modificar inscripcion en BBDD: " + e.getMessage());
        }
    }


    @Override
    public String listarInscripciones() throws PersistenciaException {
        String resul = "";
        String sql = "SELECT * FROM inscripcion";
        try (Statement st = statementSingleton.createStatement()) {
            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    int codEvento = rs.getInt(2);
                    String dni = rs.getString(3);
                    Date fecha = rs.getDate(4);
                    resul += id + "," + codEvento + "," + dni + "," + fecha + "\n";
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al listar inscripciones de BBDD: " + e.getMessage());
        } catch (PersistenciaException e) {
            throw new PersistenciaException(e.getMessage());
        }
        return resul;
    }

    @Override
    public String buscarInscripcion(int codInscripcion) throws PersistenciaException, LogicaException {
        String sql = "SELECT * FROM inscripcion WHERE id = ?";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            ps.setInt(1, codInscripcion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    int codEvento = rs.getInt(2);
                    String dni = rs.getString(3);
                    Date fecha = rs.getDate(4);
                    String resul = id + "," + codEvento + "," + dni + "," + fecha;
                    return resul;
                } else {
                    return null;
                }
            }
        } catch (PersistenciaException e) {
            throw new PersistenciaException("ERROR sentencia al buscar inscripcion en BBDD: " + e.getMessage());
        } catch (SQLException e) {
            throw new LogicaException("ERROR al encontrar inscripcion en BBDD: " + e.getMessage());
        }
    }
}
