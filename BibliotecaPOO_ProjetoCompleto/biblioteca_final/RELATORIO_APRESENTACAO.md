# Projeto POO — Sistema de Biblioteca

## 1. Objetivo
Sistema de console para gerir pessoas (alunos e funcionários), livros e empréstimos. Implementa cadastros, alterações, remoções com regras de negócio, pesquisa e ordenação.

## 2. Classes e responsabilidades
- **Exportavel (interface):** contrato comum `getId`, `setId` e `paraLinhaTabela`, usado para identificar e exibir registros.
- **Pessoa (abstrata):** concentra id, nome e CPF; declara `getTipo` e `getLimiteEmprestimos` abstratos e fornece formatação base.
- **Aluno:** herda Pessoa, acrescenta matrícula e curso, define tipo Aluno e limite de 3 livros.
- **Funcionario:** herda Pessoa, acrescenta cargo e setor, define tipo Funcionário e limite de 5 livros.
- **Livro:** título, autor, ano e disponibilidade.
- **Emprestimo:** associa Pessoa e Livro, data e situação. `devolver()` devolve o status e libera o livro, sem duplicar ação em caso de chamada repetida.
- **Repositorio<T>:** interface genérica de operações CRUD essenciais.
- **RepositorioEmMemoria<T>:** implementação com ArrayList, IDs sequenciais e lista devolvida em cópia.
- **DadosIniciais:** cria 14 pessoas (7 alunos e 7 funcionários), 10 livros e 7 empréstimos de demonstração. Dois empréstimos estão devolvidos, há empréstimo de funcionário e há livros disponíveis para testar novas operações.
- **Entrada:** leitura de texto e inteiros, com repetição quando inteiro inválido.
- **TabelaUtil:** cabeçalhos e impressão de listas; sinaliza listas vazias.
- **PessoaServico:** regras e operações sobre pessoas, incluindo impedir remoção de quem tem empréstimo aberto.
- **LivroServico:** regras de cadastro, alteração, pesquisa, ordenação e remoção de livros; impede excluir livro indisponível ou ligado a empréstimo aberto.
- **EmprestimoServico:** valida data, pessoa, livro, disponibilidade e limite individual; registra, devolve, pesquisa e ordena empréstimos.
- **Programa:** composição/arranque do sistema, instancia repositórios, serviços, liga dependências e apresenta os menus.

## 3. Conceitos exigidos de POO (onde demonstrar)
### Abstração
`Pessoa.java`: classe abstrata modela os atributos comuns e obriga subclasses a definir tipo e limite.

### Herança
`Aluno.java` e `Funcionario.java` estendem `Pessoa`, reutilizando seus atributos e métodos, e adicionando características próprias.

### Polimorfismo
- `getTipo()` e `getLimiteEmprestimos()` são implementados de forma diferente por Aluno e Funcionario. Em `EmprestimoServico.registrar`, o limite é consultado pela referência `Pessoa` (`p.getLimiteEmprestimos()`), sem precisar distinguir manualmente o subtipo.
- `paraLinhaTabela()` é sobrescrito nos tipos Pessoa, Aluno, Funcionario e demais classes exportáveis. A impressão usa referências da interface/base e invoca a versão correspondente.

### Interfaces
- `Exportavel` define contrato de identificação e apresentação.
- `Repositorio<T>` define operações de repositório, sem acoplar serviços à implementação concreta.

### Composição / associação de objetos
`Emprestimo` mantém referências para a pessoa e o livro envolvidos. O registro liga esses objetos; a devolução altera o estado do livro associado. (Na arguição, descrevam como composição/relacionamento entre objetos, conforme nomenclatura adotada na disciplina.)

### Encapsulamento
Atributos privados e acesso por getters/setters; regras de empréstimo ficam nos serviços em vez de espalhadas pelo menu.

### Generics
`Repositorio<T extends Exportavel>` e `RepositorioEmMemoria<T>` permitem reutilizar a implementação para Pessoas, Livros e Empréstimos, respeitando o contrato.

### Inversão de controle / injeção de dependência
Serviços trabalham com interfaces `Repositorio`, recebidas por construtor. `Programa` cria e conecta as dependências, evitando que os serviços instanciem seus repositórios internamente.

### Comparator / ordenação
`PessoaServico` ordena por nome; `LivroServico` por título e ano; `EmprestimoServico` converte a String da data em `LocalDate` para ordenação cronológica correta.

## 4. Regras e validações implementadas
- IDs sequenciais atribuídos no repositório.
- Aluno: máximo 3 empréstimos em aberto; funcionário: máximo 5.
- Não registra empréstimo para pessoa/livro inexistente ou livro indisponível.
- Data validada no formato `dd/MM/yyyy`.
- Devolução libera o livro; devolução repetida não reabre nem altera indevidamente.
- Remoção de pessoa bloqueada se houver empréstimo aberto.
- Remoção de livro bloqueada se indisponível ou com empréstimo aberto.
- Excluir empréstimo aberto devolve o livro antes de excluir o registro.
- Pesquisa textual por trecho e sem diferenciar maiúsculas/minúsculas.
- Cabeçalho de pessoas apresenta complemento, evitando confusão entre matrícula/curso e cargo/setor.

## 5. Menus
- Pessoas: incluir aluno, incluir funcionário, alterar dados comuns, excluir, listar, pesquisar e ordenar por nome.
- Livros: incluir, alterar, excluir, listar, pesquisar por título, ordenar por título e por ano.
- Empréstimos: registrar, devolver, excluir, listar, pesquisar pelo nome da pessoa e ordenar por data.

## 6. Dados de demonstração
14 pessoas; 10 livros (além dos 7 exigidos para permitir disponibilidade de teste); 7 empréstimos seed. A seleção inicial contempla aluno e funcionário, e inclui empréstimos devolvidos para que o acervo não comece completamente indisponível.

## 7. Divisão de autoria para apresentar
**Pessoa A — Modelo, repositório e dados:** Exportavel, Pessoa, Aluno, Funcionario, Livro, Emprestimo, Repositorio, RepositorioEmMemoria e DadosIniciais.

**Pessoa B — Serviços, utilitários e aplicação:** Entrada, TabelaUtil, PessoaServico, LivroServico, EmprestimoServico e Programa. Pessoa B também pode explicar as validações e as ligações entre as partes.

## 8. Roteiro de demonstração oral
1. Mostrar `Pessoa` e explicar por que não é instanciada diretamente.
2. Mostrar `Aluno`/`Funcionario` e os overrides, incluindo limites diferentes.
3. Mostrar `Repositorio<T>` e `RepositorioEmMemoria<T>` com geração de IDs e cópia da listagem.
4. Cadastrar pessoa e livro; pesquisar e ordenar.
5. Registrar empréstimo, destacar limite polimórfico e disponibilidade.
6. Devolver e mostrar que livro volta a ficar disponível.
7. Tentar excluir pessoa com empréstimo aberto para demonstrar regra; repetir após devolução.
8. Mostrar ordenação de empréstimos convertendo datas para `LocalDate`.
9. Explicar `Programa` como ponto de montagem das dependências e menus.

## 9. Teste técnico
O projeto foi compilado com `javac` (Java 21) e o menu principal foi executado com saída de encerramento. Isso comprova compilação e inicialização básica. Recomenda-se, antes da entrega final, executar manualmente cada opção no Eclipse e conferir a codificação UTF-8 do console.
