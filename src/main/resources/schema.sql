CREATE DATABASE  IF NOT EXISTS `rocket`;
USE `rocket`;

DROP TABLE IF EXISTS `user_authority`;
DROP TABLE IF EXISTS `authority`;
DROP TABLE IF EXISTS `token`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `userconnection`;


CREATE TABLE `authority` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255),
  `last_name` varchar(255),
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

CREATE TABLE `token` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `token` varchar(45) NOT NULL,
  `user` bigint(20) unsigned NOT NULL,
  `expiration` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_UNIQUE` (`token`),
  KEY `token_user_fk_idx` (`user`),
  CONSTRAINT `token_user_fk` FOREIGN KEY (`user`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

CREATE TABLE `user_authority` (
  `user_id` bigint(20) unsigned NOT NULL,
  `authority_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`user_id`,`authority_id`),
  KEY `user_authority_authority_fk_idx` (`authority_id`),
  CONSTRAINT `user_authority_authority_fk` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_authority_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `userconnection` (
  `userId` varchar(255) NOT NULL,
  `providerId` varchar(255) NOT NULL,
  `providerUserId` varchar(255) NOT NULL,
  `rank` int(11) NOT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(512) DEFAULT NULL,
  `imageUrl` varchar(512) DEFAULT NULL,
  `accessToken` varchar(512) NOT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `refreshToken` varchar(512) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`userId`,`providerId`,`providerUserId`),
  UNIQUE KEY `UserConnectionRank` (`userId`,`providerId`,`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
