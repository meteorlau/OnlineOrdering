drop database if exists online_order_database;
create database online_order_database;
use online_order_database;

CREATE TABLE stores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    is_active TINYINT(1) DEFAULT 1
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_active TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

insert into roles (name) values ('ADMIN');
insert into roles (name) values ('USER');

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    image_url VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    id BINARY(16) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_items (
    order_id BINARY(16) NOT NULL,
    store_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (order_id, store_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
	FOREIGN KEY (store_id) REFERENCES stores(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE store_products (
    store_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    stock INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (store_id, product_id),
    FOREIGN KEY (store_id) REFERENCES stores(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

insert into stores (name, location) values ('Malatang', '123 cupertino');
insert into products (name, image_url) values ('fried rice', 'https://thechutneylife.com/wp-content/uploads/2021/03/Thaibasilfriedrice-reshoot-3-scaled.jpg');
insert into store_products (store_id, product_id, stock, unit_price) values (1, 1, 10, 10);

Select * from store_products 
JOIN stores ON store_products.store_id = stores.id
JOIN products on store_products.product_id = products.id
WHERE stores.location = '123 cupertino' 
  AND products.name = 'fried rice';
  
Select * from users;
Select * from user_roles;
Select * from orders;

