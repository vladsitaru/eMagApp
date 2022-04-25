package DB;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;

public class DBProduct {

    public int listAllProducts () {

        // 1. ma conectez la db
        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id =-1;
        int itemCount = 0;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. fac un query pe o tabela , intai creez obiectul



            PreparedStatement pSt = conn.prepareStatement("select id, productname, price, isonstock, category from products order by id ASC");

            // 3. executie
            ResultSet rs = pSt.executeQuery();


            // atata timp cat am randuri
            System.out.println("ID |     Products           |  Price  | Stock status |  Category");
            System.out.println("---|------------------------|---------|--------------|----------");

            while (rs.next()) {
                printOutAllResults(rs);
                itemCount++;
            }
            System.out.println("---|------------------------|---------|--------------|----------");

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return itemCount;
    }

    public HashMap listProductsByCategory () {

        // map pentru stocarea id-ului de produs cu cel afisat in query
        HashMap productIdMap = new HashMap();

        // 1. ma conectez la db
        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id =-1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            PreparedStatement pStCategories = conn.prepareStatement("select distinct category from products");
            ResultSet rsCategories = pStCategories.executeQuery();

            int i = 1;
            while(rsCategories.next()){
                    System.out.print("[" + i + "] " + rsCategories.getString("category").trim() + " | ");
                    i++;
            }
            System.out.println();

            pStCategories.close();

            System.out.println("Type in the category: ");
            String category = new Scanner(System.in).nextLine();
            System.out.println("Showing query results for category: " + "'"+category+"'");

            PreparedStatement pSt = conn.prepareStatement("select * from products where category=?");

            pSt.setString(1,category);
            // 3. executie
            ResultSet rs = pSt.executeQuery();

            System.out.println();
            System.out.println("---|------------------------|---------|--------------|----------");
            System.out.println("ID |     Products           |  Price  | Stock status |  Category");
            System.out.println("---|------------------------|---------|--------------|----------");
            // atata timp cat am randuri
            i = 1;
            while (rs.next()) {
                productIdMap.put(i, rs.getInt("id"));
                printOutQueryResults(rs, i);
                i++;
            }
            System.out.println("---|------------------------|---------|--------------|----------");

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return productIdMap;
    }

    public HashMap searchProductsByKeyword () {
        // map pentru stocarea id-ului de produs cu cel afisat in query
        HashMap productIdMap = new HashMap();

        // 1. ma conectez la db
        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id =-1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. fac un query pe o tabela , intai creez obiectul

            System.out.println("Enter the keyword: ");
            String keyword = new Scanner(System.in).nextLine();
            System.out.println("Showing query results for keyword: " + "'"+keyword+"'");

            String sql = "SELECT * FROM products WHERE productname LIKE " + "'%"+keyword+"%'" + " OR productname LIKE" + "'%"+keyword+"'" + "OR productname LIKE " + "'"+keyword+"%'";

            PreparedStatement pSt = conn.prepareStatement(sql);

//            pSt.setString(1,"'%"+keyword+"%'");
            // 3. executie
            ResultSet rs = pSt.executeQuery();


            // atata timp cat am randuri
            int i = 1;

            System.out.println();
            System.out.println("---|------------------------|---------|--------------|----------");
            System.out.println("ID |     Products           |  Price  | Stock status |  Category");
            System.out.println("---|------------------------|---------|--------------|----------");
            while (rs.next()) {
                productIdMap.put(i, rs.getInt("id"));
                printOutQueryResults(rs, i);
                i++;
            }
            System.out.println("---|------------------------|---------|--------------|----------");

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return productIdMap;
    }

    public HashMap listAllProductsByPriceASC() {

        HashMap productIdMap = new HashMap();
        // 1. ma conectez la db
        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id =-1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. fac un query pe o tabela , intai creez obiectul

            PreparedStatement pSt = conn.prepareStatement("select * from products ORDER BY price ASC");

            // 3. executie
            ResultSet rs = pSt.executeQuery();


            // atata timp cat am randuri
            int i = 1;

            System.out.println();
            System.out.println("---|------------------------|---------|--------------|----------");
            System.out.println("ID |     Products           |  Price  | Stock status |  Category");
            System.out.println("---|------------------------|---------|--------------|----------");
            while (rs.next()) {
                productIdMap.put(i, rs.getInt("id"));
                printOutQueryResults(rs, i);
                i++;
            }
            System.out.println("---|------------------------|---------|--------------|----------");

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return productIdMap;
    }

    public HashMap listAllProductsByPriceDESC() {

        HashMap productIdMap = new HashMap();
        // 1. ma conectez la db
        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id =-1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. fac un query pe o tabela , intai creez obiectul

            PreparedStatement pSt = conn.prepareStatement("select * from products ORDER BY price DESC");

            // 3. executie
            ResultSet rs = pSt.executeQuery();


            // atata timp cat am randuri
            int i = 1;

            System.out.println();
            System.out.println("---|------------------------|---------|--------------|----------");
            System.out.println("ID |     Products           |  Price  | Stock status |  Category");
            System.out.println("---|------------------------|---------|--------------|----------");
            while (rs.next()) {
                productIdMap.put(i, rs.getInt("id"));
                printOutQueryResults(rs, i);
                i++;
            }
            System.out.println("---|------------------------|---------|--------------|----------");

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return productIdMap;
    }

    public HashMap listAllProductsByNameASC() {

        HashMap productIdMap = new HashMap();
        // 1. ma conectez la db
        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id =-1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. fac un query pe o tabela , intai creez obiectul

            PreparedStatement pSt = conn.prepareStatement("select * from products ORDER BY productname ASC");

            // 3. executie
            ResultSet rs = pSt.executeQuery();


            // atata timp cat am randuri
            int i = 1;

            System.out.println();
            System.out.println("---|------------------------|---------|--------------|----------");
            System.out.println("ID |     Products           |  Price  | Stock status |  Category");
            System.out.println("---|------------------------|---------|--------------|----------");
            while (rs.next()) {
                productIdMap.put(i, rs.getInt("id"));
                printOutQueryResults(rs, i);
                i++;
            }
            System.out.println("---|------------------------|---------|--------------|----------");

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return productIdMap;
    }

    public HashMap listAllProductsByNameDESC() {

        HashMap productIdMap = new HashMap();
        // 1. ma conectez la db
        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id =-1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. fac un query pe o tabela , intai creez obiectul

            PreparedStatement pSt = conn.prepareStatement("select * from products ORDER BY productname DESC");

            // 3. executie
            ResultSet rs = pSt.executeQuery();


            // atata timp cat am randuri
            int i = 1;

            System.out.println();
            System.out.println("---|------------------------|---------|--------------|----------");
            System.out.println("ID |     Products           |  Price  | Stock status |  Category");
            System.out.println("---|------------------------|---------|--------------|----------");
            while (rs.next()) {
                productIdMap.put(i, rs.getInt("id"));
                printOutQueryResults(rs, i);
                i++;
            }
            System.out.println("---|------------------------|---------|--------------|----------");

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return productIdMap;
    }

    public HashMap searchKeywordProductsByKeyword (String keyword) {
        // map pentru stocarea id-ului de produs cu cel afisat in query
        HashMap productIdMap = new HashMap();



        // 1. ma conectez la db
        final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
        final String USERNAME = "ftuser";
        final String PASSWORD = System.getenv("PWD");
        int id =-1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            String sql = "SELECT * FROM products WHERE productname LIKE " + "'%"+keyword+"%'" + " OR productname LIKE" + "'%"+keyword+"'" + "OR productname LIKE " + "'"+keyword+"%'";

            PreparedStatement pSt = conn.prepareStatement(sql);

//            pSt.setString(1,"'%"+keyword+"%'");
            // 3. executie
            ResultSet rs = pSt.executeQuery();


            // atata timp cat am randuri
            int i = 1;

            System.out.println();
            System.out.println("---|------------------------|---------|--------------|----------");
            System.out.println("ID |     Products           |  Price  | Stock status |  Category");
            System.out.println("---|------------------------|---------|--------------|----------");
            while (rs.next()) {
                productIdMap.put(i, rs.getInt("id"));
                printOutQueryResults(rs, i);
                i++;
            }
            System.out.println("---|------------------------|---------|--------------|----------");

            rs.close();
            pSt.close();
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return productIdMap;
    }

    public static boolean addProduct() {

        Scanner sc = new Scanner(System.in);

        boolean isInserted=false;
        try {
            boolean isOnStock = false;
            boolean correctIsOnStockInput = false;
            // 1. ma conectez la db
            final String URL = "jdbc:postgresql://idc.cluster-custom-cjcsijnttbb2.eu-central-1.rds.amazonaws.com:5432/vladsitaru";
            final String USERNAME = "ftuser";

            final String PASSWORD = System.getenv("PWD");


            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 2. creez un prepared statement si il populez cu date
            PreparedStatement pSt = conn.prepareStatement("INSERT INTO products (productname, price, isonstock, category) VALUES(?,?,?,?)");

            System.out.println("Enter the product name: ");
            String productname = new Scanner(System.in).nextLine();
            pSt.setString(1,productname);

            double price = 0;
            boolean isRightDoubleInput = false;
            while(isRightDoubleInput == false){
            try{
                System.out.println("Enter the price: ");
                price = new Scanner(System.in).nextDouble();
                pSt.setDouble(2,price);
                isRightDoubleInput = true;
            }
            catch(InputMismatchException e) {
                System.out.println("Invalid input type - price can only be entered in digits");
                isRightDoubleInput = false;
                }
            }


            while(correctIsOnStockInput == false) {
                System.out.println("Is the product on stock? (Y/N)");
                String isOnStockString = new Scanner(System.in).nextLine();

                if (isOnStockString.equalsIgnoreCase("Y")) {
                    isOnStock = true;
                    correctIsOnStockInput = true;
                } else if (isOnStockString.equalsIgnoreCase("N")) {
                    isOnStock = false;
                    correctIsOnStockInput = true;
                } else System.out.println("Invalid input - try again");
            }
            pSt.setBoolean(3, isOnStock);

            System.out.println("Enter the product category: ");
            String category = new Scanner(System.in).nextLine();
            pSt.setString(4,category);



            // 3. executie
            int insert = pSt.executeUpdate();
            if(insert!=-1) {
                isInserted = true;
                System.out.println("Product " + productname + " of price " + price + " and category \"" + category + "\" has been successfully added to the catalog.");
            }
            pSt.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            isInserted=false;

        }

        return isInserted;
    }

    public void printOutAllResults(ResultSet rs) throws SQLException {
        StringBuffer priceLengthBuffer = new StringBuffer();
        for (int i = 0; i < 7-rs.getString("price").length(); i++){
            priceLengthBuffer = priceLengthBuffer.append(" ");
        }

        StringBuffer onStockString = new StringBuffer();
        if(rs.getBoolean("isonstock")==true){
            onStockString = onStockString.append("On stock    ");
        }
        else onStockString = onStockString.append("Not on stock");


        int idDigits = rs.getInt("id");

        if (idDigits < 10) {
            System.out.println(rs.getInt("id") +  "  | " + rs.getString("productname").substring(0,22) + " | " + rs.getString("price")+priceLengthBuffer + " | " + onStockString + " | " + rs.getString("category").trim());
        }
        else if (idDigits < 100) {
            System.out.println(rs.getInt("id") +  " | " + rs.getString("productname").substring(0,22) + " | " + rs.getString("price")+priceLengthBuffer + " | " + onStockString  + " | " + rs.getString("category").trim());

        }
        else
            System.out.println(rs.getInt("id") +  " | " + rs.getString("productname").substring(0,22) + " | " + rs.getString("price")+priceLengthBuffer + " | " + onStockString  + " | " + rs.getString("category").trim());

    }

    public void printOutQueryResults(ResultSet rs, int i) throws SQLException {
        StringBuffer priceLengthBuffer = new StringBuffer();
        for (int j = 0; j < 7-rs.getString("price").length(); j++){
            priceLengthBuffer = priceLengthBuffer.append(" ");
        }

        StringBuffer onStockString = new StringBuffer();
        if(rs.getBoolean("isonstock")==true){
            onStockString = onStockString.append("On stock    ");
        }
        else onStockString = onStockString.append("Not on stock");



        if (i < 10) {
            System.out.println(i +  "  | " + rs.getString("productname").substring(0,22) + " | " + rs.getString("price")+priceLengthBuffer + " | " + onStockString + " | " + rs.getString("category").trim());
        }
        else if (i < 100) {
            System.out.println(i +  " | " + rs.getString("productname").substring(0,22) + " | " + rs.getString("price")+priceLengthBuffer + " | " + onStockString  + " | " + rs.getString("category").trim());

        }
        else
            System.out.println(i +  " | " + rs.getString("productname").substring(0,22) + " | " + rs.getString("price")+priceLengthBuffer + " | " + onStockString  + " | " + rs.getString("category").trim());

    }
}
