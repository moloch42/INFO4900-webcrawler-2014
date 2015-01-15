-- updated the site pattern for futureshop to be able to crawl properly.
-- kijiji not yet tested

drop database webcrawler;
create database webcrawler;
use webcrawler;
    
-- -------------------------------------------------------------
-- TYPE TABLES
-- -------------------------------------------------------------

CREATE TABLE IF NOT EXISTS Code_NodeObject_Attribute
(
    id int NOT NULL AUTO_INCREMENT,  
    name varchar(64) NOT NULL,
    data_type varchar(64),
    
    PRIMARY KEY (id)
)auto_increment=1000;

-- CREATE TABLE IF NOT EXISTS Code_NodeObject_Category
-- (
--     id int NOT NULL AUTO_INCREMENT,
--     name varchar(512) NOT NULL,
-- 	
--     parent_code_nodeObject_category_id int,
-- 
--     PRIMARY KEY (id),
--     FOREIGN KEY (parent_code_nodeObject_category_id) REFERENCES Code_NodeObject_Category(id)
-- )auto_increment=1000;

-- -------------------------------------------------------------
-- DATA TABLES
-- -------------------------------------------------------------

CREATE TABLE IF NOT EXISTS Seller
(
    id int NOT NULL AUTO_INCREMENT,
    name varchar(512),

    PRIMARY KEY (id)
)auto_increment=1000;

CREATE TABLE IF NOT EXISTS NodeObject
(
    id int NOT NULL AUTO_INCREMENT,
    seller_id int NOT NULL,
--     name varchar(16384),
--     description varchar(16384),
--     source varchar(16384),
    active_flag boolean,

    PRIMARY KEY (id),
    FOREIGN KEY (seller_id) REFERENCES Seller(id)
)auto_increment=1000;

-- CREATE TABLE IF NOT EXISTS Seller_NodeObject
-- (
--     id int NOT NULL AUTO_INCREMENT,
--     nodeObject_id int NOT NULL,
--     seller_id int NOT NULL,
-- 
--     PRIMARY KEY (id),
--     FOREIGN KEY (seller_id) REFERENCES Seller(id),
--     FOREIGN KEY (nodeObject_id) REFERENCES NodeObject(id)
-- )auto_increment=1000;

CREATE TABLE IF NOT EXISTS NodeObject_Attribute
(
    nodeObject_id int NOT NULL,
    code_nodeObject_attribute_id int NOT NULL,
    
    value varchar(16384),

    PRIMARY KEY (nodeObject_id, code_nodeObject_attribute_id),
    FOREIGN KEY (code_nodeObject_attribute_id) REFERENCES Code_NodeObject_Attribute(id),
    FOREIGN KEY (nodeObject_id) REFERENCES NodeObject(id)
)auto_increment=1000;

-- -------------------------------------------------------------
-- SITE FORMAT TABLES
-- -------------------------------------------------------------

CREATE TABLE IF NOT EXISTS SiteFormat_NodeObject
(
    id int NOT NULL AUTO_INCREMENT,
    
    parent_siteFormat_nodeObject_id int,
    code_nodeObject_attribute_id int,

    element_name varchar(32) NOT NULL,
    element_attribute_name varchar(32),
    element_attribute_value varchar(1024),
    element_content_attribute_name varchar(32),
    
    FOREIGN KEY (parent_siteFormat_nodeObject_id) REFERENCES SiteFormat_NodeObject(id),
    FOREIGN KEY (code_nodeObject_attribute_id) REFERENCES Code_NodeObject_Attribute(id),
    PRIMARY KEY (id)
)auto_increment=1000;

CREATE TABLE IF NOT EXISTS SiteFormat
(
    id int NOT NULL AUTO_INCREMENT,
    
    seller_id int,
    siteFormat_nodeObject_id int,
    
    url varchar(16384),
    url_page_id varchar(1024),
    
    PRIMARY KEY (id),
    FOREIGN KEY (seller_id) REFERENCES Seller(id),
    FOREIGN KEY (siteFormat_nodeObject_id) REFERENCES SiteFormat_NodeObject(id)
)auto_increment=1000;

-- -------------------------------------------------------------
-- EXAMPLE DATA
-- -------------------------------------------------------------

INSERT INTO Seller(name) VALUES('Unknown');
INSERT INTO Seller(name) VALUES('Kijiji');
INSERT INTO Seller(name) VALUES('Future Shop');

INSERT INTO Code_NodeObject_Attribute(name, data_type) VALUES('description', 'varchar');
INSERT INTO Code_NodeObject_Attribute(name, data_type) VALUES('price', 'double');

-- START Future Shop pattern
-- NodeObject pattern
INSERT INTO SiteFormat_NodeObject(parent_siteFormat_nodeObject_id, code_nodeObject_attribute_id, element_name, element_attribute_name, element_attribute_value, element_content_attribute_name) 
VALUES(NULL, NULL, 'div', 'class', 'item-inner clearfix', NULL);

-- name pattern
INSERT INTO SiteFormat_NodeObject(parent_siteFormat_nodeObject_id, code_nodeObject_attribute_id, element_name, element_attribute_name, element_attribute_value, element_content_attribute_name) 
VALUES(1000, NULL, 'div', 'class', 'prod-info', NULL);
INSERT INTO SiteFormat_NodeObject(parent_siteFormat_nodeObject_id, code_nodeObject_attribute_id, element_name, element_attribute_name, element_attribute_value, element_content_attribute_name) 
VALUES(1001, 1000, 'a', NULL, NULL, NULL);


-- price pattern
INSERT INTO SiteFormat_NodeObject(parent_siteFormat_nodeObject_id, code_nodeObject_attribute_id, element_name, element_attribute_name, element_attribute_value, element_content_attribute_name) 
VALUES(1000, NULL, 'div', 'class', 'price-wrapper price-large', NULL); 
INSERT INTO SiteFormat_NodeObject(parent_siteFormat_nodeObject_id, code_nodeObject_attribute_id, element_name, element_attribute_name, element_attribute_value, element_content_attribute_name) 
VALUES(1003, 1001, 'div', 'class', 'prod-price', NULL);
-- INSERT INTO SiteFormat_NodeObject(parent_siteFormat_nodeObject_id, code_nodeObject_attribute_id, element_name, element_attribute_name, element_attribute_value, element_content_attribute_name) 
-- VALUES(1004, 1001, 'span', 'class', 'dollars', NULL);


INSERT INTO SiteFormat(seller_id, siteFormat_nodeObject_id, url, url_page_id)
VALUES (1002, 1000, 'http://www.futureshop.ca/Search/SearchResults.aspx?path=ca77b9b4beca91fe414314b86bb581f8en20&q=&sp=FS_en-CA_Site-WideSP&Category=Entire%2520Site&pcname=MCCPCatalog&AllowSecure=True', '&page=');
-- END FutureShop pattern

-- START Kijiji pattern
-- NodeObject pattern
INSERT INTO SiteFormat_NodeObject(parent_siteFormat_nodeObject_id, code_nodeObject_attribute_id, element_name, element_attribute_name, element_attribute_value, element_content_attribute_name) 
VALUES(NULL, NULL, 'tr', 'class', 'resultsTableSB rrow', NULL);

-- name pattern
INSERT INTO SiteFormat_NodeObject(parent_siteFormat_nodeObject_id, code_nodeObject_attribute_id, element_name, element_attribute_name, element_attribute_value, element_content_attribute_name) 
VALUES(1006, NULL, 'td', 'class', ' hgk', NULL);
INSERT INTO SiteFormat_NodeObject(parent_siteFormat_nodeObject_id, code_nodeObject_attribute_id, element_name, element_attribute_name, element_attribute_value, element_content_attribute_name) 
VALUES(1007, 1000, 'a', NULL, NULL, NULL);

-- price pattern
INSERT INTO SiteFormat_NodeObject(parent_siteFormat_nodeObject_id, code_nodeObject_attribute_id, element_name, element_attribute_name, element_attribute_value, element_content_attribute_name) 
VALUES(1006, 1001, 'td', 'class', 'prc ', NULL); 


INSERT INTO SiteFormat(seller_id, siteFormat_nodeObject_id, url, url_page_id)
VALUES (1001, 1006, 'http://newbrunswick.kijiji.ca/f-buy-and-sell-W0QQCatIdZ10', 'QQPageZ');
-- END Kijiji pattern

-- -------------------------------------------------------------
-- -------------------------------------------------------------
