package pl.indexpz.db_manager_2.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserDao {
    //    CRUD
    private static final String CREATE_USER_QUERY = "INSERT INTO users (name, email, password) VALUES (?, ?, ?);";
    private static final String READ_USER_QUERU = "SELECT * FROM users WHERE id = (?);";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET name = (?), email = (?), password = (?) WHERE id = (?);";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = (?);";

    private static final String ALL_USERS_QUERY = "SELECT * FROM users;";

    private static final String DB_NAME = "db_workshop2";


    public User create(User user) {
        int id = 0;
        try (Connection db_workshop2_conn = DbUtil.conn(DB_NAME)) {
            PreparedStatement preparedStatement = db_workshop2_conn.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, hashPassword(user.getPassword()));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
               id = resultSet.getInt(1);

            }
            System.out.println("Użytkownik " + user.getName() + " został dodany do bazy danych. Id użytkownika to: " + id);
            return user;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Nie udało się dodać użytkownika " + user.getName() + " do bazy danych.");
            return null;
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User read(int userID) {
        try (Connection db_workshop2_conn = DbUtil.conn(DB_NAME)) {
            User user = new User();
            PreparedStatement preparedStatement = db_workshop2_conn.prepareStatement(READ_USER_QUERU);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
            }
            return user;
        } catch (SQLException ex) {
            ex.printStackTrace();
//            throw new RuntimeException("Nie udało połączyć się z bazą danych. " + ex);
            return null;
        }
    }

    public void update(User user) {
        try (Connection db_workshop2_conn = DbUtil.conn(DB_NAME)) {
            PreparedStatement preparedStatement = db_workshop2_conn.prepareStatement(UPDATE_USER_QUERY);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, this.hashPassword(user.getPassword()));
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
            System.out.println("Zaktualizowano użytkownika " + user.getName() + " o id " + user.getId());

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Nie udało połączyć się z bazą danych. " + ex);
        }
    }

    public void delete(int id){
        try(Connection db_workshop2_conn = DbUtil.conn(DB_NAME)){
            PreparedStatement preparedStatement = db_workshop2_conn.prepareStatement(DELETE_USER_QUERY);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }catch (SQLException ex){
            ex.printStackTrace();
            throw new RuntimeException("Nie udało połączyć się z bazą danych. " + ex);
        }
    }


    public User[] findAll(){
        try(Connection db_workshop2_conn = DbUtil.conn(DB_NAME)){
            User[] users = new User[0];
            PreparedStatement preparedStatement = db_workshop2_conn.prepareStatement(ALL_USERS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName((resultSet.getString("name")));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return addNewToArray(user, users);
            }
            return users;
        }catch (SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    private User[] addNewToArray(User user, User[] users){
        User[] tmpUsers = Arrays.copyOf(users, users.length);
        tmpUsers[users.length] = user;
        return tmpUsers;
    }


    private Boolean isRecordInDB(User user) {
        boolean result = false;
        User[] allUsers = new User[0];
        try (Connection db_workshop2_conn = DbUtil.conn(DB_NAME)) {
            PreparedStatement preparedStatement = db_workshop2_conn.prepareStatement(ALL_USERS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();


        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Nie udało połączyć się z bazą danych. " + ex);


        }
        return result;
    }
}

