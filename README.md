# desafio-agenda-pro

Este proyecto es una solución de ejemplo desarrollada para un desafío laboral, con el objetivo de gestionar productos y generar estadísticas basadas en las ventas. La aplicación está construida con Spring Boot y está desplegada en AWS. El propósito principal es demostrar la capacidad de implementación de una aplicación robusta y la generación de estadísticas de manera asincrónica.

**Funcionamiento de la Aplicación**

Características Principales
- Gestión de Productos: La aplicación permite agregar, actualizar y eliminar productos. Cada producto tiene atributos como nombre, precio, y categoría.
- Estadísticas de Ventas: Se generan estadísticas a partir de las ventas de productos, incluyendo:
- Resumen de ventas por producto.
- Últimos 3 productos vendidos. Si las ultimas ventas fueron A, A, B, D, A, C, C -> retornara D, A, C
- Historial de precios de un producto.

**Implementación**

- Estadísticas Asíncronas: Las estadísticas se generan de manera asíncrona. Cabe destacar que en un entorno real, la actualización de la tabla de ventas debería ser manejada dentro de una transacción que actualice también el stock.
- Paginación: La paginación está implementada solo en el endpoint /api/productos y de manera estática (20 registros). En un entorno real, sería necesario paginar todas las consultas que manejan grandes volúmenes de datos para mejorar la eficiencia.

**Despliegue**

AWS: La aplicación (Java), la base de datos y jenkins están desplegadas en AWS:

La aplicación Java y Jenkins corren en una instancia EC2.
La base de datos MySQL está configurada en un volumen RDS.
Seguridad: Las credenciales de la base de datos están protegidas mediante AWS Secrets Manager. La aplicación Java es la única con permisos suficientes para recuperar las credenciales y conectarse a la base de datos.

Dockerización y Pipeline: La aplicación está dockerizada. El pipeline de Jenkins realiza las siguientes tareas:

- Construye el archivo JAR.
- Ejecuta los tests.
- Se conecta a Docker
- Construye la imagen y la sube al repositorio Docker.
- Despliega un contenedor con la imagen recién creada.

**Pruebas con Postman**
Dentro del proyecto Postman, encontrarás dos entornos:

Entorno para Pruebas Locales: Configurado para conectarse a la aplicación ejecutándose localmente en Docker.
Entorno para AWS: Configurado para conectarse a la instancia desplegada en AWS.
Ejecutar Localmente
Para probar la aplicación localmente, he incluido un archivo docker-compose.yml que permite ejecutar la aplicación junto con una instancia de MySQL dentro de una red Docker. Para usar esta configuración:

**Configura Docker Compose:**

Asegúrate de tener Docker y Docker Compose instalados en tu máquina.
Construye el jar utilizando maven
Ubicate en el directorio donde se encuentra el archivo docker-compose.yml y corre **docker-compose down && docker-compose build && docker-compose up**

Esto levantará tanto la aplicación como la base de datos MySQL en contenedores Docker, permitiéndote realizar pruebas locales. El puerto expuesto es el 
Pruebas: Usa el enviroment **Docker** de Postman.

**Probar en AWS**

Si prefieres usar la instancia desplegada en AWS, utiliza el enviroment de Postman configurado para AWS. La aplicación está accesible a través de la IP pública de la instancia EC2 en el puerto 8090.

**Pasos para Probar la Aplicación**

- Registrar un Usuario: Crea un usuario con nombre y contraseña.
- Iniciar Sesión: Inicia sesión con el usuario registrado.
- Crear Productos: Agrega tantos productos como desees.
- Realizar Ventas: Registra tantas ventas como desees.
- Actualizar Precios: Modifica los precios de los productos para generar un historial de precios.
- Consultar Estadísticas: Utiliza los endpoints de la API para consultar las distintas estadísticas generadas.

**Consideraciones para un Entorno Productivo**
- Gestión de Inventario: En un entorno real, la gestión de productos debería incluir:
  - Un registro detallado por unidad y por sucursal.
  - Manejo de stock en múltiples almacenes.
- Registro de información adicional en las ventas como seguro, garantía, medio de pago, info del comprador, uso de gift cards, etc.
- Consideración de devoluciones y manejo de productos defectuosos.
- Paginación y Consultas: En un entorno real, todas las consultas que manejan grandes volúmenes de datos deberían estar paginadas para mejorar la eficiencia y evitar problemas de rendimiento.
- Seguridad: Implementar rotación de secretos en AWS Secrets Manager y otras prácticas de seguridad para proteger la información sensible.





