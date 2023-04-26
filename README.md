# Task Manager para a ESIG

_Um gerenciador de tarefas responsável por cadastrar, atualizar, listar e deletar tarefas_

## Introdução

Esta aplicação foi criada no intuito de ter um controle e gerenciamento sobre as tarefas da ESIG, conseguindo designar responsáveis por determinadas tarefas, cadastrar quando surgirem novas, além de editar e remover as que já existem.


## Frameworks

Esta se trata de uma aplicação [Spring Boot](https://spring.io/), que une [Maven](https://maven.apache.org/), [JSF](https://pt.wikipedia.org/wiki/JavaServer_Faces) e [Primefaces](https://www.primefaces.org/). Fiz o uso do [Undertow Server](https://undertow.io/), que é um servidor web de desempenho flexível escrito em Java, e para estes casos em específico costuma desempenhar melhor do que o Tomcat Server.

O projeto conta com uma implementação personalizada do padrão de arquitetura de software [MVC](https://pt.wikipedia.org/wiki/MVC) e se conecta de forma persistente ao banco de dados [PostgreSQL](https://www.postgresql.org/) através do [JDBC Driver](https://jdbc.postgresql.org/).

## Código

A classe modelo "_Tarefa_", possui todos os atributos necessários para descrever uma tarefa. Sendo os principais:
1. Título
2. Descrição
3. Responsável
4. Prioridade
5. Data Limite

As páginas em _.xhtml_ se comunicam diretamente com o [ManagedBean](https://www.devmedia.com.br/introducao-ao-jsf-managed-bean/29390#:~:text=O%20ManagedBean%20funciona%20como%20uma%20classe%20que%20delega%20fun%C3%A7%C3%B5es%20para%20cada%20View.&text=O%20JSF%20(Java%20Server%20Faces,XHTML%20e%20assim%20por%20diante).) "_TarefaMBean_", uma classe Java que possui métodos e atributos que são utilizados durante a execução da aplicação para realizar operações que dialogam com a classe "_AcoesBD_". O nome é bem sugestivo, essa classe Java conta com métodos responsáveis por atuar diretamente no banco de dados, realizando cadastro, atualização, leitura e remoção de tarefas.

## Otimizações

A página "_listar-tarefas.xhtml_" é composta por duas tabelas. 
1. Tarefas em andamento
2. Tarefas concluídas

A tentativa de englobar todas as tarefas numa tabela só não era a mais apropriada, pois os botões de ação "_Editar_", "_Excluir_" e "_Concluir_" não funcionavam corretamente após filtrar ou ordenar a tabela de acordo com sua situação. O Primefaces não dá suporte para que seja possível e viável esta funcionalidade. 

Além de que não parece adequado que uma tarefa suja situação já é concluída, tenha um botão "_Concluir_", algo que aconteceria ao englobar todas as tarefas numa única tabela.

O campo "Responsável" na página "_cadastrar-tarefas.xhtml_" é um *inputText*, pois não seria estratégico utilizar um seletor de opções, visto que podem existir dezenas ou centenas de funcionários na equipe, que poderiam ser uma opção como responsável por aquela tarefa.

Além de que, ao expandirmos a equipe, ou reduzirmos, seria necessário estar constantemente atualizando esse _Enum_ Responsável, para adicionar ou remover valores.

## Utilização

### Banco de Dados:

Você precisa ter o [PostgreSQL](https://www.postgresql.org/) (pgAdmin 4) instalado na sua máquina. Após instalar, execute o programa, e ao longo das configurações iniciais, você precisará criar uma senha. Essa senha será utilizada para se conectar ao banco de dados que você vai criar. No dashboard inicial, localize _**Servers**_ (geralmente está na parte superior esquerda da tela), clique e vai abrir **PostgreSQL 15**, com o botão direito clique em **Databases**, depois crie uma nova database com o nome "_TaskManager_".

Na sua IDE de preferência, com o projeto já aberto, busque pelo caminho _**src/main/resources**_ abra o arquivo "_application.properties_", realize as alterações necessárias para se adequar com a sua máquina, tal qual:
1. Porta
2. Nome de usuário
3. Senha

Caso as configurações padrão não tenham sido alteradas, você vai precisar apenas adequar a sua *Senha*.

### Execução e uso:

Você irá executar o projeto com o [Undertow Server](https://undertow.io/). Feito isso, a aplicação irá carregar todas as dependências e executar.  

Para desfrutar das funções da aplicação basta abrir o url da sua porta local, geralmente **localhost:8080/main.xhtml** e a partir de lá navegar pelas páginas **cadastrar-tarefa.xhtml** e **listar-tarefas.xhtml**, ou ir direto para cada uma delas, apenas substituindo o endpoint no url da main.

**Sobre as páginas:** 
> Cadastrar Tarefa
1. Não esqueça de preencher os campos obrigatórios;
2. Para esvaziar os campos, recarregue a página.

> Listar Tarefas
1. Ao clicar nos botões _"Editar"_, _"Excluir"_ ou _"Concluir"_ recarregue a página para ver as alterações.

