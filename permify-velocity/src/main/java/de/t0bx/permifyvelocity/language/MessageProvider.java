package de.t0bx.permifyvelocity.language;

import de.t0bx.permifyvelocity.PermifyPlugin;

import java.util.Map;

public class MessageProvider {

    private final String prefix;

    private final Language currentLanguage;

    private final Map<MessageId, Map<Language, String>> MESSAGES = Map.of(
            MessageId.GROUP_EXISTS, Map.of(
                    Language.ENGLISH, "<red>The group %group% already exist.",
                    Language.GERMAN, "<red>Die Gruppe %group% existiert bereits."
            ),
            MessageId.GROUP_CREATED, Map.of(
                    Language.ENGLISH, "<green>The group %group% has been created.",
                    Language.GERMAN, "<green>Die Gruppe %group% wurde erstellt."
            ),
            MessageId.GROUP_DOES_NOT_EXIST, Map.of(
                    Language.ENGLISH, "<red>The group %group% doesn't exist.",
                    Language.GERMAN, "<red>Die Gruppe %group% existiert nicht."
            ),
            MessageId.GROUP_DELETED, Map.of(
                    Language.ENGLISH, "<green>The group %group% has been deleted.",
                    Language.GERMAN, "<green>Die Gruppe %group% wurde gelöscht."
            ),
            MessageId.PLAYER_NOT_FOUND, Map.of(
                    Language.ENGLISH, "<red>The player %player% doesn't exist.",
                    Language.GERMAN, "<red>Der Spieler %player% existiert nicht."
            )
    );

    public MessageProvider(Language language) {
        this.currentLanguage = language;
        this.prefix = PermifyPlugin.getInstance().getPrefix();
    }

    public String getMessage(MessageId messageId) {
        return this.prefix + this.MESSAGES
                .getOrDefault(messageId, Map.of())
                .getOrDefault(currentLanguage, "language_error");
    }
}
