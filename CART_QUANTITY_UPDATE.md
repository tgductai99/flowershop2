# Cart Quantity Update Feature

## Overview
Added functionality to adjust item quantities in the shopping cart with an editable input field, stock validation, and automatic removal when quantity reaches zero.

## Changes Made

### 1. ClientController.java
**Location:** `src/main/java/poly/edu/controllers/ClientController.java`

#### Method: `updateQuantity()`
```java
@PostMapping("/cart/update")
public String updateQuantity(
    @RequestParam Integer productId,
    @RequestParam Integer quantity,
    @ModelAttribute("order") Order order,
    RedirectAttributes redirect
)
```

**Features:**
- **Automatic Removal**: If quantity is 0 or less, removes item from cart
- **Stock Validation**: Checks if requested quantity exceeds available stock
- **Real-time Stock Check**: Fetches fresh product data to verify current stock levels
- **User Feedback**: Provides appropriate success/error messages

**Validation Rules:**
1. If `quantity <= 0` → Remove item from cart
2. If `quantity > product.amount` → Show "quantity exceeded" error
3. Otherwise → Update quantity and show success message

### 2. checkout.html
**Location:** `src/main/resources/templates/checkout.html`

#### Updated Quantity Display
Replaced static badge with interactive quantity controls including an editable input field:

**Components:**
1. **Decrease Button** (-)
   - Always enabled (can go down to 0)
   - Submits form with `quantity - 1`
   - Icon: `bi-dash`

2. **Editable Quantity Input**
   - Type: `number`
   - Width: 70px
   - Min: 0 (allows removal)
   - Max: Dynamic (based on stock)
   - Auto-submit on change
   - Real-time validation via JavaScript

3. **Increase Button** (+)
   - Disabled when quantity >= available stock
   - Submits form with `quantity + 1`
   - Icon: `bi-plus`

4. **Stock Indicator**
   - Shows available stock below controls
   - Format: "Stock: X"

#### JavaScript Validation Function
```javascript
function validateQuantity(input, maxStock) {
    // Remove non-numeric characters
    input.value = input.value.replace(/[^0-9]/g, '');
    
    // Parse the value
    let value = parseInt(input.value);
    
    // Handle empty or invalid input
    if (isNaN(value) || input.value === '') {
        input.value = 0;
        return;
    }
    
    // If value exceeds stock, set to max stock
    if (value > maxStock) {
        input.value = maxStock;
    }
    
    // If value is negative, set to 0
    if (value < 0) {
        input.value = 0;
    }
}
```

**Validation Features:**
- Strips non-numeric characters
- Automatically caps value at available stock
- Prevents negative values
- Handles empty input (sets to 0)

#### New Alert Messages
Added three new alert types:

1. **quantityUpdated** (Success)
   - "Cart quantity updated successfully!"
   - Green alert with check icon

2. **quantityExceeded** (Warning)
   - "Requested quantity exceeds available stock."
   - Yellow alert with warning icon

3. **insufficientStock** (Warning)
   - "Some products in your cart don't have enough stock. Please adjust quantities."
   - Yellow alert with warning icon

## User Experience

### Adjusting Quantity via Buttons
1. User clicks **+** or **-** button
2. Form submits to `/cart/update` endpoint
3. System validates against available stock
4. Updates quantity and shows success message

### Adjusting Quantity via Input
1. User clicks on quantity input field
2. Types desired quantity
3. JavaScript validates in real-time:
   - Removes non-numeric characters
   - Caps at available stock
   - Prevents negative values
4. On blur or Enter key, form auto-submits
5. Server validates and updates cart

### Automatic Removal
- When user sets quantity to 0 (via input or minus button), item is automatically removed
- Success message confirms update

### Stock Limits
- **Input field**: Automatically adjusts to max stock if user types higher value
- **+** button: Disabled when cart quantity equals available stock
- **-** button: Always enabled (can go to 0)
- Stock amount displayed below controls for transparency

## UI Layout

```
Product | Price | Quantity                    | Subtotal | Action
--------|-------|----------------------------|----------|--------
Image   | $XX   | [-] [Input: 2] [+]        | $XXX     | [Trash]
Name    |       | Stock: 10                  |          |
```

## Input Field Behavior

### Normal Input
```
User types: "5"
Stock: 10
Result: Accepts 5, submits on change
```

### Exceeding Stock
```
User types: "15"
Stock: 10
JavaScript: Automatically changes to "10"
Result: Submits 10
```

### Non-numeric Input
```
User types: "5abc"
JavaScript: Strips to "5"
Result: Accepts 5
```

### Zero Input
```
User types: "0"
Result: Submits 0, removes item from cart
```

### Empty Input
```
User clears field
JavaScript: Sets to "0"
Result: Submits 0, removes item from cart
```

## Benefits

1. **Flexible Input**: Users can type exact quantity instead of clicking multiple times
2. **Smart Validation**: Automatically prevents invalid inputs
3. **Stock Protection**: Cannot exceed available stock
4. **Auto-removal**: Items with 0 quantity are automatically removed
5. **Real-time Feedback**: JavaScript validation provides instant feedback
6. **User-friendly**: Combines button controls with direct input
7. **Mobile Friendly**: Works well on touch devices

## Technical Details

### Form Submission
- Uses POST method for data modification
- Each control (-, input, +) has its own form
- Hidden inputs pass productId
- Input field auto-submits on change event
- Leverages Spring's `@SessionAttributes` for cart persistence

### JavaScript Validation
- Runs on `oninput` event (real-time)
- Validates before form submission
- Prevents invalid data from reaching server
- Uses Thymeleaf inline JavaScript for dynamic max values

### Stock Validation
- Client-side: JavaScript prevents exceeding stock
- Server-side: Controller validates against current stock
- Double validation ensures data integrity

### Session Management
- Cart stored in session via `@SessionAttributes("order")`
- Direct manipulation of `order.orderDetails` list
- Changes persist across requests

## Testing Recommendations

1. Test typing valid quantities (1-10)
2. Test typing quantity exceeding stock
3. Test typing 0 (should remove item)
4. Test typing negative numbers
5. Test typing non-numeric characters
6. Test clearing the input field
7. Test using +/- buttons
8. Test with multiple items in cart
9. Test on mobile devices (touch input)
10. Test rapid input changes
