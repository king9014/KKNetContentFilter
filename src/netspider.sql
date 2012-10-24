CREATE DATABASE  IF NOT EXISTS `netspider` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `netspider`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: netspider
-- ------------------------------------------------------
-- Server version	5.5.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `net_info`
--

DROP TABLE IF EXISTS `net_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `net_info` (
  `INFO_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `INFO_NAME` varchar(75) DEFAULT NULL,
  `INFO_WEBSITE` varchar(45) NOT NULL,
  `INFO_CATEGORY` varchar(45) DEFAULT NULL,
  `INFO_HTML_URL` varchar(105) DEFAULT NULL,
  `INFO_ORIGIN_URL` varchar(255) NOT NULL,
  `INFO_IMG_URL` varchar(255) DEFAULT NULL,
  `INFO_DATE` datetime DEFAULT NULL,
  `INFO_DATE_OPT` datetime NOT NULL,
  `INFO_INTRO` varchar(255) DEFAULT NULL,
  `INFO_CID` bigint(20) DEFAULT NULL,
  `INFO_STATUS` varchar(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`INFO_ID`),
  UNIQUE KEY `INFO_ORIGIN_URL_UNIQUE` (`INFO_ORIGIN_URL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `net_info`
--

LOCK TABLES `net_info` WRITE;
/*!40000 ALTER TABLE `net_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `net_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `net_info_page`
--

DROP TABLE IF EXISTS `net_info_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `net_info_page` (
  `INFO_PAGE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PAGE_TOTAL` int(11) DEFAULT NULL,
  `PAGE_CURRENT` int(11) DEFAULT NULL,
  `PAGE_HTML_URL` varchar(105) DEFAULT NULL,
  `PAGE_ORIGIN_URL` varchar(255) NOT NULL,
  `PARENT_ID` bigint(20) DEFAULT NULL,
  `PAGE_STATUS` varchar(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`INFO_PAGE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `net_info_page`
--

LOCK TABLES `net_info_page` WRITE;
/*!40000 ALTER TABLE `net_info_page` DISABLE KEYS */;
/*!40000 ALTER TABLE `net_info_page` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-10-19 13:32:32
