package dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {
        private DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeSql(final String query) throws SQLException{
        workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement(query);
                return ps;
            }
        });
    }

    public void workWithStatementStrategy(StatementStrategy stmt){
            Connection c = null;
            PreparedStatement ps = null;

            try {
                c = this.dataSource.getConnection();
                ps = stmt.makePreparedStatement(c);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (ps != null) {
                    try{
                        ps.close();
                    } catch (SQLException e){
                    }
                }
                if (c != null){
                    try {
                        c.close();
                    }catch (SQLException e){
                    }
                }
            }
        }
}
