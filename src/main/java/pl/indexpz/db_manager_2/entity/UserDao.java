package pl.indexpz.db_manager_2.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserDao {
    //    CRUD
    private static final String CREATE_USER_QUERY = "INSERT INTO users (name, email, password) VALUES (?, ?, ?);";
    private static final String READ_USER_QUERY = "SELECT * FROM users WHERE id = (?);";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET name = (?), email = (?), password = (?) WHERE id = (?);";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = (?);";

    private static final String ALL_USERS_QUERY = "SELECT * FROM users;";

    private static final String DB_NAME = "db_workshop2";


    public User create(User user) {
        if (isRecordInDB(user)) {
            System.out.println("Użytkownik nie został dodany do bazy danych.");
            return null;
        }
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
                PreparedStatement preparedStatement = db_workshop2_conn.prepareStatement(READ_USER_QUERY);
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

        public void update (User user){
            if (isRecordInDB(user)) {
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
            } else {
                System.out.println("Brak użytkownika w bazie danych.");
            }
        }

        public void delete ( int id){
            if (isRecordInDB(id)) {

                try (Connection db_workshop2_conn = DbUtil.conn(DB_NAME)) {
                    PreparedStatement preparedStatement = db_workshop2_conn.prepareStatement(DELETE_USER_QUERY);
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    System.out.println("Użytkownik o id: " + id + " został usunięty z bazy danych.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException("Nie udało połączyć się z bazą danych. " + ex);
                }
            } else {
                System.out.println("Nie udało się usunąć użytkownika o id: " + id + ". Nie było takiego id w bazie danych.");
            }

        }


        public User[] findAll () {
            try (Connection db_workshop2_conn = DbUtil.conn(DB_NAME)) {
                User[] users = new User[0];
                PreparedStatement preparedStatement = db_workshop2_conn.prepareStatement(ALL_USERS_QUERY);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName((resultSet.getString("name")));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    users = addNewToArray(user, users);

                }
                return users;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        private User[] addNewToArray (User user, User[]users){
            User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
            tmpUsers[users.length] = user;
            return tmpUsers;
        }


        public Boolean isRecordInDB (User user){
            boolean result = false;
            try (Connection db_workshop2_conn = DbUtil.conn(DB_NAME)) {
                PreparedStatement preparedStatement = db_workshop2_conn.prepareStatement(ALL_USERS_QUERY);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    try {
                        if (user.getEmail().equalsIgnoreCase(resultSet.getString("email")) || user.getId() == resultSet.getInt("id")) {
                            result = true;
                        }
                    } catch (NullPointerException ex) {
                        result = false;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Nie udało połączyć się z bazą danych. " + ex);
            }
            return result;
        }

        public Boolean isRecordInDB ( int userId){
            return isRecordInDB(read(userId));
        }


    }

