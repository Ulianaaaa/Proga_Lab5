package common.commands;

import java.io.Serializable;

public interface Command extends Serializable {
    String execute(Object argument);  // универсальный метод
    String getDescription();
}