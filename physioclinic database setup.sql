CREATE DATABASE  IF NOT EXISTS `physioclinic`;
USE `physioclinic`;


DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `users`;


CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` char(68) NOT NULL,
  `enabled` tinyint NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `users` (`username`,`password`,`enabled`)
VALUES 
-- default password is a first name of a user lowercase --  
('john.johnson@physioclinic.com','{bcrypt}$2a$10$bmP2wX8O0p0zP5aCV9uQXejfYDYK621Gmr4QFtI6Jqo28.oiRoaq6',1),
('mary.marys@physioclinic.com','{bcrypt}$2a$10$VhiCsy7MtUh9j3/jwpMy5exOVFOvgUiUiou2V1ydQaU9jpBOcutUC',1),
('susan.susanes@physioclinic.com','{bcrypt}$2a$10$e16QFt0LHnzF0.vzqQK6UetXzzvtrUf6oRf0qcpvwXO1ohSXM7rVe',1),
('jack.jackson@physioclinic.com','{bcrypt}$2a$10$Pau2mGGUEEoirlCQ8UtlF.5NxQNsfk6nhkKxIJUyvw2csXMbulwT.',1);


CREATE TABLE `authorities` (
  `authority_id` int NOT NULL AUTO_INCREMENT,
  `authority` varchar(50) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`authority_id`),
  UNIQUE KEY (`authority`, `user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `authorities` (`authority`, `user_id`)
VALUES 
('ROLE_ADMIN', 1),
('ROLE_PHYSIOTHERAPIST', 1),
('ROLE_LEADER', 2),
('ROLE_PHYSIOTHERAPIST', 2),
('ROLE_PHYSIOTHERAPIST', 3),
('ROLE_PHYSIOTHERAPIST', 4);