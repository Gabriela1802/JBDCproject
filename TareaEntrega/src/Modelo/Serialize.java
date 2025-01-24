package Modelo;

import Excepciones.PersistenciaException;

public interface Serialize {

    /**
     * Recibe como parametro un objeto y devuelve un String
     * @param object : Object
     * @return : String
     */
    public abstract String serialize(Object object);

    /**
     * Recibe como parametro un String y devuelve un Objeto
     * @param string : String
     * @return : Object
     * @throws PersistenciaException : si existe algun error en la serializacion
     */
    public abstract Object serialize(String string) throws PersistenciaException;
}
