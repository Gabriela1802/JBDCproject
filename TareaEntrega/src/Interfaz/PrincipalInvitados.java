package Interfaz;

import Excepciones.Loggerfichero;
import Excepciones.LogicaException;
import Excepciones.PersistenciaException;
import Modelo.Invitado;
import Persistencia.GestorJDBC;

import java.io.IOException;
import java.util.Scanner;

public class PrincipalInvitados {
    public static final String ANSI_DARK_ORANGE = "\u001B[38;5;208m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private GestorJDBC gestor;
    private Scanner sc;

    public PrincipalInvitados(GestorJDBC gestor, Scanner sc) {
        this.gestor = gestor;
        this.sc = sc;
    }

    public void agregarInvitado() throws PersistenciaException, IOException {

        System.out.println(ANSI_DARK_ORANGE+"-------- AGREGAR INVITADO --------"+ANSI_RESET);
        try {
            System.out.print("DNI: ");
            String dni = sc.nextLine();
            String inv = gestor.buscarInvitado(dni);
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Apellidos: ");
            String apellidos = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Telefono: ");
            String telefono = sc.nextLine();

            Invitado newInvitado = new Invitado(dni, nombre, apellidos, email, telefono);
            gestor.addInvitado(newInvitado);
            System.out.println(ANSI_GREEN+"INVITADO AÑADIDO CORRECTAMENTE"+ANSI_RESET);

        } catch (Exception e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        }
    }

    public void modificarInvitado() throws IOException, PersistenciaException {

        System.out.println(ANSI_DARK_ORANGE+"-------- MODIFICAR INVITADO --------"+ANSI_RESET);
        try {
            System.out.print("DNI: ");
            String dni = sc.nextLine();
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Apellidos: ");
            String apellidos = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Telefono: ");
            String telefono = sc.nextLine();
            gestor.modificarInvitado(dni, nombre, apellidos, email, telefono);
            System.out.println(ANSI_GREEN+"INVITADO MODIFICADO CORRECTAMENTE"+ANSI_RESET);
            ;
        } catch (LogicaException e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        }

    }

    public void listarInvitados() throws PersistenciaException {
        System.out.println(ANSI_DARK_ORANGE+"-------- LISTAR INVITADOS --------"+ANSI_RESET);
        System.out.println(gestor.listarInvitados());
    }

    public void borrarInvitado() throws PersistenciaException, IOException {
        System.out.println(ANSI_DARK_ORANGE+"-------- ELIMINAR INVITADO --------"+ANSI_RESET);
        try {
            System.out.print("DNI: ");
            String dni = sc.nextLine();
            gestor.borrarInvitado(dni);
            System.out.println(ANSI_GREEN+"INVITADO ELIMINADO CORRECTAMENTE"+ANSI_RESET);
        } catch (LogicaException e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        }
    }

    public void buscarInvitado() throws Exception {
        System.out.println(ANSI_DARK_ORANGE+"-------- BUSCAR INVITADO --------"+ANSI_RESET);
        try {
            System.out.print("DNI: ");
            String dni = sc.nextLine();
            System.out.println(gestor.buscarInvitado(dni));
        } catch (NumberFormatException e) {
            Loggerfichero.getInstance().writeSmg
                    ("ERROR formato. Por favor, introduce los datos correctamente.");
        }
    }
}
