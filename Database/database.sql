CREATE TABLE IF NOT EXISTS tasks(code VARCHAR(30) PRIMARY KEY, title VARCHAR(50),detail TEXT,location TEXT,startD DATE, endD DATE, startT TIME, endT TIME,status INT(3));
INSERT INTO tasks VALUES('XXXXX','Sample task', 'Simple description','My room','2019-01-01','2020-01-01','00:00:00','23:59:59',1);