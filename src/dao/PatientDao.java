package dao;

import dao.util.Close;
import dto.Doctor;
import dto.Incidence;
import dto.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDao implements DAO<Patient, Integer> {

    final String INSERT = "INSERT INTO Patient (name, id_health_insurance) VALUES(?, ?);";
    final String UPDATE = "UPDATE Patient SET id = ?, name = ?, id_health_insurance = ? WHERE id = ?;";
    final String DELETE = "DELETE FROM Patient WHERE id = ?;";
    final String GETALL = "SELECT id, name, id_health_insurance FROM Patient;";
    final String GETONE = "SELECT id, name, id_health_insurance FROM Patient WHERE id = ?;";
    final String GETBYNAME = "SELECT id, name, id_health_insurance FROM Patient WHERE name = ?;";
    private Connection conn;

    public PatientDao(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insert(Patient a) throws DAOException {
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.prepareStatement(INSERT);
            stat.setString(1, a.getName());
            stat.setInt(2, a.getHealthInsuranceId());
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
    public void modify(Patient a, Integer id) throws DAOException{
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE);
            stat.setInt(1,a.getId());
            stat.setString(2, a.getName());
            stat.setInt(3, a.getHealthInsuranceId());
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

    private Patient convert(ResultSet rs) throws DAOException {
        try {
            Patient patient = new Patient();
            patient.setId(rs.getInt("id"));
            patient.setName(rs.getString("name"));
            patient.setHealthInsuranceId(rs.getInt("id_health_insurance"));
            return patient;
        }
        catch (SQLException e ) {
            throw new DAOException("Error al obtener la información");
        }
    }

    @Override
    public List<Patient> getAll() throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Patient> list = new ArrayList<>();
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
    public Patient getById(Integer id) throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        Patient patient = null;
        try {
            stat = conn.prepareStatement(GETONE);
            stat.setInt(1, id);
            rs = stat.executeQuery();

            if(rs.next()) {
                patient = convert(rs);
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
        return patient;
    }
    public Patient getByName(String name) throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        Patient patient = null;
        try {
            stat = conn.prepareStatement(GETBYNAME);
            stat.setString(1, name);
            rs = stat.executeQuery();

            if(rs.next()) {
                patient = convert(rs);
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
        return patient;
    }
}
