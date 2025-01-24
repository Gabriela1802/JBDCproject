package Dao;

import Excepciones.Loggerfichero;
import Excepciones.LogicaException;
import Excepciones.PersistenciaException;
import Persistencia.StatementSingleton;

import java.io.IOException;
import java.sql.*;

public class DaoEventoImpl implements IEventoDao {
    private final StatementSingleton statementSingleton;

    public DaoEventoImpl() throws PersistenciaException {
        this.statementSingleton = StatementSingleton.getInstance();
    }

    @Override
    public void addEvento(String infoEvento) throws PersistenciaException, IOException {
        String[] trozos = infoEvento.split(",");

        String sql = "INSERT INTO evento (id, nombre, fecha, lugar, aforo) VALUES (? ,? ,? ,? ,?)";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            try {
                int codigo = Integer.parseInt(trozos[0]);
                String nombre = trozos[1];
                Date fecha = Date.valueOf(trozos[2]);
                String lugar = trozos[3];
                int aforo = Integer.parseInt(trozos[4]);

                ps.setInt(1, codigo);
                ps.setString(2, nombre);
                ps.setDate(3, fecha);
                ps.setString(4, lugar);
                ps.setInt(5, aforo);
                ps.executeUpdate();
            } catch (NumberFormatException e) {
                Loggerfichero.getInstance().writeSmg("ERROR formato de Evento al subir en BBDD: " + infoEvento);
            }
        } catch (SQLException | PersistenciaException e) {
            throw new PersistenciaException("ERROR al insertar la lista de eventos en BBDD: " + e.getMessage());
        }
    }

    @Override
    public void borrarEvento(int codEvento) throws LogicaException, PersistenciaException {
        if (buscarEvento(codEvento) == null) {
            throw new LogicaException("El evento con id " + codEvento + " no existe en BBDD");
        }

        String sql = "DELETE FROM evento WHERE id = ?";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            ps.setInt(1, codEvento);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al borrar evento en BBDD: " + e.getMessage());
        }
    }

    @Override
    public void modificarEvento(int codEvento, Date fecha, String lugar) throws PersistenciaException, LogicaException {
        // comprobar que exista el evtno
        if (buscarEvento(codEvento) == null) {
            throw new LogicaException("El evento con id " + codEvento + " no existe en BBDD");
        }

        String sql = "UPDATE evento SET fecha = ?, lugar = ? WHERE id = ?";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            ps.setDate(1, fecha);
            ps.setString(2, lugar);
            ps.setInt(3, codEvento);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al modificar evento en BBDD: " + e.getMessage());
        }
    }


    @Override
    public String listarEventos() throws PersistenciaException {
        String resul = "";
        String sql = "SELECT * FROM evento";
        try (Statement st = statementSingleton.createStatement()) {
            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String nombre = rs.getString(2);
                    Date fecha = rs.getDate(3);
                    String lugar = rs.getString(4);
                    int aforo = rs.getInt(5);
                    resul += id + "," + nombre + "," + fecha + "," + lugar + "," + aforo + "\n";
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("ERROR al listar eventos de BBDD: " + e.getMessage());
        } catch (PersistenciaException e) {
            throw new PersistenciaException(e.getMessage());
        }
        return resul;
    }

    @Override
    public String buscarEvento(int codEvento) throws LogicaException, PersistenciaException {
        String sql = "SELECT * FROM evento WHERE id = ?";
        try (PreparedStatement ps = statementSingleton.prepareStatement(sql)) {
            ps.setInt(1, codEvento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    String nombre = rs.getString(2);
                    Date fecha = rs.getDate(3);
                    String lugar = rs.getString(4);
                    int aforo = rs.getInt(5);
                    String resul = id + "," + nombre + "," + fecha + "," + lugar + "," + aforo;
                    return resul;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new LogicaException("ERROR al encontrar evento en BBDD: " + e.getMessage());
        }
    }



}
