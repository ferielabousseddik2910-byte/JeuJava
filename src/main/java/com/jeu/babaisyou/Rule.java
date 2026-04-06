package com.jeu.babaisyou;

import java.util.Objects;

public class Rule {
    public enum Source {
        CODE,
        BOARD
    }

    private final GameObjectType subject;
    private final Property property;
    private final Source source;

    public Rule(GameObjectType subject, Property property, Source source) {
        this.subject = subject;
        this.property = property;
        this.source = source;
    }

    public static Rule ofCode(GameObjectType subject, Property property) {
        return new Rule(subject, property, Source.CODE);
    }

    public static Rule ofBoard(GameObjectType subject, Property property) {
        return new Rule(subject, property, Source.BOARD);
    }

    public GameObjectType getSubject() {
        return subject;
    }

    public Property getProperty() {
        return property;
    }

    public Source getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rule)) {
            return false;
        }
        Rule rule = (Rule) o;
        return subject == rule.subject && property == rule.property && source == rule.source;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, property, source);
    }

    @Override
    public String toString() {
        return subject.name() + " IS " + property.name() + " [" + source.name() + "]";
    }
}
