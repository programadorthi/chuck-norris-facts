# Stone Mobile Engineer Challenge

Este projeto tem como finalidade participar da candidatura a vaga de Mobile Engineer na Stone.
É um projeto mobile Android que aplica, ao máximo possível, o que foi soliciado no desafio.

# Simulando Build de CI

As configuraçoes de CI variam de serviço para serviço. Esse projeto tem apenas um script que simula como seria o build de CI. As chamadas presentes no script funciona em qualquer ferramenta de CI. Para simular basta executar o script:

```
./any-ci-build.sh
```

> Na execução serão realizados os seguintes passos:
  - Validação com o ktlint
  - Validação com o detekt
  - Execução de todos os testes existentes no projeto
  - Build do projeto versão desenvolvimento

# Configurações mínimas

- Gradle 5.4.1;
- Android Studio 3.5.1 (mesmo não tendo suporte total a Kotlin DSL);
- File -> Settings -> Editor -> Code Style ->
	- Kotlin ->
		- Set from... -> Predefined Style -> Kotlin Style guide
		- Imports ->
			- Top-level Symbols -> Use single name import
			- Java Statics and Enum members -> Use single name import
			- Packages to Use import with '*' -> Uncheck all checkbox
	- XML ->
		- Set from... -> Predefined Style -> Android

# Projeto com Kotlin DSL

Kotlin DSL oferece um mecanismo mais amigável para configurações de projetos Gradle, além de outros suportes como:

- A statically typed and a type-safe DSL (inherited from Kotlin);
- An enhanced IDE editing experience (inherited from Kotlin);
- Interoperability with existing scripts (as a JVM language);
- Maximum readability (as any DSL);
- Consistency and super power (by using the same language across the project — source code and configuration 💯 Kotlin);

Referências: [Migrating Android build scripts from Groovy to Kotlin DSL], [Kotlin + buildSrc for Better Gradle Dependency Management]

## Problemas com Gradle DSL

-   Very poor IDE assistant when writing a Groovy DSL script
-   Performance issues*
-   Errors at build runtime instead of compile time
-   Painful build script debugging experience
-   Refactoring is a pain in 🤬

Referência: [Migrating Android build scripts from Groovy to Kotlin DSL]

# Estrutura do projeto

O projeto está estruturado para garantir uma boa escalabilidade e fornecer suporte futuro para recursos como [Dynamic Delivery] que ajudam a reduzir o tamanho do app e entregar features sobre demanda. Para facilitar encontrar as coisas, projetos que são features tem sufixos `-ui`, `-domain` e `-data` e projetos que tem coisas compartilháveis para outros projetos tem o prefixo `shared-`. Com isso, outros projetos podem escolher o que utilizar referenciando coisas específicas e melhorando o build do projeto.

> **Aviso**: esse projeto não nasceu do zero. Algumas das melhores práticas, estruturas e comportamentos foram obtidas combinando outras referências da internet. Todas elas serão referenciadas durante toda a documentação.
> "Na natureza nada se cria, nada se perde, tudo se transforma." - **Antoine Lavoisier**

## buildSrc

**Projeto com as configurações de todos os outros projetos**.
*A estrutura de buildSrc foi baseada no [Norris] com algumas modificações*.

 - **configs** - Pacote com arquivos que contém configurações de projeto Android, Kotlin, etc.
    - `AndroidConfig.kt` - Configurações para projetos Android. Ver [Configure your build].
    - `FlavorConfig.kt` - Configurações de product flavors. Ver [Configure build variants].
    - `KotlinConfig.kt` - Configurações do Kotlin como versão e JVM target.
    - `ProguardConfig.kt` - Configurações de proguard. Usada para aplicar arquivos proguard nos projetos.
    - `SigningConfig.kt` - Configurações de assinatura usadas para gerar a versão de produção do aplicativo.
 - **dependencies** - Pacote com as dependências e configurações para desenvolvimento e testes.
    - `Libraries.kt` - Contém as dependências de desenvolvimento e testes usadas no projeto.
    - `UnitTestDependencies.kt` - Classe helper que ajudar a adicionar dependências de testes unitários nos módulos usando uma lista pré-definida de dependências.
    - `InstrumentationTestsDependencies.kt` - Classe helper que ajudar a adicionar dependências de testes instrumentados nos módulos Android usando uma lista pré-definida de dependências.
 - **modules** - Pacotes com os nomes dos módulos existentes no projeto.
    - `ProjectModules.kt` - Contém os nomes dos módulos existentes no projeto. Todas vez que um módulo for adicionado ao projeto é necessário registrar nesse object. 😛
    - `LibraryType.kt` - Uma sealed class usada para carregar configurações específicas de cada módulo.
    - `LibraryModule.kt` - Classe helper que carrega as configurações do módulo que estão em arquivos `.gradle` com base no tipo informado pela `LibraryType`.
 - `BuildPlugins.kt` - Arquivos com os plugins que determinam o tipo de cada módulo e os que são usados para desenvolvimento e testes.
 - `Version.kt` - Uma data class usada para auxiliar na geração do número de versão.
 - `Versioning.kt` - Contém a lógica para versionamento do app. Usada para gerar o `versionCode` e `versionName` do aplicativo.

## shared-domain

**Projeto Kotlin com conteúdos de domínio compartilhados por todos os outros módulos**.
*Geralmente todos os outros módulos domain, data e UI terão referência direta/indireta para esse módulo*.

## shared-network

**Projeto Kotlin com recursos para consumir API. A ideia é centralizar recursos que são necessários para consumir API Rest**.
*Apenas módulos que consomem API ou configuração de DI terão referência para esse projeto*.

# Tecnologias
  - **Kotlin Serialization** - Usado para converter dados JSON. Com ela evita o Reflection do Gson e o generate adapter do Moshi.
  - **RxJava** - Poderia ser coroutines, mas quis manter a tecnologia descrita na última entrevista.
  - **Retrofit** - Esse dispensa explicações, pois é a lib mais utilizada para consumir APIs no Android.
  - **AndroidX** - Deixa o projeto mais clean baixando apenas dependências necessárias.
  - **ViewModel** - Ajuda a manter os dados após a Activity ser destruída, mesmo não tendo rotação de tela.
  - **LiveData** - Uma alternativa para emitir dados no padrão Observer. Trabalha bem com o clico de vida do Android, emitindo valores apenas quando a Activity/Fragment está disponível para receber.
  - **Material** Design Components - Componentes do Android com estilos do Material Design. Ajudam na criação de layout como Cards e Chips.
  - **Koin** - Um framework para ajudar na injeção de dependências. Poderia ser Dagger 2, Kodein, etc...
  - **assertJ** - Framework de testes que fornece informações significativas quando os testes falham
  - **Robolectric** - Ajuda na criação de testes de UI que se comportam como testes unitários. É mais rápido na execução e validação dos testes, diminuindo o tempo de execução no trabalho local e na execução de um build no CI.

# Explicando alguns comportamentos

## shared-network
  - **NetworkingError** - Uma sealed class com alguns tipos de erro que podem acontecer durante uma operação de chamada de API. São utilizadas para representar erros que aconteceram na camada de data quando tentava realizar uma chamada de API.
  - **DefaultNetworkManager** - Executa todo tipo de chamada de API e mapeia os cenários de erro quando encontrado algum para o **NetworkingError**. Todas as suas operações só são executadas após checagem de conexão com a internet.

## facts-data
  - **FactsMapper** - Toda chamada de API que retorna dados para serem consumidos pelo app deveria ter um Mapper. A responsabilidade do Mapper é garantir que campos obrigatórios para funcionamento da aplicação estejam sendo retornados pela API. Caso algum desses campos esteja faltando, um **NetworkingError.EssentialParamMissing** deveria ser lançando informando quais campos estão faltando e em qual resposta da API. Assim, basta logar a exceção em algum sistema de crash para monitorar o problema e reportar ao backend. Com isso, garante-se que o app sempre vai funcionar mesmo quando algum dos dados obrigatórios não foi retornado.
  - **classes raw** - São classes com configurações de serialização. Os campos obrigatórios vão para `const val` em `companion object`, pois assim é possível logar o nome do campo no Mapper.
  - **FactsRepositoryImpl** - Como existe mais de uma fonte de dados, para o Domain só deveria existir uma e é por isso que existe esse repository para centralizar os dados que podem ser local ou remoto na hora de retornar para a Domain.

## facts-domain
  - **FactsBusiness** - Uma sealed class com todos os tipos de validação que pode acontecer. Como a View e o ViewModel não deveriam e não devem realizar validações, todos os status de validação de são representados por classes como essas.
  - **FactsUseCaseImpl** - Aplica validações para os dados obtidos pela camada de apresentação. Também, em caso de validado com sucesso, pode solicitar a camada de data que execute alguma ação.

## facts-ui
  - **classes ViewModel** - Gerenciam os dados apresentados na UI. Seu comportamento é apenas obter informações e repassar para a UI quando a mesma estiver disponível. Também mapeia o sucesso ou erro para o tipo `Result` facilitando para a UI receber a informação unificada. PS: **Não há necessidade de usar o observeOn aqui, pois toda operação já executa no Scheduler de IO e, quando usado o postValue do MutableLiveData, ele já envia o valor na Main Thread.**
  - **app module** - Algumas abstrações foram implementadas dentro do módulo de aplicação. Indenpendente da abordagem, como a que foi aplicada no projeto com `library` ou um futuramete `dynamic feature`, isso facilita compartilhar entre módulos os comportametos, já que são instanciados via dependency injection.
  - **MainActivity** - Está usando um `Handler` apenas para simular uma Splash Screen que realiza uma operação de checagem de informação, como por exemplo, verificar se o usuário está logado.





[Migrating Android build scripts from Groovy to Kotlin DSL]: <https://proandroiddev.com/migrating-android-build-scripts-from-groovy-to-kotlin-dsl-f8db79dd6737>

[Kotlin + buildSrc for Better Gradle Dependency Management]: <https://handstandsam.com/2018/02/11/kotlin-buildsrc-for-better-gradle-dependency-management/>

[Norris]: <https://github.com/dotanuki-labs/norris>

[Configure your build]: <https://developer.android.com/studio/build>

[Configure build variants]: <https://developer.android.com/studio/build/build-variants>