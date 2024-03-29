show databases;
create database task_management;
drop database task_management;
use task_management;
show tables;
desc task;
select * from user;
select * from task;
delete from user;
select * from project;
INSERT INTO user (employee_name, user_name, mobile_number, employee_role, password) 
VALUES ('John Doe', 'john.doe', '1234567890', 'ADMIN', 'password123'),
('Jane Smith', 'jane.smith', '9876543210', 'USER', 'pass123'), 
('Bob Johnson', 'bob.johnson', '5551234567', 'USER', 'securepass'),
('Alice Turner', 'alice.turner', '1112223333', 'MANAGER', 'abc@123'),
('Charlie Brown', 'charlie.brown', '9998887777', 'USER', 'brownie321'),
('Eva Martinez', 'eva.martinez', '4445556666', 'ADMIN', 'evaPass!'),
('David Williams', 'david.williams', '7778889999', 'USER', 'davidPass'),
('Sophie Adams', 'sophie.adams', '1239876543', 'MANAGER', 'sophie123'),
('Franklin White', 'franklin.white', '5556667777', 'ADMIN', 'franklinPass'),
('Grace Mitchell', 'grace.mitchell', '9991112222', 'USER', 'gracePass'),
('Oliver Davis', 'oliver.davis', '1114447777', 'MANAGER', 'oliver123'),
('Zoe Clark', 'zoe.clark', '3336669999', 'ADMIN', 'zoePass'), 
('Daniel Lee', 'daniel.lee', '7775553333', 'USER', 'danielPass'),
('Mia Hall', 'mia.hall', '1112223333', 'MANAGER', 'miaPass!'),
('William Turner', 'william.turner', '4448882222', 'ADMIN', 'williamPass');

-- Project table insert queries
-- Example 1
INSERT INTO project (project_name, project_description, start_date, end_date) 
VALUES ('Project Alpha', 'Description for Project Alpha', '2024-01-01', '2024-03-31'),
('Project Beta', 'Description for Project Beta', '2024-02-15', '2024-05-15'),
('Project Gamma', 'Description for Project Gamma', '2024-03-10', '2024-06-30'),
('Project Delta', 'Description for Project Delta', '2024-04-05', '2024-07-15'),
('Project Epsilon', 'Description for Project Epsilon', '2024-05-20', '2024-08-31'),
('Project Zeta', 'Description for Project Zeta', '2024-06-15', '2024-10-10'),
('Project Eta', 'Description for Project Eta', '2024-07-01', '2024-11-30'),
('Project Theta', 'Description for Project Theta', '2024-08-10', '2025-01-15'),
('Project Iota', 'Description for Project Iota', '2024-09-05', '2025-02-28'),
('Project Kappa', 'Description for Project Kappa', '2024-10-20', '2025-03-31'),
('Project Lambda', 'Description for Project Lambda', '2024-11-15', '2025-05-10'),
('Project Mu', 'Description for Project Mu', '2024-12-01', '2025-06-30'),
('Project Nu', 'Description for Project Nu', '2025-01-10', '2025-08-15'),
('Project Xi', 'Description for Project Xi', '2025-02-05', '2025-09-30'),
('Project Omicron', 'Description for Project Omicron', '2025-03-20', '2025-12-31');

-- Task table insert queries
-- Example 1
desc task;
INSERT INTO task (task_title, task_description, priority, status, due_date) 
VALUES 
  ('Task 1', 'Description for Task 1', 1, 1, '2024-01-15'),
  ('Task 2', 'Description for Task 2', 2, 2, '2024-02-28'),
  ('Task 3', 'Description for Task 3', 3, 3, '2024-03-20'),
  ('Task 4', 'Description for Task 4', 1, 1, '2024-04-05'),
  ('Task 5', 'Description for Task 5', 2, 2, '2024-05-20'),
  ('Task 6', 'Description for Task 6', 3, 3, '2024-06-15'),
  ('Task 7', 'Description for Task 7', 1, 1, '2024-07-01'),
  ('Task 8', 'Description for Task 8', 2, 2, '2024-08-10'),
  ('Task 9', 'Description for Task 9', 3, 3, '2024-09-05'),
  ('Task 10', 'Description for Task 10', 1, 1, '2024-10-20'),
  ('Task 11', 'Description for Task 11', 2, 2, '2024-11-15'),
  ('Task 12', 'Description for Task 12', 3, 3, '2024-12-01'),
  ('Task 13', 'Description for Task 13', 1, 1, '2025-01-10'),
  ('Task 14', 'Description for Task 14', 2, 2, '2025-02-05'),
  ('Task 15', 'Description for Task 15', 3, 3, '2025-03-20');
