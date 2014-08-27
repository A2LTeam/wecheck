-- -----------------------------------------------------
-- Table `CATEGORY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CATEGORY` (
  `ID` BIGINT NOT NULL,
  `CAT_NAME_EN` VARCHAR(128) NOT NULL,
  `CAT_NAME_TC` VARCHAR(128) NOT NULL,
  `CAT_NAME_SC` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`ID`));

-- -----------------------------------------------------
-- Table `SUB_CATEGORY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SUB_CATEGORY` (
  `ID` bigint(20) NOT NULL,
  `CATEGORY_ID` bigint(20) NOT NULL,
  `NAME_EN` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `NAME_TC` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `NAME_SC` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`,`CATEGORY_ID`),
  KEY `CATEGORY_ID` (`CATEGORY_ID`)
);

-- -----------------------------------------------------
-- Table `ITEM`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ITEM` (
  `ID` BIGINT NOT NULL,
  `ITEM_CODE` VARCHAR(50) NOT NULL,
  `BARCODE` VARCHAR(128) NULL,
  `CATEGORY_ID` BIGINT NOT NULL,
  `ORDER_INDEX` INT NULL,
  `BRAND_EN` VARCHAR(128) NULL,
  `NAME_EN` VARCHAR(128) NULL,
  `BRAND_TC` VARCHAR(128) NULL,
  `NAME_TC` VARCHAR(128) NULL,
  `BRAND_SC` VARCHAR(128) NULL,
  `NAME_SC` VARCHAR(128) NULL,
  INDEX `fk_ITEM_CATEGORY1_idx` (`CATEGORY_ID` ASC),
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ITEM_CODE_UNIQUE` (`ITEM_CODE` ASC),
  UNIQUE INDEX `BARCODE_UNIQUE` (`BARCODE` ASC),
  CONSTRAINT `fk_ITEM_CATEGORY1`
    FOREIGN KEY (`CATEGORY_ID`)
    REFERENCES `SUB_CATEGORY` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `SHOP`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SHOP` (
  `ID` BIGINT NOT NULL,
  `ORDER_INDEX` INT NULL,
  `NAME_EN` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `NAME_TC` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `NAME_SC` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`));

-- -----------------------------------------------------
-- Table `SHOP_ITEM`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SHOP_ITEM` (
  `SHOP_ID` BIGINT NOT NULL,
  `ITEM_ID` BIGINT NOT NULL,
  `PRICE` DECIMAL NOT NULL,
  `EFF_DATE` DATE NOT NULL,
  `PREV_PRICE` DECIMAL NULL,
  INDEX `fk_SHOP_ITEM_SHOP_idx` (`SHOP_ID` ASC),
  INDEX `fk_SHOP_ITEM_ITEM1_idx` (`ITEM_ID` ASC),
  PRIMARY KEY (`SHOP_ID`, `ITEM_ID`),
  CONSTRAINT `fk_SHOP_ITEM_SHOP`
    FOREIGN KEY (`SHOP_ID`)
    REFERENCES `SHOP` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SHOP_ITEM_ITEM1`
    FOREIGN KEY (`ITEM_ID`)
    REFERENCES `ITEM` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

