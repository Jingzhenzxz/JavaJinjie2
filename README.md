# 煎饼计划 Java 组进阶任务（二）
## 创建数据库表
打开MySQL客户端，输入你的用户名和密码，执行下述代码：

```mysql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS attendance_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 使用数据库
USE attendance_db;

-- 创建群组表
CREATE TABLE `group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci

-- 创建请假表
CREATE TABLE `leave_request` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `reason` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `week_number` int NOT NULL,
  `status` enum('已提交','未提交') COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_leave_request_user` (`user_id`),
  CONSTRAINT `fk_leave_request_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci

-- 创建用户表
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `qq` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userRole` varchar(10) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci

-- 创建周报表
CREATE TABLE `weekly_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `group_id` int DEFAULT NULL,
  `week_number` int NOT NULL,
  `content_completed` text COLLATE utf8mb4_general_ci NOT NULL,
  `content_problems` text COLLATE utf8mb4_general_ci NOT NULL,
  `content_plan` text COLLATE utf8mb4_general_ci NOT NULL,
  `content_link` text COLLATE utf8mb4_general_ci,
  `status` enum('已提交','未提交','已请假') COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_weekly_report_user` (`user_id`),
  KEY `fk_weekly_report_group` (`group_id`),
  CONSTRAINT `fk_weekly_report_group` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`),
  CONSTRAINT `fk_weekly_report_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci

--- 创建用户和群组之间的关系表
CREATE TABLE `user_group` (
  `user_id` int NOT NULL,
  `group_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `fk_user_group_user` (`user_id`),
  KEY `fk_user_group_group` (`group_id`),
  CONSTRAINT `fk_user_group_group` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`),
  CONSTRAINT `fk_user_group_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
```
## 下载并运行项目
下载不用说了。 把application.properties文件中
```properties
spring.datasource.password=yourpassword
```
中的yourpassword改成你的MySQL的密码。然后运行attendanceApplication类。

## 使用 Postman 模拟注册请求
在请求类型中选择 POST，然后在请求 URL 输入框中输入 http://localhost:8086/attendance/api/authentication/register，
并在 "Body" 部分输入你的注册信息，格式为 JSON，例如：

```json
{
  "username": "test",
  "email": "test@example.com",
  "qq": "12345678",
  "password": "test",
  "confirmPassword": "test"
}
```

然后点击 "Send" 按钮。注意，一定要记住密码，数据库里存的不是明文！

## 使用 Postman 模拟登录请求

在请求类型中选择 POST，然后在请求 URL 输入框中输入 http://localhost:8086/attendance/api/authentication/login，在 Headers 中添加一个
”Content-Type“，值为”application/json“，并在 "Body" 部分输入你的登录信息，格式为 JSON，例如：

```json
{
  "email": "test@example.com",
  "password": "test"
}
```
然后点击 "Send" 按钮，你会收到你的 token 和你的用户信息。