# Order Management Troubleshooting Guide

## Compilation Status
✅ Code compiles successfully with no errors

## Potential Runtime Issues to Check

### 1. Database Connection
- Ensure SQL Server is running on `localhost:1433`
- Database `AsmJava5` exists
- Tables `Orders`, `OrderDetails`, `Accounts`, `Products` exist with proper schema

### 2. Test Data
Check if you have test data in your database:
```sql
-- Check if orders exist
SELECT * FROM Orders;

-- Check if order details exist
SELECT * FROM OrderDetails;

-- Check if accounts exist
SELECT * FROM Accounts;
```

### 3. Application Startup
Run the application:
```bash
.\mvnw.cmd spring-boot:run
```

Then access: `http://localhost:8080/dashboard/order`

### 4. Common Errors and Solutions

#### Error: "No orders found"
- **Cause**: No data in database
- **Solution**: Insert test data into Orders table

#### Error: "Failed to load order details"
- **Cause**: Order relationships not properly loaded
- **Solution**: Check if `@OneToMany` and `@ManyToOne` relationships are correct

#### Error: "CurrencyUtil cannot be autowired"
- **Cause**: Component scanning not picking up the util class
- **Solution**: Verify `@Component` annotation is present (already fixed)

#### Error: "Template not found: order-dashboard"
- **Cause**: Template file not in correct location
- **Solution**: Verify file exists at `src/main/resources/templates/order-dashboard.html`

#### Error: "Fragment not found: order-detail"
- **Cause**: Fragment file missing
- **Solution**: Verify file exists at `src/main/resources/templates/fragments/order-detail.html`

### 5. Testing the Features

#### Test Search Functionality:
1. Go to `/dashboard/order`
2. Enter a customer name in the search box
3. Click Search button
4. Verify filtered results appear

#### Test Status Filter:
1. Select a status from dropdown (e.g., "Pending")
2. Click Search
3. Verify only orders with that status appear

#### Test Date Range:
1. Select a "From Date"
2. Select a "To Date"
3. Click Search
4. Verify only orders within that date range appear

#### Test View Details:
1. Click the eye icon (👁️) button on any order row
2. Modal should open showing order details
3. Verify customer info, order info, and order items display correctly

#### Test Update Status:
1. Click the pencil icon (✏️) button on any order row
2. Modal should open with status dropdown
3. Select a new status
4. Click "Update Status"
5. Verify success message appears
6. Verify order status updated in table

### 6. Browser Console Errors
If modals don't open, check browser console (F12) for JavaScript errors:
- Ensure Bootstrap JS is loaded
- Check for AJAX errors when loading order details

### 7. Network Tab
If order details don't load:
1. Open browser DevTools (F12)
2. Go to Network tab
3. Click "View Details" button
4. Check if `/dashboard/order/detail/{id}` request succeeds
5. Verify response contains HTML fragment

## Quick Test SQL Script

```sql
-- Insert test account if not exists
IF NOT EXISTS (SELECT 1 FROM Accounts WHERE Username = 'testuser')
BEGIN
    INSERT INTO Accounts (Username, Password, Fullname, Email, Phone, Address, Activated, Admin)
    VALUES ('testuser', 'password', 'Test User', 'test@example.com', '0123456789', '123 Test St', 1, 0);
END

-- Insert test order
INSERT INTO Orders (CreateDate, Status, Address, Username)
VALUES (GETDATE(), 'Pending', '123 Test Street, Ho Chi Minh City', 'testuser');

-- Get the order ID
DECLARE @OrderId INT = SCOPE_IDENTITY();

-- Insert test order detail (assuming product ID 1 exists)
INSERT INTO OrderDetails (ProductId, Price, Quantity, OrderId)
VALUES (1, 500000, 2, @OrderId);
```

## Files Modified

1. `src/main/java/poly/edu/controllers/OrderController.java` - Main controller
2. `src/main/java/poly/edu/models/repositories/OrderRepository.java` - Added search query
3. `src/main/java/poly/edu/models/services/OrderServices.java` - Added new methods
4. `src/main/java/poly/edu/models/services/impl/OrderServiceImpl.java` - Implemented new methods
5. `src/main/java/poly/edu/utils/CurrencyUtil.java` - Made it a Spring component
6. `src/main/resources/templates/order-dashboard.html` - Complete rewrite
7. `src/main/resources/templates/fragments/order-detail.html` - New fragment
8. `src/main/resources/static/css/admin.css` - Added status badge styles

## Next Steps

1. Start the application
2. Navigate to `/dashboard/order`
3. If you see errors, check the console output
4. Share the specific error message for further assistance
