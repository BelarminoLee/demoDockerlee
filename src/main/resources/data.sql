--- CONFIGURATIONS
INSERT INTO ru_configuration(id,property,type,cfg_value,description,tag,create_date)
VALUES (1,'ussd_service_status','NUMERIC','1','Configuration to set availability of the ussd service','ussd',CURRENT_DATE);
--- ROLES
INSERT INTO ru_role(role_id,label,description,create_date) VALUES ('sadmin','System Administrator','Admin Role', CURRENT_DATE);
INSERT INTO ru_role(role_id,label,description,create_date) VALUES ('hr','Human Resources','HR Role', CURRENT_DATE);
INSERT INTO ru_role(role_id,label,description,create_date) VALUES ('supervisor','Supervisor','Supervisor Role', CURRENT_DATE);
INSERT INTO ru_role(role_id,label,description,create_date) VALUES ('coordinator','Coordinator','Coordinator Role', CURRENT_DATE);
--- ADMIN USERS
INSERT INTO ru_user(user_id,username,email,password_hash,first_name,last_name,role_id,locked,create_date) VALUES (1,'sadmin','sadmin@attendance.co.mz', '$2a$12$OIguBmTbquP3H5PLghH.Xe8CScJdbPuDGIiBiIszWVqXMYOoufqwC', 'System', 'Administrador','sadmin', false, CURRENT_DATE);
--- LOCATION
INSERT INTO ru_country (id, create_date, name)
VALUES (1, CURRENT_DATE, 'Moçambique');
INSERT INTO ru_province (id, create_date, name, country_id)
VALUES (1, CURRENT_DATE, 'Cabo Delgado', 1),
       (2, CURRENT_DATE, 'Gaza', 1),
       (3, CURRENT_DATE, 'Inhambane', 1),
       (4, CURRENT_DATE, 'Manica', 1),
       (5, CURRENT_DATE, 'Maputo (Cidade)', 1),
       (6, CURRENT_DATE, 'Maputo (Provincia)', 1),
       (7, CURRENT_DATE, 'Nampula', 1),
       (8, CURRENT_DATE, 'Niassa', 1),
       (9, CURRENT_DATE, 'Sofala', 1),
       (10, CURRENT_DATE, 'Tete', 1),
       (11, CURRENT_DATE, 'Zambezia', 1);
INSERT INTO ru_district (id, create_date, name, province_id)
VALUES (1, CURRENT_DATE, 'Cidade de Pemba', 1),
       (2, CURRENT_DATE, 'Distrito de Ancuabe', 1),
       (3, CURRENT_DATE, 'Distrito de Balama', 1),
       (4, CURRENT_DATE, 'Distrito de Chiure', 1),
       (5, CURRENT_DATE, 'Distrito de Ibo', 1),
       (6, CURRENT_DATE, 'Distrito de Macomia', 1),
       (7, CURRENT_DATE, 'Distrito de Mecufi', 1),
       (8, CURRENT_DATE, 'Distrito de Meluco', 1),
       (9, CURRENT_DATE, 'Distrito de Mocimboa da Praia', 1),
       (10, CURRENT_DATE, 'Distrito de Montepuez', 1),
       (11, CURRENT_DATE, 'Distrito de Mueda', 1),
       (12, CURRENT_DATE, 'Distrito de Muidumbe', 1),
       (13, CURRENT_DATE, 'Distrito de Namuno', 1),
       (14, CURRENT_DATE, 'Distrito de Nangade', 1),
       (15, CURRENT_DATE, 'Distrito de Palma', 1),
       (16, CURRENT_DATE, 'Distrito de Pemba-Metuge', 1),
       (17, CURRENT_DATE, 'Distrito de Quissanga', 1),
       (18, CURRENT_DATE, 'Cidade de Xai-Xai', 2),
       (19, CURRENT_DATE, 'Distrito de Bilene', 2),
       (20, CURRENT_DATE, 'Distrito de Chibuto', 2),
       (21, CURRENT_DATE, 'Distrito de Chicualacuala', 2),
       (22, CURRENT_DATE, 'Distrito de Chigubo', 2),
       (23, CURRENT_DATE, 'Distrito de Chókwé', 2),
       (24, CURRENT_DATE, 'Distrito de Guijá', 2),
       (25, CURRENT_DATE, 'Distrito de Mabalane', 2),
       (26, CURRENT_DATE, 'Distrito de Manjacaze', 2),
       (27, CURRENT_DATE, 'Distrito de Massangena', 2),
       (28, CURRENT_DATE, 'Distrito de Massingir', 2),
       (29, CURRENT_DATE, 'Distrito de Xai-Xai', 2),
       (30, CURRENT_DATE, 'Cidade de Inhambane', 3),
       (31, CURRENT_DATE, 'Cidade de Maxixe', 3),
       (32, CURRENT_DATE, 'Distrito de Funhalouro', 3),
       (33, CURRENT_DATE, 'Distrito de Govuro', 3),
       (34, CURRENT_DATE, 'Distrito de Homoine', 3),
       (35, CURRENT_DATE, 'Distrito de Inharrime', 3),
       (36, CURRENT_DATE, 'Distrito de Inhassoro', 3),
       (37, CURRENT_DATE, 'Distrito de Jangamo', 3),
       (38, CURRENT_DATE, 'Distrito de Mabote', 3),
       (39, CURRENT_DATE, 'Distrito de Massinga', 3),
       (40, CURRENT_DATE, 'Distrito de Morrumbene', 3),
       (41, CURRENT_DATE, 'Distrito de Panda', 3),
       (42, CURRENT_DATE, 'Distrito de Vilanculos', 3),
       (43, CURRENT_DATE, 'Distrito de Zavala', 3),
       (44, CURRENT_DATE, 'Cidade de Chimoio', 4),
       (45, CURRENT_DATE, 'Distrito de Barue', 4),
       (46, CURRENT_DATE, 'Distrito de Gondola', 4),
       (47, CURRENT_DATE, 'Distrito de Guro', 4),
       (48, CURRENT_DATE, 'Distrito de Machaze', 4),
       (49, CURRENT_DATE, 'Distrito de Macossa', 4),
       (50, CURRENT_DATE, 'Distrito de Manica', 4),
       (51, CURRENT_DATE, 'Distrito de Mossurize', 4),
       (52, CURRENT_DATE, 'Distrito de Sussundenga', 4),
       (53, CURRENT_DATE, 'Distrito de Tambara', 4),
       (54, CURRENT_DATE, 'Distrito Urbano de Nlhamankulu', 5),
       (55, CURRENT_DATE, 'Distrito Urbano de KaMpfumo', 5),
       (56, CURRENT_DATE, 'Distrito Urbano de KaMaxaquene', 5),
       (57, CURRENT_DATE, 'Cidade de Matola', 6),
       (58, CURRENT_DATE, 'Distrito de Boane', 6),
       (59, CURRENT_DATE, 'Distrito de Magude', 6),
       (60, CURRENT_DATE, 'Distrito de Manhiça', 6),
       (61, CURRENT_DATE, 'Distrito de Marracuene', 6),
       (62, CURRENT_DATE, 'Distrito de Matutuine', 6),
       (63, CURRENT_DATE, 'Distrito de Moamba', 6),
       (64, CURRENT_DATE, 'Distrito de Namaacha', 6),
       (65, CURRENT_DATE, 'Cidade de Nacala-Porto', 7),
       (66, CURRENT_DATE, 'Cidade de Nampula', 7),
       (67, CURRENT_DATE, 'Distrito de Angoche', 7),
       (68, CURRENT_DATE, 'Distrito de Erati-Namapa', 7),
       (69, CURRENT_DATE, 'Distrito de Ilha de Moçambique', 7),
       (70, CURRENT_DATE, 'Distrito de Laláua', 7),
       (71, CURRENT_DATE, 'Distrito de Malema', 7),
       (72, CURRENT_DATE, 'Distrito de Meconta', 7),
       (73, CURRENT_DATE, 'Distrito de Mecuburi', 7),
       (74, CURRENT_DATE, 'Distrito de Memba', 7),
       (75, CURRENT_DATE, 'Distrito de Mogovolas', 7),
       (76, CURRENT_DATE, 'Distrito de Moma', 7),
       (77, CURRENT_DATE, 'Distrito de Monapo', 7),
       (78, CURRENT_DATE, 'Distrito de Mongincual', 7),
       (79, CURRENT_DATE, 'Distrito de Mossuril', 7),
       (80, CURRENT_DATE, 'Distrito de Muecate', 7),
       (81, CURRENT_DATE, 'Distrito de Murrupula', 7),
       (82, CURRENT_DATE, 'Distrito de Nacala Velha', 7),
       (83, CURRENT_DATE, 'Distrito de Nacarôa', 7),
       (84, CURRENT_DATE, 'Distrito de Nampula', 7),
       (85, CURRENT_DATE, 'Distrito de Ribáuè', 7),
       (86, CURRENT_DATE, 'Cidade de Lichinga', 8),
       (87, CURRENT_DATE, 'Distrito de Cuamba', 8),
       (88, CURRENT_DATE, 'Distrito de Lago', 8),
       (89, CURRENT_DATE, 'Distrito de Lichinga', 8),
       (90, CURRENT_DATE, 'Distrito de Majune', 8),
       (91, CURRENT_DATE, 'Distrito de Mandimba', 8),
       (92, CURRENT_DATE, 'Distrito de Marrupa', 8),
       (93, CURRENT_DATE, 'Distrito de Maúa', 8),
       (94, CURRENT_DATE, 'Distrito de Mavago', 8),
       (95, CURRENT_DATE, 'Distrito de Mecanhelas', 8),
       (96, CURRENT_DATE, 'Distrito de Mecula', 8),
       (97, CURRENT_DATE, 'Distrito de Metarica', 8),
       (98, CURRENT_DATE, 'Distrito de Muembe', 8),
       (99, CURRENT_DATE, 'Distrito de Nipepe', 8),
       (100, CURRENT_DATE, 'Distrito de Sanga', 8),
       (101, CURRENT_DATE, 'Cidade de Beira', 9),
       (102, CURRENT_DATE, 'Distrito de Búzi', 9),
       (103, CURRENT_DATE, 'Distrito de Caia', 9),
       (104, CURRENT_DATE, 'Distrito de Chemba', 9),
       (105, CURRENT_DATE, 'Distrito de Cheringoma', 9),
       (106, CURRENT_DATE, 'Distrito de Chibabava', 9),
       (107, CURRENT_DATE, 'Distrito de Dondo', 9),
       (108, CURRENT_DATE, 'Distrito de Gorongosa', 9),
       (109, CURRENT_DATE, 'Distrito de Machanga', 9),
       (110, CURRENT_DATE, 'Distrito de Maringue', 9),
       (111, CURRENT_DATE, 'Distrito de Marromeu', 9),
       (112, CURRENT_DATE, 'Distrito de Muanza', 9),
       (113, CURRENT_DATE, 'Distrito de Nhamatanda', 9),
       (114, CURRENT_DATE, 'Cidade de Tete', 10),
       (115, CURRENT_DATE, 'Distrito de Angónia', 10),
       (116, CURRENT_DATE, 'Distrito de Cahora-Bassa', 10),
       (117, CURRENT_DATE, 'Distrito de Changara', 10),
       (118, CURRENT_DATE, 'Distrito de Chifunde', 10),
       (119, CURRENT_DATE, 'Distrito de Chiuta', 10),
       (120, CURRENT_DATE, 'Distrito de Macanga', 10),
       (121, CURRENT_DATE, 'Distrito de Mágoe', 10),
       (122, CURRENT_DATE, 'Distrito de Marávia', 10),
       (123, CURRENT_DATE, 'Distrito de Moatize', 10),
       (124, CURRENT_DATE, 'Distrito de Mutarara', 10),
       (125, CURRENT_DATE, 'Distrito de Tsango', 10),
       (126, CURRENT_DATE, 'Distrito de Zumbo', 10),
       (127, CURRENT_DATE, 'Cidade de Quelimane', 11),
       (128, CURRENT_DATE, 'Distrito de Alto Molócué', 11),
       (129, CURRENT_DATE, 'Distrito de Chinde', 11),
       (130, CURRENT_DATE, 'Distrito de Gilé', 11),
       (131, CURRENT_DATE, 'Distrito de Gurué', 11),
       (132, CURRENT_DATE, 'Distrito de Ile', 11),
       (133, CURRENT_DATE, 'Distrito de Inhassunge', 11),
       (134, CURRENT_DATE, 'Distrito de Lugela', 11),
       (135, CURRENT_DATE, 'Distrito de Maganja da Costa', 11),
       (136, CURRENT_DATE, 'Distrito de Milange', 11),
       (137, CURRENT_DATE, 'Distrito de Mocuba', 11),
       (138, CURRENT_DATE, 'Distrito de Mopeia', 11),
       (139, CURRENT_DATE, 'Distrito de Morrumbala', 11),
       (140, CURRENT_DATE, 'Distrito de Namacurra', 11),
       (141, CURRENT_DATE, 'Distrito de Namarrói', 11),
       (142, CURRENT_DATE, 'Distrito de Nicoadala', 11),
       (143, CURRENT_DATE, 'Distrito de Pebane', 11),
       (144, CURRENT_DATE, 'Distrito Urbano de KaMavota', 5),
       (145, CURRENT_DATE, 'Distrito Urbano de KaMubukwana', 5),
       (146, CURRENT_DATE, 'Distrito Municipal de KaTembe', 5),
       (147, CURRENT_DATE, 'Distrito Municipal de KaNyaka', 5);
INSERT INTO ru_health_center(id, name, district_id,create_date, latitude, longitude)
VALUES (1, 'Centro de Saúde da Polana Cimento', 55, CURRENT_DATE, '-25.9629498','32.5943592');
--- MOCKED DATA
INSERT INTO ru_user(user_id,username,email,password_hash,first_name,last_name,role_id,locked) VALUES (2,'hr','hr@attendance.co.mz', '$2a$12$OIguBmTbquP3H5PLghH.Xe8CScJdbPuDGIiBiIszWVqXMYOoufqwC', 'HR', 'HR','hr', false);
INSERT INTO ru_user(user_id,username,email,password_hash,first_name,last_name,role_id,locked) VALUES (3,'supervisor','supervisor@attendance.co.mz', '$2a$12$OIguBmTbquP3H5PLghH.Xe8CScJdbPuDGIiBiIszWVqXMYOoufqwC', 'Supervisor', 'Supervisor','supervisor', false);
INSERT INTO ru_user(user_id,username,email,password_hash,first_name,last_name,role_id,locked) VALUES (4,'coordinator','coordinator@attendance.co.mz', '$2a$12$OIguBmTbquP3H5PLghH.Xe8CScJdbPuDGIiBiIszWVqXMYOoufqwC', 'Coordinator', 'Coordinator','coordinator', false);
INSERT INTO ru_employee (id, name,email,nuit,bank,account,nib,category,enabled,health_center_id,subsidy,subsidy_currency) VALUES (1, 'Diogo Rogério Amaral', 'diogoamaral91@gmail.com', 11122234,'STB', 1189868901003, 000301180986890100332, 'VOLUNTEER', true, 1, 11237.06, 'MZN');
INSERT INTO ru_employee_cellphone(number,pin, language,employee_id) VALUES ('258841845213', '$2a$12$4CFKsCP7lArt8esAYCx0veeHdcjYNfQLD0LjEOGX3Ma1G8c3ro0uG','EN', 1);
UPDATE ru_employee
SET cellphone_number = '258841845213'
WHERE id = 1;
