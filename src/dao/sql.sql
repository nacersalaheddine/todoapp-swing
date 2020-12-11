SELECT * FROM todos;
SELECT * FROM todos_lists;
---
CREATE TABLE `todos` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`status`	NUMERIC NOT NULL DEFAULT 3,
	`todo_content`	TEXT NOT NULL,
	`datetime_created`	TEXT NOT NULL,
	`creation_timestamp`	TEXT NOT NULL,
	`active_status`	NUMERIC NOT NULL DEFAULT 1,
	`todos_list_fk`	INTEGER NOT NULL,
	FOREIGN KEY(`todos_list_fk`) REFERENCES `todos_lists`(`list_id`)
);
CREATE TABLE `todos_lists` (
	`list_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`creation_timestamp`	TEXT NOT NULL,
	`datetime_created`	TEXT NOT NULL,
	`list_name`	TEXT NOT NULL,
	`active_status`	INTEGER DEFAULT 1
);



-- This is used to fix the timezone issue
SET GLOBAL time_zone = '+3:00';