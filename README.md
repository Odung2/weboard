# weboard Java로 만든 간단한 게시판 backend
Java + Spring boot Framework로 만든 게시판 backend입니다.

### DB 구성

#### user / post / comment


#### | user  | CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) DEFAULT NULL,
  `nickname` varchar(30) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_login` datetime DEFAULT CURRENT_TIMESTAMP,
  `login_fail` int DEFAULT '0',
  `is_locked` int DEFAULT '0',
  `last_pw_updated` datetime DEFAULT CURRENT_TIMESTAMP,
  `login_locked` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_defined_id` (`user_id`),
  KEY `updated_by` (`updated_by`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3 |

#### | post  | CREATE TABLE `post` (
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 |

#### | comment | CREATE TABLE `comment` (
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 |


## 기능

### User
#### 1. 회원가입 ([post] weboard/users/signup)
user는 회원가입할 수 있습니다.
#### 2. 로그인 ([post] weboard/users/login)
user는 userId와 password를 통해 로그인할 수 있습니다.
#### 3. 계정 정보 조회 ([get] weboard/users/{id})
user는 본인의 계정 정보를 조회할 수 있습니다.
#### 4. 계정 정보 수정 ([put] weboard/users/{id})
user는 본인의 계정 정보를 수정할 수 있습니다.
#### 5. 계정 정보 삭제(회원 탈퇴) ([del] weboard/users/{id})
user는 본인의 계정 정보를 삭제할 수 있습니다.

### Post
#### 1. 포스트 조회([get] weboard/posts)
게시글을 5개씩 조회 가능합니다. (offset 활용)
#### 2. 개별 포스트 / 댓글 조회([get] weboard/posts/{postId})
각 게시글 및 게시글에 달린 댓글을 조회가능합니다.
#### 3. 포스트 작성([post] weboard/posts)
로그인 후 게시글을 작성할 수 있습니다.
#### 4. 포스트 수정([put] weboard/posts/{postId})
로그인 후 본인이 작성한 게시글을 수정할 수 있습니다.
#### 5. 포스트 삭제([del] weboard/posts/{postId})
로그인 후 본인이 작성한 게시글을 삭제할 수 있습니다.

### Comment
#### 1. 댓글 작성([post] comments/{postId})
로그인 후 게시글에 댓글을 작성할 수 있습니다.
#### 2. 댓글 수정([put] comments/{commentId})
로그인 후 본인이 작성한 댓글을 수정할 수 있습니다.
#### 3. 댓글 삭제([del] comments/{commentId})
로그인 후 본인이 작성한 댓글을 삭제할 수 있습니다.

### Security
#### OAuth 2.0
로그인 시 Access token 및 Refresh token을 발급합니다.

로그인 후 가능한 활동들은 Access token으로 Authentication이 이루어집니다.

Access token이 만료된 후에, Refresh token이 유효하다면 다시 Access token을 발급합니다.

만약 Refresh token도 만료되었다면 다시 로그인 하여 Access token 및 Refresh token을 새로 발급받아야 합니다.


#### 비밀번호 검사
1. 비밀번호는 8~16자리로 이루어져야 합니다.
2. 12자 미만인 경우, 영문 대문자 + 영문 소문자 + 숫자 + 특수문자가 포함되어야 합니다.
3. 12자 이상인 경우, 영문 소문자 + 숫자 + 특수문자가 포함되어야 합니다.

#### 마지막 로그인 검사
마지막 로그인으로부터 1개월이 지났다면, 계정이 잠깁니다.

#### 비밀번호 5회 이상 실패 제한
만약 5회 이상 비밀번호를 틀려 로그인에 실패한다면, 해당 계정의 로그인이 5분간 제한됩니다.

#### 마지막 비밀번호 변경 검사
마지막 비밀번호 변경일로부터 3개월이 지났다면, 비밀번호를 바꾸라는 (에러)메시지가 나타납니다.
