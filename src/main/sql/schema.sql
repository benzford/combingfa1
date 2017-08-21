CREATE DATABASE seckill;
USE seckill;
CREATE TABLE seckill (
  `seckill_id`  BIGINT       NOT NULL  AUTO_INCREMENT
  COMMENT '商品库存id',
  `name`        VARCHAR(120) NOT NULL
  COMMENT '商品名称',
  `number`      INT          NOT NULL
  COMMENT '商品数量',
  `start_time`  TIMESTAMP    NOT NULL
  COMMENT '秒杀开始时间',
  `end_time`    TIMESTAMP    NOT NULL
  COMMENT '秒杀结束时间',
  `create_time` TIMESTAMP    NOT NULL  DEFAULT current_timestamp
  COMMENT '创建时间',
  PRIMARY KEY (seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
)
  ENGINE = innodb
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
  COMMENT ='秒杀库存表';


INSERT INTO
  seckill (name, number, start_time, end_time)
VALUES
  ('1000元秒杀iphone6', 100, '2015-01-01 00:00:00', '2015-01-02 00:00:00'),
  ('500元秒杀ipad2', 100, '2015-01-01 00:00:00', '2015-01-02 00:00:00'),
  ('300元秒杀小米4', 100, '2015-01-01 00:00:00', '2015-01-02 00:00:00'),
  ('200元秒杀秒杀红米note', 100, '2015-01-01 00:00:00', '2015-01-02 00:00:00');

CREATE TABLE success_killed (
  `seckill_id`  BIGINT    NOT NULL AUTO_INCREMENT
  COMMENT '秒杀商品id',
  `user_phone`  BIGINT    NOT NULL
  COMMENT '用户手机号',
  `state`       TINYINT   NOT NULL DEFAULT -1
  COMMENT '状态表示:-1:失败 0:成功 1:已付款 失败:已发货',
  `create_time` TIMESTAMP NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`, `user_phone`)/*联合主键*/,
  KEY idx_create_time(`create_time`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8
  COMMENT ='秒杀成功明细表';


mysql -uroot -p;
