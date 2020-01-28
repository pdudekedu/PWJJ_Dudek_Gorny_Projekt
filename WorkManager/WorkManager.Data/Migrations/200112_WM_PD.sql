ALTER TABLE ACCOUNT_TEAM ADD ID INT NOT NULL IDENTITY(1, 1);
ALTER TABLE ACCOUNT_TEAM ADD MODUSER NVARCHAR(260) NULL;
ALTER TABLE ACCOUNT_TEAM ADD MODCOMP NVARCHAR(200) NULL;
GO

ALTER TABLE ACCOUNT_TEAM DROP CONSTRAINT PK_ACCOUNT_TEAM
ALTER TABLE ACCOUNT_TEAM ADD CONSTRAINT PK_ACCOUNT_TEAM PRIMARY KEY (ID);
GO

