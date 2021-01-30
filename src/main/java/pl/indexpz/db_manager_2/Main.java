package pl.indexpz.db_manager_2;

import pl.indexpz.db_manager_2.entity.User;
import pl.indexpz.db_manager_2.entity.UserDao;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        User pawel = new User();
        pawel.setName("Paweł");
        pawel.setEmail("pawel@gmail.com");
        pawel.setPassword("żyrafyDoSzafy");
        userDao.create(pawel);

        User beata = new User();
        beata.setName("Beata");
        beata.setEmail("beata@gmail.com");
        beata.setPassword("krowaJeTrawe");
        userDao.create(beata);

        User zuzia = new User();
        zuzia.setName("Zuzia");
        zuzia.setEmail("zusia@gmail.com");
        zuzia.setPassword("alaMaKota");
        userDao.create(zuzia);

        User userUpdate = userDao.read(6);
        userUpdate.setName("Zuzanna");
        userUpdate.setEmail("zuzanna@wp.pl");
        userUpdate.setPassword("kora123");
        userDao.update(userUpdate);

        User userDelete = userDao.read(8);
        userDao.delete(8);
        System.out.println("----------------------");
        UserDao userDaoRead = new UserDao();
        System.out.println(userDaoRead.read(1));
        System.out.println(userDaoRead.read(3));
        System.out.println(userDaoRead.read(4));
        System.out.println(userDaoRead.read(10));
        System.out.println("----------------------");

        User[] allUsers = userDao.findAll();
        for (User user: allUsers) {
            System.out.println(user);
        }

        System.out.println(userDao.isRecordInDB(pawel));
        System.out.println(userDao.isRecordInDB(userDao.read(3)));
        System.out.println(userDao.isRecordInDB(userDao.read(4)));

    }
}
