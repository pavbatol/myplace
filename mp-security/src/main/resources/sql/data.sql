-- fill all roles
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
