package Persistencia;

import Dao.*;
import Excepciones.Loggerfichero;
import Excepciones.LogicaException;
import Excepciones.PersistenciaException;
import Logica.Agenda;
import Modelo.Evento;
import Modelo.Inscripcion;
import Modelo.Invitado;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GestorCSV {
    private Agenda agenda;
    private IEventoDao eventoDao;
    private IInvitadoDao invitadoDao;
    private IInscripcionDao inscripcionDao;

    /**
     * Constructor con todos los parametros
     *
     * @param agenda : Agenda
     */
    public GestorCSV(Agenda agenda) throws PersistenciaException {
        this.agenda = agenda;
        // inicializar para acceder a los metodos
        StatementSingleton.getInstance();
        this.eventoDao = new DaoEventoImpl();
        this.invitadoDao = new DaoInvitadoImpl();
        this.inscripcionDao = new DaoInscripcionImpl();
    }

    /**
     * Recibe el nombre de un fichero de Eventos y lo procesa linea por linea, notifica si existe un error
     * alguna linea
     *
     * @param fichero : String
     * @throws PersistenciaException : si el fichero no existe o si existe un error al cerrar el fichero
     */
    public void leerEventos(String fichero) throws PersistenciaException, IOException {
        File file = new File(fichero);
        if (!file.exists()) {
            throw new PersistenciaException("ERROR fichero de eventos no encontrado");
        }

        BufferedReader buffer = null;
        int contLineas = 1;
        try {
            buffer = new BufferedReader(new FileReader(file));
            buffer.readLine();
            while (buffer.ready()) {
                String linea = buffer.readLine();
                contLineas++;
                try {
                    tratarLineaEvento(linea);
                } catch (Exception e) {
                    Loggerfichero.getInstance().writeSmg(contLineas + " : " + e.getMessage() + "\n");
                }
            }

        } catch (IOException e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        } finally {
            try {
                buffer.close();
            } catch (IOException e) {
                throw new PersistenciaException("ERROR al cerrar fichero");
            }
        }
    }

    /**
     * Verifica que la linea del evento de ficheros tenga 5 campos y que el codigo sea unico
     *
     * @param linea : String
     * @throws PersistenciaException : si no estan todos los campos
     * @throws LogicaException       : el codigo esta duplicado
     */
    private void tratarLineaEvento(String linea) throws PersistenciaException, LogicaException {
        String[] trozos = linea.split(",");
        if (trozos.length != 5) {
            throw new PersistenciaException("ERROR registro de evento incorrecto");
        }
        Evento e = new Evento();
        e = (Evento) e.serialize(linea);
        if (agenda.buscarEvento(e.getCodEvento()) != null) {
            throw new LogicaException("ERROR codigo evento duplicado");
        }
        agenda.addEvento(e);
    }

    /**
     * Recibe el nombre de un fichero de Invitados y lo procesa linea por linea, notifica si existe un error
     * alguna linea
     *
     * @param fichero : String
     * @throws PersistenciaException : si el fichero no existe o si existe un error al cerrar el fichero
     */
    public void leerInvitados(String fichero) throws PersistenciaException, IOException {
        File file = new File(fichero);
        if (!file.exists()) {
            throw new PersistenciaException("ERROR fichero de invitados no encontrado");
        }

        BufferedReader buffer = null;
        int contLineas = 1;
        try {
            buffer = new BufferedReader(new FileReader(file));
            buffer.readLine();
            while (buffer.ready()) {
                String linea = buffer.readLine();
                contLineas++;
                try {
                    tratarLineaInvitado(linea);
                } catch (Exception e) {
                    Loggerfichero.getInstance().writeSmg(contLineas + " : " + e.getMessage() + "\n");
                }
            }

        } catch (IOException e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        } finally {
            try {
                buffer.close();
            } catch (IOException e) {
                throw new PersistenciaException("ERROR al cerrar fichero");
            }
        }
    }

    /**
     * Verifica que la linea de Invitado tenga 5 campos y el invitado no este duplicado
     *
     * @param linea : String
     * @throws PersistenciaException : si no estan todos los campos
     * @throws LogicaException       : el dni esta de Invitado esta duplicado
     */
    private void tratarLineaInvitado(String linea) throws PersistenciaException, LogicaException {
        String[] trozos = linea.split(",");
        if (trozos.length != 5) {
            throw new PersistenciaException("ERROR registro de invitado incorrecto");
        }
        Invitado inv = new Invitado();
        inv = (Invitado) inv.serialize(linea);
        if (agenda.buscarInvitado(inv.getDni()) != null) {
            throw new LogicaException("ERROR dni invitado duplicado");
        }
        agenda.addInvitado(inv);
    }

    /**
     * Recibe el nombre de un fichero de Inscripciones y lo procesa linea por linea, notifica si existe un error
     * alguna linea
     *
     * @param fichero : String
     * @throws PersistenciaException : si el fichero no existe o si existe un error al cerrar el fichero
     */
    public void leerInscripciones(String fichero) throws PersistenciaException, IOException {
        File file = new File(fichero);
        if (!file.exists()) {
            throw new PersistenciaException("ERROR fichero de inscripciones no encontrado");
        }

        BufferedReader buffer = null;
        int contLineas = 1;
        try {
            buffer = new BufferedReader(new FileReader(file));
            buffer.readLine();
            while (buffer.ready()) {
                String linea = buffer.readLine();
                contLineas++;
                try {
                    tratarLineaInscripcion(linea);
                } catch (Exception e) {
                    Loggerfichero.getInstance().writeSmg(contLineas + " : " + e.getMessage() + "\n");
                }
            }
        } catch (IOException e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        } finally {
            try {
                buffer.close();
            } catch (IOException e) {
                throw new PersistenciaException("ERROR al cerrar fichero");
            }
        }
    }

    /**
     * Verfica que la linea de Inscripcion tenga 4 campos y el codigo no de inscripcion
     * no este repetido
     *
     * @param linea : String
     * @throws PersistenciaException : si no estan todos los campos, el evento no existe, el
     *                               invitado no existe, el codigo de Inscripcion esta duplicado o el registro esta mal formado
     */
    private void tratarLineaInscripcion(String linea) throws PersistenciaException {
        String[] trozos = linea.split(",");
        if (trozos.length != 4) {
            throw new PersistenciaException("ERROR registro de inscripcion incorrecto");
        }
        try {
            int codEvento = Integer.parseInt(trozos[1]);
            String dni = trozos[2];

            Evento e = agenda.buscarEvento(codEvento);
            if (e == null) {
                throw new PersistenciaException("ERROR no se ha encontrado el evento para hacer la inscripcion");
            }

            Invitado inv = agenda.buscarInvitado(dni);
            if (inv == null) {
                throw new PersistenciaException("ERROR no se ha encontrado el invitado para hacer la inscripcion");
            }

            Inscripcion ins = new Inscripcion();
            ins = (Inscripcion) ins.serialize(linea);
            if (agenda.buscarInscripcion(ins.getCodInscripcion()) != null) {
                throw new LogicaException("ERROR codigo Inscripcion duplicado");
            }
            agenda.addInscripcion(ins);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenciaException("ERROR registro de inscripcion mal formado: " + e.getMessage());
        }
    }

    public void importarDatosEventos() throws PersistenciaException, IOException {

        for (Evento evt : this.agenda.getEventos()) {
            // saca el objeto evento de la lista y lo vuelve String
            String linea = evt.serialize(evt);
            try {
                this.eventoDao.addEvento(linea);
            } catch (PersistenciaException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void importarDatosInvitados() throws PersistenciaException, IOException {
        for (Invitado inv : this.agenda.getInvitados()) {
            String infoInvitado = inv.serialize(inv);
            // llama al metodo add de un Invitado siguiendo el patron Dao
            try {
                this.invitadoDao.addInvitado(infoInvitado);
            } catch (PersistenciaException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void importarDatosInscripciones() throws PersistenciaException, IOException {
        for (Inscripcion ins : this.agenda.getInscripciones()) {
            String infoInscripcion = ins.serialize(ins);
            this.inscripcionDao.addInscripcion(infoInscripcion);
            try {
                this.inscripcionDao.addInscripcion(infoInscripcion);
            } catch (PersistenciaException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Recibe el nombre de un fichero donde se van a grabar los datos de la lista de Eventos,
     *
     * @param rutaFicheroEventos : String
     * @throws PersistenciaException : error de entrada salida al garabar los datos o si hay un
     *                               error al cerrar el fichero
     */
    public void exportarDatosEventos(String rutaFicheroEventos) throws PersistenciaException, IOException {
        File newFile = new File(rutaFicheroEventos);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(newFile, true));
            String cabecera = "                      codEvento,nombre,fecha,lugar,aforo\n";
            bufferedWriter.write(cabecera);
            for (Evento e : agenda.getEventos()) {
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String linea = fecha + "   " + e.serialize(e) + "\n";
                bufferedWriter.write(linea);
            }
        } catch (IOException e) {
            throw new PersistenciaException("ERROR I/O al grabar eventos");
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new PersistenciaException("ERROR al cerrar el fichero al grabar eventos");
            }
        }
    }

    public void exportarDatosInvitados(String rutaFicheroInvitados) throws PersistenciaException, IOException {
        File newFile = new File(rutaFicheroInvitados);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(newFile, true));
            String cabecera = "                      dni,nombre,apellido,email,telefono\n";
            bufferedWriter.write(cabecera);
            for (Invitado inv : agenda.getInvitados()) {
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String linea = fecha + "   " + inv.serialize(inv) + "\n";
                bufferedWriter.write(linea);
            }
        } catch (IOException e) {
            throw new PersistenciaException("ERROR I/O al grabar invitados");
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new PersistenciaException("ERROR al cerrar el fichero al grabar invitados");
            }
        }
    }

    public void exportarDatosInscripciones(String rutaFicheroInscripciones) throws PersistenciaException, IOException {
        File newFile = new File(rutaFicheroInscripciones);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(newFile, true));
            String cabecera = "                      codInscripcion,codEvento,dni,fecha\n";
            bufferedWriter.write(cabecera);
            for (Inscripcion ins : agenda.getInscripciones()) {
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String linea = fecha + "   " + ins.serialize(ins) + "\n";
                bufferedWriter.write(linea);
            }
        } catch (IOException e) {
            throw new PersistenciaException("ERROR I/O al grabar inscripciones");
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new PersistenciaException("ERROR al cerrar el fichero al grabar invitados");
            }
        }
    }




}
