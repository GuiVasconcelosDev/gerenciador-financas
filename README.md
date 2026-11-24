# Gerenciador de Finanças

Aplicação Java de terminal para registrar e consultar receitas e despesas. O projeto pratica orientação a objetos, JDBC, persistência em PostgreSQL e manipulação de arquivos CSV.

> **Finalidade acadêmica:** este projeto é funcional e foi desenvolvido exclusivamente para estudos e fins acadêmicos. Não é destinado ao uso em produção nem à gestão de finanças reais.

## Funcionalidades

- Cadastro de receitas e despesas
- Listagem das movimentações financeiras
- Persistência em PostgreSQL
- Implementação alternativa de leitura e escrita em CSV
- Exportação das transações do PostgreSQL para `financas.csv` pelo menu
- Ordenação das transações por data
- Menu interativo no terminal

## Tecnologias

- Java 25
- Maven
- JDBC
- PostgreSQL Driver 42.7.3
- PostgreSQL
- CSV

## Banco de dados

### Docker Compose

Suba o PostgreSQL isolado deste projeto:

```bash
docker compose up -d postgres
```

As configurações locais ficam no arquivo `.env`. Use `.env.example` como referência e
não versione o arquivo `.env`. Na primeira execução, crie o arquivo local e troque
`change-me` por uma senha de desenvolvimento:

```bash
cp .env.example .env
```

Mantenha `POSTGRES_PASSWORD` e `DB_PASSWORD` com o mesmo valor.

Em seguida, exporte as variáveis para executar a aplicação Java:

```bash
set -a
. ./.env
set +a
mvn compile exec:java -Dexec.mainClass=br.com.guilhermedev.financas.principal.Principal
```

Crie a tabela antes de executar:

```sql
CREATE TABLE transacoes (
    id BIGSERIAL PRIMARY KEY,
    data DATE NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    valor NUMERIC(12, 2) NOT NULL
);
```

Depois, ajuste a URL, o usuário e a senha em `ConnectionFactory.java` para o seu ambiente.

> Não mantenha credenciais reais no repositório. Prefira variáveis de ambiente.

## Como executar

```bash
git clone https://github.com/GuiVasconcelosDev/gerenciador-finacas.git
cd gerenciador-finacas
mvn clean compile
```

Execute a classe `br.com.guilhermedev.finacas.principal.Principal` pela IDE.

> Para execução direta pela JVM, o método de entrada precisa seguir a assinatura `public static void main(String[] args)`.

## Estrutura

```text
src/main/java/br/com/guilhermedev/finacas/
├── model/
│   ├── TipoTransacao.java
│   └── Transacao.java
├── principal/Principal.java
└── service/
    ├── ConnectionFactory.java
    ├── GerenciadorArquivo.java
    └── GerenciadorBanco.java
```

## Melhorias sugeridas

- Externalizar as credenciais do PostgreSQL
- Usar `BigDecimal` para valores monetários
- Criar a tabela com migrações
- Adicionar validações e testes
- Ajustar o ponto de entrada para a assinatura padrão
- Permitir edição, exclusão e resumo por categoria

## Autor

Desenvolvido por [Guilherme Vasconcelos](https://github.com/GuiVasconcelosDev).
