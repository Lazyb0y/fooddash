CREATE TABLE rider_rating (
  id       BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
  rider_id BIGINT   NOT NULL,
  order_id CHAR(36) NOT NULL,
  stars    INT      NOT NULL
);
