DROP SCHEMA IF EXISTS webcrawler;
CREATE SCHEMA webcrawler;
USE webcrawler ;

-- -----------------------------------------------------
-- Table webcrawler.attribute_name
-- -----------------------------------------------------
CREATE TABLE attribute_name (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(64) NOT NULL,
  data_type VARCHAR(64) NULL DEFAULT NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table webcrawler.seller
-- -----------------------------------------------------
CREATE TABLE seller (
  seller_id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(512) NULL DEFAULT NULL,
  PRIMARY KEY (seller_id));


-- -----------------------------------------------------
-- Table webcrawler.item
-- -----------------------------------------------------
CREATE TABLE item (
  item_id INT(11) NOT NULL AUTO_INCREMENT,
  seller_id INT(11) NOT NULL,
  active_flag TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (item_id),
  INDEX seller_id (seller_id ASC),
  CONSTRAINT item_fk_seller
    FOREIGN KEY (seller_id)
    REFERENCES seller (seller_id));


-- -----------------------------------------------------
-- Table webcrawler.item_attribute
-- -----------------------------------------------------
CREATE TABLE item_attribute (
  attribute_id INT(11) NOT NULL AUTO_INCREMENT,
  item_id INT(11) NOT NULL,
  attribute_name_fk INT(11) NOT NULL,
  attribute_value TEXT NULL DEFAULT NULL,
  PRIMARY KEY (attribute_id, item_id),
  INDEX attribute_name_fk (attribute_name_fk ASC),
  CONSTRAINT item_attribute_fk_attribute_name
    FOREIGN KEY (attribute_name_fk)
    REFERENCES attribute_name (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT item_attribute_fk_item
    FOREIGN KEY (item_id)
    REFERENCES item (item_id)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- View webcrawler.item_details
-- -----------------------------------------------------
CREATE VIEW item_details AS
	SELECT item.item_id, seller.name seller, an.name attribute, ia.attribute_value value
	FROM seller
    JOIN item
		ON seller.seller_id = item.seller_id
	JOIN item_attribute ia
		ON item.item_id = ia.item_id
	JOIN attribute_name an
		ON ia.attribute_name_fk = an.id
;