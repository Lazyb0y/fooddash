-- FoodDash schema. Money is DECIMAL, never FLOAT; times are UTC; URLs use the random public_id.

CREATE TABLE customer (
  id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  public_id  CHAR(36)     NOT NULL,
  name       VARCHAR(100) NOT NULL,
  phone      VARCHAR(16)  NOT NULL,
  email      VARCHAR(255) NOT NULL,
  created_at DATETIME(6)  NOT NULL,
  CONSTRAINT uq_customer_public_id UNIQUE (public_id),
  CONSTRAINT uq_customer_phone UNIQUE (phone),
  CONSTRAINT uq_customer_email UNIQUE (email)
);

CREATE TABLE restaurant (
  id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  public_id  CHAR(36)     NOT NULL,
  name       VARCHAR(120) NOT NULL,
  area       VARCHAR(50)  NOT NULL,
  is_open    BOOLEAN      NOT NULL,
  created_at DATETIME(6)  NOT NULL,
  CONSTRAINT uq_restaurant_public_id UNIQUE (public_id)
);

CREATE INDEX ix_restaurant_area ON restaurant (area);

CREATE TABLE menu_item (
  id            BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  public_id     CHAR(36)      NOT NULL,
  restaurant_id BIGINT        NOT NULL,
  name          VARCHAR(120)  NOT NULL,
  price         DECIMAL(10,2) NOT NULL,
  available     BOOLEAN       NOT NULL,
  CONSTRAINT uq_menu_item_public_id UNIQUE (public_id),
  CONSTRAINT fk_menu_item_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant (id),
  CONSTRAINT ck_menu_item_price CHECK (price >= 0)
);

CREATE TABLE rider (
  id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  public_id  CHAR(36)     NOT NULL,
  name       VARCHAR(100) NOT NULL,
  phone      VARCHAR(16)  NOT NULL,
  created_at DATETIME(6)  NOT NULL,
  CONSTRAINT uq_rider_public_id UNIQUE (public_id),
  CONSTRAINT uq_rider_phone UNIQUE (phone)
);

CREATE TABLE orders (
  id             BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  public_id      CHAR(36)      NOT NULL,
  customer_id    BIGINT        NOT NULL,
  restaurant_id  BIGINT        NOT NULL,
  rider_id       BIGINT        NULL,
  status         VARCHAR(20)   NOT NULL,
  subtotal       DECIMAL(10,2) NOT NULL,
  delivery_fee   DECIMAL(10,2) NOT NULL,
  total          DECIMAL(10,2) NOT NULL,
  delivery_line1 VARCHAR(200)  NOT NULL,
  delivery_area  VARCHAR(50)   NOT NULL,
  delivery_phone VARCHAR(16)   NOT NULL,
  created_at     DATETIME(6)   NOT NULL,
  CONSTRAINT uq_orders_public_id UNIQUE (public_id),
  CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customer (id),
  CONSTRAINT fk_orders_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant (id),
  CONSTRAINT fk_orders_rider FOREIGN KEY (rider_id) REFERENCES rider (id),
  CONSTRAINT ck_orders_status CHECK (status IN ('PLACED', 'PAID', 'PICKED_UP', 'DELIVERED', 'CANCELLED')),
  CONSTRAINT ck_orders_total CHECK (total = subtotal + delivery_fee)
);

CREATE TABLE order_item (
  id           BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_id     BIGINT        NOT NULL,
  menu_item_id BIGINT        NOT NULL,
  quantity     INT           NOT NULL,
  unit_price   DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders (id),
  CONSTRAINT fk_order_item_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_item (id),
  CONSTRAINT ck_order_item_quantity CHECK (quantity > 0)
);

CREATE TABLE idempotency_key (
  idem_key     VARCHAR(64) NOT NULL PRIMARY KEY,
  request_hash CHAR(64)    NOT NULL,
  order_id     BIGINT      NOT NULL,
  created_at   DATETIME(6) NOT NULL,
  CONSTRAINT fk_idempotency_key_order FOREIGN KEY (order_id) REFERENCES orders (id)
);
