CREATE TABLE library_books 
USE library_system;

CREATE TABLE library_books (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(100),
    department VARCHAR(100),
    book_name VARCHAR(100),
    issue_date DATE,
    return_date DATE,
    fine INT,
    status VARCHAR(50)
);
select * from library_books