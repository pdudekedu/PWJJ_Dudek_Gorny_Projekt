sp_rename 'HST.ELEMENTS.LENTH', 'LENGTH', 'COLUMN';
ALTER TABLE HST.VALUESHISTORY ADD MODDATE DATETIME NOT NULL DEFAULT(GETDATE());
GO

CREATE VIEW HST.V_VALUESHISTORY
AS
	SELECT
		 VH.MODUSER,
		 COALESCE(IIF(VH.MODUSERNAME IS NOT NULL AND VH.MODUSERSURNAME IS NOT NULL, VH.MODUSERNAME + ' ' + VH.MODUSERSURNAME, NULL), VH.MODUSERNAME, VH.MODUSERSURNAME) MODUSERDISPLAYNAME,
		 VH.MODDATE,
		 VH.MODCOMP,
		 VH.MODTYPE,
		 A.VALUE AREA,
		 CASE
			WHEN E.LENGTH = 1
				THEN EV0.VALUE
			WHEN E.LENGTH = 2
				THEN EV0.VALUE + ' -> ' + EV1.VALUE
			WHEN E.LENGTH = 3
				THEN EV0.VALUE + ' -> ' + EV1.VALUE + ' -> ' + EV2.VALUE
			WHEN E.LENGTH = 4
				THEN EV0.VALUE + ' -> ' + EV1.VALUE + ' -> ' + EV2.VALUE + ' -> ' + EV3.VALUE
			WHEN E.LENGTH = 5
				THEN EV0.VALUE + ' -> ' + EV1.VALUE + ' -> ' + EV2.VALUE + ' -> ' + EV3.VALUE + ' -> ' + EV4.VALUE
		 END ELEMENT,
		 P.VALUE [PATH],
		 OV.VALUE OLDVALUE,
		 NV.VALUE NEWVALUE
	FROM HST.VALUESHISTORY VH
	JOIN HST.AREAS A ON A.ID = VH.AREAID
	JOIN HST.PATHS P ON P.TABLENAME = VH.TABLENAME AND P.COLUMNNAME = VH.COLUMNNAME
	JOIN HST.[ELEMENTS] E ON E.TABLENAME = VH.TABLENAME AND E.ROWID = VH.ROWID
	JOIN HST.ELEMENTVALUES EV0 ON EV0.ID = E.ELEMENT0
	LEFT JOIN HST.ELEMENTVALUES EV1 ON EV0.ID = E.ELEMENT1
	LEFT JOIN HST.ELEMENTVALUES EV2 ON EV0.ID = E.ELEMENT2
	LEFT JOIN HST.ELEMENTVALUES EV3 ON EV0.ID = E.ELEMENT3
	LEFT JOIN HST.ELEMENTVALUES EV4 ON EV0.ID = E.ELEMENT4
	LEFT JOIN HST.[VALUES] OV ON OV.ID = VH.OLDVALUEID
	LEFT JOIN HST.[VALUES] NV ON NV.ID = VH.NEWVALUEID
GO

SET IDENTITY_INSERT HST.AREAS ON;
INSERT INTO HST.AREAS (ID, VALUE) VALUES (1, 'Zadania'), (2, 'Projekty'), (3, 'Zasoby'), (4, 'U�ytkownicy'), (5, 'Zespo�y');
SET IDENTITY_INSERT HST.AREAS OFF;
GO

INSERT INTO HST.PATHS (TABLENAME, COLUMNNAME, VALUE) VALUES 
('RESOURCES', 'ID', N'Zas�b'),
('RESOURCES', 'NAME', N'Zas�b -> Nazwa'),
('RESOURCES', 'DESCRIPTION', N'Zas�b -> Opis'),
('RESOURCES', 'INUSE', N'Zas�b -> W u�yciu');
GO

SET IDENTITY_INSERT HST.[VALUES] ON;
INSERT INTO HST.[VALUES] (ID, [VALUE]) VALUES 
(1, 'Tak'),
(2, 'Nie');
SET IDENTITY_INSERT HST.[VALUES] OFF;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE TRIGGER TR_RESOURCES_History_Insert
   ON RESOURCES 
   AFTER INSERT
AS 
BEGIN
	SET NOCOUNT ON;
	INSERT INTO HST.ELEMENTVALUES (ROWID, TABLENAME, VALUE)
	SELECT I.ID, 'RESOURCES', I.NAME FROM inserted I;

	INSERT INTO HST.ELEMENTS (ROWID, TABLENAME, [LENGTH], ELEMENT0)
	SELECT
		I.ID,
		'RESOURCES',
		1,
		EV.ID
	FROM inserted I
	JOIN HST.ELEMENTVALUES EV ON EV.ROWID = I.ID AND EV.TABLENAME = 'RESOURCES'

	DECLARE @values TABLE
	(
		ROWID INT NOT NULL,
		COLUMNNAME NVARCHAR(128) NOT NULL,
		VALUEID INT NULL
	);

	MERGE HST.[VALUES] I
	USING 
	(  
		SELECT I.ID, 'NAME' COLUMNNAME, I.[NAME] [VALUE] FROM inserted I WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL AND I.[NAME] IS NOT NULL
		UNION ALL
		SELECT I.ID, 'DESCRIPTION' COLUMNNAME, I.[DESCRIPTION] [VALUE] FROM inserted I WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL AND I.[DESCRIPTION] IS NOT NULL
	) SRC ON (1 = 0)
	WHEN NOT MATCHED THEN 
		INSERT ([VALUE])
		VALUES (SRC.[VALUE])
	OUTPUT SRC.ID, SRC.COLUMNNAME, inserted.ID INTO @values;

	INSERT INTO HST.VALUESHISTORY (MODUSER, MODUSERNAME, MODUSERSURNAME, MODCOMP, MODTYPE, AREAID, TABLENAME, COLUMNNAME, ROWID, OLDVALUEID, NEWVALUEID)
	SELECT * FROM
	(
		SELECT 
			I.MODUSER,
			A.NAME,
			A.SURNAME,
			I.MODCOMP,
			0 MODTYPE,
			3 AREAID,
			'RESOURCES' [TABLENAME],
			'NAME' [COLUMNNAME],
			I.ID ROWID,
			NULL OLDVALUEID,
			NV.VALUEID NEWVALUEID
		FROM inserted I
		LEFT JOIN MGMNT.ACCOUNTS A ON A.USERNAME = I.MODUSER
		LEFT JOIN @values NV ON NV.ROWID = I.ID AND NV.COLUMNNAME = 'NAME'
		WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL
		UNION ALL
		SELECT 
			I.MODUSER,
			A.NAME,
			A.SURNAME,
			I.MODCOMP,
			0 MODTYPE,
			3 AREAID,
			'RESOURCES' [TABLENAME],
			'DESCRIPTION' [COLUMNNAME],
			I.ID ROWID,
			NULL OLDVALUEID,
			NV.VALUEID NEWVALUEID
		FROM inserted I
		LEFT JOIN MGMNT.ACCOUNTS A ON A.USERNAME = I.MODUSER
		LEFT JOIN @values NV ON NV.ROWID = I.ID AND NV.COLUMNNAME = 'DESCRIPTION'
		WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL
		UNION ALL
		SELECT 
			I.MODUSER,
			A.NAME,
			A.SURNAME,
			I.MODCOMP,
			0 MODTYPE,
			3 AREAID,
			'RESOURCES' [TABLENAME],
			'INUSE' [COLUMNNAME],
			I.ID ROWID,
			NULL OLDVALUEID,
			IIF(I.INUSE = 1, 1, 2) NEWVALUEID
		FROM inserted I
		LEFT JOIN MGMNT.ACCOUNTS A ON A.USERNAME = I.MODUSER
		WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL
	) AS T 
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE TRIGGER TR_RESOURCES_History_Update
   ON RESOURCES 
   AFTER UPDATE
AS 
BEGIN
	SET NOCOUNT ON;
	MERGE HST.ELEMENTVALUES EV
	USING 
	(  
		SELECT I.ID ROWID, 'RESOURCES' TABLENAME, I.NAME [VALUE] FROM inserted I									
	) SRC ON (EV.ROWID = SRC.ROWID AND EV.TABLENAME = SRC.TABLENAME)
	WHEN NOT MATCHED THEN 
		INSERT (ROWID, TABLENAME, [VALUE]) VALUES (SRC.ROWID, SRC.TABLENAME, SRC.[VALUE])
	WHEN MATCHED AND EV.[VALUE] != SRC.[VALUE] THEN
		UPDATE SET EV.[VALUE] = SRC.[VALUE];

	INSERT INTO HST.ELEMENTS (ROWID, TABLENAME, [LENGTH], ELEMENT0)
	SELECT
		I.ID,
		'RESOURCES',
		1,
		EV.ID
	FROM inserted I
	JOIN HST.ELEMENTVALUES EV ON EV.ROWID = I.ID AND EV.TABLENAME = 'RESOURCES'
	WHERE NOT EXISTS(SELECT * FROM HST.ELEMENTS E WHERE E.ROWID = I.ID AND E.TABLENAME = 'RESOURCES')
	
	DECLARE @values TABLE
	(
		ROWID INT NOT NULL,
		COLUMNNAME NVARCHAR(128) NOT NULL,
		ISNEW BIT NOT NULL,
		VALUEID INT NULL
	);

	MERGE HST.[VALUES] I
	USING 
	(  
		SELECT I.ID, 'NAME' COLUMNNAME, 1 ISNEW, I.[NAME] [VALUE] 
		FROM inserted I
		JOIN deleted D ON D.ID = I.ID
		WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL AND I.[NAME] != D.[NAME]
		UNION ALL
		SELECT I.ID, 'DESCRIPTION' COLUMNNAME, 1 ISNEW, I.[DESCRIPTION] [VALUE] 
		FROM inserted I 
		JOIN deleted D ON D.ID = I.ID
		WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL AND I.[DESCRIPTION] != D.[DESCRIPTION]
		UNION ALL
		SELECT I.ID, 'NAME' COLUMNNAME, 0 ISNEW, D.[NAME] [VALUE] 
		FROM inserted I
		JOIN deleted D ON D.ID = I.ID
		WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL AND I.[NAME] != D.[NAME]
		UNION ALL
		SELECT I.ID, 'DESCRIPTION' COLUMNNAME, 0 ISNEW, D.[DESCRIPTION] [VALUE] 
		FROM inserted I 
		JOIN deleted D ON D.ID = I.ID
		WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL AND I.[DESCRIPTION] != D.[DESCRIPTION]
	) SRC ON (1 = 0)
	WHEN NOT MATCHED THEN 
		INSERT ([VALUE])
		VALUES (SRC.[VALUE]) 
	OUTPUT SRC.ID, SRC.COLUMNNAME, SRC.ISNEW, inserted.ID INTO @values;

	INSERT INTO HST.VALUESHISTORY (MODUSER, MODUSERNAME, MODUSERSURNAME, MODCOMP, MODTYPE, AREAID, TABLENAME, COLUMNNAME, ROWID, OLDVALUEID, NEWVALUEID)
	SELECT * FROM
	(
		SELECT 
			I.MODUSER,
			A.NAME,
			A.SURNAME,
			I.MODCOMP,
			1 MODTYPE,
			3 AREAID,
			'RESOURCES' [TABLENAME],
			'NAME' [COLUMNNAME],
			I.ID ROWID,
			OV.VALUEID OLDVALUEID,
			NV.VALUEID NEWVALUEID
		FROM inserted I
		JOIN deleted D ON D.ID = I.ID
		LEFT JOIN MGMNT.ACCOUNTS A ON A.USERNAME = I.MODUSER
		LEFT JOIN @values NV ON NV.ROWID = I.ID AND NV.COLUMNNAME = 'NAME' AND NV.ISNEW = 1
		LEFT JOIN @values OV ON OV.ROWID = I.ID AND OV.COLUMNNAME = 'NAME' AND OV.ISNEW = 0
		WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL AND I.[NAME] != D.[NAME]
		UNION ALL
		SELECT 
			I.MODUSER,
			A.NAME,
			A.SURNAME,
			I.MODCOMP,
			1 MODTYPE,
			3 AREAID,
			'RESOURCES' [TABLENAME],
			'DESCRIPTION' [COLUMNNAME],
			I.ID ROWID,
			OV.VALUEID OLDVALUEID,
			NV.VALUEID NEWVALUEID
		FROM inserted I
		JOIN deleted D ON D.ID = I.ID
		LEFT JOIN MGMNT.ACCOUNTS A ON A.USERNAME = I.MODUSER
		LEFT JOIN @values NV ON NV.ROWID = I.ID AND NV.COLUMNNAME = 'DESCRIPTION' AND NV.ISNEW = 1
		LEFT JOIN @values OV ON OV.ROWID = I.ID AND OV.COLUMNNAME = 'DESCRIPTION' AND OV.ISNEW = 0
		WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL AND I.[DESCRIPTION] != D.[DESCRIPTION]
		UNION ALL
		SELECT 
			I.MODUSER,
			A.NAME,
			A.SURNAME,
			I.MODCOMP,
			1 MODTYPE,
			3 AREAID,
			'RESOURCES' [TABLENAME],
			'INUSE' [COLUMNNAME],
			I.ID ROWID,
			IIF(D.INUSE = 1, 1, 2) OLDVALUEID,
			IIF(I.INUSE = 1, 1, 2) NEWVALUEID
		FROM inserted I
		JOIN deleted D ON D.ID = I.ID
		LEFT JOIN MGMNT.ACCOUNTS A ON A.USERNAME = I.MODUSER
		WHERE I.MODUSER IS NOT NULL AND I.MODCOMP IS NOT NULL AND I.INUSE != D.INUSE
	) AS T 
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE TRIGGER TR_RESOURCES_History_Delete
   ON RESOURCES 
   AFTER DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	MERGE HST.ELEMENTVALUES EV
	USING 
	(  
		SELECT D.ID ROWID, 'RESOURCES' TABLENAME, D.NAME [VALUE] FROM deleted D									
	) SRC ON (EV.ROWID = SRC.ROWID AND EV.TABLENAME = SRC.TABLENAME)
	WHEN NOT MATCHED THEN 
		INSERT (ROWID, TABLENAME, [VALUE]) VALUES (SRC.ROWID, SRC.TABLENAME, SRC.[VALUE]);

	INSERT INTO HST.ELEMENTS (ROWID, TABLENAME, [LENGTH], ELEMENT0)
	SELECT
		D.ID,
		'RESOURCES',
		1,
		EV.ID
	FROM deleted D
	JOIN HST.ELEMENTVALUES EV ON EV.ROWID = D.ID AND EV.TABLENAME = 'RESOURCES'
	WHERE NOT EXISTS(SELECT * FROM HST.ELEMENTS E WHERE E.ROWID = D.ID AND E.TABLENAME = 'RESOURCES')

	INSERT INTO HST.VALUESHISTORY (MODUSER, MODUSERNAME, MODUSERSURNAME, MODCOMP, MODTYPE, AREAID, TABLENAME, COLUMNNAME, ROWID, OLDVALUEID, NEWVALUEID)
	SELECT 
		D.MODUSER,
		A.NAME,
		A.SURNAME,
		D.MODCOMP,
		2 MODTYPE,
		3 AREAID,
		'RESOURCES' [TABLENAME],
		'ID' [COLUMNNAME],
		D.ID ROWID,
		NULL OLDVALUEID,
		NULL NEWVALUEID
	FROM deleted D
	LEFT JOIN MGMNT.ACCOUNTS A ON A.USERNAME = D.MODUSER
END
GO

