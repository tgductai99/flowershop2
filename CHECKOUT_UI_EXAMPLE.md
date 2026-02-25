# Checkout Page UI - Editable Quantity Controls

## Visual Layout

### Cart Item Row Example

```
┌─────────────────────────────────────────────────────────────────────────────┐
│ Product              │ Price      │ Quantity           │ Subtotal │ Action  │
├─────────────────────────────────────────────────────────────────────────────┤
│ [Image] Rose Bouquet │ 500,000 ₫  │ [-] [  3  ] [+]   │1,500,000₫│ [🗑️]   │
│                      │            │ Stock: 10          │          │         │
├─────────────────────────────────────────────────────────────────────────────┤
│ [Image] Tulip Basket │ 750,000 ₫  │ [-] [  1  ] [+]   │ 750,000₫ │ [🗑️]   │
│                      │            │ Stock: 5           │          │         │
└─────────────────────────────────────────────────────────────────────────────┘
```

Note: The quantity number is now an editable input field!

## Input Field States

### Normal State (Editable)
```
Quantity: 3, Stock: 10

[-] [ 3 ] [+]  ← Input field is editable, all buttons enabled
Stock: 10
```

### At Maximum (Quantity = Stock)
```
Quantity: 10, Stock: 10

[-] [ 10 ] [🚫]  ← Plus button disabled, can still edit input
Stock: 10
```

### At Zero (Will Remove)
```
Quantity: 0, Stock: 10

[-] [ 0 ] [+]  ← Setting to 0 will remove item on submit
Stock: 10
```

## Input Field Behavior

### Typing Valid Quantity
```
User clicks input: [-] [|3|] [+]
User types "5":    [-] [|5|] [+]
User presses Enter or clicks away
→ Form submits, quantity updated to 5

✓ Cart quantity updated successfully!
```

### Typing Quantity Over Stock
```
Stock: 10
User types "15":   [-] [|15|] [+]
JavaScript auto-corrects: [-] [|10|] [+]
User presses Enter
→ Form submits with quantity 10

✓ Cart quantity updated successfully!
```

### Typing Zero
```
User types "0":    [-] [|0|] [+]
User presses Enter
→ Form submits, item removed from cart

✓ Cart quantity updated successfully!
```

### Typing Non-numeric Characters
```
User types "5abc": [-] [|5abc|] [+]
JavaScript strips: [-] [|5|] [+]
→ Only numeric characters remain
```

### Clearing Input
```
User selects all and deletes: [-] [||] [+]
JavaScript sets to 0:         [-] [|0|] [+]
User presses Enter
→ Item removed from cart
```

## Button Interactions

### Scenario 1: Click Plus Button
```
Initial State:
[-] [ 2 ] [+]
Stock: 10

User clicks [+]
↓
Updated State:
[-] [ 3 ] [+]
Stock: 10

✓ Cart quantity updated successfully!
```

### Scenario 2: Click Minus Button
```
Initial State:
[-] [ 3 ] [+]
Stock: 10

User clicks [-]
↓
Updated State:
[-] [ 2 ] [+]
Stock: 10

✓ Cart quantity updated successfully!
```

### Scenario 3: Click Minus to Zero (Auto-remove)
```
Initial State:
[-] [ 1 ] [+]
Stock: 10

User clicks [-]
↓
Item removed from cart

✓ Cart quantity updated successfully!
```

## Alert Messages

### Success - Quantity Updated
```
┌─────────────────────────────────────────────────────────────────┐
│ ✓ Cart quantity updated successfully!                      [×] │
└─────────────────────────────────────────────────────────────────┘
```

### Warning - Quantity Exceeded (Server-side validation)
```
┌─────────────────────────────────────────────────────────────────┐
│ ⚠ Requested quantity exceeds available stock.              [×] │
└─────────────────────────────────────────────────────────────────┘
```

### Warning - Insufficient Stock (at checkout)
```
┌─────────────────────────────────────────────────────────────────┐
│ ⚠ Some products in your cart don't have enough stock.      [×] │
│   Please adjust quantities.                                     │
└─────────────────────────────────────────────────────────────────┘
```

## JavaScript Validation Examples

### Example 1: Exceeding Stock
```javascript
Input: "25"
Stock: 10
JavaScript: validateQuantity(input, 10)
Result: input.value = "10"
```

### Example 2: Negative Number
```javascript
Input: "-5"
JavaScript: Strips to "5"
Result: input.value = "5"
```

### Example 3: Mixed Characters
```javascript
Input: "3.5abc"
JavaScript: Strips to "35"
Result: input.value = "35"
Then checks stock: if 35 > 10, sets to "10"
```

### Example 4: Empty Input
```javascript
Input: ""
JavaScript: Sets to "0"
Result: input.value = "0"
```

## Mobile Responsive Design

### Desktop View (≥992px)
```
┌──────────────────────────────────────────────────────────────┐
│ [Image] Product Name  │ Price │ [-][ 3 ][+] │ Subtotal │ [🗑️] │
│                       │       │ Stock: 10   │          │      │
└──────────────────────────────────────────────────────────────┘
```

### Tablet View (768px - 991px)
```
┌────────────────────────────────────────────────────┐
│ [Image] Product Name  │ Price │ [-][ 3 ][+] │ [🗑️]  │
│                       │       │ Stock: 10   │       │
│                       │       │ Subtotal: $XXX     │
└────────────────────────────────────────────────────┘
```

### Mobile View (<768px)
```
┌──────────────────────────────┐
│ [Image] Product Name         │
│ Price: $XX                   │
│ [-] [ 3 ] [+]  Stock: 10     │
│ Subtotal: $XXX          [🗑️] │
└──────────────────────────────┘
```

## CSS Classes Used

### Input Field
- `form-control form-control-sm text-center` - Bootstrap form control, small size, centered text
- `width: 70px` - Fixed width for consistent layout

### Buttons
- `btn btn-sm btn-outline-secondary` - Quantity adjustment buttons
- `btn btn-sm btn-outline-danger` - Remove button

### Layout
- `d-flex align-items-center justify-content-center gap-2` - Quantity controls container
- `d-inline` - Inline forms
- `text-muted` - Stock label

### Icons
- `bi-dash` - Decrease icon
- `bi-plus` - Increase icon
- `bi-trash` - Remove icon

## HTML Attributes

### Input Field Attributes
```html
<input type="number" 
       name="quantity" 
       th:value="${item.quantity}"
       th:max="${item.product.amount}"
       min="0"
       class="form-control form-control-sm text-center"
       style="width: 70px;"
       onchange="this.form.submit()"
       oninput="validateQuantity(this, [[${item.product.amount}]])">
```

**Key Attributes:**
- `type="number"` - Numeric keyboard on mobile
- `min="0"` - Allows zero (for removal)
- `th:max` - Dynamic max based on stock
- `onchange` - Auto-submit when user finishes editing
- `oninput` - Real-time validation as user types

## Accessibility Features

1. **Keyboard Navigation**: Input field is fully keyboard accessible
2. **Visual Feedback**: Stock information always visible
3. **Clear Actions**: Icons and buttons clearly indicate purpose
4. **Confirmation**: Success messages confirm actions
5. **Error Prevention**: JavaScript prevents invalid inputs before submission
6. **Touch Friendly**: Large enough touch targets for mobile users
7. **Number Input**: Mobile devices show numeric keyboard
