# vacation-software-development
假期开发实训

基于SpringBoot+MyBatis+MySQL


---

# 购物管理项目

## 项目简介

购物管理项目是一个基于Java的Web应用，旨在帮助开发者学习Java开发、Spring Boot和MyBatis框架的应用。该项目实现了一个简单的管理员和客户管理系统，涵盖了管理员登录、密码管理、客户管理、商品管理等功能，同时提供了相应的接口和页面供用户进行操作。

## 开始

### 先决条件

在开始使用项目之前，确保你的开发环境满足以下要求：

- Java JDK 17
- IntelliJ IDEA 2023.3 或其他Java IDE
- MySQL 8

### 安装和配置

1. 克隆项目到你的本地环境：

   ```bash
   git clone https://github.com/yourusername/shopping-management-project.git
   ```

2. 在IntelliJ IDEA中导入项目。

3. 配置数据库连接信息，在 `application.properties` 或 `application.yml` 文件中设置数据库URL、用户名和密码。

4. 启动Spring Boot应用程序。

### 数据库结构

项目使用了两个数据库表：

- `user`表包含了用户信息，字段包括：uid、username、level、password、salt、phone、email、gender、consumption、avatar、is_delete、created_user、created_time、modified_user、modified_time。

- `product`表包含了商品信息，字段包括：uid、category_id、item_type、title、sell_point、price、num。

你可以在项目的 `resources` 目录下找到数据库初始化脚本，使用它来创建这两个表。

### 使用示例

#### 管理员功能

- 访问管理员登录页面：http://localhost:8080/admin/login
    - 输入管理员用户名和密码进行登录。
- 访问管理员密码管理页面：http://localhost:8080/admin/password
    - 在这里你可以修改自己的密码或者重置客户的密码。
- 访问客户管理页面：http://localhost:8080/admin/customers
    - 在这里你可以列出客户信息、删除客户信息、查询客户信息等。

#### 商品管理

- 访问商品管理页面：http://localhost:8080/admin/products
    - 在这里你可以列出商品信息、添加商品信息、修改商品信息、删除商品信息、查询商品信息等。

#### 客户功能

- 访问客户功能页面：http://localhost:8080/customer
    - 在这里你可以注册、登录、修改密码、进行购物以及查看购物历史等。

### 测试

在浏览器中访问相应的页面，进行功能测试和验证。你可以使用预设的管理员账号登录并测试管理员功能，或者创建新的客户账号并测试客户功能。

## 技术栈

- Java
- Spring Boot
- MyBatis
- MySQL
- Thymeleaf（用于前端模板渲染）
---