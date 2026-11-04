package com.fooddash.order;

import org.springframework.data.jpa.repository.JpaRepository;

interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, String> {}
