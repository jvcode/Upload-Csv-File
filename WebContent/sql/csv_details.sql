CREATE TABLE `csv_details` (
  `cd_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `phone_number` bigint(20) DEFAULT NULL,
  `cf_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`cd_id`),
  KEY `cf_id` (`cf_id`),
  CONSTRAINT `csv_details_ibfk_1` FOREIGN KEY (`cf_id`) REFERENCES `csv_file` (`cf_id`)
) ENGINE=InnoDB AUTO_INCREMENT=386 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci