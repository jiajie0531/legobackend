-- 菜单权限
insert into resource_menu values (1,SYSDATE(),0,SYSDATE(),0,'首页',0,'/',null,'home');
insert into resource_menu values (100,SYSDATE(),0,SYSDATE(),0,'用户管理',0,null,null,'user');
insert into resource_menu values (101,SYSDATE(),0,SYSDATE(),0,'账号管理',0,'/backendUser',100,'solution');
insert into resource_menu values (102,SYSDATE(),0,SYSDATE(),0,'角色管理',0,'/role',100,'team');
insert into resource_menu values (200,SYSDATE(),0,SYSDATE(),0,'订单管理',0,null,null,'appstore');
insert into resource_menu values (201,SYSDATE(),0,SYSDATE(),0,'搬家订单',0,'/order',200,'database');


-- 按钮权限
insert into resource_button values (10000,SYSDATE(),0,SYSDATE(),0,'查询账号列表',0,101,1,'bars');
insert into resource_button values (10001,SYSDATE(),0,SYSDATE(),0,'新增账号',0,101,2,'user-add');
insert into resource_button values (10002,SYSDATE(),0,SYSDATE(),0,'修改账号',0,101,3,'edit');
insert into resource_button values (10003,SYSDATE(),0,SYSDATE(),0,'修改密码',0,101,4,'key');
insert into resource_button values (10004,SYSDATE(),0,SYSDATE(),0,'删除账号',0,101,5,'user-delete');


insert into resource_button values (30003,SYSDATE(),0,SYSDATE(),0,'修改密码',0,101,4,'key');

insert into resource_button values (10100,SYSDATE(),0,SYSDATE(),0,'查询角色列表',0,102,1,'bars');
insert into resource_button values (10101,SYSDATE(),0,SYSDATE(),0,'新增角色',0,102,2,'plus');
insert into resource_button values (10102,SYSDATE(),0,SYSDATE(),0,'修改角色',0,102,3,'edit');
insert into resource_button values (10103,SYSDATE(),0,SYSDATE(),0,'设置权限',0,102,4,'setting');
insert into resource_button values (10104,SYSDATE(),0,SYSDATE(),0,'删除角色',0,102,5,'minus');

insert into resource_button values (10300,SYSDATE(),0,SYSDATE(),0,'查询机构',0,103,1,'bars','system:company:list');
insert into resource_button values (10301,SYSDATE(),0,SYSDATE(),0,'新增机构',0,103,2,'plus','system:company:add');
insert into resource_button values (10302,SYSDATE(),0,SYSDATE(),0,'修改机构',0,103,3,'edit','system:company:edit');
insert into resource_button values (10303,SYSDATE(),0,SYSDATE(),0,'删除机构',0,103,4,'minus','system:company:del');
insert into resource_button values (10304,SYSDATE(),0,SYSDATE(),0,'分配商品',0,103,5,'share-alt','system:company:grantGoods');
