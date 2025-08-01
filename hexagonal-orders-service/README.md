# Servicio de Órdenes - Arquitectura Hexagonal

## Descripción General
Este microservicio gestiona las órdenes y sus ítems, implementando la Arquitectura Hexagonal (Ports and Adapters) con Spring Boot. Permite la creación, consulta y eliminación de órdenes, promoviendo una separación clara entre la lógica de negocio y las dependencias externas.

## Estructura del Proyecto
```
src/main/java/com/example/hexagonalorders/
├── domain/                    # Capa de Dominio (Lógica de Negocio)
│   ├── model/                 # Entidades de Dominio (Order, OrderItem, etc.)
│   ├── port/                  # Puertos de Dominio (Interfaces de negocio)
│   │   └── in/                # Puertos de entrada (casos de uso)
│   └── event/                 # Eventos de dominio
├── application/               # Capa de Aplicación (Servicios de aplicación)
│   └── service/               # Servicios de aplicación (OrderService)
├── infrastructure/            # Capa de Infraestructura
│   ├── config/                # Configuración
│   └── in/                    # Adaptadores de entrada (Web)
│       ├── web/               # Controladores REST
│       │   ├── OrderController.java
│       │   ├── dto/           # Objetos de transferencia de datos
│       │   └── mapper/        # Mapeadores de DTO
└── HexagonalOrdersApplication.java
```

## Tecnologías Utilizadas
- Java 17
- Spring Boot 3.2.x
- Maven
- Lombok

## Ejecución del Servicio
1. Clona el repositorio
2. Compila el proyecto:
   ```bash
   mvn clean install
   ```
3. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```

## Principales Endpoints
- `POST /api/orders` - Crear una nueva orden
- `GET /api/orders/{orderNumber}` - Consultar una orden por número
- `DELETE /api/orders/{orderNumber}` - Eliminar una orden

## Beneficios de la Arquitectura Hexagonal
- **Aislamiento de la lógica de negocio** respecto a frameworks y tecnologías externas.
- **Facilidad de pruebas** unitarias y de integración.
- **Flexibilidad** para cambiar adaptadores o tecnologías sin afectar el núcleo del dominio.

## Notas sobre la Arquitectura
- Los puertos de dominio definen las interfaces de negocio y casos de uso.
- Los controladores REST se implementan como adaptadores de entrada en la capa de infraestructura.
- El dominio no depende de ningún framework ni tecnología externa.

## Contribución
1. Haz un fork del repositorio
2. Crea una rama para tu funcionalidad
3. Realiza tus cambios y haz commit
4. Envía un Pull Request

## Licencia
Este proyecto está bajo la licencia MIT.