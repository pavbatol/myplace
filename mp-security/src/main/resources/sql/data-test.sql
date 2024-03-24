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
