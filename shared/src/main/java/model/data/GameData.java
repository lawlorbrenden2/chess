package model.data;

public record GameData(
        Integer gameID,
        String whiteUsername,
        String blackUsername,
        String gameName
) {
}
