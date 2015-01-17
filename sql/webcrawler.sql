
CREATE SCHEMA IF NOT EXISTS `webcrawler`;
USE `webcrawler` ;

DROP TABLE IF EXISTS `webcrawler`.`item_attribute`;
DROP TABLE IF EXISTS `webcrawler`.`item`;
DROP TABLE IF EXISTS `webcrawler`.`attribute_name`;
DROP TABLE IF EXISTS `webcrawler`.`seller`;

-- -----------------------------------------------------
-- Table `webcrawler`.`attribute_name`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `webcrawler`.`attribute_name` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `data_type` VARCHAR(64) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `webcrawler`.`seller`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `webcrawler`.`seller` (
  `seller_id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(512) NULL DEFAULT NULL,
  PRIMARY KEY (`seller_id`));


-- -----------------------------------------------------
-- Table `webcrawler`.`item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `webcrawler`.`item` (
  `item_id` INT(11) NOT NULL AUTO_INCREMENT,
  `seller_id` INT(11) NOT NULL,
  `active_flag` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  INDEX `seller_id` (`seller_id` ASC),
  CONSTRAINT `item_ibfk_1`
    FOREIGN KEY (`seller_id`)
    REFERENCES `webcrawler`.`seller` (`seller_id`));


-- -----------------------------------------------------
-- Table `webcrawler`.`item_attribute`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `webcrawler`.`item_attribute` (
  `attribute_id` INT(11) NOT NULL,
  `item_id` INT(11) NOT NULL,
  `attribute_name_fk` INT(11) NOT NULL,
  `attribute_value` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`attribute_id`, `item_id`),
  INDEX `attribute_name_fk` (`attribute_name_fk` ASC),
  CONSTRAINT `item_attribute_ibfk_1`
    FOREIGN KEY (`attribute_name_fk`)
    REFERENCES `webcrawler`.`attribute_name` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `item_attribute_ibfk_2`
    FOREIGN KEY (`item_id`)
    REFERENCES `webcrawler`.`item` (`item_id`)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);
