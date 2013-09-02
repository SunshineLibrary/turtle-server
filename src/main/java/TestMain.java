import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * User: fxp
 * Date: 13-8-30
 * Time: PM2:30
 */
public class TestMain {


    public static Dao<ApiResponse, String> cacheDAO;

    public static void main(String[] args) throws SQLException {
        // TODO: change to a db file
        String databaseUrl = "jdbc:h2:mem:ApiResponse";
        ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
        cacheDAO = DaoManager.createDao(connectionSource, ApiResponse.class);
        TableUtils.createTable(connectionSource, ApiResponse.class);
        ApiResponse response = new ApiResponse();
        response.uri = "/exercise/v1/root";
        response.content_type = "application/json";
        response.content = "{abc:123}";

        // persist the ApiResponse object to the database
        cacheDAO.create(response);

        // retrieve the ApiResponse from the database by its id field (name)
        ApiResponse resp2 = cacheDAO.queryForId("/exercise/v1/root");
        System.out.println("ApiResponse: " + new Gson().toJson(resp2));

        // close the connection source
        connectionSource.close();
    }

    @DatabaseTable(tableName = "cached_response")
    public static class ApiResponse {
        @DatabaseField(id = true)
        public String uri;
        @DatabaseField
        public String ts;
        @DatabaseField
        public String content_type;
        @DatabaseField
        public String content;
    }


}
