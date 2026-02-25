-- Test if you have orders in the database
-- Run these queries in SQL Server Management Studio or your SQL client

-- Check if Orders table exists and has data
SELECT COUNT(*) AS TotalOrders FROM Orders;

-- Check first 10 orders with account info
SELECT TOP 10 
    o.Id,
    o.CreateDate,
    o.Status,
    o.Address,
    o.Username,
    a.Fullname,
    a.Email,
    a.Phone
FROM Orders o
LEFT JOIN Accounts a ON o.Username = a.Username
ORDER BY o.CreateDate DESC;

-- Check if there are any orders without account info (this would cause errors)
SELECT COUNT(*) AS OrdersWithoutAccount
FROM Orders o
LEFT JOIN Accounts a ON o.Username = a.Username
WHERE a.Username IS NULL;

-- If you have no orders, insert a test order:
-- First, make sure you have a test account
IF NOT EXISTS (SELECT 1 FROM Accounts WHERE Username = 'testadmin')
BEGIN
    INSERT INTO Accounts (Username, Password, Fullname, Email, Phone, Address, Activated, Admin)
    VALUES ('testadmin', 'admin123', 'Test Admin', 'admin@test.com', '0123456789', '123 Test Street', 1, 1);
END

-- Insert a test order
IF NOT EXISTS (SELECT 1 FROM Orders WHERE Id = 999)
BEGIN
    INSERT INTO Orders (CreateDate, Status, Address, Username)
    VALUES (GETDATE(), 'Pending', '456 Order Street, Ho Chi Minh City', 'testadmin');
END
