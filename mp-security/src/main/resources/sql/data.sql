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
----add admin-user
INSERT into users (user_uuid, login, password, deleted)
select '5a8cd83c-a032-4010-8d5a-ce0fdafc23f0',
        'admin1',
        '$2a$10$lkn0Q8SUE2QHddzQKeRIdup/Cq1Pue46GTd3HfN9il7UDoyCSKnJK',
        false
    where not exists (select 1 from users where login = 'admin1');

--users_roles:
----add roles for admin
INSERT into users_roles (user_id, role_id)
select 1, 1
    where not exists (select 1 from users_roles where user_id = 1 and role_id = 1);
INSERT into users_roles (user_id, role_id)
select 1, 2
    where not exists (select 1 from users_roles where user_id = 1 and role_id = 2);
