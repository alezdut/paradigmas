package jdbc;

import entidades.Materia;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MateriaJDBC extends AccesoJDBC {
    
    public void crearTabla() {
        Connection conexion = null;
        Statement statement = null;
        
        try {
            conexion = createConnection();
            
            //Crear objeto Statement para realizar queries a la base de datos
            statement = conexion.createStatement();
            
            int result = statement.executeUpdate("CREATE TABLE MATERIA "
                    + "(CODIGO INT NOT NULL, "
                    + "NOMBRE VARCHAR(45),"
                    + "CUATRIMESTRE INT(45))");
            
            System.out.println("Tabla MATERIA creada satisfactoriamente!");
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
    
    
    public void guardarMateria(Materia materia) {
        Connection conexion = null;
        Statement statement = null;
        
        try {
            conexion = createConnection();
            
            //Crear objeto Statement para realizar queries a la base de datos
            statement = conexion.createStatement();
            
            int result = statement.executeUpdate("INSERT INTO MATERIA VALUES (" 
                    + materia.getCodigo() + ",'" 
                    + materia.getNombre() + "','" 
                    + materia.getCuatrimestre() +"')");
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
    
    
    public Materia buscarMateriaPorCodigo(int codigoMateria) {
        Materia materia = null;
        
        Connection conexion = null;
        Statement statement = null;
        ResultSet rs = null;
        
        try {
            conexion = createConnection();
            
            //Crear objeto Statement para realizar queries a la base de datos
            statement = conexion.createStatement();
            
            //Un objeto ResultSet, almacena los datos de resultados de una consulta
            rs = statement.executeQuery("SELECT * FROM MATERIA WHERE MATERIA.CODIGO=" + codigoMateria);
            
            while(rs.next()) {
                materia = new Materia();
                materia.setCodigo(rs.getInt("CODIGO"));
                materia.setNombre(rs.getString("NOMBRE"));
                materia.setCuatrimestre(rs.getString("CUATRIMESTRE"));
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
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        return materia;
    }
    
    public Materia buscarMateriaPorNombre(String nombreMateria) {
        Materia materia = null;
        
        Connection conexion = null;
        Statement statement = null;
        ResultSet rs = null;
        
        try {
            conexion = createConnection();
            
            //Crear objeto Statement para realizar queries a la base de datos
            statement = conexion.createStatement();
            
            //Un objeto ResultSet, almacena los datos de resultados de una consulta
            rs = statement.executeQuery("SELECT * FROM MATERIA WHERE MATERIA.NOMBRE='" + nombreMateria + "'");
            
            while(rs.next()) {
                materia = new Materia();
                materia.setCodigo(rs.getInt("CODIGO"));
                materia.setNombre(rs.getString("NOMBRE"));
                materia.setCuatrimestre(rs.getString("CUATRIMESTRE"));
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
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        return materia;
    }
    
}
