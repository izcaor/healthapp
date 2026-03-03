# HealthApp

Aplicacion web MVC para gestion de citas medicas.

## Descripcion

Este proyecto implementa:

- Registro y login de usuarios.
- Autenticacion y autorizacion con JWT.
- Roles `ROLE_PACIENTE` y `ROLE_MEDICO`.
- Gestion de paciente y citas medicas (crear, listar, filtrar, editar y cancelar).
- Manejo global de errores con vista de error.

Se ha realizado la ampliacion opcional usando MongoDB (Atlas).

## Instrucciones de ejecucion

Requisitos:

- Java 17 o superior
- Maven Wrapper (incluido en el proyecto)

Ejecutar:

```bash
./mvnw spring-boot:run
```

Abrir en el navegador:

- `http://localhost:8080/auth/register`
- `http://localhost:8080/auth/login`

## Seguridad (resumen)

- La contrasena se guarda cifrada con `BCrypt`.
- En login se genera un token JWT con email y rol.
- El token se guarda en cookie HTTP-only (`HEALTHAPP_TOKEN`).
- Un filtro valida el token en cada peticion.
- Las rutas estan protegidas por rol:
  - `/paciente/**` solo `ROLE_PACIENTE`
  - `/medico/**` solo `ROLE_MEDICO`
