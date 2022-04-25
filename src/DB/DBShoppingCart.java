package DB;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;

public class DBShoppingCart {

    // add product to shopping cart if the product is on stock, under userId
    public static boolean addProductToCartById(int id, int userId, int nrOfItems) {

        boolean isInserted = false;
        try {
            // 1. ma conectez la db
            final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
            final String USERNAME = "ftuser";

            final String PASSWORD = System.getenv("PWD");

            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            //fetch product by id parameter

            PreparedStatement pStCheckIfProductIdIsOnStock = conn.prepareStatement("select productname, price, isonstock from products where id=?");
            pStCheckIfProductIdIsOnStock.setInt(1, id);

            ResultSet rs = pStCheckIfProductIdIsOnStock.executeQuery();
            while (rs.next()) {
                //if the product is on stock
                if (rs.getBoolean("isonstock") == true) {
                    PreparedStatement pSt = conn.prepareStatement("INSERT INTO shoppingcart (userid, productname, price) VALUES(?,?,?)");
                    for (int i = 0; i < nrOfItems; i++) {
                        // add it to the shopping cart under the userid

                        pSt.setInt(1, userId);
                        pSt.setString(2, rs.getString("productname"));
                        pSt.setDouble(3, rs.getDouble("price"));

                        // execute query
                        int insert = pSt.executeUpdate();
                        if (insert != -1)
                            isInserted = true;
                    }
                    System.out.println(nrOfItems + " x " + rs.getString("productname").trim() + " of total price " + rs.getDouble("price") * nrOfItems + " have been added to your cart.");
                    pSt.close();
                }
                //if the product is not on stock, display message
                else System.out.println("Sorry, the product is not on stock");
            }

            pStCheckIfProductIdIsOnStock.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {

            e.printStackTrace();
            isInserted = false;

        }
        return isInserted;
    }

    //show shopping cart content for User u
    public static HashMap<Integer, String> showCart(User u) {


        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id = -1;
        double orderSum = 0;
        int numarInCos = 1;

        HashMap<Integer, String> productNameAndIdMatch = new HashMap<Integer, String>(); //folosit pentru a stoca produsul si pozitia lor ca numar in cos
        HashSet<String> setProductName = new HashSet<String>(); // folosit pentru a stoca toate produsele unice
        List<String> list = new ArrayList<String>(); // folosit pentru a stoca toate produsele
        HashMap<String, Integer> map = new HashMap<String, Integer>(); // folosit pentru a stoca produsele unice si cantiatea lor din cos
        HashMap<String, Double> doubleMap = new HashMap<String, Double>(); // folosit ca dictionar intre produse si pretul lor

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            //show all items in shopping cart for userId
            PreparedStatement pSt = conn.prepareStatement("select productname, price from shoppingcart where userid=?");


            pSt.setInt(1, u.getId());

            ResultSet rs = pSt.executeQuery();



            while (rs.next()) {
                setProductName.add(rs.getString("productname"));
                doubleMap.put(rs.getString("productname"), rs.getDouble("price"));
                list.add(rs.getString("productname"));
            }

            Object[] setProductNameArray = setProductName.toArray(); // convertim setul de produse unice in array de tip Object ulterior sunt convertite in String
            Object[] listArray = list.toArray(); // la fel cu lista de produse
            String productNameSQLEntry = ""; // String folosit pentru comanda pe care o vom transmite lui SQL prin JDBC pentru inserare in coloana "products"

            System.out.println("Viewing the shopping cart for " + u.getUsername());
            System.out.println("------------------------------");

            Integer matchCount = 1;
            //trecem prin fiecare produs unic din set
            for (int i = 0; i < setProductNameArray.length; i++) {
                for (int j = 0; j < listArray.length; j++) {

                    if (setProductNameArray[i].equals(listArray[j])) {     //comparam fiecare produs unic cu numarul lor in lista
                        matchCount++; // pentru fiecare match +1
                    }
                    if (j == list.size() - 1) { // la ultimul produs verificat din lista, inainte sa intre pe urmatoarea iteratie de i
                        map.put((String) setProductNameArray[i], matchCount); // introducem in map numele produsului si cantitatea adaugata in coș
                        if (i == 0)
                            productNameSQLEntry = setProductNameArray[i].toString().trim() + " x " + matchCount + " = " + Math.multiplyExact((int) (double) (doubleMap.get((String) setProductNameArray[i])), matchCount);
                        else
                            productNameSQLEntry = productNameSQLEntry + "\n" + setProductNameArray[i].toString().trim() + " x " + matchCount + " = " + Math.multiplyExact((int) (double) (doubleMap.get((String) setProductNameArray[i])), matchCount);
                        System.out.println(numarInCos + ". " + setProductNameArray[i].toString().trim() + " x " + matchCount + " = " + Math.multiplyExact((int) (double) (doubleMap.get((String) setProductNameArray[i])), matchCount));
                        orderSum = orderSum + Math.multiplyExact((int) (double) (doubleMap.get((String) setProductNameArray[i])), matchCount);
                        productNameAndIdMatch.put(numarInCos,setProductNameArray[i].toString().trim());
                        numarInCos++;
                        matchCount = 0;
                    }
                }
            }
            System.out.println("------------------------------");
            System.out.println("Total: " + orderSum);
            System.out.println("------------------------------");

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return productNameAndIdMatch;
    }

    public static void checkOut(User u) {

        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id = -1;
        boolean isInserted = false;

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. fac un query pe o tabela , intai creez obiectul


            PreparedStatement pStExtract = conn.prepareStatement("select * from shoppingcart where userid=?");

            pStExtract.setInt(1, u.getId());

            ResultSet rs = pStExtract.executeQuery();

            ///
            LocalDate l = LocalDate.now();
            double orderSum = 0;
            HashSet<String> setProductName = new HashSet<String>(); // folosit pentru a stoca toate produsele unice
            List<String> list = new ArrayList<String>(); // folosit pentru a stoca toate produsele
            HashMap<String, Integer> map = new HashMap<String, Integer>(); // folosit pentru a stoca produsele unice si cantiatea lor din cos
            HashMap<String, Double> doubleMap = new HashMap<String, Double>(); // folosit ca dictionar intre produse si pretul lor
            ///


            while (rs.next()) {
                setProductName.add(rs.getString("productname"));
                doubleMap.put(rs.getString("productname"), rs.getDouble("price"));
                list.add(rs.getString("productname"));
            }

            Object[] setProductNameArray = setProductName.toArray(); // convertim setul de produse unice in array de tip Object ulterior sunt convertite in String
            Object[] listArray = list.toArray(); // la fel cu lista de produse
            String productNameSQLEntry = ""; // String folosit pentru comanda pe care o vom transmite lui SQL prin JDBC pentru inserare in coloana "products"

            //obtinem ultimul numar de order pentru a calcula order-ul curent
            PreparedStatement pStExtractLastOrderId = conn.prepareStatement("select MAX(orderid) from orders");
            ResultSet rsExtractLastOrderId = pStExtractLastOrderId.executeQuery();

            while (rsExtractLastOrderId.next()) {
                System.out.println("Placing order number " + Math.addExact(rsExtractLastOrderId.getInt("max"), 1));
            }
            System.out.println("------------------------------");

            Integer matchCount = 0;
            //trecem prin fiecare produs unic din set
            for (int i = 0; i < setProductNameArray.length; i++) {
                for (int j = 0; j < listArray.length; j++) {

                    if (setProductNameArray[i].equals(listArray[j])) {     //comparam fiecare produs unic cu numarul lor in lista
                        matchCount++; // pentru fiecare match +1
                    }
                    if (j == list.size() - 1) { // la ultimul produs verificat din lista, inainte sa intre pe urmatoarea iteratie de i
                        map.put((String) setProductNameArray[i], matchCount); // introducem in map numele produsului si cantitatea adaugata in coș
                        if (i == 0)
                            productNameSQLEntry = setProductNameArray[i].toString().trim() + " x " + matchCount + " = " + Math.multiplyExact((int) (double) (doubleMap.get((String) setProductNameArray[i])), matchCount);
                        else
                            productNameSQLEntry = productNameSQLEntry + "\n" + setProductNameArray[i].toString().trim() + " x " + matchCount + " = " + Math.multiplyExact((int) (double) (doubleMap.get((String) setProductNameArray[i])), matchCount);
                        System.out.println(setProductNameArray[i].toString().trim() + " x " + matchCount + " = " + Math.multiplyExact((int) (double) (doubleMap.get((String) setProductNameArray[i])), matchCount));
                        orderSum = orderSum + Math.multiplyExact((int) (double) (doubleMap.get((String) setProductNameArray[i])), matchCount); //
                        matchCount = 0;
                    }
                }
            }
            System.out.println("------------------------------");
            System.out.println("Total: " + orderSum);
            System.out.println("------------------------------");


            PreparedStatement pStInsert = conn.prepareStatement("INSERT INTO orders (userid, products, total, date) VALUES(?,?,?,?)");
            pStInsert.setInt(1, u.getId());
            pStInsert.setString(2, productNameSQLEntry);
            pStInsert.setDouble(3, orderSum);
            pStInsert.setDate(4, Date.valueOf(l));

            // execute query
            int insert = pStInsert.executeUpdate();
            if (insert != -1)
                isInserted = true;
            System.out.println("Your order has been processed!");
            pStInsert.close();

            rs.close();
            pStInsert.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void clearCart(User u) {


        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id = -1;
        boolean isInserted = false;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            //show all items in shopping cart for userId
            PreparedStatement pSt = conn.prepareStatement("delete from shoppingcart where userid=?");

            pSt.setInt(1, u.getId());
            // 3. executie

            int insert = pSt.executeUpdate();
            if (insert != -1)
                isInserted = true;
            System.out.println("Your cart has been cleared");


            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void deleteItemFromCart(User u, String productname) {

        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id = -1;
        boolean isInserted = false;

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            PreparedStatement pSt = conn.prepareStatement("delete from shoppingcart where userid=? and productname=?");

            pSt.setInt(1, u.getId());
            pSt.setString(2,productname);

            int insert = pSt.executeUpdate();
            if (insert != -1)
                isInserted = true;
            System.out.println("Item " + productname + " has been deleted");


            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void addItemFromCart(User u, String productname, int quantity) {

        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id = -1;
        boolean isInserted = false;

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            PreparedStatement pStExtract = conn.prepareStatement("select max(price) from shoppingcart where productname=?");

            pStExtract.setString(1, productname);

            ResultSet rs = pStExtract.executeQuery();


            while(rs.next()){
                for (int i = 0; i < quantity; i++) {
                PreparedStatement pSt = conn.prepareStatement("insert into shoppingcart (userid, productname, price) values(?,?,?)");

                pSt.setInt(1, u.getId());
                pSt.setString(2,productname);
                pSt.setDouble(3,rs.getDouble("max"));
                int insert = pSt.executeUpdate();
                if (insert != -1)
                    isInserted = true;
                    pSt.close();
                }
                System.out.println("Item " + productname + " x " + quantity + " added to cart");
            }
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void removeItemFromCart(User u, String productname, int quantity) {

        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id = -1;
        boolean isInserted = false;

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            PreparedStatement pStExtract = conn.prepareStatement("select * from shoppingcart where productname=?");

            pStExtract.setString(1, productname);

            ResultSet rs = pStExtract.executeQuery();
///

            PreparedStatement pStCount = conn.prepareStatement("SELECT COUNT(*) FROM shoppingcart WHERE userid=? and productname=?");

            pStCount.setInt(1, u.getId());
            pStCount.setString(2,productname);

            ResultSet rsCount = pStCount.executeQuery();

            int objectCountsInRs = 0;

            while(rsCount.next())
                objectCountsInRs = rsCount.getInt("count");


            if(quantity>objectCountsInRs)
                System.out.println("That's more than the number of items in the basket");
            else if(quantity == objectCountsInRs) {

                PreparedStatement pSt = conn.prepareStatement("delete from shoppingcart where userid=? and productname=?");

                pSt.setInt(1, u.getId());
                pSt.setString(2,productname);


                int insert = pSt.executeUpdate();
                if (insert != -1)
                    isInserted = true;
                System.out.println("Item " + productname + " has been deleted from the cart");
                pSt.close();
            }
            else if (quantity < objectCountsInRs){
                int i = 0;
                while(rs.next() && i < quantity)
                {
                    PreparedStatement pSt = conn.prepareStatement("delete from shoppingcart where userid=? and productname=? and basketid=?");

                    pSt.setInt(1, u.getId());
                    pSt.setString(2,productname);
                    pSt.setInt(3,rs.getInt("basketid"));
                    int insert = pSt.executeUpdate();
                    if (insert != -1)
                        isInserted = true;

                    pSt.close();
                    i++;
                }
                System.out.println("Item " + productname + " x " + quantity + " removed from cart");
            }
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
