
package Interfaz;

import Excepciones.Loggerfichero;
import Excepciones.LogicaException;
import Excepciones.PersistenciaException;
import Modelo.Evento;
import Logica.Agenda;
import Persistencia.GestorJDBC;

import java.io.IOException;
import java.sql.Date;
import java.util.Scanner;

public class PrincipalEventos {
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private GestorJDBC gestor;
    private Scanner sc;

    public PrincipalEventos(GestorJDBC gestor, Scanner sc) {
        this.gestor = gestor;
        this.sc = sc;
    }

    public void agregarEvento() throws PersistenciaException, IOException {

        System.out.println(ANSI_BLUE + "-------- NUEVO EVENTO --------" + ANSI_RESET);
        try {
            System.out.print("Codigo: ");
            int codigo = Integer.parseInt(sc.nextLine());

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            System.out.print("Fecha (yyyy-MM-dd): ");
            String fecha = sc.nextLine();

            System.out.print("Lugar: ");
            String lugar = sc.nextLine();

            System.out.print("Aforo: ");
            int aforo = Integer.parseInt(sc.nextLine());

            Evento newEvento = new Evento(codigo, nombre, Date.valueOf(fecha), lugar, aforo);
            gestor.addEvento(newEvento);
            System.out.println(ANSI_GREEN + "EVENTO AGREGADO CORRECTAMENTE"+ ANSI_RESET);

        } catch (NumberFormatException e) {
            Loggerfichero.getInstance().writeSmg
                    ("ERROR formato. Por favor, introduce los datos correctamente.");
        } catch (Exception e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        }
    }

    public void modificarEvento() throws Exception {
        System.out.println(ANSI_BLUE + "-------- MODIFICAR EVENTO --------"+ ANSI_RESET);
        try {
            System.out.print("Codigo: ");
            int codigo = Integer.parseInt(sc.nextLine());
            System.out.print("Introduce nueva fecha: ");
            String fecha = sc.nextLine();
            System.out.print("Introduce nuevo lugar: ");
            String lugar = sc.nextLine();
            gestor.modificarEvento(codigo, Date.valueOf(fecha), lugar);
            System.out.println(ANSI_GREEN + "EVENTO MODIFICADO CORRECTAMENTE"+ ANSI_RESET);
        } catch (NumberFormatException e) {
            Loggerfichero.getInstance().writeSmg
                    ("ERROR formato. Por favor, introduce los datos correctamente.");
        } catch (LogicaException e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        }

    }

    public void listarEventos() throws PersistenciaException {
        System.out.println(ANSI_BLUE + "-------- LISTAR EVENTOS --------"+ ANSI_RESET);
        System.out.println(gestor.listarEventos());
    }

    public void borrarEvento() throws PersistenciaException, IOException {
        System.out.println(ANSI_BLUE + "-------- ELIMINAR EVENTO --------"+ ANSI_RESET);
        try {
            System.out.print("Codigo: ");
            int codigo = Integer.parseInt(sc.nextLine());
            gestor.borrarEvento(codigo);
            System.out.println(ANSI_GREEN + "EVENTO ELIMINADO CORRECTAMENTE"+ ANSI_RESET);
        } catch (NumberFormatException e) {
            Loggerfichero.getInstance().writeSmg
                    ("ERROR formato. Por favor, introduce los datos correctamente.");
        } catch (LogicaException e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        }
    }

    public void buscarEvento() throws IOException, PersistenciaException, LogicaException {
        System.out.println(ANSI_BLUE + "-------- BUSCAR EVENTO --------"+ ANSI_RESET);
        try {
            System.out.print("Codigo: ");
            int codigo = Integer.parseInt(sc.nextLine());
            System.out.println(gestor.buscarEvento(codigo));
        } catch (NumberFormatException e) {
            Loggerfichero.getInstance().writeSmg
                    ("ERROR formato. Por favor, introduce los datos correctamente.");
        }
    }
}
