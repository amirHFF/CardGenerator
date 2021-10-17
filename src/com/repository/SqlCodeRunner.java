package com.repository;

import com.controller.Controller;
import com.db.DataBaseConnection;
import com.model.CardInfo;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SqlCodeRunner {
    private Connection connection;
    private CardInfo cardInfo=new CardInfo();
    private StringBuffer code=new StringBuffer(
            "select  c_autistic_first_name as name ,c_autistic_last_name as lastName ,\n" +
                    "to_char(c_autistic_birth_date,'yyyy','nls_calendar=persian') as year ,to_char(c_autistic_birth_date,'mm','nls_calendar=persian') as month ,to_char(c_autistic_birth_date,'dd','nls_calendar=persian') as day,\n" +
                    "c_autistic_national_code as nationalCode,\n" +
                    "c_residence_place_country_title as country,C_RESIDENCE_PLACE_PROVINCE_OR_STATE_TITLE as state,C_RESIDENCE_PLACE_CITY_TITLE as city,\n" +
                    "(select c_title_fa from t_bif_category where c_autistic_gender = c_id) as gender,\n" +
                    "(SELECT C_value from t_ars_contact_data c where c.F_SELF_DECLARATION_PROFILE = s.C_id  and c.c_vr_type in(SELECT c_id from t_bif_category where c_code='CONTACT_TYPE_MOBILE')) as contact,\n" +
                    "(select c_hash_code  from T_ARS_POD_FILE_DATA where F_AUTISTIC_PICTURE =c_id) as picture,\n" +
                    "(select CASE WHEN  EXISTS (select TAQA.C_VALUE  from t_ars_autism_record_question_answer arqa\n" +
                    "                                                             inner join t_ars_autism_record_question arq on arqa.F_AUTISM_RECORD_QUESTION=arq.c_id\n" +
                    "                                                            inner join T_ARS_QUESTION_ANSWER TAQA on arqa.F_SELECTED_ANSWER = TAQA.C_ID\n" +
                    "                                                            inner join t_ars_autism_record record on arq.f_autism_record=record.c_id and record.f_self_declaration_profile = s.c_id\n" +
                    "                                                           \n" +
                    "\n" +
                    "where taqa.c_Answer_code ='DISEASE_TYPE_SEIZURE' ) THEN  '1' ELSE '0' END seizure  from dual) as seizure,\n" +
                    "\n" +
                    "(select CASE WHEN  EXISTS (select TAQA.C_VALUE  from t_ars_autism_record_question_answer arqa\n" +
                    "                                                             inner join t_ars_autism_record_question arq on arqa.F_AUTISM_RECORD_QUESTION=arq.c_id\n" +
                    "                                                            inner join T_ARS_QUESTION_ANSWER TAQA on arqa.F_SELECTED_ANSWER = TAQA.C_ID\n" +
                    "                                                            inner join t_ars_autism_record record on arq.f_autism_record=record.c_id and record.f_self_declaration_profile = s.c_id\n" +
                    "                                                            \n" +
                    "\n" +
                    "where taqa.c_Answer_code ='DISEASE_TYPE_ADHD' ) THEN  '1' ELSE '0' END adhd  from dual) as adhd,\n" +
                    "\n" +
                    "(select CASE WHEN  EXISTS (select TAQA.C_VALUE  from t_ars_autism_record_question_answer arqa\n" +
                    "                                                             inner join t_ars_autism_record_question arq on arqa.F_AUTISM_RECORD_QUESTION=arq.c_id\n" +
                    "                                                            inner join T_ARS_QUESTION_ANSWER TAQA on arqa.F_SELECTED_ANSWER = TAQA.C_ID\n" +
                    "                                                            inner join t_ars_autism_record record on arq.f_autism_record=record.c_id  and record.f_self_declaration_profile = s.c_id\n" +
                    "                                                           \n" +
                    "\n" +
                    "where taqa.c_Answer_code ='DISEASE_TYPE_COGNITIVE_COMMUNICATION_PROBLEM' ) THEN  '1' ELSE '0' END cominucation_problem  from dual) as cominucation_problem,\n" +
                    "(select custodial.c_first_name from t_ars_person custodial inner join t_ars_autism_record record on custodial.c_id=record.F_custodial_data  and record.f_self_declaration_profile = s.c_id)as cName,\n" +
                    "(select custodial.c_last_name from t_ars_person custodial inner join t_ars_autism_record record on custodial.c_id=record.F_custodial_data  and record.f_self_declaration_profile = s.c_id) as cLastNAme,\n" +
                    "(select custodial.c_national_code  from t_ars_person custodial inner join t_ars_autism_record record on custodial.c_id=record.F_custodial_data  and record.f_self_declaration_profile = s.c_id) as cNationalCode,\n" +
                    "(select custodial.c_phone from t_ars_person custodial inner join t_ars_autism_record record on custodial.c_id=record.F_custodial_data  and record.f_self_declaration_profile = s.c_id) as cPhone,\n" +
                    "(select custodial.c_vr_user_id from t_ars_person custodial inner join t_ars_autism_record record on custodial.c_id=record.F_custodial_data  and record.f_self_declaration_profile = s.c_id) as cSsoid,\n" +
                    "\n" +
                    "(select autistic.c_first_name from t_ars_person autistic inner join t_ars_autism_record record on autistic.c_id=record.F_autistic and record.f_self_declaration_profile = s.c_id) as aName,\n" +
                    "(select autistic.c_last_name from t_ars_person autistic inner join t_ars_autism_record record on autistic.c_id=record.F_autistic and record.f_self_declaration_profile = s.c_id)as aLastName,\n" +
                    "(select autistic.c_national_code from t_ars_person autistic inner join t_ars_autism_record record on autistic.c_id=record.F_autistic and record.f_self_declaration_profile = s.c_id) as aNationalCode,\n" +
                    "(select autistic.c_vr_user_id  from t_ars_person autistic inner join t_ars_autism_record record on autistic.c_id=record.F_autistic and record.f_self_declaration_profile = s.c_id) as aSsoid\n" +
                    "\n" +
                    "from T_ARS_SELF_DECLARATION_PROFILE s\n" +
                    "where C_AUTISTIC_NATIONAL_CODE in (\n" )
                    ;

    public HashSet<CardInfo> fetchAutismByNationalCode(HashSet<String> nationalCodes) throws SQLException {
        connection=DataBaseConnection.getInstance();
//        List<CardInfo> cardInfoList=new ArrayList<>();
        HashSet<CardInfo> cardInfoList=new HashSet<>();
        for (String nationalCode : nationalCodes) {
            code.append(nationalCode);
            code.append(",");
        }
        code.replace(code.length()-1,code.length(),")");
        try {
            PreparedStatement statement = connection.prepareStatement(code.toString());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cardInfo.setFirstName(resultSet.getString(1));
                cardInfo.setLastName(resultSet.getString(2));
                cardInfo.setYear(resultSet.getInt(3));
                cardInfo.setMonth(resultSet.getInt(4));
                cardInfo.setDay(resultSet.getInt(5));
                cardInfo.setNationalCode(resultSet.getString(6));
                cardInfo.setCountry(resultSet.getString(7));
                cardInfo.setState(resultSet.getString(8));
                cardInfo.setCity(resultSet.getString(9));
                cardInfo.setGender(resultSet.getString(10));
                cardInfo.setContact(resultSet.getString(11));
                cardInfo.setPhone(cardInfo.getContact());
                cardInfo.setPicHash(resultSet.getString(12));
                cardInfo.setSeizure(resultSet.getBoolean(13));
                cardInfo.setADHD(resultSet.getBoolean(14));
                cardInfo.setCommunicationProblem(resultSet.getBoolean(15));
                cardInfo.setCustodialName(resultSet.getString(16));
                cardInfo.setCustodialLastName(resultSet.getString(17));
                cardInfo.setCustodialNationalCode(resultSet.getString(18));
                cardInfo.setCustodialPhone(resultSet.getString(19));
                cardInfo.setCustodialNationalCode(resultSet.getString(20));
                cardInfo.setAutismName(resultSet.getString(21));
                cardInfo.setAutismLastName(resultSet.getString(22));
                cardInfo.setASSOID(resultSet.getString(23));
                cardInfo.setCSSOID(resultSet.getString(24));
                cardInfoList.add(cardInfo);
            }
            if (cardInfoList.size()==0)
                Controller.lunchAlert("no record registered for these nationalCodes", Alert.AlertType.INFORMATION);
        }catch (Exception ex){
            Controller.lunchAlert(ex.getMessage(), Alert.AlertType.ERROR);
        }
        return cardInfoList;
    }
    public boolean changeSession(String session) throws SQLException {
        connection=DataBaseConnection.getInstance();
        Statement statement = connection.createStatement();
        return statement.execute("ALTER SESSION SET CURRENT_SCHEMA =".concat(session));
    }
}
