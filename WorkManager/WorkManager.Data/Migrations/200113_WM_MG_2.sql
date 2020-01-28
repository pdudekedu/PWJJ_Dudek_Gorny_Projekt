ALTER TABLE TASKS ADD ESTIMATESTART DATETIME NULL;
ALTER TABLE TASKS ADD ESTIMATEEND DATETIME NULL;

GO
CREATE OR ALTER VIEW [dbo].[V_TASKS] AS

SELECT T.ID, T.NAME, T.PROJECTID, ACCOUNTID, A.NAME ACCOUNT, STATE, DESCRIPTION, T.INUSE, ESTIMATESTART, ESTIMATEEND FROM TASKS T
LEFT JOIN MGMNT.ACCOUNTS A ON A.ID = T.ACCOUNTID 
WHERE T.INUSE = 1
GO

EXEC sp_msforeachtable "ALTER TABLE ? NOCHECK CONSTRAINT all"
DELETE HST.PATHS;
INSERT INTO HST.PATHS (TABLENAME, COLUMNNAME, VALUE) VALUES 
('RESOURCES', 'ID', N'Zas�b'),
('RESOURCES', 'NAME', N'Zas�b -> Nazwa'),
('RESOURCES', 'DESCRIPTION', N'Zas�b -> Opis'),
('RESOURCES', 'INUSE', N'Zas�b -> W u�yciu'),
('MGMNT.ACCOUNTS', 'ID', N'Konto u�ytkownika'),
('MGMNT.ACCOUNTS', 'USERNAME', N'Konto u�ytkownika -> Nazwa'),
('MGMNT.ACCOUNTS', 'PASSWORD', N'Konto u�ytkownika -> Has�o'),
('MGMNT.ACCOUNTS', 'PERMISSIONS', N'Konto u�ytkownika -> Uprawnienia'),
('MGMNT.ACCOUNTS', 'TYPE', N'Konto u�ytkownika -> Typ konta'),
('MGMNT.ACCOUNTS', 'TITLE', N'Konto u�ytkownika -> Tytu�'),
('MGMNT.ACCOUNTS', 'NAME', N'Konto u�ytkownika -> Imi�'),
('MGMNT.ACCOUNTS', 'SURNAME', N'Konto u�ytkownika -> Nazwisko'),
('MGMNT.ACCOUNTS', 'ISLOCKED', N'Konto u�ytkownika -> Zablokowane'),
('MGMNT.ACCOUNTS', 'INUSE', N'Konto u�ytkownika -> W u�yciu'),
('PROJECTS', 'ID', N'Projekt'),
('PROJECTS', 'NAME', N'Projekt -> Nazwa'),
('PROJECTS', 'DESCRIPTION', N'Projekt -> Opis'),
('PROJECTS', 'TEAMID', N'Projekt -> Zesp�'),
('PROJECTS', 'INUSE', N'Projekt -> W u�yciu'),
('PROJECTRESOURCES', 'ID', N'Projekt -> Zas�b'),
('PROJECTRESOURCES', 'COUNT', N'Projekt -> Zas�b -> Ilo��'),
('TASKS', 'ID', N'Zadanie'),
('TASKS', 'NAME', N'Zadanie -> Nazwa'),
('TASKS', 'DESCRIPTION', N'Zadanie -> Opis'),
('TASKS', 'STATE', N'Zadanie -> Status'),
('TASKS', 'ESTIMATESTART', N'Zadanie -> Przewidwany pocz�tek'),
('TASKS', 'ESTIMATEEND', N'Zadanie -> Przewidywany koniec'),
('TASKS', 'ACCOUNTID', N'Zadanie -> Wykonawca'),
('TASKS', 'INUSE', N'Zadanie -> W u�yciu'),
('TASKRESOURCES', 'ID', N'Zadanie -> Zas�b'),
('TASKRESOURCES', 'COUNT', N'Zadanie -> Zas�b -> Ilo��'),
('TEAMS', 'ID', N'Zesp�'),
('TEAMS', 'NAME', N'Zesp� -> Nazwa'),
('TEAMS', 'DESCRIPTION', N'Zesp� -> Opis'),
('TEAMS', 'INUSE', N'Zesp� -> W u�yciu'),
('TEAMACCOUNTS', 'ID', N'Zesp� -> Cz�onek zespo�u')
GO
EXEC sp_msforeachtable "ALTER TABLE ? WITH CHECK CHECK CONSTRAINT all"