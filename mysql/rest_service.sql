CREATE DATABASE rest_service;

USE rest_service;
CREATE TABLE `category` (
  `cate_no` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cate_no`)
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO category (cate_no, `name`) values (1, '한식');
INSERT INTO category (cate_no, `name`) values (2, '중식');
INSERT INTO category (cate_no, `name`) values (3, '일식');
INSERT INTO category (cate_no, `name`) values (4, '양식');
INSERT INTO category (cate_no, `name`) values (6, '기타');

CREATE TABLE `user` (
  `utype` varchar(10) NOT NULL,
  `user_no` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(20) NOT NULL,
  `user_id` varchar(40) NOT NULL,
  `user_pw` varchar(256) NOT NULL,
  PRIMARY KEY (`user_no`),
  UNIQUE KEY `UK_a3imlf41l37utmxiquukk8ajc` (`user_id`)
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `restaurant` (
  `rest_no` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `coordx` double NOT NULL,
  `coordy` double NOT NULL,
  `created_at` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `review_count` bigint(20) NOT NULL,
  `score` double NOT NULL,
  `cate_no` bigint(20) DEFAULT NULL,
  `owner_no` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`rest_no`),
  FOREIGN KEY (cate_no) REFERENCES category(cate_no) ON DELETE SET NULL ON UPDATE SET NULL,
  FOREIGN KEY (owner_no) REFERENCES `user`(user_no) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `review` (
  `review_no` bigint(20) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `score` int(11) NOT NULL,
  `writer_name` varchar(40) DEFAULT NULL,
  `rest_no` bigint(20) DEFAULT NULL,
  `user_no` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`review_no`),
  FOREIGN KEY (`user_no`) REFERENCES `user`(user_no) ON DELEtE SET NULL ON UPDATE SET NULL,
  FOREIGN KEY (`rest_no`) REFERENCES `restaurant`(rest_no) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `review_report` (
  `report_no` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `review_no` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`report_no`),
  FOREIGN KEY (`review_no`) REFERENCES `review`(review_no) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `hibernate_sequence` (
  next_val bigint(20)
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `hibernate_sequence` (next_val) VALUES (2);
