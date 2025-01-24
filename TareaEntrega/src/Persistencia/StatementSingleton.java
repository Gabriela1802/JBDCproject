package Persistencia;

import Excepciones.PersistenciaException;

import java.sql.*;

public class StatementSingleton {
    private static StatementSingleton instance;
    private Connection connection;


    private StatementSingleton() throws PersistenciaException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/eventos", "root", "mysql");
        } catch (ClassNotFoundException | SQLException e) {
            throw new PersistenciaException("Error al inicializar la conexion: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static StatementSingleton getInstance() throws PersistenciaException {
        if (instance == null) {
            instance = new StatementSingleton();
        }
        return instance;
    }


    public Statement createStatement() throws PersistenciaException {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear el Statement: " + e.getMessage());
        }
    }

    public PreparedStatement prepareStatement(String sql) throws PersistenciaException {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al preparar la consulta: " + e.getMessage());
        }
    }


    public void close() throws PersistenciaException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al cerrar la conexión: " + e.getMessage());
        }
    }


}
