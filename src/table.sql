CREATE TABLE Answers (
                         id INT primary key AUTO_INCREMENT,
                         answer_id INT ,
                         question_id INT,
                         score INT,
                         body TEXT
); -- 每个question拿出20个答案

CREATE TABLE Comments (
                          id INT primary key AUTO_INCREMENT,
                          comment_id INT ,
                          question_id INT,
                          score INT,
                          body TEXT
);  -- 每个question拿出最多20个comment

CREATE TABLE Tags (
                      id INT primary key AUTO_INCREMENT,
                      name VARCHAR(255),
                      score INT,
                      view_count INT,
                      question_id INT
);  -- Tags 自增   但是score和view_count不一样  加入对应的question_id

CREATE TABLE Questions (
                           question_id INT PRIMARY KEY AUTO_INCREMENT,
                           is_answered INT,
                           creation_date datetime,
                           score INT,
                           view_count INT,
                           title TEXT,
                           body TEXT,
                           tags VARCHAR(255)
);
