# to-do-list-api

API REST para gerenciamento de tarefas desenvolvida com Java e Spring Boot.

O projeto foi criado com objetivo de estudo e portfólio, aplicando conceitos de desenvolvimento backend, APIs REST, Programação Orientada a Objetos, persistência de dados, validação, tratamento de exceções, testes automatizados e documentação de APIs.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- H2
- Bean Validation
- Maven
- JUnit 5
- Mockito
- MockMvc
- OpenAPI / Swagger

## Arquitetura

A aplicação utiliza uma arquitetura em camadas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Além das camadas principais, o projeto possui:

```text
Entity
DTO
Enum
Exception
Config
Tests
```

### Responsabilidades

- **Controller:** recebe requisições HTTP e retorna respostas.
- **Service:** concentra as regras de negócio.
- **Repository:** realiza o acesso aos dados através do Spring Data JPA.
- **Entity:** representa as entidades persistidas no banco.
- **DTO:** representa os dados recebidos pela API.
- **Enum:** representa valores fixos, como o status de uma tarefa.
- **Exception:** concentra exceções e tratamento global de erros.
- **Config:** contém configurações da aplicação, como OpenAPI.

## Funcionalidades

Atualmente a API permite:

- Criar uma tarefa
- Listar todas as tarefas
- Buscar uma tarefa pelo ID
- Atualizar o status de uma tarefa
- Excluir uma tarefa
- Validar dados recebidos
- Retornar erros HTTP adequados
- Persistir dados em PostgreSQL
- Documentar os endpoints com Swagger/OpenAPI

## Status da tarefa

Uma tarefa pode possuir os seguintes status:

```text
PENDING
IN_PROGRESS
COMPLETED
```

Toda nova tarefa é criada inicialmente com:

```text
PENDING
```

## Endpoints

### Criar tarefa

```http
POST /api/tasks
```

Exemplo:

```json
{
  "title": "Estudar Java",
  "description": "Revisar conceitos de POO"
}
```

Possíveis respostas:

```text
201 Created
400 Bad Request
```

---

### Listar tarefas

```http
GET /api/tasks
```

Resposta:

```text
200 OK
```

---

### Buscar tarefa por ID

```http
GET /api/tasks/{id}
```

Possíveis respostas:

```text
200 OK
404 Not Found
```

---

### Atualizar status

```http
PATCH /api/tasks/{id}/status
```

Exemplo:

```json
{
  "status": "COMPLETED"
}
```

Possíveis respostas:

```text
200 OK
400 Bad Request
404 Not Found
```

---

### Excluir tarefa

```http
DELETE /api/tasks/{id}
```

Possíveis respostas:

```text
204 No Content
404 Not Found
```

## Validação

A API utiliza Bean Validation para validar os dados recebidos.

Exemplos de regras:

```text
title       → obrigatório e máximo de 100 caracteres
description → máximo de 500 caracteres
status      → obrigatório durante atualização de status
```

Quando os dados enviados são inválidos, a API retorna:

```text
400 Bad Request
```

Exemplo:

```json
{
  "title": "O título é obrigatório."
}
```

## Tratamento de erros

A aplicação possui tratamento global de exceções através de:

```java
@RestControllerAdvice
```

Quando uma tarefa não existe, é lançada uma:

```text
TaskNotFoundException
```

e a API retorna:

```text
404 Not Found
```

Exemplo:

```json
{
  "error": "Tarefa com ID 999 não encontrada."
}
```

## Banco de dados

A aplicação utiliza **PostgreSQL** como banco de dados principal.

Durante os testes que necessitam do contexto da aplicação é utilizado **H2 em memória**, mantendo os testes independentes do PostgreSQL local.

### Configuração principal

A conexão utilizada pela aplicação segue o formato:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=todolist_user
spring.datasource.password=${DB_PASSWORD}
```

A senha não é armazenada diretamente no projeto.

Ela deve ser fornecida através da variável de ambiente:

```bash
export DB_PASSWORD='sua_senha'
```

## Pré-requisitos

Para executar o projeto é necessário possuir:

- Java 21
- PostgreSQL
- Git

O projeto utiliza Maven Wrapper, portanto não é necessário instalar o Maven separadamente.

## Configuração do PostgreSQL

Crie um usuário para a aplicação:

```sql
CREATE USER todolist_user WITH PASSWORD 'sua_senha';
```

Crie o banco:

```sql
CREATE DATABASE todolist OWNER todolist_user;
```

Depois configure a variável de ambiente:

```bash
export DB_PASSWORD='sua_senha'
```

## Executando o projeto

Clone o repositório:

```bash
git clone git@github.com:EverteSantos/to-do-list-api.git
```

Entre no diretório:

```bash
cd to-do-list-api
```

Execute:

```bash
./mvnw spring-boot:run
```

Por padrão, a API estará disponível em:

```text
http://localhost:8080
```

## Swagger / OpenAPI

Com a aplicação em execução, a documentação interativa pode ser acessada em:

```text
http://localhost:8080/swagger-ui.html
```

A especificação OpenAPI em JSON pode ser acessada em:

```text
http://localhost:8080/v3/api-docs
```

A documentação inclui:

- descrição dos endpoints
- parâmetros
- request bodies
- respostas HTTP
- exemplos
- schemas dos DTOs
- status disponíveis para tarefas

## Testes

O projeto possui testes automatizados para as camadas de Service e Controller.

### Service

Os testes de Service utilizam:

- JUnit 5
- Mockito
- Repository mockado

São testados cenários como:

- criação
- listagem
- busca
- tarefa inexistente
- atualização de status
- atualização de tarefa inexistente
- exclusão
- exclusão de tarefa inexistente

### Controller

Os testes de Controller utilizam:

```java
@WebMvcTest
MockMvc
@MockitoBean
```

São testados:

- criação válida
- criação inválida
- listagem
- busca existente
- busca inexistente
- atualização válida
- atualização inválida
- atualização de tarefa inexistente
- exclusão
- exclusão de tarefa inexistente

Para executar todos os testes:

```bash
./mvnw test
```

Estado atual da suíte:

```text
19 testes
0 failures
0 errors
```

## Estrutura do projeto

```text
src
├── main
│   ├── java
│   │   └── br.com.everte.todolistapi
│   │       ├── config
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── enums
│   │       ├── exception
│   │       ├── repository
│   │       └── service
│   │
│   └── resources
│       └── application.properties
│
└── test
    ├── java
    │   └── br.com.everte.todolistapi
    │       ├── controller
    │       └── service
    │
    └── resources
        └── application.properties
```

## Próximas melhorias

Algumas evoluções planejadas para o projeto:

- melhorar continuamente a documentação da API
- adicionar migrations para controle do schema do banco
- configurar CI/CD com GitHub Actions
- evoluir a cobertura de testes quando novas funcionalidades forem adicionadas
- continuar aprimorando arquitetura e boas práticas

## Autor

Desenvolvido por **Everte Santos** como projeto de estudo e portfólio backend Java.
