Sistema de Empréstimo de Equipamentos

Esse é o meu Projeto 2: uma API para controlar o empréstimo de equipamentos (notebooks, monitores, headsets) para os colaboradores de uma empresa. A ideia é acabar com a planilha e saber direitinho quem está com o quê e o que está atrasado.

**API no Render**:(https://emprestimo-equipamento.onrender.com)

Feito com Java 25, Spring Boot 4, Spring Data JPA, banco H2, Spring Security com JWT, JUnit 5 e MockMvc.

## Como rodar

Você precisa ter o Java 25 instalado. Na pasta do projeto, rode:

```
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`. A API sobe em `http://localhost:8080`.

Para rodar os testes:

```
./mvnw test
```

O banco é o H2 e fica salvo na pasta `dados`. O console dele abre em `/h2-console`. Se quiser trocar o segredo do token, use a variável de ambiente `JWT_SEGREDO` (com 32 caracteres ou mais). Se não definir, a aplicação usa um valor de desenvolvimento.

## Como fazer login

Quase todas as rotas pedem um token. Para conseguir um, faça um `POST /auth/login` com este corpo:

```json
{"usuario": "admin", "senha": "admin123"}
```

A resposta traz o token. Depois é só mandá-lo nas outras requisições, no cabeçalho `Authorization: Bearer <token>`.

Existem dois usuários de teste:

- **admin** (senha `admin123`): pode tudo.
- **colaborador** (senha `colab123`): pode listar, buscar, emprestar e devolver.

Sem token, a API responde 401. Com token, mas sem permissão para aquela rota, responde 403.

## Rotas

**Autenticação**
- `POST /auth/login`: faz o login e devolve o token

**Equipamentos**
- `GET /equipamentos`: lista todos (dá para filtrar com `?tipo=` e `?disponivel=`)
- `GET /equipamentos/{id}`: busca um equipamento
- `POST /equipamentos`: cadastra (201)
- `PUT /equipamentos/{id}`: atualiza o nome e o tipo
- `DELETE /equipamentos/{id}`: remove, mas só se ele nunca foi emprestado (204)

**Colaboradores**
- `POST /colaboradores`: cadastra (201)
- `GET /colaboradores`: lista todos
- `GET /colaboradores/{id}`: busca um colaborador
- `GET /colaboradores/{id}/emprestimos`: mostra o histórico de empréstimos dele

**Empréstimos**
- `POST /emprestimos?equipamentoId=1&colaboradorId=2`: registra um empréstimo (201)
- `GET /emprestimos`: lista todos (com `?atrasados=true`, só os atrasados)
- `GET /emprestimos/{id}`: busca um empréstimo
- `PUT /emprestimos/{id}/devolucao`: registra a devolução

## Códigos de erro

- **400**: os dados enviados são inválidos
- **401**: não mandou o token, ou o login está errado
- **403**: o perfil não tem permissão para essa rota
- **404**: o equipamento, o colaborador ou o empréstimo não existe
- **409**: conflito com as regras do sistema, por exemplo patrimônio ou e-mail repetido, equipamento já emprestado, empréstimo já devolvido, equipamento com histórico ou colaborador com empréstimo atrasado

## Bônus que eu fiz

- Validação dos dados (`@Valid`, `@NotBlank`, `@Email`, `@NotNull`)
- Tratamento de erros com `@RestControllerAdvice`, cada erro com o status certo
- Relacionamento com `@ManyToOne` no empréstimo
- Testes de integração com MockMvc
- Spring Security com JWT e dois perfis (ADMIN e COLABORADOR)
- Swagger para documentar e testar as rotas (`/swagger-ui/index.html`)
- Actuator com a rota de saúde da aplicação (`/actuator/health`)