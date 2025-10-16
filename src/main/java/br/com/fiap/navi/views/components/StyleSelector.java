package br.com.fiap.navi.views.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;

public class StyleSelector extends Select<String> {

    public StyleSelector() {
        super();
        this.setHelperText("Escolha um estilo de escrita para a tradução");
        this.setPrefixComponent(VaadinIcon.CHAT.create());
        this.setMinWidth("300px");

        // Estilos de escrita disponíveis (Opção 2 - strings simples)
        this.setItems(
            "Gíria das ruas",
            "Criança de 2 anos",
            "Juridiquês",
            "Caipira",
            "Fausto Silva"
        );

        // Valor padrão vazio
        this.setEmptySelectionAllowed(true);
        this.setPlaceholder("Selecione um estilo...");
    }
}
