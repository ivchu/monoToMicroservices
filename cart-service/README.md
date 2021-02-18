# Java Spring Cart Service

Cart Service REST API based on Java Spring, Spring Boot, Hibernate ORM with MySQL, Spring Fox (Swagger API docs), JWT.

## REST API Endpoints

All inputs and outputs use JSON format.

**To open Swagger (interactive) API documentation, navigate your browser to [YOUR-URL]/swagger-ui.html**

To start docker-container with Mysql server instance type in command line:
```
docker run -d --name mysql_cart-service -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cart-service mysql:latest
```

```
/cart
  POST / - Create cart
  GET /{id} - Get items for card with ID = {id}
  POST /{id} - Add CartItem to cart with ID {id}
  DELETE /{id}/{product_id} - Remove product with ID {product_id} from cart with ID {id}
  POST /{id}/quantity - Updates cart item, i.e. set product quantity
  POTS /{id}/order - Create order from cart
```
