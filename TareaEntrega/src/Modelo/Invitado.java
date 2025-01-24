package Modelo;

public class Invitado implements Serialize {
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    /**
     * Constructor con todos los parametros
     * @param dni : String
     * @param nombre : String
     * @param apellido : String
     * @param email : String
     * @param telefono : String
     */
    public Invitado(String dni, String nombre, String apellido, String email, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
    }

    /**
     * Constructor vacio
     */
    public Invitado() {

    }

    /**
     * Metodo get del arributo dni
     * @return : String
     */
    public String getDni() {
        return dni;
    }

    /**
     * Metodo set del arributo dni
     * @param dni : String
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Metodo get del arributo nombre
     * @return : String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Metodo set del arributo nombre
     * @param nombre : String
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo get del arributo apellido
     * @return : String
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Metodo set del arributo apellido
     * @param apellido : String
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Metodo get del arributo email
     * @return : String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Metodo set del arributo email
     * @param email : String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Metodo get del arributo telefono
     * @return : String
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Metodo set del arributo telefono
     * @param telefono : String
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Invitado{" + "dni=" + dni + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + ", telefono=" + telefono + '}';
    }

    @Override
    public String serialize(Object object) {
        Invitado inv = (Invitado) object;
        String linea = inv.getDni()+","+inv.getNombre()+","+inv.getApellido()+","+
                        inv.getEmail()+","+inv.getTelefono();
        return linea;
    }

    @Override
    public Object serialize(String linea) {
        String[]trozos = linea.split(",");
        String dni = trozos[0];
        String nombre = trozos[1];
        String apellidos = trozos[2];
        String email = trozos[3];
        String telefono = trozos[4];

        return new Invitado(dni, nombre, apellidos, email, telefono);
    }
}
