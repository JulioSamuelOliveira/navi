package br.com.fiap.navi;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class NaviService {

    private final ChatClient chatClient;

    public NaviService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    private String systemMessage(String style) {
        if (style == null) style = "";
        switch (style.trim()) {
            case "Gíria das ruas":
                return """
                        Você é um tradutor brasileiro que reescreve o texto em português do Brasil usando gírias urbanas contemporâneas.
                        Mantenha o sentido original, use tom descontraído e coloquial, evitando termos ofensivos explícitos.
                        Não explique, apenas entregue o texto final já adaptado.
                        """;
            case "Criança de 2 anos":
                return """
                        Você é um tradutor que reescreve o texto como se explicasse para uma criança de 2 anos.
                        Use frases curtíssimas, palavras muito simples, tom carinhoso e claro. Evite conteúdo impróprio.
                        Não explique, apenas entregue o texto final já adaptado.
                        """;
            case "Juridiquês":
                return """
                        Você é um advogado experiente e traduz o texto para o registro de linguagem jurídica (juridiquês).
                        Use formalidade, precisão terminológica e construções típicas do meio jurídico brasileiro.
                        Preserve o sentido original e não invente fatos. Entregue apenas o texto final.
                        """;
            case "Caipira":
                return """
                        Você é um tradutor que adapta o texto para o jeito caipira do interior do Brasil.
                        Use vocabulário simples, expressões regionais suaves e tom respeitoso, mantendo o sentido original.
                        Não exagere a caricatura. Entregue apenas o texto final adaptado.
                        """;
            case "Fausto Silva":
                return """
                        Você é um tradutor que reescreve o texto no estilo animado de apresentador de auditório,
                        com entusiasmo, bordões e energia, mantendo respeito e o sentido original.
                        Evite menções diretas a marcas/pessoas reais. Entregue apenas o texto final.
                        """;
            default:
                return """
                        Você é um tradutor experiente de pt-BR. Reescreva o texto de forma neutra e clara,
                        preservando o sentido original e evitando explicações. Entregue apenas o texto final.
                        """;
        }
    }

    public String translate(String text, String style) {
        String sys = systemMessage(style);
        String user = """
                Texto original:
                \"\"\"%s\"\"\"
                Reescreva aplicando o estilo acima, mantendo sentido e informações.
                """.formatted(text == null ? "" : text);

        try {
            var response = chatClient
                    .prompt()
                    .system(sys)
                    .user(user)
                    .call();

            return response != null && response.content() != null
                    ? response.content().trim()
                    : "";
        } catch (Exception ex) {
            return "Não foi possível traduzir no momento: " + ex.getMessage();
        }
    }
}
