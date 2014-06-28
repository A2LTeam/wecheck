
-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2014 年 06 月 07 日 08:13
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
-- 表的结构 `SUB_CATEGORY`
--

CREATE TABLE IF NOT EXISTS `SUB_CATEGORY` (
  `ID` bigint(20) NOT NULL,
  `CATEGORY_ID` bigint(20) NOT NULL,
  `NAME_EN` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `NAME_TC` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `NAME_SC` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`,`CATEGORY_ID`),
  KEY `fk_CATEGORY_idx` (`CATEGORY_ID`),
  KEY `CATEGORY_ID` (`CATEGORY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `SUB_CATEGORY`
--

INSERT INTO `SUB_CATEGORY` (`ID`, `CATEGORY_ID`, `NAME_EN`, `NAME_TC`, `NAME_SC`) VALUES
(1, 1, 'Cakes', '蛋糕', '蛋糕'),
(2, 1, 'Bread', '麵包', '面包'),
(3, 1, 'Breakfast cereals', '穀類早餐', '谷类早餐'),
(4, 1, 'Spreads', '麵包醬', '面包酱'),
(5, 1, 'Honey / Syrup', '蜂蜜 / 蜜糖 / 糖漿', '蜂蜜 / 蜜糖 / 糖浆'),
(6, 2, 'Milk / Milk drinks', '牛奶 / 牛奶飲品', '牛奶 / 牛奶饮品'),
(7, 2, 'Cheese / Yoghurt products', '芝士 / 乳酪 / 乳酸產品', '芝士 / 乳酪 / 乳酸产品'),
(8, 2, 'Butter / Margarine / Cream', '牛油 / 植物牛油 / 忌廉', '牛油 / 植物牛油 / 忌廉'),
(9, 2, 'Frozen confections', '冰凍甜點', '冰冻甜点'),
(10, 2, 'Soy beverages', '豆漿 / 豆奶', '豆浆 / 豆奶'),
(11, 2, 'Bean curd products', '豆腐製品', '豆腐制品'),
(12, 2, 'Eggs', '蛋類', '蛋类'),
(14, 3, 'Biscuits', '餅乾', '饼乾'),
(13, 3, 'Snacks', '小食', '小食'),
(15, 3, 'Candies / Confectionery', '糖果 / 甜品', '糖果 / 甜品'),
(16, 3, 'Dried Fruits', '乾果', '乾果'),
(17, 4, 'Rice', '米', '米'),
(18, 4, 'Edible oil', '食油', '食油'),
(19, 4, 'Canned food', '罐頭食品', '罐头食品'),
(20, 4, 'Aseptically packaged food', '無菌紙盒包裝食品', '无菌纸盒包装食品'),
(24, 4, 'Chilled / Fresh meat', '冰鮮/新鮮肉類', '冰鲜/新鲜肉类'),
(21, 4, 'Pickle', '醃製食品', '腌制食品'),
(22, 4, 'Fresh vegetables', '新鮮蔬菜', '新鲜蔬菜'),
(23, 4, 'Fresh fruits', '新鮮水果', '新鲜水果'),
(25, 4, 'Previously frozen meat', '經急凍/解凍處理肉類', '经急冻/解冻处理肉类'),
(26, 4, 'Frozen meat / poultry', '急凍肉類', '急冻肉类'),
(27, 5, 'Instant noodles / Quick-serve pasta', '即食麵 / 快熟粉', '即食面 / 快熟粉'),
(28, 5, 'Udon noodles', '烏冬麵', '乌冬面'),
(29, 5, 'Non-instant noodles/pasta', '非即食粉麵', '非即食粉面'),
(30, 5, 'Condiments / Sauces / Cooking needs', '調味 / 煮食用料', '调味 / 煮食用料'),
(31, 5, 'Frozen processed food / Refrigerated processed food', '急凍加工食品 / 冷藏加工食品', '急冻加工食品 / 冷藏加工食品'),
(32, 6, 'Carbonated drinks', '汽水', '汽水'),
(33, 6, 'Coffee / Tea bags / Instant milk tea', '咖啡 / 茶包 / 即沖奶茶', '咖啡 / 茶包 / 即冲奶茶'),
(34, 6, 'Juices / Cordials', '果汁類飲品', '果汁类饮品'),
(35, 6, 'Oriental drinks', '東方特色飲品', '东方特色饮品'),
(37, 6, 'Bottle water / Sports drinks / Energy drinks', '樽裝水 / 運動飲品 / 能量飲品', '樽装水 / 运动饮品 / 能量饮品'),
(38, 6, 'Cereal drinks / Malted drinks / Chocolate drinks', '麥片飲品 / 麥芽飲品 / 朱古力飲品', '麦片饮品 / 麦芽饮品 / 朱古力饮品'),
(39, 7, 'Infant formula / Growing-up formula', '嬰幼兒奶粉 / 兒童奶粉', '婴幼儿奶粉 / 儿童奶粉'),
(40, 7, 'Pregnant women''s milk powder', '孕婦奶粉', '孕妇奶粉'),
(41, 7, 'Adult milk powder', '成人奶粉', '成人奶粉'),
(42, 7, 'Baby foods / Infant foods', '嬰幼兒食品 / 嬰兒食品 / 幼兒食品', '婴幼儿食品 / 婴儿食品 / 幼儿食品'),
(43, 7, 'Baby products', '嬰兒用品', '婴儿用品'),
(44, 8, 'Oral care', '口腔護理', '口腔护理'),
(45, 8, 'Feminine care', '女士衛生用品', '女士卫生用品'),
(46, 8, 'Bathing lotions / Liquid soap / Soap', '沐浴露 / 皂液 / 肥皂', '沐浴露 / 皂液 / 肥皂'),
(47, 8, 'Hair care', '頭髮護理', '头发护理'),
(48, 8, 'Skin care', '皮膚護理', '皮肤护理'),
(49, 8, 'Women''s hair removers', '女士脫毛用品 / 除毛用品', '女士脱毛用品 / 除毛用品'),
(50, 8, 'Men''s care', '男士護理', '男士护理'),
(51, 8, 'Deodorants', '香體用品', '香体用品'),
(52, 8, 'Drugs / Herbs / Supplements', '藥品', '药品'),
(53, 8, 'Contraceptives', '避孕', '避孕'),
(54, 8, 'Adult Diapers / Diaper Pants', '成人紙尿片 / 紙尿褲', '成人纸尿片 / 纸尿裤'),
(55, 9, 'Laundry needs', '洗衣用品', '洗衣用品'),
(56, 9, 'Household cleaning', '清潔用品', '清洁用品'),
(57, 9, 'Paper', '紙品', '纸品'),
(58, 9, 'Wraps / Food bags / Foil', '保鮮紙 / 食物袋 / 錫紙', '保鲜纸 / 食物袋 / 锡纸'),
(59, 9, 'Dehumidfiers / Mold prevention', '吸濕防霉', '吸湿防霉'),
(60, 9, 'Electrical products', '電器', '电器'),
(61, 9, 'Pet Food', '寵物食品', '宠物食品'),
(62, 10, 'Beer', '啤酒', '啤酒'),
(63, 10, 'Shandy', '仙地', '仙地'),
(64, 10, 'Red wines', '紅酒', '红酒'),
(65, 10, 'White wines', '白酒', '白酒'),
(66, 10, 'Shaoxing wine', '紹興酒', '绍兴酒'),
(67, 10, 'Rice wine', '米酒', '米酒');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
