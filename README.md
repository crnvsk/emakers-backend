# Sistema de Gerenciamento de Empréstimo de Livros

## Descrição do Projeto

Este projeto é uma API RESTful desenvolvida com o framework Spring Boot para gerenciar um sistema de empréstimo de livros. A aplicação permite CRUDs (Create, Read, Update, Delete) de livros e pessoas, além de gerenciar o empréstimo e devolução de livros por parte das pessoas. O sistema foi projetado para seguir uma relação muitos-para-muitos (m:n) entre pessoas e livros, possibilitando que uma pessoa possa pegar emprestado vários livros e que um livro possa ser emprestado por várias pessoas.

## Funcionalidades

- **CRUD Livro**: Permite criar, ler, atualizar e deletar informações de livros.
- **CRUD Pessoa**: Permite criar, ler, atualizar e deletar informações de pessoas.
- **Relação Pessoa-Livro**: Implementa a relação m:n entre pessoas e livros.
- **Empréstimo de Livro**: Função para que uma pessoa pegue um livro emprestado.
- **Devolução de Livro**: Função para que uma pessoa devolva um livro emprestado.
- **Documentação com Swagger**: A API é documentada utilizando o Swagger para facilitar a compreensão e uso dos endpoints.

## Endpoints da API

A API oferece os seguintes endpoints para interação com os recursos de Livro e Pessoa:

### Livro

- `GET /livros` - Retorna a lista de todos os livros.
- `GET /livros/{id}` - Retorna os detalhes de um livro específico.
- `POST /livros` - Cria um novo livro.
- `PUT /livros/{id}` - Atualiza as informações de um livro existente.
- `DELETE /livros/{id}` - Deleta um livro existente.

### Pessoa

- `GET /pessoas` - Retorna a lista de todas as pessoas.
- `GET /pessoas/{id}` - Retorna os detalhes de uma pessoa específica.
- `POST /pessoas` - Cria uma nova pessoa.
- `PUT /pessoas/{id}` - Atualiza as informações de uma pessoa existente.
- `DELETE /pessoas/{id}` - Deleta uma pessoa existente.

### Empréstimo de Livro

- `POST /emprestimos` - Permite que uma pessoa pegue um livro emprestado.
- `DELETE /emprestimos` - Permite que uma pessoa devolva um livro emprestado.

## Documentação com Swagger

A documentação da API foi gerada utilizando o Swagger. Para acessar a documentação, basta iniciar a aplicação e acessar a URL `/swagger-ui.html` no navegador.