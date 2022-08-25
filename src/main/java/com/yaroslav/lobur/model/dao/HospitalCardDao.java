package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import java.sql.Connection;
import java.util.List;

public interface HospitalCardDao {
    long insertHospitalCard(Connection connection, HospitalCard hospitalCard);

    void updateHospitalCard(Connection connection, HospitalCard hospitalCard);

    List<HospitalCard> findAllHospitalCards(Connection connection);

    List<HospitalCard> findAllHospitalCardsForDoctor(Connection connection, long id, int offset, int noOfRecords);

    HospitalCard findHospitalCardById(Connection connection, long id);

    int getNumberOfRecords();

}
