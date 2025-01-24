package Interfaz;

import Excepciones.Loggerfichero;
import Excepciones.LogicaException;
import Excepciones.PersistenciaException;
import Logica.Agenda;
import Persistencia.GestorCSV;
import Persistencia.GestorJDBC;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Principal {
    public static final String ANSI_CUSTOM_BLUE = "\u001B[38;2;0;91;143m";
    public static final String ANSI_CUSTOM_PURPLE = "\u001B[38;2;163;73;164m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static Scanner sc = new Scanner(System.in);
    private static Agenda agenda;
    private static GestorCSV gestorCSV;
    private static GestorJDBC gestorJDBC;

    public static void main(String[] args) throws Exception {
        try {
            agenda = new Agenda();
            gestorCSV = new GestorCSV(agenda);
            gestorJDBC = new GestorJDBC(agenda);
            gestorJDBC.crearTablas();
        } catch (Exception e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        }
        try {
            int opcion;

            // nuevo
            do {
                opcion = mostrarMenuPrincipal();
                procesarOpcionPrincipal(opcion);
            } while (opcion != 0);
        } catch (Exception e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        }

    }

    private static int mostrarMenuPrincipal() throws IOException {
        int opcion;
        String[] opciones = {
                ANSI_CUSTOM_BLUE+"--------------- MENU PRINCIPAL ---------------"+ANSI_RESET,
                "1. Cargar datos (NOTA: solo es necesario hacerlo la primera vez que se ejecuta el programa)",
                "2. Añadir",
                "3. Modificar",
                "4. Listar",
                "5. Borrar",
                "6. Buscar",
                "7. Cargar datos desde BBDD a logica (si es necesario)",
                "8. Hacer copia seguridad",
                "9. Informacion de la base de datos",
                "0. Salir"
        };
        while (true) {
            for (int i = 0; i < opciones.length; i++) {
                System.out.println(opciones[i]);
            }
            System.out.print("Introduce una opcion: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                Loggerfichero.getInstance().writeSmg("ERROR formato opcion\n");
            }
        }
        return opcion;
    }

    private static void procesarOpcionPrincipal(int opcion) throws Exception {
        switch (opcion) {
            case 1:
                cargarDatos();
                break;
            case 2:
                menuAñadir();
                break;
            case 3:
                menuModificar();
                break;
            case 4:
                menuListar();
                break;
            case 5:
                menuBorrar();
                break;
            case 6:
                menuBuscar();
                break;
            case 7:
                bajarDatos();
                break;
            case 8:
                hacerCopiaSeguridad();
                break;
            case 9:
                mostrarBBDD();
                break;
        }
    }

    private static void mostrarBBDD() throws PersistenciaException, SQLException, ClassNotFoundException {
        System.out.println(ANSI_CUSTOM_PURPLE+gestorJDBC.mostrarBBDD()+ANSI_RESET);
    }

    private static void hacerCopiaSeguridad() {
        try {
            gestorJDBC.datosBBDDEventos();
            gestorJDBC.datosBBDDInvitados();
            gestorJDBC.datosBBDDInscripciones();

            String ruta = System.getProperty("user.dir") + "//copiaSeguridad//";
            gestorCSV.exportarDatosEventos(ruta + "copiaEventos.txt");
            gestorCSV.exportarDatosInvitados(ruta + "copiaInvitados.txt");
            gestorCSV.exportarDatosInscripciones(ruta + "copiaInscripciones.txt");

        } catch (PersistenciaException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void bajarDatos() throws PersistenciaException, IOException {
        gestorJDBC.datosBBDDEventos();
        gestorJDBC.datosBBDDInvitados();
        gestorJDBC.datosBBDDInscripciones();
    }

    private static void menuBuscar() throws IOException {
        int opcion;
        String[] opciones = {
                ANSI_CUSTOM_BLUE+"--------------- MENU OPERACION BUSCAR ---------------"+ANSI_RESET,
                "1. Buscar Evento",
                "2. Buscar Invitado",
                "3. Buscar Inscripción",
                "0. Volver"
        };
        while (true) {
            for (String opcionMenu : opciones) {
                System.out.println(opcionMenu);
            }
            System.out.print("Introduce una opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
                if (opcion == 0) break;
                procesarOpcionBuscar(opcion);
            } catch (Exception e) {
                Loggerfichero.getInstance().writeSmg("ERROR formato opcion\n");
            }
        }
    }

    private static void procesarOpcionBuscar(int opcion) throws Exception {
        PrincipalEventos menuEvento = new PrincipalEventos(gestorJDBC, sc);
        PrincipalInvitados menuInvitado = new PrincipalInvitados(gestorJDBC, sc);
        PrincipalInscripciones menuInscripcion = new PrincipalInscripciones(gestorJDBC, sc);
        switch (opcion) {
            case 1:
                menuEvento.buscarEvento();
                break;
            case 2:
                menuInvitado.buscarInvitado();
                break;
            case 3:
                menuInscripcion.buscarInscripcion();
                break;
        }
    }

    private static void menuBorrar() throws IOException {
        int opcion;
        String[] opciones = {
                ANSI_CUSTOM_BLUE+"--------------- MENU OPERACION BORRAR ---------------"+ANSI_RESET,
                "1. Borrar Evento",
                "2. Borrar Invitado",
                "3. Borrar Inscripción",
                "0. Volver"
        };
        while (true) {
            for (String opcionMenu : opciones) {
                System.out.println(opcionMenu);
            }
            System.out.print("Introduce una opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
                if (opcion == 0) break;
                procesarOpcionBorrar(opcion);
            } catch (Exception e) {
                Loggerfichero.getInstance().writeSmg("ERROR formato opcion\n");
            }
        }
    }

    private static void procesarOpcionBorrar(int opcion) throws PersistenciaException, IOException {
        PrincipalEventos menuEvento = new PrincipalEventos(gestorJDBC, sc);
        PrincipalInvitados menuInvitado = new PrincipalInvitados(gestorJDBC, sc);
        PrincipalInscripciones menuInscripcion = new PrincipalInscripciones(gestorJDBC, sc);
        switch (opcion) {
            case 1:
                menuEvento.borrarEvento();
                break;
            case 2:
                menuInvitado.borrarInvitado();
                break;
            case 3:
                menuInscripcion.borrarInscripcion();
                break;
        }
    }

    private static void menuListar() throws IOException {
        int opcion;
        String[] opciones = {
                ANSI_CUSTOM_BLUE+"--------------- MENU OPERACION LISTAR ---------------"+ANSI_RESET,
                "1. Listar eventos",
                "2. Listar invitados",
                "3. Listar inscripciones",
                "0. Volver"
        };
        while (true) {
            for (String opcionMenu : opciones) {
                System.out.println(opcionMenu);
            }
            System.out.print("Introduce una opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
                if (opcion == 0) break;
                procesarOpcionListar(opcion);
            } catch (Exception e) {
                Loggerfichero.getInstance().writeSmg("ERROR formato opcion\n");
            }
        }
    }

    private static void menuModificar() throws IOException {
        int opcion;
        String[] opciones = {
                ANSI_CUSTOM_BLUE+"--------------- MENU OPERACION MODIFICAR ---------------"+ANSI_RESET,
                "1. Modificar Evento",
                "2. Modificar Invitado",
                "3. Modificar Inscripción",
                "0. Volver"
        };
        while (true) {
            for (String opcionMenu : opciones) {
                System.out.println(opcionMenu);
            }
            System.out.print("Introduce una opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
                if (opcion == 0) break;
                procesarOpcionModificar(opcion);
            } catch (Exception e) {
                Loggerfichero.getInstance().writeSmg("ERROR formato opcion\n");
            }
        }
    }

    private static void procesarOpcionListar(int opcion) throws PersistenciaException {
        PrincipalEventos menuEvento = new PrincipalEventos(gestorJDBC, sc);
        PrincipalInvitados menuInvitado = new PrincipalInvitados(gestorJDBC, sc);
        PrincipalInscripciones menuInscripcion = new PrincipalInscripciones(gestorJDBC, sc);
        switch (opcion) {
            case 1:
                menuEvento.listarEventos();
                break;
            case 2:
                menuInvitado.listarInvitados();
                break;
            case 3:
                menuInscripcion.listarInscripciones();
                break;
        }
    }

    private static void procesarOpcionModificar(int opcion) throws Exception {
        PrincipalEventos menuEvento = new PrincipalEventos(gestorJDBC, sc);
        PrincipalInvitados menuInvitado = new PrincipalInvitados(gestorJDBC, sc);
        PrincipalInscripciones menuInscripcion = new PrincipalInscripciones(gestorJDBC, sc);
        switch (opcion) {
            case 1:
                menuEvento.modificarEvento();
                break;
            case 2:
                menuInvitado.modificarInvitado();
                break;
            case 3:
                menuInscripcion.modificarInscripcion();
                break;
        }
    }

    private static void menuAñadir() throws Exception {
        int opcion;
        String[] opciones = {
                ANSI_CUSTOM_BLUE+"--------------- MENU OPERACION AÑADIR ---------------"+ANSI_RESET,
                "1. Añadir Evento",
                "2. Añadir Invitado",
                "3. Añadir Inscripción",
                "0. Volver"
        };
        while (true) {
            for (String opcionMenu : opciones) {
                System.out.println(opcionMenu);
            }
            System.out.print("Introduce una opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
                if (opcion == 0) break;
                procesarOpcionAñadir(opcion);
            } catch (NumberFormatException e) {
                Loggerfichero.getInstance().writeSmg("ERROR formato opcion\n");
            }
        }
    }

    private static void procesarOpcionAñadir(int opcion) throws PersistenciaException, IOException {
        PrincipalEventos menuEvento = new PrincipalEventos(gestorJDBC, sc);
        PrincipalInvitados menuInvitado = new PrincipalInvitados(gestorJDBC, sc);
        PrincipalInscripciones menuInscripcion = new PrincipalInscripciones(gestorJDBC, sc);
        switch (opcion) {
            case 1:
                menuEvento.agregarEvento();
                break;
            case 2:
                menuInvitado.agregarInvitado();
                break;
            case 3:
                menuInscripcion.agregarInscripcion();
                break;
        }
    }


    private static void cargarDatos()
            throws Exception {
        String ruta = System.getProperty("user.dir") + "//ficheros//";
        String ficheroEventos = ruta + "eventos.csv";
        String ficheroInvitados = ruta + "invitados.csv";
        String ficheroInscripciones = ruta + "inscripciones.csv";
        try {
            gestorCSV.leerEventos(ficheroEventos);
            gestorCSV.leerInvitados(ficheroInvitados);
            gestorCSV.leerInscripciones(ficheroInscripciones);
            gestorCSV.importarDatosEventos();
            gestorCSV.importarDatosInvitados();
            gestorCSV.importarDatosInscripciones();
        } catch (PersistenciaException e) {
            Loggerfichero.getInstance().writeSmg(e.getMessage());
        }
    }
}
