#!/bin/bash

BASE_URL="http://localhost:8080"

echo "========================================="
echo " FINTRACK END TO END API TEST"
echo "========================================="

echo
echo "1. Register User"

curl -s -X POST "$BASE_URL/auth/register" \
-H "Content-Type: application/json" \
-d '{
"name": "Test User",
"email": "testuser@example.com",
"password": "password123"
}'

echo
echo
echo "2. Login User"

LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
-H "Content-Type: application/json" \
-d '{
"email": "testuser@example.com",
"password": "password123"
}')

TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token')

if [ "$TOKEN" == "null" ] || [ -z "$TOKEN" ]; then
  echo "Login failed. Cannot continue."
  exit 1
fi

echo "JWT Token received"

echo
echo "3. Create Categories"

curl -s -X POST "$BASE_URL/api/categories" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{"name":"Food"}'

curl -s -X POST "$BASE_URL/api/categories" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{"name":"Travel"}'

curl -s -X POST "$BASE_URL/api/categories" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{"name":"Shopping"}'

echo
echo
echo "4. Fetch Categories"

CATEGORIES=$(curl -s "$BASE_URL/api/categories" \
-H "Authorization: Bearer $TOKEN")

echo "$CATEGORIES"

CATEGORY_ID=$(echo "$CATEGORIES" | jq -r '.[0].id')

echo "Using Category ID: $CATEGORY_ID"

echo
echo "5. Create Expense"

curl -s -X POST "$BASE_URL/api/expenses" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d "{
\"amount\":500,
\"description\":\"Lunch\",
\"expenseDate\":\"2026-03-04\",
\"categoryId\":$CATEGORY_ID
}"

echo
echo
echo "6. List Expenses (Pagination)"

EXPENSES=$(curl -s "$BASE_URL/api/expenses?page=0&size=5" \
-H "Authorization: Bearer $TOKEN")

echo "$EXPENSES"

EXPENSE_ID=$(echo "$EXPENSES" | jq -r '.content[0].id')

echo "Expense ID: $EXPENSE_ID"

echo
echo "7. Get Expense By ID"

curl -s "$BASE_URL/api/expenses/$EXPENSE_ID" \
-H "Authorization: Bearer $TOKEN"

echo
echo
echo "8. Update Expense"

curl -s -X PUT "$BASE_URL/api/expenses/$EXPENSE_ID" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
"amount":600,
"description":"Updated Lunch"
}'

echo
echo
echo "9. Test Category Filter"

curl -s "$BASE_URL/api/expenses?categoryId=$CATEGORY_ID" \
-H "Authorization: Bearer $TOKEN"

echo
echo
echo "10. Test Date Filter"

curl -s "$BASE_URL/api/expenses?startDate=2026-03-01&endDate=2026-03-31" \
-H "Authorization: Bearer $TOKEN"

echo
echo
echo "11. Delete Expense"

curl -s -X DELETE "$BASE_URL/api/expenses/$EXPENSE_ID" \
-H "Authorization: Bearer $TOKEN"

echo
echo
echo "12. Verify Deletion"

curl -s "$BASE_URL/api/expenses/$EXPENSE_ID" \
-H "Authorization: Bearer $TOKEN"

echo
echo "========================================="
echo " TEST COMPLETED"
echo "========================================="
