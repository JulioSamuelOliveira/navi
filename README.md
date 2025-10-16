# Navi — Tradutor de Chat com IA (Java + Spring AI + Vaadin)

Tradutor de textos com **estilos personalizados** sobre uma interface Vaadin, usando **Spring AI** para orquestrar chamadas à API da OpenAI.

## ✨ Funcionalidades

- Tradução/adaptação de texto para **pt-BR** aplicando um **estilo de escrita**:
  - Gíria das ruas  
  - Criança de 2 anos  
  - Juridiquês  
  - Caipira  
  - Fausto Silva
- UI simples com **Vaadin**: área de texto original, área de texto traduzido (somente leitura), seletor de estilo e botão **Traduzir** (atalho **Enter**).
- Prompt engineering com **mensagem de sistema** (persona/estilo) + **mensagem do usuário** (conteúdo original).

---

## 🧱 Arquitetura (alto nível)

- `HomeView`: lê o texto original e o estilo selecionado; aciona `NaviService.translate(...)` e exibe o resultado.  
- `StyleSelector`: `Select<String>` com os estilos acima; começa vazio (placeholder).  
- `NaviService`: constrói as mensagens (sistema/usuário) e chama o `ChatClient` do Spring AI.

---

## 📦 Requisitos

- **Java 17+**
- **Maven** *ou* **Gradle**
- Uma chave da **OpenAI API** (`OPENAI_API_KEY`)

---

## ⚙️ Configuração

### 1) Variáveis de ambiente

Defina a chave de API:

**Windows (PowerShell)**
```powershell
$env:OPENAI_API_KEY="sua_chave_aqui"
```

**macOS/Linux (bash)**
```bash
export OPENAI_API_KEY="sua_chave_aqui"
```

### 2) `application.yml` (exemplo)

> ajuste o nome do modelo conforme sua conta/limites (ex.: `gpt-4o-mini`, `gpt-4.1-mini`, etc.)

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-4o-mini
server:
  port: 8080
```

> Se preferir `application.properties`, use as chaves equivalentes.

---

## ▶️ Execução

### Maven
```bash
./mvnw spring-boot:run
# ou
mvn spring-boot:run
```

### Gradle
```bash
./gradlew bootRun
# ou
gradle bootRun
```

Acesse: **http://localhost:8080**

---

## 🖥️ Uso

1. Digite o **Texto Original** (esquerda).  
2. Selecione um **Estilo** no combo (fica vazio por padrão).  
3. Clique em **Traduzir** (ou pressione **Enter**).  
4. O **Texto Traduzido** aparece do lado direito (somente leitura).

---

## 📁 Principais classes

- `br.com.fiap.navi.views.HomeView`  
  - Monta o layout, escuta o clique no botão, valida seleção e texto, e atualiza o resultado.
- `br.com.fiap.navi.views.components.StyleSelector`  
  - Define os estilos: “Gíria das ruas”, “Criança de 2 anos”, “Juridiquês”, “Caipira”, “Fausto Silva”.  
  - `setEmptySelectionAllowed(true)` e `setPlaceholder("Selecione um estilo...")`.
- `br.com.fiap.navi.NaviService`  
  - **`translate(String text, String style)`**: cria a **mensagem de sistema** com instruções do estilo e a **mensagem do usuário** com o texto; faz `chatClient.prompt().system(...).user(...).call()` e retorna o conteúdo.

---

## 🧩 Como o prompt é montado (resumo)

- **Mensagem de sistema**: persona + regras do estilo (ex.: gírias urbanas, juridiquês, caipira etc.), preservar sentido, evitar explicações, responder só com o texto final.  
- **Mensagem do usuário**: inclui o **texto original** entre aspas triplas e a instrução de “reescrever aplicando o estilo”.

---

## ➕ Adicionando novos estilos

1. **UI**: em `StyleSelector`, adicione o novo rótulo no `setItems(...)`.  
2. **Service**: em `NaviService.systemMessage(String style)`, adicione um novo `case` com as instruções (persona/regras) do estilo.  
3. (Opcional) Defina **validações** e **guard-rails** (ex.: suavizar termos sensíveis).

---

## 🧪 Testes rápidos

- **Sem estilo selecionado**: o botão deve alertar para escolher um estilo.  
- **Texto vazio**: o botão deve alertar para informar o texto.  
- **Erros da API**: o serviço retorna uma mensagem amigável (“Não foi possível traduzir no momento: ...”).

## Criadores

- **Julio Samuel de Oliveira**
- **Leonardo Da Silva Pereira**
