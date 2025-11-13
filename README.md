# Plataforma de Upskilling/Reskilling 2030

## Descrição do Projeto

### Problema
O futuro do trabalho está sendo transformado por tecnologias como IA, automação e análise de dados. Existe uma necessidade crescente de:
- **Reskilling** de profissionais em risco de automação
- **Upskilling** contínuo para acompanhar demandas do mercado
- Educação permanente conectada às competências do futuro

### Solução Proposta
Desenvolvemos uma API RESTful completa para uma plataforma de Upskilling/Reskilling que ajuda pessoas a se prepararem para carreiras de 2030+, permitindo:
- Cadastro de usuários na plataforma
- Acesso a trilhas de aprendizagem focadas em competências do futuro
- Sistema de matrículas em trilhas para requalificação profissional

##Tecnologias e Versões

### **Linguagem e Framework**
- **Java**: 17
- **Spring Boot**: 3.2.0
- **Maven**: 3.8+

### **Principais Dependências**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

## Como Executar o Projeto

### Pré-requisitos
- Java 17 instalado
- Maven 3.8+ instalado

### 1. Instalar Dependências
```bash
# Clone o repositório
git clone https://github.com/ghpadula/GS_2-Java.git
cd GS_2-Java

# Instale as dependências
mvn clean install
```

### 2. Configurar Banco de Dados
O projeto usa H2 Database e ja vem configurado e com persistência em arquivo.
Caso nao venha com as tabelas populadas com dados de teste, entre no arquivo /config/Dataloader e tire os comentario do codigo

**Acesso ao H2 Console:**
- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:file:./data/gs2025db`
- **Usuário:** `sa`
- **Senha:** (vazia)

### 3. Executar a Aplicação
```bash
# Opção 1: Maven
mvn spring-boot:run

# Opção 2: Maven Wrapper
./mvnw spring-boot:run
```
A aplicação estará disponível em: `http://localhost:8080`

## Endpoints da API

### USUÁRIOS
| Método | Endpoint          | Descrição           |
|--------|-------------------|---------------------|
| GET    | /api/usuarios     | Listar todos os usuários |
| GET    | /api/usuarios/{id} | Buscar usuário por ID |
| POST   | /api/usuarios     | Criar novo usuário  |
| PUT    | /api/usuarios/{id} | Atualizar usuário   |
| DELETE | /api/usuarios/{id} | Excluir usuário     |

### TRILHAS
| Método | Endpoint        | Descrição         |
|--------|-----------------|-------------------|
| GET    | /api/trilhas    | Listar todas as trilhas |
| GET    | /api/trilhas/{id}| Buscar trilha por ID |
| POST   | /api/trilhas    | Criar nova trilha |
| PUT    | /api/trilhas/{id}| Atualizar trilha  |
| DELETE | /api/trilhas/{id}| Excluir trilha    |

###  MATRÍCULAS (Extra)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/matriculas/usuario/{usuarioId}/trilha/{trilhaId}` | Matricular usuário |
| `GET` | `/api/matriculas/usuario/{usuarioId}` | Matrículas do usuário |
| `PUT` | `/api/matriculas/{id}/cancelar?usuarioId={userId}` | Cancelar matrícula |
| `PUT` | `/api/matriculas/{id}/concluir?usuarioId={userId}` | Concluir matrícula |
| `GET` | `/api/matriculas/usuario/{usuarioId}/estatisticas` | Estatísticas do usuário |
| `DELETE` | `/api/matriculas/{id}` | Excluir matrícula |
## Exemplos de Requisições(Postman)

### Criar Usuário (POST)
```http
POST http://localhost:8080/api/usuarios

{
  "nome": "Carlos Silva",
  "email": "carlos.silva@email.com",
  "senha": "123456",
  "areaAtuacao": "Tecnologia",
  "nivelCarreira": "Pleno",
  "dataCadastro": "2024-01-20"
}
```

### Atualizar Usuário (PUT)
```http
PUT http://localhost:8080/api/usuarios/1

{
  "nome": "Carlos Silva Atualizado",
  "email": "carlos.atualizado@email.com",
  "senha": "novaSenha123",
  "areaAtuacao": "Data Science",
  "nivelCarreira": "Senior",
  "dataCadastro": "2024-01-20"
}
```

### Criar Trilha (POST)
```http
POST http://localhost:8080/api/trilhas
Content-Type: application/json

{
  "nome": "Machine Learning Avançado",
  "descricao": "Trilha avançada de ML e Deep Learning",
  "nivel": "AVANCADO",
  "cargaHoraria": 80,
  "focoPrincipal": "IA"
}
```

### Matricular Usuário (POST)
```http
POST http://localhost:8080/api/matriculas/usuario/1/trilha/1
Content-Type: application/json
```

## Como Testar a API

###  Usando Postman/Insomnia
1. Importe a coleção ou crie manualmente.
2. Configure o ambiente:
    - **Base URL:** `http://localhost:8080`
3. Teste os endpoints seguindo os exemplos acima.

### Usando cURL (Terminal)

**Listar usuários:**
```bash
curl -X GET http://localhost:8080/api/usuarios
```

**Criar usuário:**
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste API",
    "email": "teste@api.com",
    "senha": "123456",
    "areaAtuacao": "TI",
    "nivelCarreira": "Junior",
    "dataCadastro": "2024-01-20"
  }'
```

**Matricular usuário:**
```bash
curl -X POST http://localhost:8080/api/matriculas/usuario/1/trilha/1
```

### Usando Navegador
- **API:** `http://localhost:8080/api/usuarios`
- **H2 Console:** `http://localhost:8080/h2-console`
- **Dashboard:** `http://localhost:8080/login`

## Estrutura do Banco de Dados

**Tabelas Principais:**
- `usuarios` - Dados dos usuários da plataforma
- `trilhas` - Trilhas de aprendizagem
- `competencias` - Competências do futuro
- `trilha_competencia` - Relação N:N entre trilhas e competências
- `matriculas` - Matrículas de usuários em trilhas

**Dados Iniciais (Seeds):**
O sistema já vem com dados de exemplo:
- **Usuários:** João Silva, Maria Santos, etc.
- **Trilhas:** Data Science, IA e Machine Learning, Liderança 4.0
- **Competências:** IA, Análise de Dados, Liderança, Colaboração

## Funcionalidades Extras Implementadas

**Bônus Implementados:**
- Sistema completo de matrículas
- Endpoints de estatísticas para usuários
- Validações avançadas e tratamento de erros
- Relacionamentos complexos entre entidades
- Separação entre API REST e Web Views

**Sistema Web:**
- Autenticação com sessions
- Dashboard administrativo
- CRUD completo via interface web
- Controle de acesso baseado em perfil

## Arquitetura do Projeto
```
src/main/java/br/com/fiap/gs/
├── controller/          # Camada de controllers
│   ├── UsuarioController.java
│   ├── UsuarioApiController.java
│   ├── TrilhaController.java
│   ├── TrilhaApiController.java
│   └── MatriculaApiController.java
├── service/            # Camada de serviços
│   ├── UsuarioService.java
│   ├── TrilhaService.java
│   └── MatriculaService.java
├── repository/         # Camada de persistência
│   ├── UsuarioRepository.java
│   ├── TrilhaRepository.java
│   └── MatriculaRepository.java
├── model/              # Entidades JPA
│   ├── Usuario.java
│   ├── Trilha.java
│   ├── Competencia.java
│   └── Matricula.java
└── dto/                # Data Transfer Objects
    └── MatriculaRequest.java
    └── MatriculaRequest.java
    
```
