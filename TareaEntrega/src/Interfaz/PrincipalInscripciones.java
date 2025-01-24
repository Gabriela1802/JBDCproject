package Interfaz;

import Excepciones.Loggerfichero;
import Excepciones.LogicaException;
import Excepciones.PersistenciaException;
import Modelo.Evento;
import Modelo.Inscripcion;
import Modelo.Invitado;
import Persistencia.GestorJDBC;

import java.io.IOException;
import java.sql.Date;
import java.util.Scanner;

public class PrincipalInscripciones {
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private GestorJDBC gestor;
    private Scanner sc;

    public PrincipalInscripciones(GestorJDBC gestor, Scanner sc) {
        this.gestor = gestor;
        this.sc = sc;
    }

    public void agregarInscripcion() throws PersistenciaException, IOException {
        while (true) {
            try {
                System.out.println(ANSI_PURPLE + "-------- AGREGAR INSCRIPCION --------" + ANSI_RESET);
                System.out.print("Codigo Inscripcion: ");
                int codigo = Integer.parseInt(sc.nextLine());
                String ins = gestor.buscarInscripcion(codigo);
                System.out.print("Codigo Evento: ");
                int codigoEvento = Integer.parseInt(sc.nextLine());
                String e = gestor.buscarEvento(codigoEvento);
                System.out.print("DNI Invitado: ");
                String dni = sc.nextLine();
                String inv = gestor.buscarInvitado(dni);
                System.out.print("Fecha: ");
                String fecha = sc.nextLine();

                Inscripcion newInscripcion = new Inscripcion(codigo, codigoEvento, dni, Date.valueOf(fecha));
                gestor.addInscripcion(newInscripcion);
                System.out.println(ANSI_GREEN + "INSCRIPCION AÑADIDA CORRECTAMENTE" + ANSI_RESET);
                break;

            } catch (NumberFormatException e) {
                Loggerfichero.getInstance().writeSmg
                        ("ERROR formato. Por favor, introduce los datos correctamente.");
            } catch (Exception e) {
                Loggerfichero.getInstance().writeSmg(e.getMessage());
            }
        }

    }

    public void modificarInscripcion() throws LogicaException, IOException, PersistenciaException {
        System.out.println(ANSI_PURPLE + "-------- MODIFICAR INSCRIPCION --------" + ANSI_RESET);
        try {
            System.out.print("Codigo Inscripcion: ");
            int codigo = Integer.parseInt(sc.nextLine());
            System.out.print("Codigo Evento anterior: ");
            int codigoEvento = Integer.parseInt(sc.nextLine());
            System.out.print("Codigo Evento nuevo: ");
            int nuevoCodigoEvento = Integer.parseInt(sc.nextLine());
            gestor.modificarInscripcion(codigo, codigoEvento, nuevoCodigoEvento);
            System.out.println(ANSI_GREEN + "EVENTO MODIFICADO CORRECTAMENTE" + ANSI_RESET);

        } catch (LogicaException e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        } catch (NumberFormatException e) {
            Loggerfichero.getInstance().writeSmg
                    ("ERROR formato. Por favor, introduce los datos correctamente.");
        }

    }

    public void listarInscripciones() throws PersistenciaException {
        System.out.println(ANSI_PURPLE + "-------- LISTAR INSCRIPCIONES --------" + ANSI_RESET);
        System.out.println(gestor.listarInscripciones());
    }

    public void borrarInscripcion() throws PersistenciaException, IOException {
        System.out.println(ANSI_PURPLE + "-------- ELIMINAR INSCRIPCION --------" + ANSI_RESET);
        try {
            System.out.print("Codigo: ");
            int codigo = Integer.parseInt(sc.nextLine());
            gestor.borrarInscripcion(codigo);
            System.out.println(ANSI_GREEN + "INSCRIPCION ELIMINADO CORRECTAMENTE" + ANSI_RESET);
        } catch (NumberFormatException e) {
            Loggerfichero.getInstance().writeSmg("ERROR formato. Por favor, introduce los datos correctamente.");
        } catch (LogicaException e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        }
    }

    public void buscarInscripcion() throws PersistenciaException, LogicaException, IOException {
        System.out.println(ANSI_PURPLE + "-------- BUSCAR INSCRIPCION --------" + ANSI_RESET);
        try {
            System.out.print("Codigo: ");
            int codigo = Integer.parseInt(sc.nextLine());
            System.out.println(gestor.buscarInscripcion(codigo));
        } catch (NumberFormatException e) {
            Loggerfichero.getInstance().writeSmg
                    ("ERROR formato. Por favor, introduce los datos correctamente.");
        }
    }
}
