package br.com.fiap.navi.views;

import br.com.fiap.navi.NaviService;
import br.com.fiap.navi.views.components.NaviTextArea;
import br.com.fiap.navi.views.components.StyleSelector;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

@Route("")
public class HomeView extends VerticalLayout {

    private final NaviService naviService;
    private final TextArea originalTextArea = new NaviTextArea("Texto Original", VaadinIcon.PENCIL.create());
    private final TextArea translatedTextArea = new NaviTextArea("Texto Traduzido", VaadinIcon.OPEN_BOOK.create());
    private final Select<String> selectStyle = new StyleSelector();

    public HomeView(NaviService naviService) {
        this.naviService = naviService;

        translatedTextArea.setReadOnly(true);
        var split = new SplitLayout(originalTextArea, translatedTextArea);
        split.setWidthFull();
        split.setHeight("100%");

        var button = new Button("Traduzir", VaadinIcon.ARROW_RIGHT.create());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Clique/Atalho: chama o service.translate(texto, estilo) e popula o resultado
        button.addClickShortcut(Key.ENTER);
        button.addClickListener(e -> {
            String text = safe(originalTextArea.getValue());
            String style = selectStyle.getValue();

            if (text.isBlank()) {
                notifyWarn("Informe um texto para traduzir.");
                return;
            }
            if (style == null || style.isBlank()) {
                notifyWarn("Selecione um estilo de escrita.");
                return;
            }

            button.setEnabled(false);
            try {
                String result = naviService.translate(text, style);
                translatedTextArea.setReadOnly(false);
                translatedTextArea.setValue(result != null ? result : "");
                translatedTextArea.setReadOnly(true);
            } catch (Exception ex) {
                notifyError("Falha ao traduzir: " + ex.getMessage());
            } finally {
                button.setEnabled(true);
            }
        });

        add(new H1("Navi"));
        add(new Paragraph("Tradutor de textos universais"));
        add(split);
        add(new HorizontalLayout(selectStyle, button));
        setSizeFull();
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static void notifyWarn(String msg) {
        var n = Notification.show(msg, 3000, Notification.Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }

    private static void notifyError(String msg) {
        var n = Notification.show(msg, 4000, Notification.Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
