import dao.DAOException;
import datasource.DAOManager;
import dto.Doctor;
import dto.Hospital;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        try {
            DAOManager manager = new DAOManager();

            List<Hospital> hospitals = manager.getHospitalDao().getAll();
            System.out.println(hospitals);
        }
        catch (SQLException e){
            throw new SQLException(e);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
}
