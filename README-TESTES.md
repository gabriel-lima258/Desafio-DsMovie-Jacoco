# Documentação de Testes - DSMovie

Este documento descreve todos os testes implementados no projeto DSMovie, incluindo suas funcionalidades, cobertura e detalhes de implementação.

## 📊 Resumo dos Testes

- **Total de Testes**: 16
- **Sucesso**: 16 ✅
- **Falhas**: 0
- **Erros**: 0
- **Cobertura**: JaCoCo configurado para análise de cobertura de código

## 🧪 Estrutura de Testes

### 1. DsmovieApplicationTests
**Localização**: `src/test/java/com/devsuperior/dsmovie/DsmovieApplicationTests.java`

#### Testes Implementados:
- **`contextLoads()`**: Verifica se o contexto da aplicação Spring Boot carrega corretamente

**Descrição**: Teste de integração básico que valida se a aplicação inicializa sem erros.

---

### 2. ScoreServiceTests
**Localização**: `src/test/java/com/devsuperior/dsmovie/services/ScoreServiceTests.java`

**Tecnologias**: JUnit 5, Mockito, Spring Extension

#### Testes Implementados:

##### ✅ `saveScoreShouldReturnMovieDTO()`
- **Objetivo**: Verifica se o método `saveScore` retorna um `MovieDTO` corretamente após salvar uma avaliação
- **Cenário**: Usuário autenticado salva uma avaliação para um filme existente
- **Validações**:
  - Resultado não é nulo
  - ID do filme retornado corresponde ao esperado
  - Título do filme corresponde ao esperado
  - Score foi calculado corretamente (não nulo)
  - Count de avaliações é maior que zero
- **Cobertura**: Cobre o loop de cálculo da soma dos scores (`double sum = 0.0; for (ScoreEntity s : movie.getScores())`)

##### ✅ `saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId()`
- **Objetivo**: Verifica se o método lança exceção quando o filme não existe
- **Cenário**: Tentativa de salvar avaliação para um filme inexistente
- **Validações**:
  - Lança `ResourceNotFoundException` quando o ID do filme não existe

#### Mocks Utilizados:
- `MovieRepository`: Mock para buscar filmes
- `ScoreRepository`: Mock para salvar avaliações
- `UserService`: Mock para obter usuário autenticado

#### Setup (`@BeforeEach`):
- Configuração de IDs de filmes existentes e inexistentes
- Criação de entidades de teste usando factories
- Configuração de mocks para repositórios
- **Importante**: O score é adicionado à lista de scores do movie para garantir cobertura do loop de cálculo

---

### 3. MovieServiceTests
**Localização**: `src/test/java/com/devsuperior/dsmovie/services/MovieServiceTests.java`

**Tecnologias**: JUnit 5, Mockito, Spring Extension

#### Testes Implementados:

##### ✅ `findAllShouldReturnPagedMovieDTO()`
- **Objetivo**: Verifica se retorna uma página de filmes paginada
- **Validações**: Resultado não é nulo e contém o título esperado

##### ✅ `findByIdShouldReturnMovieDTOWhenIdExists()`
- **Objetivo**: Verifica se retorna um filme quando o ID existe
- **Validações**: Resultado não é nulo e ID corresponde ao esperado

##### ✅ `findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist()`
- **Objetivo**: Verifica se lança exceção quando o filme não existe
- **Validações**: Lança `ResourceNotFoundException`

##### ✅ `insertShouldReturnMovieDTO()`
- **Objetivo**: Verifica se insere um novo filme corretamente
- **Validações**: Resultado não é nulo e ID corresponde ao esperado

##### ✅ `updateShouldReturnMovieDTOWhenIdExists()`
- **Objetivo**: Verifica se atualiza um filme existente
- **Validações**: Resultado não é nulo e ID corresponde ao esperado

##### ✅ `updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist()`
- **Objetivo**: Verifica se lança exceção ao tentar atualizar filme inexistente
- **Validações**: Lança `ResourceNotFoundException`

##### ✅ `deleteShouldDoNothingWhenIdExists()`
- **Objetivo**: Verifica se deleta um filme existente sem erros
- **Validações**: Não lança exceção e verifica chamada do repositório

##### ✅ `deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist()`
- **Objetivo**: Verifica se lança exceção ao tentar deletar filme inexistente
- **Validações**: Lança `ResourceNotFoundException`

##### ✅ `deleteShouldThrowDatabaseExceptionWhenDependentId()`
- **Objetivo**: Verifica se lança exceção ao tentar deletar filme com dependências
- **Validações**: Lança `DatabaseException` quando há violação de integridade

#### Mocks Utilizados:
- `MovieRepository`: Mock para operações CRUD de filmes

---

### 4. UserServiceTests
**Localização**: `src/test/java/com/devsuperior/dsmovie/services/UserServiceTests.java`

**Tecnologias**: JUnit 5, Mockito, Spring Extension

#### Testes Implementados:

##### ✅ `authenticatedShouldReturnUserEntityWhenUserExists()`
- **Objetivo**: Verifica se retorna o usuário autenticado quando existe
- **Validações**: Resultado não é nulo e username corresponde ao esperado

##### ✅ `authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists()`
- **Objetivo**: Verifica se lança exceção quando usuário não existe
- **Validações**: Lança `UsernameNotFoundException` quando há erro de cast

##### ✅ `loadUserByUsernameShouldReturnUserDetailsWhenUserExists()`
- **Objetivo**: Verifica se retorna UserDetails quando usuário existe
- **Validações**: Resultado não é nulo e username corresponde ao esperado

##### ✅ `loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists()`
- **Objetivo**: Verifica se lança exceção quando usuário não existe
- **Validações**: Lança `UsernameNotFoundException` quando lista de usuários está vazia

#### Mocks Utilizados:
- `UserRepository`: Mock para buscar usuários e roles
- `PasswordEncoder`: Mock para codificação de senhas
- `CustomUserUtil`: Mock para obter usuário logado

---

## 🏭 Factories de Teste

O projeto utiliza factories para criar objetos de teste de forma consistente:

- **MovieFactory**: Cria entidades e DTOs de filmes
- **ScoreFactory**: Cria entidades e DTOs de avaliações
- **UserFactory**: Cria entidades de usuários
- **UserDetailsFactory**: Cria projeções de detalhes de usuário

## 🔧 Configuração de Testes

### Requisitos
- **Java**: 21
- **Maven**: 3.9+
- **Spring Boot**: 3.4.4
- **JUnit**: 5
- **Mockito**: Incluído no Spring Boot Test

### Executar Testes

#### Executar todos os testes:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn test
```

#### Executar teste específico:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn test -Dtest=ScoreServiceTests
```

#### Executar com cobertura JaCoCo:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn clean install
```

O relatório de cobertura será gerado em: `target/jacoco-report/index.html`

## 📈 Cobertura de Código

O projeto utiliza **JaCoCo** para análise de cobertura. As seguintes classes são excluídas da análise:

- `DsmovieApplication.class`
- `config/**`
- `entities/**`
- `dto/**`
- `controllers/**`
- `controllers/handlers/**`
- `services/exceptions/**`
- `utils/**`

A cobertura é focada nas classes de serviço:
- `MovieService`
- `ScoreService`
- `UserService`

## 🐛 Problemas Resolvidos Durante Desenvolvimento

### 1. Cobertura do Loop de Cálculo de Scores
**Problema**: O loop que calcula a soma dos scores não estava sendo executado nos testes.

**Solução**: Adicionar o score à lista de scores do movie no `setUp()` do teste para garantir que o loop seja executado.

```java
movie.getScores().add(score);
```

### 2. Configuração de Java 21
**Problema**: O projeto requer Java 21, mas o sistema estava usando Java 25.

**Solução**: Configurar `JAVA_HOME` para usar Java 21 antes de executar os testes:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

## 📝 Notas Importantes

1. **Mockito**: Os testes utilizam mocks para isolar as unidades de teste das dependências
2. **Spring Extension**: Utiliza `@ExtendWith(SpringExtension.class)` para integração com Spring
3. **Factories**: Objetos de teste são criados através de factories para manter consistência
4. **Cobertura**: O projeto garante cobertura completa dos métodos críticos de negócio

## ✅ Status Final

- ✅ Todos os 16 testes passando
- ✅ Cobertura de código configurada
- ✅ Build e instalação funcionando corretamente
- ✅ Projeto pronto para produção

---

**Última atualização**: Novembro 2025

