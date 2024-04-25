drop table if exists `member_flupet`;
CREATE TABLE `member_flupet` (
	`id`	int unsigned auto_increment
        primary key,
	`flupet_id`	int unsigned	NOT NULL,
	`member_id`	varchar(64)	NOT NULL,
	`name`	varchar(10)     NULL,
	`exp`	int	NOT NULL	DEFAULT 0,
	`steps`	int unsigned	NOT NULL	DEFAULT 0,
	`is_dead`	boolean	NOT NULL	DEFAULT false,
	`create_time`	datetime(6)	NOT NULL,
	`end_time`	datetime(6)	NULL,
	`fullness`	int	NOT NULL	DEFAULT 100,
	`health`	int	NOT NULL	DEFAULT 100,
	`pat_cnt`	int	NOT NULL	DEFAULT 5,
	`acha_time`	datetime(6)	NULL,
	`fullness_update_time`	datetime(6)	NULL,
	`energy_update_time`	datetime(6)	NULL
);

drop table if exists `food_type`;
CREATE TABLE `food_type` (
	`id`	int unsigned auto_increment
        primary key,
	`name`	varchar	NOT NULL,
	`img_url`	varchar	NOT NULL,
	`fullness_effect`	int	NOT NULL,
	`health_effect`	int	NOT NULL,
	`price`	int	NOT NULL,
	`description`	varchar	NOT NULL
);

drop table if exists `flupet`;
CREATE TABLE `flupet` (
	`id`	int unsigned auto_increment
        primary key,
	`name`	varchar(10)	NOT NULL,
	`img_url`	varchar(200)	NOT NULL,
	`stage`	int	NOT NULL
);

-- ALTER TABLE `members` ADD CONSTRAINT `PK_MEMBERS` PRIMARY KEY (
-- 	`id`
-- );
--
-- ALTER TABLE `memberFlupets` ADD CONSTRAINT `PK_MEMBERFLUPETS` PRIMARY KEY (
-- 	`id`
-- );
--
-- ALTER TABLE `steps` ADD CONSTRAINT `PK_STEPS` PRIMARY KEY (
-- 	`id`
-- );
--
-- ALTER TABLE `foodTypes` ADD CONSTRAINT `PK_FOODTYPES` PRIMARY KEY (
-- 	`id`
-- );
--
-- ALTER TABLE `runnings` ADD CONSTRAINT `PK_RUNNINGS` PRIMARY KEY (
-- 	`id`
-- );
--
-- ALTER TABLE `flupets` ADD CONSTRAINT `PK_FLUPETS` PRIMARY KEY (
-- 	`id`
-- );
--
-- ALTER TABLE `battleRankings` ADD CONSTRAINT `PK_BATTLERANKINGS` PRIMARY KEY (
-- 	`Key`
-- );
--
-- ALTER TABLE `battle` ADD CONSTRAINT `PK_BATTLE` PRIMARY KEY (
-- 	`id`
-- );
--
-- ALTER TABLE `AppVersion` ADD CONSTRAINT `PK_APPVERSION` PRIMARY KEY (
-- 	`id`
-- );
--
-- ALTER TABLE `PetRangkings` ADD CONSTRAINT `PK_PETRANGKINGS` PRIMARY KEY (
-- 	`Key`
-- );
--
-- ALTER TABLE `battleTypes` ADD CONSTRAINT `PK_BATTLETYPES` PRIMARY KEY (
-- 	`id`
-- );

