-- Demo data with fixed public IDs, so the class demos and the Swagger examples always work.
-- Foreign keys are looked up by public_id, so the inserts don't depend on generated numeric IDs.

INSERT INTO customer (public_id, name, phone, email, created_at) VALUES
  ('c0000000-0000-4000-8000-000000000001', 'Nusrat Jahan', '+8801711000001', 'nusrat@example.com', '2026-01-05 03:00:00'),
  ('c0000000-0000-4000-8000-000000000002', 'Tanvir Hasan', '+8801711000002', 'tanvir@example.com', '2026-01-06 04:00:00');

INSERT INTO restaurant (public_id, name, area, is_open, created_at) VALUES
  ('d0000000-0000-4000-8000-000000000001', 'Dhanmondi Kacchi House', 'Dhanmondi', TRUE, '2026-01-01 03:00:00'),
  ('d0000000-0000-4000-8000-000000000002', 'Old Town Biryani', 'Old Dhaka', FALSE, '2026-01-01 03:00:00'),
  ('d0000000-0000-4000-8000-000000000003', 'Gulshan Grill', 'Gulshan', TRUE, '2026-01-01 03:00:00'),
  ('d0000000-0000-4000-8000-000000000004', 'Mirpur Fuchka Corner', 'Mirpur', TRUE, '2026-01-01 03:00:00'),
  ('d0000000-0000-4000-8000-000000000005', 'Banani Bowl', 'Banani', TRUE, '2026-01-01 03:00:00'),
  ('d0000000-0000-4000-8000-000000000006', 'হাজী বিরিয়ানি (Haji Biryani)', 'Old Dhaka', TRUE, '2026-01-01 03:00:00'),
  ('d0000000-0000-4000-8000-000000000007', 'Uttara Pizza Point', 'Uttara', TRUE, '2026-01-01 03:00:00'),
  ('d0000000-0000-4000-8000-000000000008', 'Mohammadpur Tehari Ghor', 'Mohammadpur', FALSE, '2026-01-01 03:00:00');

INSERT INTO menu_item (public_id, restaurant_id, name, price, available)
SELECT m.public_id, r.id, m.name, m.price, m.available
FROM restaurant r
JOIN (
  SELECT 'e0000000-0000-4000-8000-000000000001' AS public_id, 'd0000000-0000-4000-8000-000000000001' AS restaurant, 'Kacchi Biryani' AS name, 350.00 AS price, TRUE AS available
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000002', 'd0000000-0000-4000-8000-000000000001', 'Beef Tehari', 220.00, TRUE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000003', 'd0000000-0000-4000-8000-000000000001', 'Borhani', 60.00, TRUE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000004', 'd0000000-0000-4000-8000-000000000001', 'Firni', 80.00, FALSE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000005', 'd0000000-0000-4000-8000-000000000002', 'Mutton Biryani', 320.00, TRUE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000006', 'd0000000-0000-4000-8000-000000000003', 'Chicken Steak', 450.00, TRUE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000007', 'd0000000-0000-4000-8000-000000000003', 'Grilled Fish', 520.00, TRUE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000008', 'd0000000-0000-4000-8000-000000000004', 'Fuchka (8 pieces)', 120.00, TRUE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000009', 'd0000000-0000-4000-8000-000000000004', 'Chotpoti', 100.00, TRUE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000010', 'd0000000-0000-4000-8000-000000000005', 'Chicken Rice Bowl', 380.00, TRUE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000011', 'd0000000-0000-4000-8000-000000000006', 'কাচ্চি (Kacchi)', 300.00, TRUE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000012', 'd0000000-0000-4000-8000-000000000007', 'Margherita Pizza', 650.00, TRUE
  UNION ALL SELECT 'e0000000-0000-4000-8000-000000000013', 'd0000000-0000-4000-8000-000000000008', 'Tehari', 180.00, TRUE
) m ON m.restaurant = r.public_id;

INSERT INTO rider (public_id, name, phone, created_at) VALUES
  ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa', 'Rafiq Islam', '+8801811000001', '2026-01-02 03:00:00'),
  ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb', 'Sumon Ahmed', '+8801811000002', '2026-01-02 03:00:00');

-- Orders for the demos: 1111 is PLACED (cancel -> 200), 2222 is PICKED_UP (cancel -> 409),
-- 3333 is PAID with no rider (assignment demo), 4444 belongs to another customer (404).
INSERT INTO orders (public_id, customer_id, restaurant_id, rider_id, status, subtotal, delivery_fee, total,
                    delivery_line1, delivery_area, delivery_phone, created_at)
SELECT o.public_id, c.id, r.id, d.id, o.status, o.subtotal, o.fee, o.total, o.line1, o.area, o.phone, o.created_at
FROM (
  SELECT '11111111-1111-4111-8111-111111111111' AS public_id, 'c0000000-0000-4000-8000-000000000001' AS customer,
         'd0000000-0000-4000-8000-000000000001' AS restaurant, CAST(NULL AS CHAR(36)) AS rider, 'PLACED' AS status,
         700.00 AS subtotal, 40.00 AS fee, 740.00 AS total, 'House 12, Road 5' AS line1, 'Dhanmondi' AS area,
         '+8801711000001' AS phone, TIMESTAMP '2026-01-10 06:30:00' AS created_at
  UNION ALL SELECT '22222222-2222-4222-8222-222222222222', 'c0000000-0000-4000-8000-000000000001',
         'd0000000-0000-4000-8000-000000000001', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa', 'PICKED_UP',
         280.00, 40.00, 320.00, 'House 12, Road 5', 'Dhanmondi', '+8801711000001', TIMESTAMP '2026-01-11 06:45:00'
  UNION ALL SELECT '33333333-3333-4333-8333-333333333333', 'c0000000-0000-4000-8000-000000000001',
         'd0000000-0000-4000-8000-000000000003', NULL, 'PAID',
         450.00, 40.00, 490.00, 'House 12, Road 5', 'Dhanmondi', '+8801711000001', TIMESTAMP '2026-01-12 07:00:00'
  UNION ALL SELECT '44444444-4444-4444-8444-444444444444', 'c0000000-0000-4000-8000-000000000002',
         'd0000000-0000-4000-8000-000000000004', NULL, 'PAID',
         240.00, 40.00, 280.00, 'Flat 3B, Road 10', 'Mirpur', '+8801711000002', TIMESTAMP '2026-01-12 08:15:00'
) o
JOIN customer c ON c.public_id = o.customer
JOIN restaurant r ON r.public_id = o.restaurant
LEFT JOIN rider d ON d.public_id = o.rider;

INSERT INTO order_item (order_id, menu_item_id, quantity, unit_price)
SELECT o.id, m.id, i.quantity, i.unit_price
FROM (
  SELECT '11111111-1111-4111-8111-111111111111' AS ord, 'e0000000-0000-4000-8000-000000000001' AS item, 2 AS quantity, 350.00 AS unit_price
  UNION ALL SELECT '22222222-2222-4222-8222-222222222222', 'e0000000-0000-4000-8000-000000000002', 1, 220.00
  UNION ALL SELECT '22222222-2222-4222-8222-222222222222', 'e0000000-0000-4000-8000-000000000003', 1, 60.00
  UNION ALL SELECT '33333333-3333-4333-8333-333333333333', 'e0000000-0000-4000-8000-000000000006', 1, 450.00
  UNION ALL SELECT '44444444-4444-4444-8444-444444444444', 'e0000000-0000-4000-8000-000000000008', 2, 120.00
) i
JOIN orders o ON o.public_id = i.ord
JOIN menu_item m ON m.public_id = i.item;
