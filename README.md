# CineSmart — Recomendador de Filmes por Perfil

**CineSmart** é um sistema inteligente de recomendação de filmes que personaliza sugestões com base no perfil, preferências e histórico de cada usuário.

## Visão Geral

CineSmart recebe o perfil de um usuário (gêneros preferidos, duração ideal, classificação etária máxima, idiomas, histórico de filmes) e cruza com um catálogo de filmes para gerar uma lista ranqueada das melhores recomendações.

### Fluxo Principal

```
1. Recebe perfil do usuário + catálogo
   ↓
2. Filtra filmes inválidos (já assistidos, acima faixa etária, etc.)
   ↓
3. Calcula score de compatibilidade (0-100) para cada filme
   ↓
4. Ordena por score (desc) → popularidade (desc) → aleatório
   ↓
5. Retorna top N recomendações + registra histórico + envia notificação
```

## Estrutura do Projeto (50% - Etapa 1)

```
CineSmart/
├── src/main/java/br/com/cinesmart/
│   ├── modelo/
│   │   ├── Usuario.java
│   │   ├── PerfilCinefilo.java
│   │   ├── Filme.java
│   │   ├── Recomendacao.java
│   │   ├── enums/
│   │   │   ├── Genero.java (ACAO, COMEDIA, DRAMA, FICCAO_CIENTIFICA, etc.)
│   │   │   ├── ClassificacaoEtaria.java (LIVRE, DEZ, DOZE, QUATORZE, DEZESSEIS, DEZOITO)
│   │   │   └── Idioma.java (PORTUGUES, INGLES, etc.)
│   ├── servico/
│   │   ├── RecomendadorServico.java (orquestrador)
│   │   ├── CalculadoraScore.java (lógica de cálculo)
│   │   ├── FiltroFilmes.java (filtragem)
│   │   ├── CatalogoFilmesAPI.java (interface)
│   │   ├── HistoricoUsuarioRepositorio.java (interface)
│   │   └── NotificadorPush.java (interface)
│   ├── excecao/
│   │   ├── PesoInvalidoExcecao.java
│   │   ├── DuracaoInvalidaExcecao.java
│   │   ├── NotaInvalidaExcecao.java
│   │   └── PerfilIncompletoExcecao.java
│   └── utilitario/
│       └── GeradorAleatorio.java (interface)
├── src/test/java/br/com/cinesmart/
│   ├── FilmeTest.java (5 @Test)
│   ├── PerfilCinefiloTest.java (10 @Test)
│   ├── CalculadoraScoreTest.java (8 @Test)
│   ├── FiltroFilmesTest.java (10 @Test)
│   └── RecomendadorServicoTest.java (10 @Test com Mockito)
├── pom.xml (Maven com JUnit 5, Mockito, JaCoCo)
└── README.md (este arquivo)
```

## Requisitos

- **Java 11+**
- **Maven 3.6+**

## Build & Teste

### 1. Compilar o projeto

```bash
mvn clean compile
```

### 2. Executar todos os testes

```bash
mvn test
```

Saída esperada: **43+ testes passando** (5 + 10 + 8 + 10 + 10)

### 3. Gerar relatório de cobertura JaCoCo

```bash
mvn jacoco:report
```

O relatório será gerado em: `target/site/jacoco/index.html`

**Meta de cobertura:** ≥ 80% nos pacotes `modelo/` e `servico/`

### 4. Build completo (compile + test + jacoco)

```bash
mvn clean package
```

## Exemplo de Uso (Pseudocódigo)

```java
// Criar perfil do usuário
PerfilCinefilo perfil = new PerfilCinefilo(90, 150, ClassificacaoEtaria.DEZESSEIS,
    Set.of(Idioma.PORTUGUES, Idioma.INGLES));
perfil.setPeso(Genero.FICCAO_CIENTIFICA, 0.9);
perfil.setPeso(Genero.DRAMA, 0.6);
perfil.setPeso(Genero.TERROR, 0.0);

// Criar usuário
Usuario usuario = new Usuario("U001", "Maria", 28, perfil);

// Injetar dependências (mocks em teste, reais em produção)
CatalogoFilmesAPI catalogo = new CatalogoMockRealista(); // ou API real
HistoricoUsuarioRepositorio historico = mock(...);
NotificadorPush notificador = mock(...);
GeradorAleatorio gerador = mock(...);
CalculadoraScore calculadora = new CalculadoraScore();
FiltroFilmes filtro = new FiltroFilmes();

// Criar serviço
RecomendadorServico servico = new RecomendadorServico(
    catalogo, historico, notificador, gerador, calculadora, filtro
);

// Gerar recomendações
List<Recomendacao> top5 = servico.recomendar(usuario, 5);

for (Recomendacao rec : top5) {
    System.out.println(rec.getFilme().getTitulo() + " - Score: " + rec.getScore());
    System.out.println(rec.getJustificativa());
}
```

## Componentes Principais

### PerfilCinefilo
- Armazena preferências do usuário: pesos de gênero, duração, classificação, idiomas
- Histórico de filmes assistidos e notas (1-5)
- **Validações:** peso ∈ [0.0, 1.0], duração_min ≤ duração_max, nota ∈ [1, 5]

### Filme
- Imutável: id, título, ano, duração, gêneros, classificação, idioma, popularidade
- **equals/hashCode por ID:** dois filmes com ID igual são considerados iguais

### FiltroFilmes
- **Regra 1:** Remove filmes já assistidos
- **Regra 2:** Remove filmes acima da classificação máxima
- **Regra 3:** Remove filmes em idioma não aceito
- **Regra 4:** Remove filmes com todos os gêneros com peso 0.0

### CalculadoraScore
- **Fórmula:** Gênero (50%) + Duração (20%) + Popularidade (15%) + Afinidade (15%) = Score 0-100
- **Componentes:**
  - **Gênero:** Média ponderada dos pesos dos gêneros do filme
  - **Duração:** 100 se dentro da faixa; reduz proporcionalmente se fora
  - **Popularidade:** Valor direto do filme (0-100)
  - **Afinidade:** Bônus se usuário deu notas altas a filmes similares

### RecomendadorServico
- Orquestrador que integra Catálogo → Filtro → Calculadora → Ranqueamento
- **Métodos:**
  - `recomendar(Usuario, int topN)` — retorna até N recomendações ordenadas
  - `recomendarAleatorio(Usuario)` — modo "Surpreenda-me" (1 filme aleatório do conjunto filtrado)
- Chama `HistoricoUsuarioRepositorio` para persistir recomendações
- Chama `NotificadorPush` se notificações estiverem habilitadas

## Testes Unitários

### Total de Testes: 43+ @Test

| Classe | Testes | Foco |
|--------|--------|------|
| FilmeTest | 5 | Criação, igualdade, imutabilidade |
| PerfilCinefiloTest | 10 | Validações, histórico, imutabilidade |
| CalculadoraScoreTest | 8 | Cálculo de score, componentes, determinismo |
| FiltroFilmesTest | 10 | Cada regra de filtro, casos extremos |
| RecomendadorServicoTest | 10 | Orquestração, mocks (Mockito), resilência |

### Anotações JUnit 5 Usadas

- `@BeforeEach` — setup antes de cada teste
- `@DisplayName(...)` — nomes legíveis
- `@Test` — método de teste
- `@ParameterizedTest` com `@CsvSource` — múltiplos cenários
- `@ExtendWith(MockitoExtension.class)` — integração com Mockito

### Mockito

**O que é mockado:**
- `CatalogoFilmesAPI` — controlar dados retornados na API
- `HistoricoUsuarioRepositorio` — não persistir dados reais em testes
- `NotificadorPush` — não disparar notificações reais
- `GeradorAleatorio` — testes determinísticos

**O que NÃO é mockado:**
- `CalculadoraScore` — lógica pura, sem I/O
- `FiltroFilmes` — lógica pura, sem I/O
- `Filme`, `Usuario`, `PerfilCinefilo` — objetos de domínio, sem mocks

### Exemplo de Teste com Mockito

```java
@Test
@DisplayName("deve registrar recomendações no histórico após gerar")
void deveRegistrarRecomendacoesNoHistorico() throws Exception {
    // Arrange
    when(catalogo.buscarTodos()).thenReturn(catalogoMock);

    // Act
    List<Recomendacao> resultado = servico.recomendar(usuario, 3);

    // Assert
    verify(historico, times(1)).registrarRecomendacao(eq(usuario), anyList());
}
```

## Bugs Encontrados Nos Testes

| ID | Bug encontrado | Teste que revelou | Correção indicada |
|----|----------------|-------------------|-------------------|
| B01 | `Filme.getGeneros()` não retornava uma coleção realmente imutável. O teste esperava `UnsupportedOperationException`, mas a alteração da coleção retornada não falhava. | [FilmeTest.deveRetornarCopiaDeGeneros](src/test/java/br/com/cinesmart/FilmeTest.java#L73) | Fazer o getter devolver uma visão imutável, usando `Collections.unmodifiableSet(...)` sobre uma cópia defensiva. |
| B02 | A ordenação das recomendações em `RecomendadorServico` invertia a lógica final por causa do `reversed()` aplicado ao comparador completo. | [RecomendadorServicoTest.deveInspecionarRecomendacoesRegistradas](src/test/java/br/com/cinesmart/RecomendadorServicoTest.java#L209) | Ajustar o comparador para ordenar por score decrescente e, em seguida, por popularidade decrescente, sem inverter a ordenação inteira no final. |

**Observação:** os testes também ajudam a revelar inconsistências de expectativa. No caso de [RecomendadorServicoTest.deveRetornarRecomendacoesOrdenadas](src/test/java/br/com/cinesmart/RecomendadorServicoTest.java#L98), o teste esperava 4 itens, mas o fluxo atual retorna 5 quando nenhum filme é filtrado.

## Boas Práticas Implementadas

1. **Nomenclatura em português** — código legível para contexto brasileiro
2. **Responsabilidade única** — cada classe tem uma responsabilidade clara
3. **Imutabilidade** — entidades de domínio (Filme, Recomendacao, enums)
4. **Injeção por construtor** — RecomendadorServico não cria dependências internamente
5. **Sem números mágicos** — pesos da fórmula como constantes nomeadas
6. **Sem null em coleções** — `Collections.emptyList()` sempre
7. **Exceções específicas** — PesoInvalidoExcecao, DuracaoInvalidaExcecao, etc.
8. **Validações rigorosas** — PerfilCinefilo lança exceção para entrada inválida
9. **equals/hashCode por ID** — Filme é único por ID
10. **Javadoc** — métodos públicos e interfaces documentados

## Contato

Projeto CineSmart — Recomendador de Filmes por Perfil (Java 11, JUnit 5, Mockito)
