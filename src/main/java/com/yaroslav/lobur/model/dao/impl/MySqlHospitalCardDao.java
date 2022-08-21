package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.dao.HospitalCardDao;
import com.yaroslav.lobur.model.entity.HospitalCard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlHospitalCardDao extends GenericDao<HospitalCard> implements HospitalCardDao {

    private static MySqlHospitalCardDao instance;

    public static HospitalCardDao getInstance() {
        if (instance == null) {
            instance = new MySqlHospitalCardDao();
        }
        return instance;
    }

    private MySqlHospitalCardDao(){}

    @Override
    protected HospitalCard mapToEntity(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void mapFromEntity(PreparedStatement statement, HospitalCard entity) throws SQLException {
        //TODO
    }
}
