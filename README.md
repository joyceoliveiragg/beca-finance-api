# Beca Finance API

Projeto desenvolvido como solução para o **Desafio Beca JAVA JR 2025–2026**.

## 📌 Visão Geral
A aplicação foi desenvolvida como parte do **Desafio Beca JAVA JR 2025–2026**, com o objetivo de consolidar os conhecimentos adquiridos ao longo do curso e aplicá-los em um cenário próximo ao de um sistema real.

Durante o desenvolvimento, o **principal desafio** esteve relacionado à **compreensão e ambientação do ecossistema Kafka** e de uma arquitetura baseada em microserviços e mensageria. Em diversos momentos, foi necessário recorrer a **pesquisas externas, documentação oficial e exemplos da internet**, pois o conteúdo apresentado no curso não se mostrou didático o suficiente para esclarecer, de forma prática, todos os conceitos exigidos pelo desafio, o que acabou gerando mais dúvidas durante a implementação e tornando necessário recorrer com maior frequência a pesquisas externas, documentação oficial e materiais disponíveis na internet.

Diante disso, foi tomada a decisão consciente de **priorizar a solidez das funcionalidades básicas**, garantindo primeiro um **CRUD de usuários completo, consistente e seguro**, antes de avançar para ajustes mais complexos de arquitetura. Esse foco permitiu:
- Implementar corretamente operações essenciais de CRUD
- Adotar **soft delete** (inativação de usuários) em vez de remoção física
- Integrar o comportamento de usuários inativos ao Spring Security

Com a base funcional estabilizada, o esforço seguinte foi direcionado para **organização, estruturação e profissionalização da arquitetura**, incluindo:
- Separação clara de responsabilidades entre microserviços
- Processamento assíncrono com Kafka
- Integração com API Mock e API pública
- Uso de Docker Compose para padronizar o ambiente

Esse processo refletiu um aprendizado progressivo, priorizando **correção, clareza e boas práticas**, mesmo diante das dificuldades técnicas encontradas.

---

## 🧱 Arquitetura

### Microsserviços
- **ms-usuarios**
  - CRUD de usuários
  - Autenticação (Spring Security + JWT)
  - Importação de usuários via Excel

- **ms-transacoes**
  - Criação de transações
  - Resumo/análise de gastos
  - Download de relatório em Excel
  - Publicação de eventos no Kafka

- **ms-processor**
  - Consumo de eventos do Kafka
  - Consulta de saldo via API Mock
  - Conversão de moeda via BrasilAPI
  - Aprovação ou rejeição da transação

### Infraestrutura
- PostgreSQL (bancos separados)
- Kafka + Zookeeper
- Mock API (json-server)
- Prometheus + Grafana

---

## 🚀 Como executar o projeto

### Pré-requisitos
- Docker
- Docker Compose

### Subir o ambiente
Na raiz do projeto:
```bash
docker compose up -d --build
```

### Verificar containers
```bash
docker compose ps
```

### Derrubar ambiente
```bash
docker compose down -v
```

---

## 🌐 Portas e serviços

| Serviço | URL |
|------|-----|
| ms-transacoes | http://localhost:8080 |
| ms-usuarios | http://localhost:8081 |
| ms-processor | http://localhost:8082 |
| Mock API | http://localhost:8090 |
| Swagger Usuários | http://localhost:8081/swagger-ui/index.html |
| Swagger Transações | http://localhost:8080/swagger-ui/index.html |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 |

---

## 📥 Importação de usuários via Excel

A planilha deve conter **uma aba** com o seguinte cabeçalho:

| nome | email | cpf | senha |

Exemplo disponível em:
```
planilha-importacao-usuarios-exemplo.xlsx
```

### Endpoint
```http
POST /usuarios/import
```
Formato: `multipart/form-data`
Campo: `file`

---

## 🔐 Autenticação

### Login
```http
POST /auth/login
```

Payload:
```json
{
  "login": "ana.silva@example.com",
  "senha": "Senha@123"
}
```

O token JWT retornado deve ser utilizado nos endpoints protegidos.

---

## 💸 Transações

### Criar transação
```http
POST /transacoes
```

```json
{
  "usuarioId": 1,
  "valor": 10.00,
  "moeda": "BRL",
  "categoria": "ALIMENTACAO"
}
```

- Retorno: `202 Accepted`
- Processamento assíncrono via Kafka

---

## 📊 Resumo de gastos
```http
GET /transacoes/resumo/{usuarioId}
```

---

## 📄 Download de relatório

```http
GET /transacoes/relatorio/download?usuarioId=1&periodo=mensal
```

- `periodo`: `diario` ou `mensal`
- Retorna arquivo Excel

---

## 🧪 Testes

O projeto conta com **testes automatizados utilizando JUnit 5, Mockito e Spring Boot Test** (via `spring-boot-starter-test`), focados nos fluxos essenciais definidos pelo desafio.

Os testes cobrem principalmente:
- Regras de **CRUD de usuários**, incluindo o comportamento de *soft delete* (usuários inativos não são retornados nem autenticam)
- Regras de autenticação e autorização
- Casos principais de uso nos microsserviços de transações e processamento

A estratégia adotada prioriza **clareza, coesão e confiabilidade**, garantindo que as funcionalidades críticas estejam validadas sem adicionar complexidade desnecessária ao projeto.

### Executar os testes
```bash
mvn test
```

Para executar apenas os testes do microsserviço de usuários:
```bash
cd ms-usuarios
mvn test
```

---

## 🧪 Testes manuais com Insomnia

Para testes manuais e validação dos endpoints, foi utilizado o **Insomnia**, permitindo executar requisições HTTP, simular fluxos completos da aplicação e validar respostas da API de forma prática durante o desenvolvimento.

O uso do Insomnia auxiliou principalmente na validação de:
- Fluxo de autenticação (login e uso do token JWT)
- Operações do CRUD de usuários
- Criação e processamento de transações
- Geração e download de relatórios

Essa abordagem facilitou a verificação funcional dos serviços sem acoplamento a ferramentas específicas de automação externa.

---

## ✅ Requisitos atendidos
- Docker Compose com ambiente completo
- CRUD de usuários
- Importação via Excel
- Processamento assíncrono
- Integração com API mock
- Integração com API pública
- Relatórios em Excel
- OpenAPI / Swagger
- Testes unitários

---


