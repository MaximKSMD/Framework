package widgets.common.adSizes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdSizesList {
    A120x60("120x60", "Financial (120x60)"),
    A728x90("728x90", "Leaderboard (728x90)"),
    A16x600("160x600", "Skyscraper (160x600)"),
    A970x250("970x250", "Billboard (970x250)"),
    A300x600("300x600", "1x2 Large (300x600)"),
    A300x1050("300x1050", "Portrait (300x1050)"),
    A970x90("970x90", "Super Leaderboard (970x90)"),
    A300x50("300x50", "Smartphone Banner (300x50)"),
    A320x50("320x50", "Smartphone Banner (320x50)"),
    A300x250("300x250", "Medium Rectangle (300x250)"),
    A168x28("168x28","Feature Phone Medium Banner (168x28)"),
    A216x36("216x36", "Feature Phone Large Banner (216x36)"),
    A120x20("120x20", "Feature Phone Small Banner (120x20)"),
    A640x1136("640x1136", "Mobile Phone Interstitial (640x1136)"),
    A750x1134("750x1134", "Mobile Phone Interstitial (750x1134)"),
    A1080x1920("1080x1920", "Mobile Phone Interstitial (1080x1920)"),
    AInFeedNative("In-Feed Native (1x1)", "In-Feed Native (1x1)");

    private String size;
    private String fullName;
}