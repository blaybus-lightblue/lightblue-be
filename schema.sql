CREATE TABLE IF NOT EXISTS `account` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `account_type` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `company` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `account_id` INT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `artist` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `account_id` INT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `job_field` VARCHAR(255),
    `city` VARCHAR(255),
    `activity_field` VARCHAR(255),
    `desired_collaboration_field` VARCHAR(255),
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `portfolio` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `artist_id` INT NOT NULL,
    `url` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `partnership` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `initiator_account_id` INT NOT NULL,
    `recipient_account_id` INT NOT NULL,
    `type` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

ALTER TABLE `company` ADD CONSTRAINT `fk_company_account_id` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);
ALTER TABLE `artist` ADD CONSTRAINT `fk_artist_account_id` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);
ALTER TABLE `portfolio` ADD CONSTRAINT `fk_portfolio_artist_id` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`);
ALTER TABLE `partnership` ADD CONSTRAINT `fk_partnership_initiator_account_id` FOREIGN KEY (`initiator_account_id`) REFERENCES `account` (`id`);
ALTER TABLE `partnership` ADD CONSTRAINT `fk_partnership_recipient_account_id` FOREIGN KEY (`recipient_account_id`) REFERENCES `account` (`id`);