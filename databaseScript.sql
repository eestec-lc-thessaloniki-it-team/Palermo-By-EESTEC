CREATE DATABASE palermo;

CREATE TABLE `palermo`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC));

  
  CREATE TABLE `palermo`.`game` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `leader_id` INT NOT NULL,
  `started` VARCHAR(45) NULL DEFAULT 0,
  `random_id` VARCHAR(6) NULL,
  `started_date` DATETIME NULL,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `leader_id_UNIQUE` (`leader_id` ASC),
  UNIQUE INDEX `random_number_UNIQUE` (`random_id` ASC));

  
  CREATE TABLE `palermo`.`user_to_game` (
  `user_id` INT NOT NULL,
  `game_id` VARCHAR(6) NOT NULL,
  `role_type` VARCHAR(45) NULL,
  `is_dead` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`, `game_id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC));

  CREATE TABLE `palermo`.`vote` (
  `game_id` VARCHAR(6) NOT NULL,
  `user_id` INT NOT NULL,
  `dead_user_id` INT NOT NULL,
  PRIMARY KEY (`game_id`, `user_id`));
