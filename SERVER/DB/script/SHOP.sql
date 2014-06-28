
-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2014 年 06 月 07 日 08:15
-- 服务器版本: 5.1.61
-- PHP 版本: 5.2.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `u363963258_test`
--

-- --------------------------------------------------------

--
-- 表的结构 `SHOP`
--

CREATE TABLE IF NOT EXISTS `SHOP` (
  `ID` bigint(20) NOT NULL,
  `ORDER_INDEX` int(11) DEFAULT NULL,
  `NAME_EN` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `NAME_TC` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `NAME_SC` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `SHOP`
--

INSERT INTO `SHOP` (`ID`, `ORDER_INDEX`, `NAME_EN`, `NAME_TC`, `NAME_SC`) VALUES
(1, 1, 'Wellcome', '惠康', '惠康'),
(2, 2, 'PARKnSHOP', '百佳', '百佳'),
(3, 3, 'Market Place', 'Market Place', 'Market Place'),
(4, 4, 'AEON (JUSCO)', '永旺 (吉之島)', '永旺 (吉之島)'),
(5, 5, 'DCH Food Mart', '大昌食品', '大昌食品');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
