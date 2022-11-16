package dao;


import dao.util.Close;
import dto.HealthInsurance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HealthInsuranceDao implements DAO<HealthInsurance, Integer> {

    final String INSERT = "INSERT INTO Health_insurance (name) VALUES(?);";
    final String UPDATE = "UPDATE Health_insurance SET id = ?, name = ? WHERE id = ?;";
    final String DELETE = "DELETE FROM Health_insurance WHERE id = ?;";
    final String GETALL = "SELECT id, name FROM Health_insurance;";
    final String GETONE = "SELECT id, name FROM Health_insurance WHERE id = ?;";
    private Connection conn;

    public HealthInsuranceDao(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insert(HealthInsurance a) throws DAOException {
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.prepareStatement(INSERT);
            stat.setString(1, a.getName());
            if(stat.executeUpdate() == 0){
                throw new DAOException("Error al guardar la informacion");
            };
            rs = stat.getGeneratedKeys();
            if(rs.next()){
                a.setId(rs.getInt(1));
            }
            else{
                throw new DAOException("No se pudo asignar el ID");
            }
        }
        catch (SQLException e){
            throw new DAOException("Error al ejecutar SQL", e);
        }finally {
            Close.close(rs, stat);
        }
    }

    @Override
    public void modify(HealthInsurance a, Integer id) throws DAOException{
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE);
            stat.setInt(1,a.getId());
            stat.setString(2, a.getName());
            stat.setInt(3, id);
            if(stat.executeUpdate() == 0){
                throw new DAOException("Error al guardar la información");
            };
        }
        catch (SQLException e){
            throw new DAOException("Error al ejecutar SQL", e);
        }finally {
            Close.close(null, stat);
        }
    }

    @Override
    public void delete(Integer id) throws DAOException{
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(DELETE);
            stat.setInt(1, id);
            if(stat.executeUpdate() == 0){
                throw new DAOException("Error al guardar la información");
            };
        }
        catch (SQLException e){
            throw new DAOException("Error al ejecutar SQL", e);
        }finally {
            Close.close(null, stat);
        }
    }

    private HealthInsurance convert(ResultSet rs) throws DAOException {
        try {
            HealthInsurance healthInsurance = new HealthInsurance();
            healthInsurance.setId(rs.getInt("id"));
            healthInsurance.setName(rs.getString("name"));
            return healthInsurance;
        }
        catch (SQLException e ) {
            throw new DAOException("Error al obtener la información");
        }
    }

    @Override
    public List<HealthInsurance> getAll() throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<HealthInsurance> list = new ArrayList<>();
        try {
            stat = conn.prepareStatement(GETALL);
            rs = stat.executeQuery();

            while(rs.next()) {
                list.add(convert(rs));
            }
        }
        catch (SQLException e){
            throw new DAOException("Error al ejecutar SQL", e);
        }finally {
            Close.close(rs, stat);
        }
        return list;
    }

    @Override
    public HealthInsurance getById(Integer id) throws DAOException {
        PreparedStatement stat = null;
        ResultSet rs = null;
        HealthInsurance healthInsurance = null;
        try {
            stat = conn.prepareStatement(GETONE);
            stat.setInt(1, id);
            rs = stat.executeQuery();
            if(rs.next()){
                healthInsurance = convert(rs);
            }
            else {
                throw new DAOException("No se Encontro el registro");
            }
        }
        catch (SQLException e){
            throw new DAOException("Error al ejecutar SQL", e);
        }finally {
            Close.close(rs, stat);
        }
        return healthInsurance;
    }
}
