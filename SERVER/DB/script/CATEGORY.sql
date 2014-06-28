
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
-- 表的结构 `CATEGORY`
--

CREATE TABLE IF NOT EXISTS `CATEGORY` (
  `ID` bigint(20) NOT NULL,
  `CAT_NAME_EN` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `CAT_NAME_TC` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `CAT_NAME_SC` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `CATEGORY`
--

INSERT INTO `CATEGORY` (`ID`, `CAT_NAME_EN`, `CAT_NAME_TC`, `CAT_NAME_SC`) VALUES
(1, 'Bread / Cakes / Cereals / Spreads', '麵包蛋糕 / 穀類早餐 / 麵包醬', '面包蛋糕 / 谷类早餐 / 面包酱'),
(2, 'Dairy / Soy products / Eggs', '奶類及乳製品 / 大豆製品 / 蛋類', '奶类及乳制品 / 大豆制品 / 蛋类'),
(3, 'Candies / Biscuits / Snacks', '糖果 / 餅乾 / 小食', '糖果 / 饼乾 / 小食'),
(4, 'Rice / Oil / Canned food / Fruits / Vegetables / Meat', '米 / 食油 / 罐頭 / 蔬果 / 肉類', '米 / 食油 / 罐头 / 蔬果 / 肉类'),
(5, 'Noodles / Seasoning / Chilled & frozen processed food', '粉麵 / 調味 / 冷凍加工食品', '粉面 / 调味 / 冷冻加工食品'),
(6, 'Beverages', '飲品', '饮品'),
(7, 'Milk powder / Baby care', '奶粉 / 嬰兒用品', '奶粉 / 婴儿用品'),
(8, 'Personal care', '個人護理', '个人护理'),
(9, 'Household needs', '家居用品', '家居用品'),
(10, 'Beer / Wines / Spirits', '酒類', '酒类');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
