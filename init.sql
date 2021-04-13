CREATE TABLE `catalog`.`artifact` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `description` MEDIUMTEXT NOT NULL,
  `exhibitionId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `exhibitionId_idx` (`exhibitionId` ASC) VISIBLE,
  CONSTRAINT `exhibitionId`
    FOREIGN KEY (`exhibitionId`)
    REFERENCES `catalog`.`exhibition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
