-- user 테이블 생성
CREATE TABLE `user` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `user_id` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                  `nickname` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                  `password` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                  `updated_by` int DEFAULT NULL,
                                  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `last_login_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                  `login_fail_count` int DEFAULT '0',
                                  `is_locked` tinyint(1) DEFAULT '0',
                                  `pw_updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                  `login_locked_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `user_defined_id` (`user_id`),
                                  KEY `updated_by` (`updated_by`)
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

-- post 테이블 생성
CREATE TABLE `post` (
                                  `post_id` int NOT NULL AUTO_INCREMENT,
                                  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                  `created_by` int DEFAULT NULL,
                                  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                  `updated_by` int DEFAULT NULL,
                                  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `views` int DEFAULT NULL,
                                  `contents` longtext COLLATE utf8mb4_unicode_ci,
                                  `file_path` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                  PRIMARY KEY (`post_id`),
                                  KEY `created_by` (`created_by`),
                                  KEY `updated_by` (`updated_by`)
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

-- comment 테이블 생성
CREATE TABLE `comment` (
                           `comment_id` int NOT NULL AUTO_INCREMENT,
                           `post_id` int DEFAULT NULL,
                           `user_id` int DEFAULT NULL,
                           `comment_text` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                           `created_by` int DEFAULT NULL,
                           `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                           `updated_by` int DEFAULT NULL,
                           `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`comment_id`),
                           KEY `created_by` (`created_by`),
                           KEY `updated_by` (`updated_by`),
                           KEY `comment_ibfk_2` (`user_id`),
                           KEY `comment_ibfk_1` (`post_id`),
                           CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci