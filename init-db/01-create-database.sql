-- Create database if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'AsmJava5')
BEGIN
    CREATE DATABASE AsmJava5;
    PRINT 'Database AsmJava5 created successfully';
END
ELSE
BEGIN
    PRINT 'Database AsmJava5 already exists';
END
GO

USE AsmJava5;
GO

-- Add any initial setup here if needed
PRINT 'Database initialization complete';
GO
