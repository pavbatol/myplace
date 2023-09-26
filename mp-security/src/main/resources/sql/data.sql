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