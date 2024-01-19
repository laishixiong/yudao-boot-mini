# 安装启动教程
https://doc.iocoder.cn/quick-start/#_3-%E5%88%9D%E5%A7%8B%E5%8C%96-mysql
* 准备好数据库
* 准备好Redis
* 初始化数据库数据

# 框架研究
## RBAC权限控制模型
users -> user_role <- role 用户可以绑定多个角色
role -> role_menu <- menu 每个角色可以绑定多个菜单
menu里面有多种类型菜单:目录、菜单、按钮

## 权限控制
### 菜单权限

## 数据字典
数据字典使用dict_type与dict_data表。dict_type里面放置的是字典类型，比如‘性别’这个大类型；
dict_data里面放置的是每一个类型的具体子项,比如‘男’、‘女’。
使用yudao-ui-admin-vue2发现界面‘字典数据’页面没有出现



