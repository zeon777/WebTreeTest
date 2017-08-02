DROP DATABASE IF EXISTS webtree;

CREATE DATABASE webtree;

USE webtree;

CREATE TABLE `tree`
(
	`id` INT(8) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(25) NOT NULL DEFAULT '0',
	`parentId` INT(8),
	`haveChild` BIT(1) NOT NULL DEFAULT b'0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci';


REPLACE INTO `tree` (`id`,`name`,`parentId`,`haveChild`) VALUES (0,"root",-1,1),(1,"node1",0,1),(2,"node2",0,0),(3,"node3",1,1),(4,"node4",1,0),(5,"node5",1,0),(6,"node6",3,0),(7,"node7",3,0),(8,"node8",3,0);