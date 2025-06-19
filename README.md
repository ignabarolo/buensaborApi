# Buen SaborApi

Backend para el proyecto “El Buen Sabor 2025 - UTN TUP”.

Este repositorio implementa el backend de la aplicación **El Buen Sabor**, una solución orientada a la gestión de pedidos para restaurantes, desarrollada como parte de la carrera Tecnicatura Universitaria en Programación (UTN).

## Descripción

API RESTful desarrollada en Java que permite gestionar:

- Usuarios y autenticación.
- Productos y categorías.
- Pedidos y estados.
- Clientes y direcciones.
- Administración de sucursales y empleados.
- Procesos internos de cocina y delivery.

## Tecnologías

- **Lenguaje:** Java 17
- **Framework principal:** Spring Boot
- **ORM:** Hibernate
- **Base de datos:** MySQL
- **Autenticación:** JWT (Json Web Token)
- **Documentación API:** Swagger / OpenAPI
- **Gestión de dependencias:** Gradle
- **Testing:** JUnit

## Instalación

1. Clona el repositorio:
    ```bash
    git clone https://github.com/ignabarolo/buensaborApi.git
    cd buensaborApi
    ```
2. Asegúrate de tener Java 17+ y Gradle instalados.
3. Copia y configura el archivo de propiedades (ejemplo: `application.properties`).
4. Instala las dependencias y compila:
    ```bash
    ./gradlew build
    ```
5. Ejecuta la aplicación:
    ```bash
    ./gradlew bootRun
    ```

## Uso

El backend expone endpoints REST en la ruta `/api`. 

## Endpoints principales

- `POST /api/auth/login` — Iniciar sesión y obtener token JWT
- `GET /api/productos` — Listar todos los productos
- `GET /api/productos/{id}` — Obtener detalle de producto por ID
- `POST /api/pedidos` — Crear un nuevo pedido
- `GET /api/pedidos/{id}` — Consultar estado de un pedido
- `GET /api/categorias` — Listar categorías de productos
- `GET /api/usuarios/me` — Obtener información del usuario autenticado

> Puedes consultar y probar todos los endpoints en la documentación interactiva Swagger (por ejemplo: http://localhost:8080/swagger-ui.html).

## Frontend relacionado

Puedes encontrar el frontend de este proyecto en el siguiente repositorio:  
[el-buen-sabor-frontend](https://github.com/SantiagoVidela933/el-buen-sabor-frontend)

---

> Proyecto académico - UTN TUP 2025.
