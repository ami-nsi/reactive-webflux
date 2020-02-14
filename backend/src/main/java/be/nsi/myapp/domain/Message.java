package be.nsi.myapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {
    private String content;

    public Message(String content) {
        this.content = content;
    }

    // Fixme to investigate
    // For mystic reasons, if I remove the getter/setter the mapping fail. Even with Lombock..
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
