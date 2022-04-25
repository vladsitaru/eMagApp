package DB;

import java.sql.*;
import java.util.Scanner;

public class DBUser {

    public boolean newUser(User u) {

          boolean isInserted=false;
        try {
            // 1. ma conectez la db
            final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
            final String USERNAME = "ftuser";

            final String PASSWORD = "FastTrack&2022NEW";


            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. creez un prepared statement si il populez cu date
            PreparedStatement pSt = conn.prepareStatement("INSERT INTO users (username, password, admin) VALUES(?,?,?)");
            pSt.setString(1,u.getUsername());
            pSt.setString(2,u.getPassword());
            pSt.setBoolean(3,u.getIsAdmin());



            // 3. executie
            int insert = pSt.executeUpdate();
            if(insert!=-1)
                isInserted=true;

            pSt.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
            isInserted=false;

        }


        return isInserted;
    }

    public User login (String username, String password) {

        User u = null;
        // 1. ma conectez la db
        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id =-1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. fac un query pe o tabela , intai creez obiectul



            PreparedStatement pSt = conn.prepareStatement("select id, username, password from users where username=? and password=?");

            pSt.setString(1,username);
            pSt.setString(2,password);


            // 3. executie
            ResultSet rs = pSt.executeQuery();


            // atata timp cat am randuri
            while (rs.next()) {

                u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));

            }

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return u;
    }

    public static void newUser() {
        DBUser u = new DBUser();
        Scanner sc = new Scanner(System.in);
        System.out.println("Username:");
        String username = sc.nextLine();

        System.out.println("Password:");
        String pwd = sc.nextLine();

        System.out.println("Opt for offer? (true/false):");
        boolean offer = sc.nextBoolean();

        u.newUser(new User(username, pwd, offer));
    }
    public static User login () {
        DBUser u = new DBUser();
        Scanner sc = new Scanner(System.in);
        System.out.println("Username:");
        String username = sc.nextLine();

        System.out.println("Password:");
        String pwd = sc.nextLine();

        return u.login(username, pwd);
    }
    public static boolean checkIfAdmin(User u) {

        boolean isAdmin = false;
        // 1. ma conectez la db
        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = "FastTrack&2022NEW";
        int id =-1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. fac un query pe o tabela , intai creez obiectul



            PreparedStatement pSt = conn.prepareStatement("select * from users where username=? and password=?");

            pSt.setString(1,u.getUsername());
            pSt.setString(2,u.getPassword());


            // 3. executie
            ResultSet rs = pSt.executeQuery();


            // atata timp cat am randuri
            while (rs.next()) {
                if(rs.getBoolean("admin")==true) {
                    isAdmin = true;
                }
            }

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return isAdmin;
    }
}
