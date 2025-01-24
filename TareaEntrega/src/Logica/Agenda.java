package Logica;

import Excepciones.LogicaException;
import Modelo.Evento;
import Modelo.Inscripcion;
import Modelo.Invitado;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private List<Evento> eventos;
    private List<Inscripcion> inscripciones;
    private List<Invitado> invitados;

    /**
     * Constructor sin parametros
     */
    public Agenda() {
        this.eventos = new ArrayList<>();
        this.inscripciones = new ArrayList<>();
        this.invitados = new ArrayList<>();
    }



    /**
     * Metodo que añade un objeto evento a una lista parametrizada
     * @param e : Evento
     */
    public void addEvento(Evento e) {
        eventos.add(e);
    }

    /**
     * Metodo que añade un objeto Inscripcion a una lista parametrizada
     * @param i : Inscripcion
     */
    public void addInscripcion(Inscripcion i) {
        inscripciones.add(i);
    }

    /**
     * Metodo que añade un objeto Invitado a una lista parametrizada
     * @param i : Invitado
     */
    public void addInvitado(Invitado i) {
        invitados.add(i);
    }

    /**
     * Metodo que busca un Evento a partir de su codigo
     * @param codEvento : entero
     * @return : objeto Evento o null si no lo encuentra
     */
    public Evento buscarEvento(int codEvento) {
        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            if (e.getCodEvento() == codEvento) {
                return e;
            }
        }
        return null;
    }

    /**
     * Metodo que busca una Inscripcion a partir de su codigo
     * @param codInscripcion : entero
     * @return : objeto Inscripcion o null si no lo encuentra
     */
    public Inscripcion buscarInscripcion(int codInscripcion) {
        for (int i = 0; i < inscripciones.size(); i++) {
            Inscripcion ins = inscripciones.get(i);
            if (ins.getCodInscripcion() == codInscripcion) {
                return ins;
            }
        }
        return null;
    }

    /**
     * Metodo que busca un Invitado a partir de su dni
     * @param codInvitado : String
     * @return : objeto Invitado o null si no lo encuentra
     */
    public Invitado buscarInvitado(String codInvitado) {
        for (int i = 0; i < invitados.size(); i++) {
            Invitado inv = invitados.get(i);
            if (inv.getDni().compareTo(codInvitado) == 0) {
                return inv;
            }
        }
        return null;
    }

    /**
     * Muestra todos los objetos en la lista de Eventos
     * @return : String
     */
    public String listarEventos() {
        String texto = "Eventos: " + eventos.size() + "\n";
        for (Evento e : eventos) {
            texto += "\t" + e.toString() + "\n";
        }
        return texto;
    }

    /**
     * Muestra todos los objetos en la lista de Invitados
     * @return : String
     */
    public String listarInvitados() {
        String texto = "Invitados: " + invitados.size() + "\n";
        for (Invitado i : invitados) {
            texto += "\t" + i.toString() + "\n";
        }
        return texto;
    }

    /**
     * Muestra todos los objetos en la lista de Inscripciones
     * @return : String
     */
    public String listarInscripciones() {
        String texto = "Inscripciones: " + inscripciones.size() + "\n";
        for (Inscripcion ins : inscripciones) {
            texto += "\t" + ins.toString() + "\n";
        }
        return texto;
    }

    /**
     * Elimina un Evento en la lista a partir de su codigo
     * @param codEvento : entero
     * @throws LogicaException : si no se encuentra el Evento
     */
    public void eliminarEvento(int codEvento) throws LogicaException {
        Evento currentE = buscarEvento(codEvento);
        if (currentE != null) {
            eventos.remove(currentE);
        } else {
            throw new LogicaException("ERROR evento no encontrado");
        }
    }

    /**
     * Elimina una Inscripcion en la lista a partir de su codigo
     * @param codInscripcion : entero
     * @throws LogicaException : si no se encuentra la Inscripcion
     */
    public void eliminarInscripcion(int codInscripcion) throws LogicaException {
        Inscripcion currentIns = buscarInscripcion(codInscripcion);
        if (currentIns != null) {
            inscripciones.remove(currentIns);
        } else {
            throw new LogicaException("ERROR inscripcion no encontrada");
        }
    }

    /**
     * Elimina un Invitado en la lista a partir de su dni
     * @param dni : String
     * @throws LogicaException : si no se encuentra el Invitado
     */
    public void eliminarInvitado(String dni) throws LogicaException {
        Invitado currentInv = buscarInvitado(dni);
        if (currentInv != null) {
            invitados.remove(currentInv);
        } else {
            throw new LogicaException("ERROR invitado no encontrado");
        }
    }

    /**
     * Modifica la fecha y el lugar de un Evento
     * @param codEvento : entero
     * @param fecha : String
     * @param lugar : String
     * @throws LogicaException : si no se encuentra el Evento
     */
    public void modificarEvento(int codEvento, Date fecha, String lugar) throws LogicaException {
        Evento evento = buscarEvento(codEvento);
        if (evento != null) {
            evento.setFecha(fecha);
            evento.setLugar(lugar);
        } else {
            throw new LogicaException("ERROR evento no encontrado en la lista");
        }
    }

    /**
     * Modifica la inscripcion a un Evento y la cambia a otro Evento
     * (solo se puede modificar la inscripcion si el nuevo evento al que se va a inscribir existe)
     * @param codInscripcion : entero
     * @param codEvento : entero
     * @param codNuevoEvento : entero
     * @throws LogicaException : no se encuentra la Inscripcion o no se encuentran los Eventos
     */
    public void modificarInscripcion(int codInscripcion, int codEvento, int codNuevoEvento)
            throws LogicaException {
        Inscripcion ins = buscarInscripcion(codInscripcion);
        if (ins == null) {
            throw new LogicaException("ERROR inscripcion no encontrada");
        }
        Evento e = buscarEvento(codEvento);
        if (e != null) {
            Evento nuevoEvento = buscarEvento(codNuevoEvento);
            if(nuevoEvento != null){
                ins.setCodEvento(codNuevoEvento);
            } else {
                throw new LogicaException("ERROR evento no existe");
            }
        } else {
            throw new LogicaException("ERROR evento nuevo no existe");
        }

    }

    /**
     * Modifica el nombre, apellidos, email y telefono de un Invitado
     * @param dni : String
     * @param nombre : String
     * @param apellidos : String
     * @param email : String
     * @param telefono : String
     * @throws LogicaException : si el Invitado no se encuentra el Invitado
     */
    public void modificarInvitado(String dni, String nombre, String apellidos, String email, String telefono)
            throws LogicaException {
        Invitado inv = buscarInvitado(dni);
        if (inv != null) {
            inv.setNombre(nombre);
            inv.setApellido(apellidos);
            inv.setTelefono(telefono);
            inv.setEmail(email);
        } else {
            throw new LogicaException("ERROR invitado no encontrado");
        }
    }

    /**
     * Metodo get de la lista de Eventos
     * @return : Lista parametrizada
     */
    public List<Evento> getEventos() {
        return eventos;
    }

    /**
     * Metodo get de la lista de Inscripciones
     * @return : Lista parametrizada
     */
    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    /**
     * Metodo get de la lista de Invitados
     * @return : Lista parametrizada
     */
    public List<Invitado> getInvitados() {
        return invitados;
    }
}
