Vlad Sitaru - 25-APR-2022

eMagApp is a commandline application offering basic virtual shop functionalities:

- user registration and authentication, with and without admin privileges
- accessing the product catalogue, sorting products by name and price ASC/DESC, search for products by keywords or category
- add products to the shopping cart, delete, add or remove products from the basket, checkout stores the order details in db with unique id
- admin users can introduce products to the catalogue

Packages:

DB
    DBProduct - receive and create operations for the products table
    DBShoppingCart - receive, create, delete and update operations for the shopping cart table
    DBUser - registration & authentication operations, check admin privilege

src
    AppBusinessLogic - commandline application logic/flow
    Main - start of the application