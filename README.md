# URL Shortener API 

Este es un servicio RESTful para acortar URLs, redirigir a las URLs originales y ver estadísticas de los enlaces acortados. 

## Endpoints 

### Acortar una URL 

**POST** `/shorten`

**Descripción:** Envía una URL en el cuerpo de la solicitud para generar un enlace corto. 

**Ejemplo de solicitud:**

```json
{
  "url": "https://example.com/"
}
```

**Ejemplo de respuesta:**

```json
{
  "id": 1,
    "url": "https://example.com/",
    "shortCode": "tr8wty",
    "createdAt": "2025-02-20T18:17:09.4734371",
    "updatedAt": "2025-02-20T18:17:09.4734371"
}
```

---

### Redirigir a la URL original 

**GET** `/shorten/{shortCode}`

 **Descripción:** Usa el código corto generado para redirigir a la URL original. 

**Ejemplo:**

```
GET /shorten/abc123
```

 Redirige a `https://www.ejemplo.com` 

---

###  Ver estadísticas de una URL corta 

**GET** `/shorten/{shortCode}/stats`

 **Descripción:** Obtiene información sobre el uso del enlace corto. 

**Ejemplo de respuesta:**

```json
{
  "id": 1,
    "url": "https://example.com/",
    "shortCode": "tr8wty",
    "createdAt": "2025-02-20T18:17:09.4734371",
    "updatedAt": "2025-02-20T18:17:09.4734371",
    "accessCount": 16
}
```

---

###  Actualizar una URL corta 

**PUT** `/shorten/{shortCode}`

 **Descripción:** Actualiza la URL original asociada a un código corto. 

**Ejemplo de solicitud:**

```json
{
  "url": "https://www.nueva-url.com"
}
```

**Ejemplo de respuesta:**

```json
{
  "id": 1,
    "url": "https://www.nueva-url.com",
    "shortCode": "tr8wty",
    "createdAt": "2025-02-20T18:17:09.4734371",
    "updatedAt": "2025-02-20T18:17:09.4734371"
}
```

---

###  Eliminar una URL corta 

**DELETE** `/shorten/{shortCode}`

 **Descripción:** Elimina una URL corta del sistema. 

**Ejemplo:**

```
DELETE /shorten/abc123
```

**Ejemplo de respuesta:**

```json
204 No Content
```

## Tecnologías utilizadas 

- **Spring Boot**  para el backend REST
- **Spring Data JPA** 🗄 para la persistencia de datos
- **Hibernate**  como ORM
- **H2/MySQL**  como base de datos (según configuración)

##  Instalación y ejecución 

1️ Clona este repositorio 
   ```bash
   git clone https://github.com/agus1k/url-shortener.git
   cd url-shortener
   ```
2️ Configura la base de datos en `application.properties` 
3 Ejecuta la aplicación con Maven o Gradle 
   ```bash
   mvn spring-boot:run
   ```
   o
   ```bash
   ./gradlew bootRun
   ```
4 La API estará disponible en `http://localhost:8080` 

##  Licencia 

Este proyecto está bajo la licencia MIT. 

