PromptVault API
Gerenciador de Prompts para Times de IA

Permite versionar, categorizar e compartilhar prompts de IA dentro de equipes, com versionamento semântico, tags de contexto e um endpoint para testar prompts via integração simulada com Azure OpenAI ou Ollama local.

Stack
Java 25
Spring Boot 4.0.0
Spring Data JPA
H2 Database (dev) / PostgreSQL (prod)
Springdoc OpenAPI (Swagger UI)
Bean Validation
Lombok
Maven
Como executar
Pré-requisitos: JDK 25 e Maven 3.9+.

mvn spring-boot:run
A aplicação sobe em http://localhost:8080.

Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs
H2 Console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:promptvault)
Para rodar com PostgreSQL, ative o profile prod:

mvn spring-boot:run -Dspring-boot.run.profiles=prod
Endpoints principais

Criar prompt
curl -X POST http://localhost:8080/api/v1/prompts \
  -H "Content-Type: application/json" \
  -d '{
    "name": "resumo-executivo",
    "description": "Prompt para gerar resumos executivos",
    "category": "business",
    "tags": ["resumo", "executivo"]
  }'
Criar versão do prompt
curl -X POST http://localhost:8080/api/v1/prompts/1/versions \
  -H "Content-Type: application/json" \
  -d '{
    "version": "1.0.0",
    "content": "Você é um analista. Resuma o seguinte texto em {{idioma}}: {{texto}}",
    "changelog": "Versão inicial",
    "author": "Eduardo Domingues"
  }'

  
Testar prompt (simulado)
curl -X POST http://localhost:8080/api/v1/prompts/1/test \
  -H "Content-Type: application/json" \
  -d '{
    "version": "1.0.0",
    "provider": "azure-openai",
    "model": "gpt-4o",
    "variables": {
      "idioma": "português",
      "texto": "Relatório trimestral com métricas de crescimento..."
    }
  }'

  
Listar prompts (paginado)
curl "http://localhost:8080/api/v1/prompts?page=0&size=10"
Adicionar/remover tags
curl -X POST http://localhost:8080/api/v1/prompts/1/tags/producao
curl -X DELETE http://localhost:8080/api/v1/prompts/1/tags/producao
Estrutura de pastas


promptvault-api/
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java/com/eduardodomingues/promptvault
    │   │   ├── PromptVaultApplication.java
    │   │   ├── config/
    │   │   ├── controller/
    │   │   ├── dto/
    │   │   ├── exception/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   └── service/
    │   └── resources
    │       ├── application.properties
    │       └── application-prod.properties
    └── test
        └── java/com/eduardodomingues/promptvault
            └── PromptVaultApplicationTests.java

            
Observações sobre a integração com providers
O endpoint POST /api/v1/prompts/{id}/test atualmente executa uma simulação: renderiza o template substituindo {{variavel}} pelos valores enviados e retorna uma resposta simulada indicando o provider (azure-openai ou ollama). Para integração real, basta implementar clientes HTTP nos serviços correspondentes usando as propriedades já reservadas em application.properties.

Autor
Eduardo Domingues
