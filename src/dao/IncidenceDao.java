package dao;

import dao.util.Close;
import dto.Hospital;
import dto.Incidence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IncidenceDao implements DAO<Incidence, Integer> {

    final String INSERT = "INSERT INTO Incidence (description, date, state, id_patient, id_doctor) VALUES(?, ?, ?, ?, ?);";
    final String UPDATE = "UPDATE Incidence SET  description = ?, state = ?, id_patient = ?, id_doctor = ? WHERE id = ?;";
    final String DELETE = "DELETE FROM Incidence WHERE id = ?;";
    final String GETALL = "SELECT id, description, date, state, id_patient, id_doctor FROM Incidence;";
    final String GETONE = "SELECT id, description, date, state, id_patient, id_doctor FROM Incidence WHERE id = ?;";
    private Connection conn;

    public IncidenceDao(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insert(Incidence a) throws DAOException {
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.prepareStatement(INSERT);
            stat.setString(1, a.getDescription());
            stat.setString(2, a.getDate());
            stat.setString(3, a.getState());
            stat.setInt(4, a.getPatientId());
            stat.setInt(5, a.getDoctorId());
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
    public void modify(Incidence a, Integer id) throws DAOException{
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE);
            stat.setString(1, a.getDescription());
            stat.setString(2, a.getState());
            stat.setInt(3, a.getPatientId());
            stat.setInt(4, a.getDoctorId());
            stat.setInt(5, id);
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

    private Incidence convert(ResultSet rs) throws DAOException {
        try {
            Incidence incidence = new Incidence();
            incidence.setId(rs.getInt("id"));
            incidence.setDescription(rs.getString("description"));
            incidence.setDate(rs.getString("date"));
            incidence.setState(rs.getString("state"));
            incidence.setPatientId(rs.getInt("id_patient"));
            incidence.setDoctorId(rs.getInt("id_doctor"));
            return incidence;
        }
        catch (SQLException e ) {
            throw new DAOException("Error al obtener la información");
        }
    }

    @Override
    public List<Incidence> getAll() throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Incidence> list = new ArrayList<>();
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
    public Incidence getById(Integer id) throws DAOException{
        PreparedStatement stat = null;
        ResultSet rs = null;
        Incidence incidence = null;
        try {
            stat = conn.prepareStatement(GETONE);
            stat.setInt(1, id);
            rs = stat.executeQuery();

            if(rs.next()) {
                incidence = convert(rs);
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
        return incidence;
    }
}
