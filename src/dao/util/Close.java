package dao.util;

import dao.DAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Close {
    public static void close(ResultSet rs, PreparedStatement stat) throws DAOException{
        if(rs != null){
            try {
                rs.close();
            }
            catch(SQLException e){
                throw new DAOException("Error", e);
            }
        }
        if(stat != null){
            try {
                stat.close();
            }
            catch (SQLException e ){
                throw new DAOException("Error al ejecutar SQL", e);
            }
        }
    }
}
