import DB.DBProduct;
import DB.DBShoppingCart;
import DB.DBUser;
import DB.User;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AppBusinessLogic {

    public static void appStart() {

        boolean exitAppStart = false;
        int startOption = 0;

        while (!exitAppStart) {
            if (startOption >= 0 && startOption < 4) {
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
            }
            startOption = -1;
            while (startOption == -1) {

                try {
                    startOption = new Scanner(System.in).nextInt();
                    if (startOption == -1)
                        throw new InputMismatchException();
                } catch (InputMismatchException e) {
                    System.out.println("Wrong input - try again");
                }
            }

            switch (startOption) {
                case 1:
                    System.out.println("Username: ");
                    String username = new Scanner(System.in).nextLine();

                    System.out.println("Password: ");
                    String pwd = new Scanner(System.in).nextLine();

                    DBUser u = new DBUser();

                    if (u.login(username, pwd) != null) {
                        System.out.println("Logged in");
                        exitAppStart = true;
                        loggedInInterface(u.login(username, pwd));
                    } else System.out.println("Wrong credentials");
                    break;

                case 2:
                    System.out.println("Username: ");
                    username = new Scanner(System.in).nextLine();

                    System.out.println("Password: ");
                    pwd = new Scanner(System.in).nextLine();

                    boolean isAdmin = false;
                    System.out.println("Set admin privilege? (Y/N): ");

                    if (new Scanner(System.in).nextLine().equalsIgnoreCase("Y"))
                        isAdmin = true;

                    u = new DBUser();
                    if (u.newUser(new User(username, pwd, isAdmin)) == true)
                        System.out.println("User " + username + " registered successfully");
                    else System.out.println("Error: User already exists");
                    break;

                case 3:
                    exitAppStart = true;
                    System.out.println("Exiting application");
                    break;
                default:
                    System.out.println("Wrong input - try again");
                    break;

            }
        }
    }

    private static void loggedInInterface(User u) {
        boolean loggedIn = true;
        int option = 0;
        boolean noError = true;
        DBProduct p = new DBProduct();

        System.out.println("Welcome, you are now logged in as " + u.getUsername());

        while (loggedIn) {
            if (option >= 0 && option < 7) {
                System.out.println("1. List all products");
                System.out.println("2. Search products by keyword");
                System.out.println("3. Search products by category");
                System.out.println("4. View your shopping cart/checkout");
                System.out.println("5. Add a product to the database [admin only]");
                System.out.println("6. Sign off");
            }

            option = -1;
            while (option == -1) {
                try {
                    System.out.println("Select from the menu:");
                    option = new Scanner(System.in).nextInt();
                    if (option == -1)
                        throw new InputMismatchException();
                } catch (InputMismatchException e) {
                    System.out.println("Wrong input - try again");
                }
            }

            switch (option) {
                case 1:
                    boolean doneShopping = false;
                    int listProductMenuOption = 0;
                    int itemCount = 0;

                    while (!doneShopping) {

                        itemCount = p.listAllProducts();
                        System.out.println("(1) Add to cart, (2) Sort by price ASC, (3) Sort by price DESC, (4) Sort by name ASC, (5) Sort by name DESC, (6) Return to the main menu");

                        listProductMenuOption = -1;

                        while (listProductMenuOption == -1) {
                            try {
                                listProductMenuOption = new Scanner(System.in).nextInt();
                                if (listProductMenuOption <= 0 || listProductMenuOption > 6)
                                    throw new InputMismatchException();
                            } catch (InputMismatchException e) {
                                System.out.println("Wrong input - try again");
                                listProductMenuOption = -1;
                            }
                        }

                        switch (listProductMenuOption) {
                            case 1:

                                int productId = -1;
                                int nrOfItems = -1;
                                doneShopping = false;
                                noError = false;

                                while (productId == -1) {
                                    try {
                                        System.out.println("Enter the product id to add to cart: ");
                                        productId = new Scanner(System.in).nextInt();
                                        if (productId < 1 || productId > itemCount) {
                                            throw new InputMismatchException();

                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Wrong ID - try again");
                                        productId = -1;
                                    }
                                }
                                while (nrOfItems == -1) {
                                    try {
                                        System.out.println("How many items would you like to add?");
                                        nrOfItems = new Scanner(System.in).nextInt();
                                        if (nrOfItems <= 0)
                                            throw new InputMismatchException();
                                        DBShoppingCart.addProductToCartById(productId, u.getId(), nrOfItems);
                                    } catch (InputMismatchException e) {
                                        System.out.println("Wrong input - please try again");
                                        nrOfItems = -1;
                                    }

                                }
                                while (!doneShopping) {
                                    while (!noError) {
                                        noError = false;
                                        productId = -1;
                                        nrOfItems = -1;

                                        System.out.println("Would you like to make another purchase? (Y/N)");
                                        String makePurchase = new Scanner(System.in).nextLine();
                                        if (makePurchase.equalsIgnoreCase("N")) {
                                            System.out.println("Returning to the menu");
                                            doneShopping = true;
                                            noError = true;
                                        } else if (makePurchase.equalsIgnoreCase("Y")) {
                                            while (productId == -1) {
                                                try {
                                                    System.out.println("Enter the product id: ");
                                                    productId = new Scanner(System.in).nextInt();
                                                    if (productId < 1 || productId > itemCount)
                                                        throw new InputMismatchException();
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Wrong input - try again");
                                                    productId = -1;
                                                }
                                            }
                                            while (nrOfItems == -1) {
                                                try {
                                                    System.out.println("How many items would you like to add?");
                                                    nrOfItems = new Scanner(System.in).nextInt();
                                                    if (nrOfItems < 1)
                                                        throw new InputMismatchException();
                                                    DBShoppingCart.addProductToCartById(productId, u.getId(), nrOfItems);
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Wrong input - try again");
                                                    nrOfItems = -1;
                                                }
                                            }
                                        } else System.out.println("Wrong input - try again");
                                    }
                                }
                                break;
                            case 2:
                                HashMap productIdMap = p.listAllProductsByPriceASC();

                                if (wouldYouLikeToMakeAPurchase(productIdMap, u))
                                    wouldYouLikeToMakeAnotherPurchase(productIdMap, u);
                                break;

                            case 3:
                                productIdMap = p.listAllProductsByPriceDESC();

                                if (wouldYouLikeToMakeAPurchase(productIdMap, u))
                                    wouldYouLikeToMakeAnotherPurchase(productIdMap, u);
                                break;

                            case 4:
                                productIdMap = p.listAllProductsByNameASC();

                                if (wouldYouLikeToMakeAPurchase(productIdMap, u))
                                    wouldYouLikeToMakeAnotherPurchase(productIdMap, u);
                                break;

                            case 5:
                                productIdMap = p.listAllProductsByNameDESC();

                                if (wouldYouLikeToMakeAPurchase(productIdMap, u))
                                    wouldYouLikeToMakeAnotherPurchase(productIdMap, u);
                                break;

                            case 6:
                                doneShopping = true;
                                break;
                            default:
                                doneShopping = true;
                                break;
                        }
                    }
                    break;

                case 2:
                    p = new DBProduct();
                    HashMap productIdMap = p.searchProductsByKeyword();
                    if (!productIdMap.isEmpty()) {
//                        pickFromProductList(productIdMap,u);
                        if (wouldYouLikeToMakeAPurchase(productIdMap, u))
                            wouldYouLikeToMakeAnotherPurchase(productIdMap, u);
                    } else System.out.println("Keyword not found");
                    break;
                case 3:
                    p = new DBProduct();
                    productIdMap = p.listProductsByCategory();
                    if (!productIdMap.isEmpty()) {
//                        pickFromProductList(productIdMap,u);
                        if (wouldYouLikeToMakeAPurchase(productIdMap, u))
                            wouldYouLikeToMakeAnotherPurchase(productIdMap, u);
                    } else System.out.println("Category not found");
                    break;
                case 4:

                    boolean exitCart = false;
                    int cartOption = -1;
                    HashMap<Integer, String> productNameAndIdMatch = DBShoppingCart.showCart(u);

                    while (exitCart == false) {
                        System.out.println("(1) Delete item from cart | (2) Add item to cart | (3) Remove item from cart | (4) Checkout | (5) Show cart | (6) Return to the menu");
                        try {
                            cartOption = new Scanner(System.in).nextInt();
                        } catch (InputMismatchException e) {
                        }

                        switch (cartOption) {
                            case 1:
                                int selectItemNr = -1;
                                if (!productNameAndIdMatch.isEmpty()) {
                                    while (selectItemNr == -1) {
                                        try {
                                            System.out.println("Which item to delete?");
                                            selectItemNr = new Scanner(System.in).nextInt();
                                            if (selectItemNr < 1 || selectItemNr > productNameAndIdMatch.size())
                                                throw new InputMismatchException();
                                            DBShoppingCart.deleteItemFromCart(u, (String) (productNameAndIdMatch.get((Object) selectItemNr)));
                                        } catch (InputMismatchException e) {
                                            System.out.println("Wrong input - try again");
                                            selectItemNr = -1;
                                        }
                                    }
                                } else System.out.println("Cart empty - cannot delete items");
                                break;
                            case 2:

                                selectItemNr = -1;
                                int nrOfItemsToAdd = -1;

                                if (!productNameAndIdMatch.isEmpty()) {
                                    while (selectItemNr == -1) {
                                        try {
                                            System.out.println("Select the item: ");
                                            selectItemNr = new Scanner(System.in).nextInt();
                                            if (selectItemNr < 1 || selectItemNr > productNameAndIdMatch.size())
                                                throw new InputMismatchException();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Wrong input - try again");
                                            selectItemNr = -1;
                                        }
                                    }
                                    while (nrOfItemsToAdd == -1) {
                                        try {
                                            System.out.println("How many items to add to the cart?");
                                            nrOfItemsToAdd = new Scanner(System.in).nextInt();
                                            if (nrOfItemsToAdd < 1)
                                                throw new InputMismatchException();
                                            DBShoppingCart.addItemFromCart(u, (String) (productNameAndIdMatch.get((Object) selectItemNr)), nrOfItemsToAdd);
                                        } catch (InputMismatchException e) {
                                            System.out.println("Wrong input - try again");
                                            nrOfItemsToAdd = -1;
                                        }
                                    }
                                } else
                                    System.out.println("Cart empty - cannot add items if they are not placed in cart");
                                break;
                            case 3:
                                selectItemNr = -1;
                                int nrOfItemsToRemove = -1;

                                if (!productNameAndIdMatch.isEmpty()) {
                                    while (selectItemNr == -1) {
                                        try {
                                            System.out.println("Select the item to remove: ");
                                            selectItemNr = new Scanner(System.in).nextInt();
                                            if (selectItemNr < 1 || selectItemNr > productNameAndIdMatch.size())
                                                throw new InputMismatchException();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Wrong input - try again");
                                            selectItemNr = -1;
                                        }
                                        try {
                                            System.out.println("How many items to remove to the cart?");
                                            nrOfItemsToRemove = new Scanner(System.in).nextInt();
                                            if (nrOfItemsToRemove < 1)
                                                throw new InputMismatchException();
                                            DBShoppingCart.removeItemFromCart(u, (String) (productNameAndIdMatch.get((Object) selectItemNr)), nrOfItemsToRemove);
                                        } catch (InputMismatchException e) {
                                            System.out.println("Wrong input - try again");
                                            selectItemNr = -1;
                                        }

                                    }
                                } else System.out.println("Cart empty - cannot remove items");
                                break;
                            case 4:
                                if (!productNameAndIdMatch.isEmpty()) {
                                    DBShoppingCart.checkOut(u);
                                    DBShoppingCart.clearCart(u);
                                } else System.out.println("Cannot checkout empty cart");
                                break;

                            case 5:
                                DBShoppingCart.showCart(u);
                                break;
                            case 6:
                                exitCart = true;
                                break;
                            default:
                                System.out.println("Invalid option, try again");
                                break;
                        }
                    }
                    break;
                case 5:
                    if (DBUser.checkIfAdmin(u) == true) {
                        DBProduct.addProduct();
                    } else {
                        System.out.println("Sorry, you do not have admin privileges");
                    }
                    break;
                case 6:
                    loggedIn = false;
                    appStart();
                    break;
                default:
                    System.out.println("Option out of bound");
                    break;
            }

        }
    }

    public static void pickFromProductList(HashMap productIdMap, User u) {
        boolean addedToCart = false;
        Integer i = 0;
        System.out.println("Would you like to make a purchase? (Y/N)");
        String makePurchase = new Scanner(System.in).nextLine();
        if (makePurchase.equalsIgnoreCase("Y")) {


            System.out.println("Enter the product id: ");
            int productId = new Scanner(System.in).nextInt();

            try {
                i = (Integer) productIdMap.get(productId);
                System.out.println("How many items would you like to add?");
                int nrOfItems = new Scanner(System.in).nextInt();
                DBShoppingCart.addProductToCartById(i, u.getId(), nrOfItems);
            } catch (NullPointerException e) {
                System.out.println("You've entered the wrong product id.");
            }
        } else if (makePurchase.equalsIgnoreCase("N")) {
            System.out.println("Back to the menu");
        } else System.out.println("Invalid input - going back to the menu");
    }

    public static boolean wouldYouLikeToMakeAPurchase(HashMap productIdMap, User u) {

        boolean noError = false;
        int productId = -1;
        int nrOfItems = -1;

        while (noError == false) {
            System.out.println("Would you like to make a purchase? (Y/N)");
            String makePurchase = new Scanner(System.in).nextLine();

            if (makePurchase.equalsIgnoreCase("Y")) {

                while (productId == -1) {
                    try {
                        System.out.println("Enter the product id to add to cart: ");
                        productId = new Scanner(System.in).nextInt();
                        if (productId <= 0 || productId > productIdMap.size())
                            throw new InputMismatchException();
                    } catch (InputMismatchException e) {
                        System.out.println("Wrong input - try again");
                        productId = -1;
                    }
                }
                while (nrOfItems == -1) {
                    try {
                        System.out.println("How many items would you like to add?");
                        nrOfItems = new Scanner(System.in).nextInt();
                        if (nrOfItems <= 0)
                            throw new InputMismatchException();
                        Integer i = (Integer) (productIdMap.get(productId));
                        DBShoppingCart.addProductToCartById(i, u.getId(), nrOfItems);
                        return true;
                    } catch (InputMismatchException e) {
                        System.out.println("Wrong input - please try again");
                        nrOfItems = -1;
                    }
                }

                noError = true;
            } else if (makePurchase.equalsIgnoreCase("N")) {
                boolean doneShopping = true;
                noError = true;
                System.out.println("Returning to the menu");
                return false;
            } else {
                System.out.println("Wrong input - please try again");
                noError = false;
            }
        }
        return false;
    }


    public static boolean wouldYouLikeToMakeAnotherPurchase(HashMap productIdMap, User u) {
        boolean doneShopping = false;
        boolean noError = false;
        boolean exitShopping = false;
        int productId = -1;
        int nrOfItems = -1;

        while (doneShopping != true) {


            while (noError == false) {
                productId = -1;
                nrOfItems = -1;
                System.out.println("Would you like to make another purchase? (Y/N)");
                String makePurchase = new Scanner(System.in).nextLine();
                if (makePurchase.equalsIgnoreCase("N")) {
                    System.out.println("Returning to the menu");
                    doneShopping = true;
                    noError = true;
                    exitShopping = true;
                } else if (makePurchase.equalsIgnoreCase("Y")) {
                    while (productId == -1) {
                        try {
                            System.out.println("Enter the product id to add to cart: ");
                            productId = new Scanner(System.in).nextInt();
                            if (productId < 1 || productId > productIdMap.size()) {
                                throw new InputMismatchException();
                            }
//                            noError = true;
                        } catch (InputMismatchException e) {
                            System.out.println("Wrong ID - try again");
                            productId = -1;
                        }
                    }
                    while (nrOfItems == -1) {
                        try {
                            System.out.println("How many items would you like to add?");
                            nrOfItems = new Scanner(System.in).nextInt();
                            if (nrOfItems <= 0)
                                throw new InputMismatchException();
                            Integer i = (Integer) (productIdMap.get(productId));
                            DBShoppingCart.addProductToCartById(i, u.getId(), nrOfItems);
                        } catch (InputMismatchException e) {
                            System.out.println("Wrong input - please try again");
                            nrOfItems = -1;
                        }
                    }
                } else System.out.println("Wrong input - please try again");
            }
        }
        return exitShopping;
    }
}

