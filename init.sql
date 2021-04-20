CREATE SCHEMA IF NOT EXISTS `catalog`;

CREATE TABLE IF NOT EXISTS `artifact` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `description` MEDIUMTEXT NOT NULL,
  `exhibitionId` INT DEFAULT NULL,
  `length` DOUBLE NOT NULL,
  `width` DOUBLE NOT NULL,
  `height` DOUBLE NOT NULL,
  `weight` DOUBLE NOT NULL,
  PRIMARY KEY (`id`),
  KEY `exhibitionId_idx` (`exhibitionId`),
  CONSTRAINT `exhibitionId`
	FOREIGN KEY (`exhibitionId`)
    REFERENCES `exhibition` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `exhibition` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `description` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`id`)
);
