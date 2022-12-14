-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema hospital
-- -----------------------------------------------------


CREATE SCHEMA IF NOT EXISTS `hospital` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `hospital`;

-- -----------------------------------------------------
-- Table `hospital`.`category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hospital`.`category`;

CREATE TABLE IF NOT EXISTS `hospital`.`category`
(
    `id`       INT         NOT NULL AUTO_INCREMENT,
    `category` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `category_UNIQUE` (`category` ASC) VISIBLE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hospital`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hospital`.`role`;

CREATE TABLE IF NOT EXISTS `hospital`.`role`
(
    `role_id`   TINYINT                           NOT NULL AUTO_INCREMENT,
    `role_name` ENUM ('ADMIN', 'NURSE', 'DOCTOR') NOT NULL,
    PRIMARY KEY (`role_id`),
    UNIQUE INDEX `name_UNIQUE` (`role_name` ASC) VISIBLE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hospital`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hospital`.`user`;

CREATE TABLE IF NOT EXISTS `hospital`.`user`
(
    `id`            INT                              NOT NULL AUTO_INCREMENT,
    `login`         VARCHAR(45)                      NOT NULL,
    `password`      VARCHAR(90)                      NOT NULL,
    `firstname`     VARCHAR(45)                      NOT NULL,
    `lastname`      VARCHAR(45)                      NOT NULL,
    `date_of_birth` DATE                             NOT NULL,
    `gender`        ENUM ('MALE', 'FEMALE', 'OTHER') NOT NULL,
    `email`         VARCHAR(45)                      NOT NULL,
    `phone`         VARCHAR(15)                      NOT NULL,
    `address`       VARCHAR(200)                     NULL DEFAULT '',
    `locale`        ENUM ('UK', 'EN')                NOT NULL,
    `role_id`       TINYINT                          NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
    UNIQUE INDEX `phone_UNIQUE` (`phone` ASC) VISIBLE,
    INDEX `fk_user_role1_idx` (`role_id` ASC) VISIBLE,
    CONSTRAINT `fk_user_role1`
        FOREIGN KEY (`role_id`)
            REFERENCES `hospital`.`role` (`role_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hospital`.`doctor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hospital`.`doctor`;

CREATE TABLE IF NOT EXISTS `hospital`.`doctor`
(
    `id`          INT NOT NULL AUTO_INCREMENT,
    `category_id` INT NULL DEFAULT NULL,
    `user_id`     INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_doctor_categories1_idx` (`category_id` ASC) VISIBLE,
    INDEX `fk_doctor_user1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_doctor_categories1`
        FOREIGN KEY (`category_id`)
            REFERENCES `hospital`.`category` (`id`)
            ON DELETE SET NULL
            ON UPDATE CASCADE,
    CONSTRAINT `fk_doctor_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `hospital`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hospital`.`patient`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hospital`.`patient`;

CREATE TABLE IF NOT EXISTS `hospital`.`patient`
(
    `id`            INT                              NOT NULL AUTO_INCREMENT,
    `status`        ENUM('NEW', 'TREATMENT', 'DISCHARGED')                     NOT NULL,
    `doctor_id`     INT                              NULL DEFAULT NULL,
    `firstname`     VARCHAR(45)                      NOT NULL,
    `lastname`      VARCHAR(45)                      NOT NULL,
    `date_of_birth` DATE                             NOT NULL,
    `gender`        ENUM ('MALE', 'FEMALE', 'OTHER') NOT NULL,
    `email`         VARCHAR(45)                      NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_patient_doctor1_idx` (`doctor_id` ASC) VISIBLE,
    CONSTRAINT `fk_patient_doctor1`
        FOREIGN KEY (`doctor_id`)
            REFERENCES `hospital`.`doctor` (`id`)
            ON DELETE SET NULL
            ON UPDATE CASCADE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hospital`.`hospital_card`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hospital`.`hospital_card`;

CREATE TABLE IF NOT EXISTS `hospital`.`hospital_card`
(
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `diagnosis`  VARCHAR(255) NULL DEFAULT NULL,
    `patient_id` INT          NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_hospital_card_patient1_idx` (`patient_id` ASC) VISIBLE,
    CONSTRAINT `fk_hospital_card_patient1`
        FOREIGN KEY (`patient_id`)
            REFERENCES `hospital`.`patient` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hospital`.`appointment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hospital`.`appointment`;

CREATE TABLE IF NOT EXISTS `hospital`.`appointment`
(
    `id`               INT                                           NOT NULL AUTO_INCREMENT,
    `start_date`       DATE                                          NULL DEFAULT NULL,
    `end_date`         DATE                                          NULL DEFAULT NULL,
    `title`            VARCHAR(255)                                  NOT NULL,
    `status`           ENUM ('ONGOING', 'DONE')                      NOT NULL,
    `type`             ENUM ('PROCEDURE', 'MEDICATION', 'OPERATION') NOT NULL,
    `hospital_card_id` INT                                           NOT NULL,
    `user_id`          INT                                           NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_appointment_hospital_card1_idx` (`hospital_card_id` ASC) VISIBLE,
    INDEX `fk_appointment_user1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_appointments_hospital_card1`
        FOREIGN KEY (`hospital_card_id`)
            REFERENCES `hospital`.`hospital_card` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_treatment_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `hospital`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;



INSERT INTO hospital.role
    (role_id, role_name)
VALUES (1, 'ADMIN'),
       (2, 'DOCTOR'),
       (3, 'NURSE');


INSERT INTO hospital.category
    (id, category)
VALUES (1, 'pediatrician'),   -- 1
       (2, 'traumatologist'), -- 2
       (3, 'surgeon'); -- 3


INSERT INTO hospital.user
(login, password, firstname, lastname, date_of_birth, gender, email, phone, address, locale, role_id)
VALUES ('admin', '70ccd9007338d6d81dd3b6271621b9cf9a97ea00', '??????????????', '??????????', '2001-07-25', 'MALE',
        'lobur13@gmail.com', '(063) 666-12-39', '', 'UK', 1);



INSERT INTO hospital.user
(login, password, firstname, lastname, date_of_birth, gender, email, phone, address, locale, role_id)
VALUES ('micx72', '70ccd9007338d6d81dd3b6271621b9cf9a97ea00', '??????????????', '????????????????', '1972-05-23', 'MALE',
        'michael.zh@gmail.com', '(063) 255-12-39', '??????.?????????????? 45, 3', 'UK', 2),
       ('yuriy64', '70ccd9007338d6d81dd3b6271621b9cf9a97ea00', '????????', '??????????????????', '1964-05-23', 'MALE',
        'yuriyp@gmail.com', '(099) 991-24-26', '??????. ?????????????? ?????????????????? 23??', 'UK', 2),
       ('johnyanderson', '70ccd9007338d6d81dd3b6271621b9cf9a97ea00', 'Johny', 'Anderson', '1998-11-05', 'MALE',
        'johny.and@gmail.com', '(050) 485-23-33', 'First Moon st. 4', 'UK', 2);


INSERT INTO hospital.doctor
    (category_id, user_id)
VALUES (1, 2),
       (3, 3),
       (2, 4);

INSERT INTO hospital.user
(login, password, firstname, lastname, date_of_birth, gender, email, phone, address, locale, role_id)
VALUES ('vera1979', '70ccd9007338d6d81dd3b6271621b9cf9a97ea00', 'Vera', 'Stepanova', '1979-01-25', 'FEMALE',
        'vara25@gmail.com', '(098) 976-30-10', '??????.???????????? 2, 13', 'UK', 3);

INSERT INTO hospital.patient
(id, status, doctor_id, firstname, lastname, date_of_birth, gender, email)
VALUES (1, 'TREATMENT', 3, 'Varvara', 'Lastname', '2000-01-25', 'FEMALE',
        'varvar2000@gmail.com');

INSERT INTO hospital.patient
(id, status, doctor_id, firstname, lastname, date_of_birth, gender, email)
VALUES (2, 'TREATMENT', 1, 'Ivan', 'Ivasiyk', '2000-01-25', 'MALE', 'ivan2000@gmail.com');

INSERT INTO hospital.patient
(id, status, doctor_id, firstname, lastname, date_of_birth, gender, email)
VALUES (3, 'TREATMENT', NULL, 'Bob', 'Birch', '1990-12-01', 'MALE', 'bob90@gmail.com');

INSERT INTO hospital.hospital_card
    (id, diagnosis, patient_id)
VALUES (1, '???????????????????? ??????????????????', 1),
       (2, '????????????', 2),
       (3, '?????????????? ???????????? ??????\'????????', 3);

INSERT INTO hospital.appointment
(id, start_date, end_date, title, status, type, user_id, hospital_card_id)
VALUES (1, curdate(), adddate(curdate(), interval 30 day), '???????????????? ???? ?????????????????? ??????????????????', 'DONE', 'OPERATION', 3,
        1),
       (2, curdate(), adddate(curdate(), interval 14 day), '???????????????? ??????????????????', 'DONE', 'MEDICATION', 4, 2),
       (3, curdate(), NULL, '???????????????? ??????????????, ??????????????????', 'ONGOING', 'PROCEDURE', 4,
        3);

