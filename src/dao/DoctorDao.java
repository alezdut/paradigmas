package dao;

import dao.util.Close;
import dto.Doctor;
import dto.HealthInsurance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao implements DAO<Doctor, Integer> {

    final String INSERT = "INSERT INTO Doctor(name, id_speciality, id_health_insurance) VALUES(?, ?, ?);";
    final String UPDATE = "UPDATE Doctor SET id = ?, name = ?, id_speciality = ?, id_health_insurance = ? WHERE id = ?;";
    final String DELETE = "DELETE FROM Doctor WHERE id = ?;";
    final String GETALL = "SELECT id, name, id_speciality, id_health_insurance FROM Doctor;";
    final String GETONE = "SELECT id, name, id_speciality, id_health_insurance FROM Doctor WHERE id = ?;";
    final String GETBYNAME = "SELECT id, name, id_speciality, id_health_insurance FROM Doctor WHERE name = ?;";
    private Connection conn;

    public DoctorDao(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insert(Doctor a) throws DAOException {
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.prepareStatement(INSERT);
            stat.setString(1, a.getName());
            stat.setInt(2, a.getSpecialityId());
            stat.setInt(3, a.getHealthInsuranceId());
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
    public void modify(Doctor a, Integer id) throws DAOException{
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE);
            stat.setInt(1,a.getId());
            stat.setString(2, a.getName());
            stat.setInt(3, a.getSpecialityId());
            stat.setInt(4, a.getHealthInsuranceId());
            stat.setInt(5, id);
            if(stat.executeUpdate() == 0){
                throw new DAOException("Error al guardar la información");
            };
        }
        catch (SQLException e){
            throw new DAOException("Error al ejecutar SQL", e);
        }finally {
            if(stat != null){
                Close.close(null, stat);
            }
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
            if(stat != null){
                Close.close(null, stat);
            }
        }
    }

    private Doctor convert(ResultSet rs) throws DAOException {
        try {
            Doctor doctor = new Doctor();
            doctor.setId(rs.getInt("id"));
            doctor.setName(rs.getString("name"));
            doctor.setSpecialityId(rs.getInt("id_speciality"));
            doctor.setHealthInsuranceId(rs.getInt("id_health_insurance"));
            return doctor;
        }
        catch (SQLException e ) {
            throw new DAOException("Error al obtener la información");
        }
    }

    @Override
    public List<Doctor> getAll() throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Doctor> list = new ArrayList<>();
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
    public Doctor getById(Integer id) throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        Doctor doctor = null;
        try {
            stat = conn.prepareStatement(GETONE);
            stat.setInt(1, id);
            rs = stat.executeQuery();

            if(rs.next()) {
                doctor = convert(rs);
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
        return doctor;
    }

    public Doctor getByName(String name) throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        Doctor doctor = null;
        try {
            stat = conn.prepareStatement(GETBYNAME);
            stat.setString(1, name);
            rs = stat.executeQuery();

            if(rs.next()) {
                doctor = convert(rs);
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
        return doctor;
    }
}
