package datasource;

import dao.*;

import org.sqlite.JDBC;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOManager {
    private Connection conn;
    public static final String DRIVER_SQLITE = "org.sqlite.JDBC";

    public static final String DBURL_SQLITE = "jdbc:sqlite:Hopital_Registration.db";

    private HospitalDao hospitals = null;
    private DoctorDao doctors = null;
    private PatientDao patients = null;
    private IncidenceDao incidences = null;
    private SpecialityDao specialities = null;
    private HealthInsuranceDao healthInsurances = null;

    public DAOManager() throws SQLException {
        conn = DriverManager.getConnection(DBURL_SQLITE);
    }

    public HospitalDao getHospitalDao(){
        if(hospitals == null){
            hospitals = new HospitalDao(conn);
        }
        return hospitals;
    }

    public DoctorDao getDoctorsDao(){
        if(doctors == null){
            doctors = new DoctorDao(conn);
        }
        return doctors;
    }

    public PatientDao getPatientsDao(){
        if(patients == null){
            patients = new PatientDao(conn);
        }
        return patients;
    }

    public IncidenceDao getIncidencesDao(){
        if(incidences == null){
            incidences = new IncidenceDao(conn);
        }
        return incidences;
    }

    public SpecialityDao getSpecialitiesDao(){
        if(specialities == null){
            specialities = new SpecialityDao(conn);
        }
        return specialities;
    }

    public HealthInsuranceDao getHealthInsurancesDao(){
        if(healthInsurances == null){
            healthInsurances = new HealthInsuranceDao(conn);
        }
        return healthInsurances;
    }

}
