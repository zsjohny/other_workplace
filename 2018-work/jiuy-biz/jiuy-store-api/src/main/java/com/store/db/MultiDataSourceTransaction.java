package com.store.db;

import org.apache.ibatis.transaction.Transaction;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MultiDataSourceTransaction implements Transaction {


    public MultiDataSourceTransaction() {
    }

    private Connection connection;
    private DataSource dataSource;
    private Boolean autoCommit;
    private Boolean isConnectionTransactional;

    @Override
    public Connection getConnection() throws SQLException {

        dataSource = HandleDataSource.getDataSourceByKey(HandleDataSource.getDataSource());

        if (dataSource != null) {
            connection = DataSourceUtils.getConnection(dataSource);
            isConnectionTransactional = DataSourceUtils.isConnectionTransactional(connection, dataSource);
            autoCommit = connection.getAutoCommit();


            return connection;
        }
        return null;
    }


    @Override
    public void rollback() throws SQLException {

        connection.setAutoCommit(false);
        System.out.println("需要回滚");

        if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
            connection.rollback();
        }
    }


    @Override
    public void commit() throws SQLException {

        if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
            System.out.println("手动提交事物");
            connection.commit();
        }

    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        if (holder != null && holder.hasTimeout()) {
            return holder.getTimeToLiveInSeconds();
        }
        return null;
    }
}
