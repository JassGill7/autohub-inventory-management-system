-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: autohub_inventory
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cars`
--

DROP TABLE IF EXISTS `cars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cars` (
  `car_id` bigint NOT NULL AUTO_INCREMENT,
  `stock_code` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `make` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `model` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `manufacture_year` year NOT NULL,
  `vin` char(17) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `registration_number` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fuel_type` enum('P','D','E') COLLATE utf8mb4_unicode_ci NOT NULL,
  `transmission` enum('M','A') COLLATE utf8mb4_unicode_ci NOT NULL,
  `colour` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `purchase_price` decimal(12,2) NOT NULL,
  `acquisition_date` date NOT NULL,
  `listing_price` decimal(12,2) NOT NULL,
  `status` enum('Available','Sold') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Available',
  `sold_date` date DEFAULT NULL,
  `sold_price` decimal(12,2) DEFAULT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`car_id`),
  UNIQUE KEY `stock_code` (`stock_code`),
  UNIQUE KEY `vin` (`vin`),
  UNIQUE KEY `registration_number` (`registration_number`),
  KEY `idx_make_model` (`make`,`model`),
  KEY `idx_status` (`status`),
  CONSTRAINT `chk_sale_details` CHECK ((((`status` = _utf8mb4'Available') and (`sold_date` is null) and (`sold_price` is null)) or ((`status` = _utf8mb4'Sold') and (`sold_date` is not null) and (`sold_price` is not null))))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cars`
--

LOCK TABLES `cars` WRITE;
/*!40000 ALTER TABLE `cars` DISABLE KEYS */;
INSERT INTO `cars` VALUES (1,'AH-001','Toyota Fortuner Legender','Toyota','Fortuner',2023,NULL,NULL,'D','A','Black',3200000.00,'2026-11-24',3500000.00,'Available',NULL,NULL,NULL),(2,'AH-TEST-B3874CBE','Toyota Fortuner Legender','Toyota','Fortuner',2023,NULL,NULL,'D','A','Black',3200000.00,'2026-11-15',3500000.00,'Available',NULL,NULL,'AutoHub registration test'),(3,'AH-TEST-898B826F','Toyota Fortuner Legender','Toyota','Fortuner',2023,NULL,NULL,'D','A','Black',3200000.00,'2026-11-15',3500000.00,'Available',NULL,NULL,'AutoHub registration test'),(4,'AH-PERSIST-0013993BA36','Toyota Fortuner Legender','Toyota','Fortuner',2023,NULL,NULL,'D','A','Black',3200000.00,'2026-11-15',3500000.00,'Available',NULL,NULL,'AutoHub registration test'),(5,'AAH-724','BMW M5','BMW','M5',2023,NULL,NULL,'P','A','Red',9500000.00,'2021-11-30',9750000.00,'Available',NULL,NULL,'nice car');
/*!40000 ALTER TABLE `cars` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-25 15:08:32
