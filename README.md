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

           
Observações sobre a integração com providers
O endpoint POST /api/v1/prompts/{id}/test atualmente executa uma simulação: renderiza o template substituindo {{variavel}} pelos valores enviados e retorna uma resposta simulada indicando o provider (azure-openai ou ollama). Para integração real, basta implementar clientes HTTP nos serviços correspondentes usando as propriedades já reservadas em application.properties.

Autor
Eduardo Domingues
