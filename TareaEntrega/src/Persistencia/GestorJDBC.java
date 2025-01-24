package Persistencia;

import Dao.*;
import Excepciones.Loggerfichero;
import Excepciones.LogicaException;
import Excepciones.PersistenciaException;
import Logica.Agenda;
import Modelo.Evento;
import Modelo.Inscripcion;
import Modelo.Invitado;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorJDBC {
    private final StatementSingleton statementSingleton;
    private Agenda agenda;
    private IEventoDao eventoDao;
    private IInvitadoDao invitadoDao;
    private IInscripcionDao inscripcionDao;


    public GestorJDBC(Agenda agenda) throws PersistenciaException {
        // crear la instancia del Singleton una vez y no hacerlo en cada metodo
        this.statementSingleton = StatementSingleton.getInstance();
        // pasar la misma agenda que el gestorCSV para que sea coherente, instanciar una vez, en este caso en el main
        this.agenda = agenda;
        this.eventoDao = new DaoEventoImpl();
        this.invitadoDao = new DaoInvitadoImpl();
        this.inscripcionDao = new DaoInscripcionImpl();
    }


    public void crearTablas() throws PersistenciaException {
        String sql1 = "CREATE TABLE IF NOT EXISTS evento (" +
                "id INT PRIMARY KEY," +
                "nombre VARCHAR(100) NOT NULL," +
                "fecha DATE NOT NULL," +
                "lugar VARCHAR(100) NOT NULL," +
                "aforo INT NOT NULL );";
        // dentro del try se cierra automaticamente
        try (Statement st = statementSingleton.createStatement()) {
            // executeUpdate para sentencias que modifiquen la base de datos (DDL)
            st.executeUpdate(sql1);
        } catch (PersistenciaException | SQLException e) {
            throw new PersistenciaException("ERROR al crear tabla de eventos: " + e.getMessage());
        }

        String sql2 = "CREATE TABLE IF NOT EXISTS invitado(" +
                "dni VARCHAR(15) PRIMARY KEY," +
                "nombre VARCHAR(100) NOT NULL," +
                "apellidos VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                "telefono VARCHAR(20) NOT NULL);";

        try (Statement st = statementSingleton.createStatement()) {
            st.executeUpdate(sql2);
        } catch (PersistenciaException | SQLException e) {
            throw new PersistenciaException("ERROR al crear tabla de invitados: " + e.getMessage());
        }

        String sql3 = "CREATE TABLE IF NOT EXISTS inscripcion (" +
                "id INT PRIMARY KEY," +
                "codEvento INT NOT NULL," +
                "dni VARCHAR(15) NOT NULL," +
                "fecha DATE NOT NULL," +
                "FOREIGN KEY (codEvento) REFERENCES evento(id) ON DELETE CASCADE," +
                "FOREIGN KEY (dni) REFERENCES invitado(dni) ON DELETE CASCADE" +
                ");";

        try (Statement st = statementSingleton.createStatement()) {
            st.executeUpdate(sql3);
        } catch (PersistenciaException | SQLException e) {
            throw new PersistenciaException("ERROR al crear tabla de inscripciones: " + e.getMessage());
        }
    }

    public String buscarEvento(int codEvento) throws PersistenciaException, LogicaException {
        return this.eventoDao.buscarEvento(codEvento);
    }

    public String buscarInvitado(String dni) throws Exception {
        return this.invitadoDao.buscarInvitado(dni);
    }

    public String buscarInscripcion(int codInscripcion) throws PersistenciaException, LogicaException {
        return this.inscripcionDao.buscarInscripcion(codInscripcion);
    }

    public void modificarEvento(int codEvento, Date fecha, String lugar) throws PersistenciaException, LogicaException {
        this.eventoDao.modificarEvento(codEvento, fecha, lugar);
        this.agenda.modificarEvento(codEvento, fecha, lugar);
    }


    public void addEvento(Evento e) throws PersistenciaException, IOException {
        String eventoLinea = e.serialize(e);
        this.eventoDao.addEvento(eventoLinea);
        this.agenda.addEvento(e);
    }

    public void addInvitado(Invitado inv) throws PersistenciaException, IOException {
        String invLinea = inv.serialize(inv);
        this.invitadoDao.addInvitado(invLinea);
        this.agenda.addInvitado(inv);

    }

    public void addInscripcion(Inscripcion ins) throws PersistenciaException, IOException {
        String insLinea = ins.serialize(ins);
        this.inscripcionDao.addInscripcion(insLinea);
        this.agenda.addInscripcion(ins);
    }

    public void modificarInvitado(String dni, String nombre, String apellidos, String email, String telefono)
            throws PersistenciaException, LogicaException {
        this.invitadoDao.modificarInvitado(dni, nombre, apellidos, email, telefono);
        this.agenda.modificarInvitado(dni, nombre, apellidos, email, telefono);

    }

    public void modificarInscripcion(int codInscripcion, int codEvento, int codNuevoEvento)
            throws PersistenciaException, LogicaException {
        this.inscripcionDao.modificarInscripcion(codInscripcion, codEvento, codNuevoEvento);
        this.agenda.modificarInscripcion(codInscripcion, codEvento, codNuevoEvento);
    }


    public void borrarEvento(int codEvento) throws PersistenciaException, LogicaException {
        List<Inscripcion> inscripcionesAEliminar = new ArrayList<Inscripcion>();
        for (Inscripcion ins : this.agenda.getInscripciones()) {
            if (ins.getCodEvento() == codEvento) {
                inscripcionesAEliminar.add(ins);
            }
        }

        this.eventoDao.borrarEvento(codEvento);
        for (Inscripcion ins : inscripcionesAEliminar) {
            this.inscripcionDao.borrarInscripcion(ins.getCodInscripcion());
            this.agenda.eliminarInscripcion(ins.getCodInscripcion());
        }
        this.agenda.eliminarEvento(codEvento);
    }

    public void borrarInvitado(String dni) throws LogicaException, PersistenciaException {
        List<Inscripcion> inscripcionesAEliminar = new ArrayList<Inscripcion>();
        for (Inscripcion ins : this.agenda.getInscripciones()) {
            if (ins.getDni().compareTo(dni) == 0) {
                inscripcionesAEliminar.add(ins);
            }
        }
        this.invitadoDao.borrarInvitado(dni);
        for (Inscripcion ins : inscripcionesAEliminar) {
            this.inscripcionDao.borrarInscripcion(ins.getCodInscripcion());
            this.agenda.eliminarInscripcion(ins.getCodInscripcion());
        }
        this.agenda.eliminarInvitado(dni);
    }


    public void borrarInscripcion(int codInscripcion) throws PersistenciaException, LogicaException {
        this.inscripcionDao.borrarInscripcion(codInscripcion);
        this.agenda.eliminarInscripcion(codInscripcion);
    }

    public String listarEventos() throws PersistenciaException {
        return this.eventoDao.listarEventos();
    }

    public String listarInvitados() throws PersistenciaException {
        return this.invitadoDao.listarInvitados();
    }

    public String listarInscripciones() throws PersistenciaException {
        return this.inscripcionDao.listarInscripciones();
    }

    public void datosBBDDEventos() throws PersistenciaException, IOException {
        String lineas = this.eventoDao.listarEventos();
        String[] eventos = lineas.split("\n");
        for (String linea : eventos) {
            Evento e = (Evento) new Evento().serialize(linea);
            if (agenda.buscarEvento(e.getCodEvento()) != null) {
                Loggerfichero.getInstance().writeSmg("ERROR evento ya existe en la lista al bajar de BBDD");
            } else {
                agenda.addEvento(e);
            }
        }
    }

    public void datosBBDDInvitados() throws PersistenciaException, IOException {
        String lineas = this.invitadoDao.listarInvitados();
        String[] invitados = lineas.split("\n");
        for (String linea : invitados) {
            Invitado inv = (Invitado) new Invitado().serialize(linea);
            if (agenda.buscarInvitado(inv.getDni()) != null) {
                Loggerfichero.getInstance().writeSmg("ERROR invitado ya existe en la lista al bajar de BBDD");
            } else {
                agenda.addInvitado(inv);
            }
        }
    }

    public void datosBBDDInscripciones() throws PersistenciaException, IOException {
        String lineas = this.inscripcionDao.listarInscripciones();
        String[] inscripciones = lineas.split("\n");
        for (String linea : inscripciones) {
            Inscripcion ins = (Inscripcion) new Inscripcion().serialize(linea);
            if (agenda.buscarInscripcion(ins.getCodInscripcion()) != null) {
                Loggerfichero.getInstance().writeSmg("ERROR inscripcion ya existe en la lista al bajar de BBDD");
            } else {
                agenda.addInscripcion(ins);
            }
        }
    }

    public String mostrarBBDD() throws SQLException, PersistenciaException {
        String resul = "";
        DatabaseMetaData dbMet = statementSingleton.getConnection().getMetaData();
        String[] types = {"TABLE"};
        try (ResultSet rs = dbMet.getTables
                (statementSingleton.getConnection().getCatalog(), null, "%", types)) {
            while (rs.next()) {
                String tabla = rs.getString("TABLE_NAME");
                resul += "TABLA : " + tabla + "\n";
                resul += infoTabla(tabla);
            }
        }
        return resul;
    }

    private String infoTabla(String tabla) throws SQLException, PersistenciaException {
        String resul = "";
        try (Statement st = statementSingleton.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + tabla)) {
            ResultSetMetaData rsmeta = rs.getMetaData();
            int col = rsmeta.getColumnCount();
            for (int i = 1; i <= col; i++) {
                String nombreCampo_i = rsmeta.getColumnLabel(i);
                String tipoCampo_i = rsmeta.getColumnTypeName(i);
                resul += "\tCampo " + nombreCampo_i + " Tipo " + tipoCampo_i + "\n";
            }
        }
        return resul;
    }


}
