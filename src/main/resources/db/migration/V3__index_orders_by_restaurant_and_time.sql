-- Restaurant order history (GET /api/v1/restaurants/{id}/orders) filters by restaurant and sorts by time.
-- With only the foreign key's index, MySQL read all of a busy restaurant's orders and sorted them
-- (EXPLAIN: "Using filesort", 60,002 rows for 20). This index returns the newest 20 directly.
-- MySQL then drops the foreign key's own index on restaurant_id: this one starts with that column.
CREATE INDEX ix_orders_restaurant_created ON orders (restaurant_id, created_at);
