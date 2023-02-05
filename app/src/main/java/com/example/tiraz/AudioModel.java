package com.example.tiraz;

import java.io.Serializable;

public class AudioModel implements Serializable {

    int id;
    String title;
    String language;
    String tags;
    int audio_id;
    String lyrics;

    public AudioModel(int id, String title, String language,
                      String tags, int audio_id, String lyrics) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.tags = tags;
        this.audio_id = audio_id;
        this.lyrics = lyrics;
//        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(int audio_id) {
        this.audio_id = audio_id;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getDuration() {
//        return duration;
//    }
//
//    public void setDuration(String duration) {
//        this.duration = duration;
//    }
}
