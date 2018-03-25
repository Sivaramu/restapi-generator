package com.freedom.code.restapi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.freedom.code.restapi.support.Column;
import com.freedom.code.restapi.support.Database;
import com.freedom.code.restapi.support.DatabaseConfig;
import com.freedom.code.restapi.support.QueryConfig;

@Repository
public class CodeGeneratorDAO {
	private static final Logger logger = LoggerFactory.getLogger(CodeGeneratorDAO.class);

	public List<Column> findColumns(QueryConfig config) throws SQLException, ClassNotFoundException {
		final String query = getMetaDataQuery(config);
		final List<Column> columns = new ArrayList<>();
		try (Connection connection = getConnection(config.getDatabase());
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query);) {
			if (StringUtils.isEmpty(config.getTable())) {

			} else {
				ResultSetMetaData metaData = resultSet.getMetaData();
				int columnCount = metaData.getColumnCount();
				
				for (int index = 1; index <= columnCount; index++) {
					final String columnName = metaData.getColumnName(index).toLowerCase();					
					columns.add(new Column().setName(changeColumnName(columnName)).setDbType(Class.forName(metaData.getColumnClassName(index))));
				}
			}
		}
		return columns;
	}
	
	private  String changeColumnName(String columnName){
        if(columnName == null){
            return null;
        }
        if(columnName.contains("_") || columnName.contains(" ")){
            char[] cs = columnName.toCharArray();
            int flag = -1;
            for(int index=0;index<columnName.toCharArray().length;index++){
                if(cs[index] == '_' || cs[index] == ' '){
                    flag = index;
                    break;
                }
            }
            columnName = columnName.substring(0, flag) + columnName.substring(flag+1,flag+2).toUpperCase() + columnName.substring(flag+2);
            return changeColumnName(columnName);
        }else{
            return columnName;
        }
    }
	
	private String getMetaDataQuery(QueryConfig config) {
		final String query = config.getDatabase().getType().equals(Database.ORACLE)
				? "SELECT * FROM " + config.getTable() + " WHERE 1=1"
				: "SELECT * FROM " + config.getTable() + " WHERE 1=1";
		return query;
	}

	public Connection getConnection(DatabaseConfig config) throws SQLException {
		Objects.requireNonNull(config, "databaseConfig is null");
		Objects.requireNonNull(config.getConnectionString(), "databaseConfig.connectionString is null");
		Objects.requireNonNull(config.getUserName(), "databaseConfig.userName is null");
		Objects.requireNonNull(config.getPassword(), "databaseConfig.password is null");

		final DriverManagerDataSource dataSource = new DriverManagerDataSource(config.getConnectionString(),
				config.getUserName(), config.getPassword());
		dataSource.setDriverClassName(config.getDriverClass());
		return dataSource.getConnection();
	}

}
