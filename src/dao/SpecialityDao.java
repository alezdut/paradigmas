package dao;

import dao.util.Close;
import dto.Patient;
import dto.Speciality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpecialityDao implements DAO<Speciality, Integer> {

    final String INSERT = "INSERT INTO Speciality (name, id_hospital) VALUES(?, ?);";
    final String UPDATE = "UPDATE Speciality SET id = ?, name = ?, id_hospital = ? WHERE id = ?;";
    final String DELETE = "DELETE FROM Speciality WHERE id = ?;";
    final String GETALL = "SELECT id, name, id_hospital FROM Speciality;";
    final String GETONE = "SELECT id, name, id_hospital FROM Speciality WHERE id = ?;";
    private Connection conn;

    public SpecialityDao(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insert(Speciality a) throws DAOException {
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.prepareStatement(INSERT);
            stat.setString(1, a.getName());
            stat.setInt(2, a.getHospitalId());
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
    public void modify(Speciality a, Integer id) throws DAOException{
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE);
            stat.setInt(1,a.getId());
            stat.setString(2, a.getName());
            stat.setInt(3, a.getHospitalId());
            stat.setInt(4, id);
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

    private Speciality convert(ResultSet rs) throws DAOException {
        try {
            Speciality speciality = new Speciality();
            speciality.setId(rs.getInt("id"));
            speciality.setName(rs.getString("name"));
            speciality.setHospitalId(rs.getInt("id_hospital"));
            return speciality;
        }
        catch (SQLException e ) {
            throw new DAOException("Error al obtener la información");
        }
    }

    @Override
    public List<Speciality> getAll() throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Speciality> list = new ArrayList<>();
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
    public Speciality getById(Integer id) throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        Speciality speciality = null;
        try {
            stat = conn.prepareStatement(GETONE);
            stat.setInt(1, id);
            rs = stat.executeQuery();

            if(rs.next()) {
                speciality = convert(rs);
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
        return speciality;
    }
}
