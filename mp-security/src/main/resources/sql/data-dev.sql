-- fill all roles
insert into roles (role_name)
select 'MP_ADMIN' where not exists (select 1 from roles where role_name='MP_ADMIN');
insert into roles (role_name)
select 'MP_EMPLOYEE_MODERATOR' where not exists (select 1 from roles where role_name='MP_EMPLOYEE_MODERATOR');
insert into roles (role_name)
select 'MP_EMPLOYEE_STOREKEEPER' where not exists (select 1 from roles where role_name='MP_EMPLOYEE_STOREKEEPER');
insert into roles (role_name)
select 'MP_EMPLOYEE_ISSUING_ORDERS_MAN' where not exists (select 1 from roles where role_name='MP_EMPLOYEE_ISSUING_ORDERS_MAN');
insert into roles (role_name)
select 'VENDOR_ADMIN' where not exists (select 1 from roles where role_name='VENDOR_ADMIN');
insert into roles (role_name)
select 'VENDOR_EMPLOYEE_MANAGER' where not exists (select 1 from roles where role_name='VENDOR_EMPLOYEE_MANAGER');
insert into roles (role_name)
select 'VENDOR_EMPLOYEE_STOREKEEPER' where not exists (select 1 from roles where role_name='VENDOR_EMPLOYEE_STOREKEEPER');
insert into roles (role_name)
select 'VENDOR_EMPLOYEE_MODERATOR' where not exists (select 1 from roles where role_name='VENDOR_EMPLOYEE_MODERATOR');
insert into roles (role_name)
select 'USER' where not exists (select 1 from roles where role_name='USER');

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