package dao;

import dto.HealthInsurance;

import java.sql.ResultSet;
import java.util.List;

public interface DAO <T, K>{

    void insert(T a) throws DAOException;

    void modify(T a, K id) throws DAOException;

    void delete(K id) throws DAOException;
    List<T> getAll() throws DAOException;

    T getById(K id) throws DAOException;

}
