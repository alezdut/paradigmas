package jdbc;

import entidades.Alumno;
import entidades.Materia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AlumnoJDBC extends AccesoJDBC {
    
    public void crearTabla() {
        Connection conexion = null;
        Statement statement = null;
        
        try {
            conexion = createConnection();
            
            //Crear objeto Statement para realizar queries a la base de datos
            statement = conexion.createStatement();
            
            int result = statement.executeUpdate("CREATE TABLE ALUMNO "
                    + "(LEGAJO INT NOT NULL, "
                    + "APELLIDO VARCHAR(45),"
                    + "COD_MATERIA INT(45))");
            
            System.out.println("Tabla ALUMNO creada satisfactoriamente!");
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();    
                }
                if (conexion != null) {
                    conexion.close();
                }
            } 
            catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }
    
    
    public void guardarAlumno(Alumno alumno) {
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        
        try {
            conexion = createConnection();
            
            //Crear objeto PreparedStatement para realizar queries a la base de datos
            preparedStatement = conexion.prepareStatement("INSERT INTO ALUMNO VALUES (?,?,?)");
            preparedStatement.setInt(1, alumno.getLegajo());
            preparedStatement.setString(2, alumno.getApellido());
            preparedStatement.setInt(3, alumno.getMateria().getCodigo());
            
            int result = preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(ex);
        } 
        finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();    
                }
                if (conexion != null) {
                    conexion.close();
                }
            } 
            catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }
    
    
    public Alumno buscarAlumnoPorApellido(String apellidoAlumno) {
        Alumno alumno = null;
        
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        
        try {
            conexion = createConnection();
            
            //Crear objeto Statement para realizar queries a la base de datos
            preparedStatement = conexion.prepareStatement("SELECT * FROM ALUMNO WHERE ALUMNO.APELLIDO = ?");
            preparedStatement.setString(1, apellidoAlumno);
            
            //Un objeto ResultSet, almacena los datos de resultados de una consulta
            rs = preparedStatement.executeQuery();
            while(rs.next()) { 
                alumno = new Alumno();
                alumno.setLegajo(rs.getInt("LEGAJO"));
                alumno.setApellido(rs.getString("APELLIDO"));

                int codigoMateria = rs.getInt("COD_MATERIA");
                
                MateriaJDBC materiaJDBC = new MateriaJDBC();
                Materia materia = materiaJDBC.buscarMateriaPorCodigo(codigoMateria);
                
                alumno.setMateria(materia);
            }
            
        }
        catch(SQLException e){ 
            System.out.println(e); 
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        return alumno;
    }
        
}
