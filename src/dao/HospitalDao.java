package dao;

import dao.util.Close;
import dto.HealthInsurance;
import dto.Hospital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HospitalDao implements DAO<Hospital, Integer> {

    final String INSERT = "INSERT INTO Hospital (name) VALUES(?);";
    final String UPDATE = "UPDATE Hospital SET id = ?, name = ? WHERE id = ?;";
    final String DELETE = "DELETE FROM Hospital WHERE id = ?;";
    final String GETALL = "SELECT id, name FROM Hospital;";
    final String GETONE = "SELECT id, name FROM Hospital WHERE id = ?;";
    private Connection conn;

    public HospitalDao(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insert(Hospital a) throws DAOException {
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
    public void modify(Hospital a, Integer id) throws DAOException{
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

    private Hospital convert(ResultSet rs) throws DAOException {
        try {
            Hospital hospital = new Hospital();
            hospital.setId(rs.getInt("id"));
            hospital.setName(rs.getString("name"));
            return hospital;
        }
        catch (SQLException e ) {
            throw new DAOException("Error al obtener la información");
        }
    }

    @Override
    public List<Hospital> getAll() throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Hospital> list = new ArrayList<>();
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
    public Hospital getById(Integer id) throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        Hospital hospital = null;
        try {
            stat = conn.prepareStatement(GETONE);
            stat.setInt(1, id);
            rs = stat.executeQuery();

            if(rs.next()) {
                hospital = convert(rs);
            }
            else {
                throw new DAOException("No se encontro el registro");
            }
        }
        catch (SQLException e){
            throw new DAOException("Error al ejecutar SQL", e);
        }finally {
            Close.close(rs, stat);
        }
        return hospital;
    }
}
