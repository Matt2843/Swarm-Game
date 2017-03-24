-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.2.4-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for swarmer
CREATE DATABASE IF NOT EXISTS `swarmer` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `swarmer`;

-- Dumping structure for table swarmer.authentication_nodes
CREATE TABLE IF NOT EXISTS `authentication_nodes` (
  `id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table swarmer.authentication_nodes: ~0 rows (approximately)
/*!40000 ALTER TABLE `authentication_nodes` DISABLE KEYS */;
/*!40000 ALTER TABLE `authentication_nodes` ENABLE KEYS */;

-- Dumping structure for table swarmer.game_nodes
CREATE TABLE IF NOT EXISTS `game_nodes` (
  `id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table swarmer.game_nodes: ~0 rows (approximately)
/*!40000 ALTER TABLE `game_nodes` DISABLE KEYS */;
/*!40000 ALTER TABLE `game_nodes` ENABLE KEYS */;

-- Dumping structure for table swarmer.greeting_nodes
CREATE TABLE IF NOT EXISTS `greeting_nodes` (
  `id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table swarmer.greeting_nodes: ~2 rows (approximately)
/*!40000 ALTER TABLE `greeting_nodes` DISABLE KEYS */;
INSERT INTO `greeting_nodes` (`id`) VALUES
	('50c0ff69-7664-4237-b6ae-8289af32caee'),
	('4ff885f5-2bfe-4038-b303-24e82c7fd2a4');
/*!40000 ALTER TABLE `greeting_nodes` ENABLE KEYS */;

-- Dumping structure for table swarmer.lobby_nodes
CREATE TABLE IF NOT EXISTS `lobby_nodes` (
  `id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table swarmer.lobby_nodes: ~2 rows (approximately)
/*!40000 ALTER TABLE `lobby_nodes` DISABLE KEYS */;
INSERT INTO `lobby_nodes` (`id`) VALUES
	('3115dee6-7f67-4c2d-962a-d38477445a30'),
	('a66f46d9-9620-4143-895b-8a9207418c48');
/*!40000 ALTER TABLE `lobby_nodes` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
