--roles:
----fill all roles
insert into roles (role_name)
select 'ADMIN' where not exists (select 1 from roles where role_name='ADMIN');
insert into roles (role_name)
select 'USER' where not exists (select 1 from roles where role_name='USER');
--
insert into roles (role_name)
select 'MANAGER' where not exists (select 1 from roles where role_name='MANAGER');
insert into roles (role_name)
select 'MODERATOR' where not exists (select 1 from roles where role_name='MODERATOR');
--
insert into roles (role_name)
select 'SHOP_ADMIN' where not exists (select 1 from roles where role_name='SHOP_ADMIN');
insert into roles (role_name)
select 'SHOP_MANAGER' where not exists (select 1 from roles where role_name='SHOP_MANAGER');
insert into roles (role_name)
select 'SHOP_MODERATOR' where not exists (select 1 from roles where role_name='SHOP_MODERATOR');
insert into roles (role_name)
select 'SHOP_STOREKEEPER' where not exists (select 1 from roles where role_name='SHOP_STOREKEEPER');

--users:
----add admin
INSERT into users (user_uuid, login, password, deleted)
select '5a8cd83c-a032-4010-8d5a-ce0fdafc23f0',
        'admin1',
        '$2a$10$lkn0Q8SUE2QHddzQKeRIdup/Cq1Pue46GTd3HfN9il7UDoyCSKnJK',
        false
    where not exists (select 1 from users where login = 'admin1');
----add users
INSERT into users (user_uuid, login, password, deleted)
select '91902887-2a61-4a4e-bdde-e581514f39df',
        'user_1',
        '$2a$10$aa0eSWMyrbdM6dBazu8pFelONIPnLW3kdSmfxaxPzHC5FfLkiTXUi',
        false
    where not exists (select 1 from users where login = 'user_1');
INSERT into users (user_uuid, login, password, deleted)
select '79a1658b-893e-4fa6-b331-cb01cbcb1ea6',
        'user_2',
        '$2a$10$5yFrfz/gfERL9.2l5PpIC.AOX4cmpIiRUTMsd4dlqmfCP2S8ltrsK',
        false
    where not exists (select 1 from users where login = 'user_2');
INSERT into users (user_uuid, login, password, deleted)
select 'b818b952-12f2-4e8f-be02-d7a9533d4718',
        'user_3',
        '$2a$10$QqopE3KOlDT/b8ewCyuLbuWVka7joCJllCDMwosuwzIRJshW9A8iu',
        false
    where not exists (select 1 from users where login = 'user_3');

--users_roles:
----set roles for admin
INSERT into users_roles (user_id, role_id)
select 1, 1
    where not exists (select 1 from users_roles where user_id = 1 and role_id = 1);
INSERT into users_roles (user_id, role_id)
select 1, 2
    where not exists (select 1 from users_roles where user_id = 1 and role_id = 2);
----set roles for users
INSERT into users_roles (user_id, role_id)
select 2, 2
    where not exists (select 1 from users_roles where user_id = 2 and role_id = 2);
INSERT into users_roles (user_id, role_id)
select 3, 2
    where not exists (select 1 from users_roles where user_id = 3 and role_id = 2);
INSERT into users_roles (user_id, role_id)
select 4, 2
    where not exists (select 1 from users_roles where user_id = 4 and role_id = 2);

-- fill all roles
--insert into roles (role_name)
--select 'MP_ADMIN' where not exists (select 1 from roles where role_name='MP_ADMIN');
--insert into roles (role_name)
--select 'MP_EMPLOYEE_MODERATOR' where not exists (select 1 from roles where role_name='MP_EMPLOYEE_MODERATOR');
--insert into roles (role_name)
--select 'MP_EMPLOYEE_STOREKEEPER' where not exists (select 1 from roles where role_name='MP_EMPLOYEE_STOREKEEPER');
--insert into roles (role_name)
--select 'MP_EMPLOYEE_ISSUING_ORDERS_MAN' where not exists (select 1 from roles where role_name='MP_EMPLOYEE_ISSUING_ORDERS_MAN');
--insert into roles (role_name)
--select 'VENDOR_ADMIN' where not exists (select 1 from roles where role_name='VENDOR_ADMIN');
--insert into roles (role_name)
--select 'VENDOR_EMPLOYEE_MANAGER' where not exists (select 1 from roles where role_name='VENDOR_EMPLOYEE_MANAGER');
--insert into roles (role_name)
--select 'VENDOR_EMPLOYEE_STOREKEEPER' where not exists (select 1 from roles where role_name='VENDOR_EMPLOYEE_STOREKEEPER');
--insert into roles (role_name)
--select 'VENDOR_EMPLOYEE_MODERATOR' where not exists (select 1 from roles where role_name='VENDOR_EMPLOYEE_MODERATOR');
--insert into roles (role_name)
--select 'USER' where not exists (select 1 from roles where role_name='USER');

-- admin, user
--INSERT into users (uuid, email, login, password, blocked, deleted, first_name, registered_on)
--select '6C84FB90-12C4-11E1-840D-7B25C5EE775A',
--        'admin@aaa.ru',
--        'admin@aaa.ru',
--        '$2a$10$lAibskUsBRZfWF/BUpKQQOQ8JdsCWpaF2lV3dXaLR9UtaL557jM5y',
--        false,
--        false,
--        'first_name_Admin',
--        current_date
--    where not exists (select 1 from users where email = 'admin@aaa.ru');
--
--INSERT into users (uuid, email, login, password, blocked, deleted, first_name, registered_on)
--select '5C63B562-B85A-4E27-8C0F-9AD49176299C',
--        'user@aaa.ru','user@aaa.ru',
--        '$2a$10$WHkIVZMfDn4HJxl9Czz.1eGmDEgPZlaFo3epTnyumkpSWtZiqVE/G',
--        false,
--        false,
--        'first_name_User',
--        current_date
--    where not exists (select 1 from users where email = 'user@aaa.ru');