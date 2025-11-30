CREATE TABLE IF NOT EXISTS devices (
  device_id VARCHAR(50) PRIMARY KEY,
  phone_number VARCHAR(20),
  last_heartbeat DATETIME,
  status ENUM('online','offline')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS messages (
  message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  device_id VARCHAR(50),
  sender VARCHAR(20),
  content TEXT,
  receive_time DATETIME,
  FOREIGN KEY (device_id) REFERENCES devices(device_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_messages_device_time ON messages(device_id, receive_time);
CREATE INDEX idx_devices_last_heartbeat ON devices(last_heartbeat);
