# Kanban

Este projeto é uma API REST para gestão de Projetos e Responsáveis utilizando um fluxo de status baseado em regras de negócio definidas no desafio técnico.  
O sistema permite criar, atualizar, realizar transições e consultar.

# Decisões Técnicas

### Padrão Strategy usado para regras de transição
Cada transição `Status X → Status Y` é uma classe individual que implementa as regras especícas da transição.

- Fácil testar cada regra isoladamente  
- Possibilita a adição de novas regras sem afetar as regras existentes  

### Validações via Bean Validation
- Validações aplicadas nos DTOs
- Permite que todas as validações sejam feitas ao mesmo tempo, e que todas as mensagens sejam retornadas com os motivos da rejeição da requisição.

### Data Transfer Object (DTO)
- Evita a exposição de dados sensíveis
- Permite validar os dados de entrada de forma centralizada
- Reduz acoplamento permitindo que as camadas da aplicação evoluam independentemente. Por exemplo, o modelo de domínio pode mudar sem afetar a forma como os dados são transferidos.

# Acesso o Swagger

Depois que a aplicação estiver rodando:

### Swagger UI:
http://localhost:8080/swagger-ui/index.html

### OpenAPI JSON:
http://localhost:8080/api-docs

# Como Rodar os Testes
`mvn test`

# Como Rodar o Projeto
Na raiz do projeto, onde está o Dockerfile

`docker compose build`

Rodar em segundo plano:

`docker compose up -d`

Finalizar a execução

`docker compose down`

# Estrutura de Pacotes

src/<br>
&nbsp;├── main/java/br/com/facilit/kanban<br>
&nbsp;│&nbsp;&nbsp;&nbsp;├── common/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;└── util/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;├── config/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;├── controller/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;├── dto/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;└── request/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;└── response/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;└── shared/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;├── mapper/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;├── model/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;├── repository/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;├── service/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;└── status/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└── strategy/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;├── validation/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;├── transition/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;├── rules/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;└── transition<br>
&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└── rule/<br>
&nbsp;│&nbsp;&nbsp;&nbsp;└── exception/<br>
&nbsp;│<br>
&nbsp;└── test/java/...<br>

# Próximos Passos
- Testes de integração com Testcontainers
- Endpoint de filtro avançado (por status, prazo, responsável)