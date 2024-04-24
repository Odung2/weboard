-- user 테이블 생성
CREATE TABLE `user` (
                        `id` INT NOT NULL AUTO_INCREMENT,
                        `user_id` VARCHAR(20) DEFAULT NULL,
                        `nickname` VARCHAR(30) DEFAULT NULL,
                        `password` VARCHAR(200) DEFAULT NULL,
                        `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
                        `updated_by` INT DEFAULT NULL,
                        `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        `last_login` DATETIME DEFAULT CURRENT_TIMESTAMP,
                        `login_fail` INT DEFAULT 0,
                        `is_locked` INT DEFAULT 0,
                        `last_pw_updated` DATETIME DEFAULT CURRENT_TIMESTAMP,
                        `login_locked` DATETIME DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `user_defined_id` (`user_id`),
                        KEY `updated_by` (`updated_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- post 테이블 생성
CREATE TABLE `post` (
                        `post_id` int NOT NULL AUTO_INCREMENT,
                        `title` varchar(255) DEFAULT NULL,
                        `created_by` int DEFAULT NULL,
                        `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                        `updated_by` int DEFAULT NULL,
                        `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        `views` int DEFAULT NULL,
                        `contents` mediumtext,
                        `file_path` varchar(200) DEFAULT NULL,
                        PRIMARY KEY (`post_id`),
                        KEY `created_by` (`created_by`),
                        KEY `updated_by` (`updated_by`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;

-- comment 테이블 생성
CREATE TABLE `comment` (
                           `comment_id` int NOT NULL AUTO_INCREMENT,
                           `post_id` int DEFAULT NULL,
                           `user_id` int DEFAULT NULL,
                           `comment_text` varchar(500) DEFAULT NULL,
                           `created_by` int DEFAULT NULL,
                           `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                           `updated_by` int DEFAULT NULL,
                           `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`comment_id`),
                           KEY `post_id` (`post_id`),
                           KEY `created_by` (`created_by`),
                           KEY `updated_by` (`updated_by`),
                           KEY `comment_ibfk_2` (`user_id`),
                           CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`),
                           CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                           CONSTRAINT `comment_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
                           CONSTRAINT `comment_ibfk_4` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
